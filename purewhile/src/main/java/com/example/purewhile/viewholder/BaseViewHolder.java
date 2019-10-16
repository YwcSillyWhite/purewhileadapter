package com.example.purewhile.viewholder;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.purewhile.adapter.BaseAdapter;
import com.example.purewhile.call.OnItemListener;
import com.example.purewhile.utils.ClickUtils;

/**
 * BaseViewHolder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> sparseArray=new SparseArray<>();
    private RecyclerView.Adapter adapter;
    public BaseViewHolder(@NonNull View itemView,@NonNull RecyclerView.Adapter adapter) {
        super(itemView);
        this.adapter=adapter;
    }

    public View fdById(@IdRes int id){
        View view = sparseArray.get(id);
        if (view==null){
            view=itemView.findViewById(id);
            sparseArray.put(id,view);
        }
        return view;
    }

    public BaseViewHolder setText(@IdRes int id,String text){
        if (!TextUtils.isEmpty(text)){
            View view = fdById(id);
            if ( view!=null && view instanceof TextView ){
                ((TextView) view).setText(text);
            }
        }
        return this;
    }

    public BaseViewHolder setRecycler(@IdRes int id, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager){
        View view = fdById(id);
        if (view!=null && view instanceof RecyclerView){
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
        return this;
    }

    public BaseViewHolder setEnable(@IdRes int id,boolean enable){
        View view = fdById(id);
        if (view!=null){
            view.setEnabled(enable);
        }
        return this;
    }

    public BaseViewHolder setVisibility(@IdRes int id,int visibility){
        View view = fdById(id);
        if (view!=null){
            view.setVisibility(visibility);
        }
        return this;
    }

    public BaseViewHolder setSelected(@IdRes int id,boolean selected){
        View view = fdById(id);
        if (view!=null){
            view.setSelected(selected);
        }
        return this;
    }

    public BaseViewHolder setSelected(@IdRes int id, RecyclerView.Adapter adapter,RecyclerView.LayoutManager layoutManager){
        View view = fdById(id);
        if (view!=null && view instanceof RecyclerView){
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        return this;
    }

    public BaseViewHolder setOnClickListener(int  ...ids){
        if (ids.length==0)
            return this;
        if (adapter instanceof BaseAdapter){
            final BaseAdapter baseAdapter = (BaseAdapter) adapter;
            if (baseAdapter.onItemListener!=null){
                for (int i = 0; i < ids.length; i++) {
                    View view = fdById(ids[i]);
                    if (view!=null){
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (baseAdapter.onItemListener!=null){
                                    if (ClickUtils.clickable(view)){
                                        baseAdapter.onItemListener.onClick(baseAdapter,view, getDataPosition(),false);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }

       return this;
    }

    public BaseViewHolder setOnLongClickListener(int ...ids){
        if (ids.length==0)
            return this;
        if (adapter instanceof BaseAdapter){
            final BaseAdapter baseAdapter = (BaseAdapter) adapter;
            if (baseAdapter.onItemLongListener!=null){
                for (int i = 0; i < ids.length; i++) {
                    View view = fdById(ids[i]);
                    if (view!=null){
                        view.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                if (baseAdapter.onItemLongListener!=null){
                                    return baseAdapter.onItemLongListener.onClick(baseAdapter,view,getDataPosition(),false);
                                }
                                return false;
                            }
                        });
                    }
                }
            }
        }
        return this;
    }


    public int  getDataPosition(){
        if (adapter instanceof BaseAdapter) {
            BaseAdapter baseAdapter = (BaseAdapter) this.adapter;
            return getLayoutPosition()-baseAdapter.obtainHeadDataCount();
        }
        return getLayoutPosition();
    }
}
