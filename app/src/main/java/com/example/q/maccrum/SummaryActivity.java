package com.example.q.maccrum;

import android.content.Intent;
import android.net.Uri;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {
    private ShareDialog shareDialog;
    private ImageButton submit;
    private EditText editText;

    ArrayList<String> text;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        submit = (ImageButton) findViewById(R.id.ProcessButton);
        shareDialog = new ShareDialog(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShareDialog.canShow(ShareLinkContent.class)){
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("www.google.com"))
                            .build();
                    shareDialog.show(linkContent);
                }
            }
        });

        Intent intent = getIntent();

        Intent i = getIntent();
        text = (ArrayList<String>)i.getSerializableExtra("text");
        if(text==null){
            text = new ArrayList<>();
        }
        HttpRequest httprequest = new HttpRequest();
        try{
            JSONObject jsonObject = new JSONObject(httprequest.execute(text).toString());
            jsonArray = new JSONArray(jsonObject.get("documents").toString());
        }catch (Exception e){

        }
        Log.d(">>>>>>>>>>>>",jsonArray.toString());


    }
}
