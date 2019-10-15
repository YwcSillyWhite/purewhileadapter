package com.example.purewhileadapter;

import com.example.purewhile.adapter.BaseMoreAdapter;
import com.example.purewhile.viewholder.BaseViewHolder;

import java.util.List;

public class MainAdapter extends BaseMoreAdapter<String,BaseViewHolder> {

    public MainAdapter(List list) {
        super(list);
        addLayoutId(R.layout.adapter_one);
    }

    @Override
    protected void onBindDataViewHolder(BaseViewHolder holder, int position, int itemViewType, String s) {
        holder.setText(R.id.tv,s).setOnClickListener(R.id.tv);
    }
}
