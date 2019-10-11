package com.example.purewhile.call;

public abstract class OnLoadListenerImp implements OnLoadListener {
    @Override
    public void againLoad() {

    }

    @Override
    public void loadMore() {
        if (judge()){
            judgeLoad();
        }
    }

    public abstract void  judgeLoad();

    public boolean judge(){
        return true;
    }
}
