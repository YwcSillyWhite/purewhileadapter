package com.example.purewhile.adapter;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.purewhile.R;
import com.example.purewhile.call.OnItemListener;
import com.example.purewhile.call.OnItemLongListener;
import com.example.purewhile.utils.ClickUtils;
import com.example.purewhile.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础适配器
 * @param <T>
 * @param <V>
 */
public abstract class BaseAdapter<T,V extends BaseViewHolder> extends RecyclerView.Adapter<V> {

    public BaseAdapter(List<T> list) {
        if (list!=null && list.size()>0){
            this.mData.addAll(list);
        }
    }

    //数据
    private List<T> mData=new ArrayList<>();
    public List<T> obtainData(){
        return mData;
    }
    public T obtainT(int position){
        if (position>=0 && position< mData.size()){
            return mData.get(position);
        }
        return null;
    }


    //中间数据
    protected boolean isCenterItemView(int viewType){
        return true;
    }


    //布局
    private SparseIntArray sparseIntArray=new SparseIntArray();
    protected void addLayoutId(@LayoutRes int layoutId){
        addLayoutId(0,layoutId);
    }
    protected void addLayoutId(int viewType,@LayoutRes int layoutId){
        if (layoutId!=0){
            sparseIntArray.put(viewType,layoutId);
        }
    }

    //点击事件
    public OnItemListener onItemListener;
    //itemview是否点击
    private boolean isItemViewClick=true;
    public void setItemViewClick(boolean itemViewClick) {
        isItemViewClick = itemViewClick;
    }
    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }
    public OnItemLongListener onItemLongListener;
    private boolean isItemViewLongClick=false;
    public void setItemViewLongClick(boolean itemViewLongClick) {
        isItemViewLongClick = itemViewLongClick;
    }
    public void setOnItemLongListener(OnItemLongListener onItemLongListener) {
        this.onItemLongListener = onItemLongListener;
    }


    /**
     * 创建viewholder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public final V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCenterItemView(viewType)){
            V v = onCreateDataViewHolder(parent, viewType);
            onBindClick(v);
            return v;
        }else{
            return onCreateRestViewHolder(parent,viewType);
        }
    }

    public V onCreateDataViewHolder(@NonNull ViewGroup parent, int viewType){
        int layoutId = sparseIntArray.get(viewType, R.layout.adapter_error_layout);
        V v = onCreateV(parent,layoutId);
        return v;
    }

    public V onCreateRestViewHolder(@NonNull ViewGroup parent, int viewType){
        return null;
    }

    public final V onCreateV(@NonNull ViewGroup parent,int layoutId){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return onCreateV(itemView);
    }

    public final V onCreateV(View itemView){
        BaseViewHolder baseViewHolder = new BaseViewHolder(itemView,this);
        return ((V) baseViewHolder);
    }

    //点击事件
    public final void onBindClick(@NonNull final V holder){
        if (holder!=null)
            return;
        View itemView = holder.itemView;
        if (itemView==null)
            return;
        if (isItemViewClick && onItemListener!=null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isItemViewClick && onItemListener!=null){
                        if (ClickUtils.clickable(view)){
                            onItemListener.onClick(BaseAdapter.this,view,holder.getDataPosition(),true);

                        }
                    }
                }
            });
        }

        if (isItemViewLongClick && onItemLongListener!=null) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (isItemViewLongClick && onItemLongListener!=null ) {
                        return onItemLongListener.onClick(BaseAdapter.this,view,holder.getDataPosition(),true);
                    }
                    //默认屏蔽点击事件
                    return true;
                }
            });
        }
    }


    /**
     * 绑定viewholder
     * @param holder
     * @param position
     */
    @Override
    public final void onBindViewHolder(@NonNull V holder, int position) {
        int itemViewType = holder.getItemViewType();
        if (isCenterItemView(itemViewType)) {
            int dataPosition = position - obtainDataHeadCout();
            onBindDataViewHolder(holder,dataPosition,itemViewType,obtainT(dataPosition));
        }else{
            onBindRestViewHolder(holder,position,itemViewType);
        }
    }

    protected abstract  void onBindDataViewHolder(V holder, int position, int itemViewType,T t);


    protected void onBindRestViewHolder(@NonNull V holder, int position,int itemViewType){

    }





    /**
     * 长度
     * @return
     */
    @Override
    public int  getItemCount() {
        return obtainDataCount()+obtainRestCount();
    }

    /**
     * 数据长度
     */
    public int obtainDataCount(){
        return mData.size();
    }

    /**
     * 其他长度
     * @return
     */
    public int obtainRestCount(){
        return 0;
    }

    /**
     * 数据头部长度
     */
    public int obtainDataHeadCout(){
        return 0;
    }





    /**
     * 设置满屏
     * @param recyclerView
     */
    @Override
    public final void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager){
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return setGridSpan(((GridLayoutManager) layoutManager),position);
                }
            });
        }
    }

    protected int setGridSpan(GridLayoutManager gridLayoutManager,int position){
        if (isCenterItemView(getItemViewType(position))){
            return 1;
        }else {
            return gridLayoutManager.getSpanCount();
        }
    }

    @Override
    public void onViewRecycled(@NonNull V holder) {
        super.onViewRecycled(holder);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams !=null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams){
            setStagSpan(((StaggeredGridLayoutManager.LayoutParams) layoutParams),holder);
        }
    }

    protected void setStagSpan(StaggeredGridLayoutManager.LayoutParams layoutParams,V holder){
        if (isCenterItemView(holder.getItemViewType())) {
            layoutParams.setFullSpan(false);
        }else{
            layoutParams.setFullSpan(true);
        }
    }



    /**
     * 刷新适配器
     */
    private int startPosition(int size){
        return obtainDataCount()+obtainDataHeadCout()-size;
    }


    //刷新数据
    public void flush(List<T> list){
        if (mData.size()>0){
            mData.clear();
            if (list!=null&&list.size()>0){
                mData.addAll(list);
            }
            notifyDataSetChanged();
        }else{
            if (list!=null&&list.size()>0){
                mData.addAll(list);
                notifyItemRangeInserted(startPosition(list.size()),list.size());
            }
        }
    }

    public void flush(T t){
        if (mData.size()>0){
            mData.clear();
            if (t!=null){
                mData.add(t);
            }
            notifyDataSetChanged();
        }else{
            if (t!=null){
                mData.add(t);
                notifyItemInserted(startPosition(1));
            }
        }
    }

    public void flush(int position){
        if (position<obtainDataCount()){
            notifyItemChanged(position+obtainDataHeadCout());
        }
    }


    //添加数据
    public void addData(List<T> list){
        if (list!=null && list.size()>0){
            mData.addAll(list);
            notifyItemRangeInserted(startPosition(list.size()),list.size());
        }
    }


    public void addData(T t){
        if (t!=null){
            mData.add(t);
            notifyItemInserted(startPosition(1));
        }
    }

    //删除数据
    public void removePosition(int position){
        if (position<obtainDataCount()){
            mData.remove(position);
            notifyItemRemoved(position+obtainDataHeadCout());
        }
    }


    //清空
    public void clear(){
        if (obtainDataCount() > 0){
            mData.clear();
            notifyDataSetChanged();
        }
    }

    public void flushOrAdd(boolean flush,List<T> list){
        if (flush){
            flush(list);
        }else{
            addData(list);
        }
    }


}
