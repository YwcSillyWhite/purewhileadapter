package com.example.purewhile.call;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 长按点击
 */
public interface OnItemLongListener {

    boolean onClick(RecyclerView.Adapter adapter, int position, boolean itemView);
}
