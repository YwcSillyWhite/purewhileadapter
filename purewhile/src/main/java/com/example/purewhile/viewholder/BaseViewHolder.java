package com.example.purewhile.viewholder;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * BaseViewHolder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> sparseArray=new SparseArray<>();
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
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
}
