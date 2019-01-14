package com.example.q.maccrum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    ArrayList<String> text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent i = getIntent();
        text = (ArrayList<String>)i.getSerializableExtra("text");
        if(text==null){
            text = new ArrayList<>();
        }
        HttpRequest httprequest = new HttpRequest();
        httprequest.execute(text);
    }
}
