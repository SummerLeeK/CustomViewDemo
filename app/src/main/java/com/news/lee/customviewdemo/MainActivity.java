package com.news.lee.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.news.lee.customviewdemo.widget.CustomView;
import com.news.lee.customviewdemo.widget.PathDemoView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CustomView customView;
    private ArrayList<Data> datas;

    private int currentAngle=0;
    private float somes[]=new float[]{
            10,20,30,80,25,44
    };

    private Button btn;
    private int CustomRadius;
    private PathDemoView pathDemoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pathDemoView= (PathDemoView) findViewById(R.id.path);

        ArrayList<Data> datas=new ArrayList<>();


        for (int i=0;i<20;i++){
            Data data=new Data();
            data.text="物理"+i;
            data.size= (int) (Math.random()*100);
            datas.add(data);
        }

        pathDemoView.setDatas(datas);



    }

    @Override
    protected void onStop() {
        Log.i("info","on Stop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i("info","on Pause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
