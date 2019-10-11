package com.example.purewhile.load;

import com.example.purewhile.R;

public class LoadViewImp extends LoadView{
    @Override
    public int obtainLayoutId() {
        return R.layout.loadview;
    }

    @Override
    public int obtainLoadId() {
        return R.id.load;
    }

    @Override
    public int obtainNoMoreId() {
        return R.id.noMore;
    }

    @Override
    public int obtainNoNetworkId() {
        return R.id.noNetwork;
    }
}
