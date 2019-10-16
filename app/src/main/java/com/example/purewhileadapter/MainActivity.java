package com.example.purewhileadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.purewhile.call.OnFullListener;
import com.example.purewhile.call.OnItemListener;
import com.example.purewhile.call.OnLoadListenerImp;
import com.example.purewhile.full.FullView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mainAdapter;
    private RecyclerView recyc;
    private int page=1;
    View inflate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainAdapter = new MainAdapter(null);
        recyc = ((RecyclerView) findViewById(R.id.recycler));

        inflate= LayoutInflater.from(this).inflate(R.layout.loadview, null);

        recyc.setLayoutManager(new GridLayoutManager(this,2));


        mainAdapter.setOnLoadListener(new OnLoadListenerImp() {
            //加载更多
            @Override
            public void judgeLoad() {
                page++;
                flush(false,page>5?9:10);
            }

            //加载更多判断
            @Override
            public boolean judge() {
                return super.judge();
            }

            //点击没有网络重新加载
            @Override
            public void againLoad() {
                super.againLoad();
            }
        });

        mainAdapter.setFullStatus(FullView.LOAD,false);
        mainAdapter.setOnFullListener(new OnFullListener() {
            @Override
            public void againLoad() {
                flush(false,10);
            }
        });

//        mainAdapter.addHead(inflate);
        mainAdapter.addFoot(inflate);




        mainAdapter.setItemViewClick(false);
        mainAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onClick(RecyclerView.Adapter adapter, View view, int position, boolean itemView) {
                Toast.makeText(view.getContext(),"点击",Toast.LENGTH_SHORT).show();

            }
        });

        recyc.setAdapter(mainAdapter);
        flush(true,10);
    }

    private void flush(final boolean flush, final int size){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> list=new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add("这是个item"+i);
                }
                mainAdapter.finishStatus(true,flush,list);
                if (size<10){
                    mainAdapter.removeFoot(inflate);
                }
            }
        },3000);
    }
}
