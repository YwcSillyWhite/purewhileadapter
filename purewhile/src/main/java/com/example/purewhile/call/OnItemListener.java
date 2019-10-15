package com.example.purewhile.call;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnItemListener {

    void onClick(RecyclerView.Adapter adapter, View view, int position, boolean itemView);

}
