package com.example.purewhile.adapter;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purewhile.call.OnFullListener;
import com.example.purewhile.call.OnLoadListener;
import com.example.purewhile.full.FullView;
import com.example.purewhile.full.FullViewImp;
import com.example.purewhile.load.LoadView;
import com.example.purewhile.load.LoadViewImp;
import com.example.purewhile.utils.ClickUtils;
import com.example.purewhile.utils.NetWorkUtils;
import com.example.purewhile.viewholder.BaseViewHolder;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class BaseMoreAdapter<T,V extends BaseViewHolder> extends BaseAdapter<T,V>{

    protected final int item_head=Integer.MIN_VALUE;
    protected final int item_foot=Integer.MIN_VALUE+1;
    protected final int item_full=Integer.MIN_VALUE+2;
    protected final int item_load=Integer.MIN_VALUE+3;
    private LinearLayout headLayout,footLayout;
    private LoadView loadView = new LoadViewImp();
    private FullView fullView = new FullViewImp();
    private OnLoadListener onLoadListener;
    private OnFullListener onFullListener;
    private Handler handler = new Handler();
    private int defaultPageSize=10;

    public void setLoadView(LoadView loadView) {
        if (loadView != null)
            this.loadView=loadView;
    }

    public void setFullView(FullView fullView) {
        if (fullView!=null)
            this.fullView = fullView;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }
    //参数2是否刷新
    public void setLoadStatus(int loadStatus,boolean flush){
        loadView.setLoadStutes(loadStatus);
        if (flush && obtainLoadCount() > 0) {
            notifyItemChanged(getItemCount()-1);
        }
    }

    public void setFullStatus(int fullStatus,boolean flush){
        fullView.setFullStatus(fullStatus);
        if (flush && obtainFullCount() > 0) {
            notifyDataSetChanged();
        }
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void setOnFullListener(OnFullListener onFullListener) {
        this.onFullListener = onFullListener;
    }

    @Override
    public final int getItemCount() {
        if (obtainFullCount() > 0)
            return obtainFullCount();
        return super.getItemCount();
    }

    @Override
    public int obtainRestCount() {
        return obtainHeadCount()+obtainFootCount()+obtainLoadCount();
    }

    @Override
    public int obtainDataHeadCout() {
        return obtainHeadCount();
    }

    //头部布局长度
    protected int obtainHeadCount(){
        if (headLayout!=null && headLayout.getChildCount() > 0) {
            return 1;
        }
        return 0;
    }

    //尾部布局长度
    protected int obtainFootCount(){
        if (footLayout!=null && footLayout.getChildCount() > 0) {
            return 1;
        }
        return 0;
    }

    //全局布局长度
    protected int obtainFullCount(){
        if (obtainDataCount() > 0 || onFullListener == null)
            return 0;
        return 1;
    }

    //加载布局长度
    protected int obtainLoadCount(){
        if (onLoadListener == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public final int getItemViewType(int position) {
        if (obtainFullCount() > 0)
            return item_full;
        if (position==0 && obtainHeadCount()!=0) {
            return item_head;
        }else if (position < getItemCount() - obtainFootCount() - obtainLoadCount()){
            return obtianCenterItemViewType(position - obtainDataHeadCout());
        }else if (position < getItemCount() - obtainLoadCount()){
            return item_foot;
        }else{
            return item_load;
        }
    }

    public int obtianCenterItemViewType(int position){
        return super.getItemViewType(position+obtainDataHeadCout());
    }

    @Override
    protected boolean isCenterItemView(int viewType) {
        return viewType!=item_head && viewType!=item_foot && viewType!=item_full && viewType!=item_load;
    }

    public BaseMoreAdapter(List<T> list) {
        super(list);
    }

    @Override
    public final V onCreateRestViewHolder(@NonNull ViewGroup parent, int viewType) {
        V viewholder = null;
        switch (viewType)
        {
            case item_full:
                viewholder = onCreateV(parent,fullView.obtainLayoutId());
                //重新加载
                viewholder.fdById(fullView.obtainAgainLoadId()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ClickUtils.clickable(view) && onFullListener != null && NetWorkUtils.isConnected()){
                            int fullStatus = fullView.obtainFullStatus();
                            if (fullStatus == FullView.NO_NETWORK) {
                                setFullStatus(FullView.LOAD,true);
                                onFullListener.againLoad();
                            }
                        }
                    }
                });
                break;
            case item_head:
                viewholder = onCreateV(headLayout);
                break;
            case item_foot:
                viewholder = onCreateV(footLayout);
                break;
            case item_load:
                viewholder = onCreateV(parent,loadView.obtainLayoutId());
                //重新加载
                viewholder.fdById(loadView.obtainNoNetworkId()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ClickUtils.clickable(view) && onLoadListener != null) {
                            if (loadView.loadStutes == LoadView.NO_NETWORK && NetWorkUtils.isConnected()){
                                setLoadStatus(LoadView.LOAD,true);
                                onLoadListener.againLoad();
                            }
                        }
                    }
                });
                break;
        }
        return viewholder;
    }

    @Override
    protected void onBindRestViewHolder(@NonNull V holder, int position, int itemViewType) {
        switch (itemViewType) {
            case item_full:
                fullView.onBindViewHolder(holder);
                break;
            case item_load:
                loadMore();
                loadView.onBindViewHolder(holder);
                break;
        }
    }

    protected  void loadMore(){
        if (obtainDataCount() == 0){
            return;
        }
        if (loadView.loadStutes == LoadView.FINISH){
            //滑动过程不能刷新数据
            if (NetWorkUtils.isConnected()){
                setLoadStatus(LoadView.LOAD,false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLoadListener.loadMore();
                    }
                },200);
            }else{
                setLoadStatus(LoadView.NO_NETWORK, false);
            }

        }
    }


    /**
     * 添加删除头尾
     * @param head
     */
    public void addHead(View head){
        addHead(-1,head);
    }

    public void addHead(int position,View head){
        addHead(position,head,LinearLayout.VERTICAL);
    }

    public void addHead(int position,View head,int orientation){
        if (head == null || head.getParent() != null)
            return;
        if (headLayout == null){
            headLayout = new LinearLayout(head.getContext());
            if (orientation == LinearLayout.VERTICAL){
                headLayout.setOrientation(orientation);
                headLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            }else{
                headLayout.setOrientation(LinearLayout.HORIZONTAL);
                headLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));

            }
        }
        int childCount = headLayout.getChildCount();
        if (childCount==0){
            headLayout.addView(head);
            notifyItemInserted(0);
        }else{
            if (position>=0 && position < childCount) {
               headLayout.addView(head,position);
            }else{
                headLayout.addView(head);
            }
            notifyItemChanged(0);
        }
    }

    public void  removeHead(View head){
        if (head == null || head.getParent() == null || headLayout == null || headLayout.getChildCount() ==0)
            return;
        headLayout.removeView(head);
        int childCount = headLayout.getChildCount();
        if (childCount==0){
            notifyItemRemoved(0);
        }else{
            notifyItemChanged(0);
        }
    }

    public void removeHeadAll(){
        if (headLayout == null || headLayout.getChildCount() == 0)
            return;
        headLayout.removeAllViews();
        notifyItemRemoved(0);
    }

    public void addFoot(View foot){
        addFoot(-1,foot);
    }

    public void addFoot(int position,View foot){
        addFoot(position,foot,LinearLayout.VERTICAL);
    }

    public void addFoot(int position,View foot,int orientation){
        if (foot == null || foot.getParent() != null)
            return;
        if (footLayout == null){
            footLayout = new LinearLayout(foot.getContext());
            if (orientation == LinearLayout.VERTICAL){
                footLayout.setOrientation(orientation);
                footLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            }else{
                footLayout.setOrientation(LinearLayout.HORIZONTAL);
                foot.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));

            }
        }
        int childCount = footLayout.getChildCount();
        if (childCount==0){
            footLayout.addView(foot);
            notifyItemInserted(getItemCount() - obtainLoadCount() - obtainFootCount());
        }else{
            if (position>=0 && position < childCount) {
                headLayout.addView(foot,position);
            }else{
                headLayout.addView(foot);
            }
            notifyItemChanged(getItemCount() - obtainLoadCount() - obtainFootCount());
        }
    }

    public void  removeFoot(View foot){
        if (foot == null || foot.getParent() == null || footLayout == null || footLayout.getChildCount() ==0)
            return;
        footLayout.removeView(foot);
        int childCount = footLayout.getChildCount();
        if (childCount==0){
            notifyItemRemoved(getItemCount() - obtainLoadCount() - obtainFootCount());
        }else{
            notifyItemChanged(getItemCount() - obtainLoadCount() - obtainFootCount());
        }
    }

    public void removeFootAll(){
        if (footLayout == null || footLayout.getChildCount() == 0)
            return;
        footLayout.removeAllViews();
        notifyItemRemoved(getItemCount() - obtainLoadCount() - obtainFootCount());
    }


    /**
     * 数据处理
     * @param network
     * @param flush
     * @param list
     */
    public void finishStatus(boolean network,boolean flush,List<T> list){
        if (network) {
            flushOrAdd(flush,list);
            int size = list!=null?list.size():0;
            if (size == 0){
                if (flush){
                    setFullStatus(FullView.NO_MORE,true);
                    setLoadStatus(LoadView.REST,false);
                }else{
                    setFullStatus(FullView.REST,false);
                    setLoadStatus(LoadView.REST,true);
                }
            }else if (size < defaultPageSize){
                setFullStatus(FullView.REST,false);
                setLoadStatus(flush?LoadView.REST:LoadView.NO_MORE,true);
            }else{
                setFullStatus(FullView.REST,false);
                setLoadStatus(LoadView.FINISH,true);
            }
        }
        else {
            if (flush){
                clear();
                setFullStatus(FullView.NO_NETWORK,true);
                setLoadStatus(LoadView.REST,false);
            } else{
                setFullStatus(FullView.REST,false);
                setLoadStatus(FullView.NO_NETWORK,true);
            }
        }
    }

}
