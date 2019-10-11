package com.example.purewhile.call;

import androidx.recyclerview.widget.RecyclerView;

public interface OnItemListener {

    void onClick(RecyclerView.Adapter adapter,int position,boolean itemView);

}
