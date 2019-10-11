package com.example.purewhile.load;

import android.view.View;

import com.example.purewhile.viewholder.BaseViewHolder;

public abstract class LoadView {

    public int loadStutes;

    public void setLoadStutes(int loadStutes) {
        this.loadStutes = loadStutes;
    }


    //加载，没有更多，没有网络，完成，其他
    public final static int REST = 0 , LOAD = 1 , NO_MORE = 2 , NO_NETWORK = 3 , FINISH = 4 ;

    public abstract int obtainLayoutId();
    //加载view
    public abstract int obtainLoadId();
    //完成view
    public abstract int obtainNoMoreId();
    //没有网络view
    public abstract int obtainNoNetworkId();


    public void onBindViewHolder(BaseViewHolder baseViewHolder){
        switch (loadStutes)
        {
            case LOAD:
                baseViewHolder.setVisibility(obtainLoadId(), View.VISIBLE);
                baseViewHolder.setVisibility(obtainNoMoreId(),View.GONE);
                baseViewHolder.setVisibility(obtainNoNetworkId(),View.GONE);
                break;
            case NO_MORE:
                baseViewHolder.setVisibility(obtainLoadId(), View.GONE);
                baseViewHolder.setVisibility(obtainNoMoreId(),View.VISIBLE);
                baseViewHolder.setVisibility(obtainNoNetworkId(),View.GONE);
                break;
            case NO_NETWORK:
                baseViewHolder.setVisibility(obtainLoadId(), View.GONE);
                baseViewHolder.setVisibility(obtainNoMoreId(),View.GONE);
                baseViewHolder.setVisibility(obtainNoNetworkId(),View.VISIBLE);
                break;
            default:
                baseViewHolder.setVisibility(obtainLoadId(), View.GONE);
                baseViewHolder.setVisibility(obtainNoMoreId(),View.GONE);
                baseViewHolder.setVisibility(obtainNoNetworkId(),View.GONE);
                break;

        }
    }
}
