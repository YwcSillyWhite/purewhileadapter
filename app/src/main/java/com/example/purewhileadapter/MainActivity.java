package com.example.purewhileadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.example.purewhile.call.OnFullListener;
import com.example.purewhile.call.OnLoadListenerImp;
import com.example.purewhile.full.FullView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mainAdapter;
    private RecyclerView recyc;
    private int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainAdapter = new MainAdapter(null);
        recyc = ((RecyclerView) findViewById(R.id.recycler));

        View inflate = LayoutInflater.from(this).inflate(R.layout.loadview, null);

        recyc.setLayoutManager(new GridLayoutManager(this,2));

        mainAdapter.setFullStatus(FullView.LOAD,false);
        mainAdapter.setOnLoadListener(new OnLoadListenerImp() {
            @Override
            public void judgeLoad() {
                page++;
                flush(false,page>5?9:10);
            }
        });
        mainAdapter.setOnFullListener(new OnFullListener() {
            @Override
            public void againLoad() {
                flush(false,10);
            }
        });

        mainAdapter.addHead(inflate);
        mainAdapter.addFoot(inflate);

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
            }
        },3000);
    }
}
