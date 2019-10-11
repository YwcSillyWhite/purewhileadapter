package com.example.purewhile.full;

import com.example.purewhile.R;

public class FullViewImp extends FullView{
    @Override
    public int obtainLayoutId() {
        return R.layout.fullview;
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

    @Override
    public int obtainAgainLoadId() {
        return R.id.again;
    }
}
