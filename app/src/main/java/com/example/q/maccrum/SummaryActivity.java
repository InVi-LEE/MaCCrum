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
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {
    private ShareDialog shareDialog;
    private ImageButton submit;
    private EditText editText;

    ArrayList<String> text;
    JSONArray jsonArray;

    TextView textview;

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
            JSONObject jsonObject = new JSONObject(httprequest.execute(text).get());
            jsonArray = new JSONArray(jsonObject.get("documents").toString());
        }catch (Exception e){

        }
        textview = findViewById(R.id.edit_text);
        Log.d(">>>>>>>>>>>>",jsonArray.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = new String();
        try{
            JSONObject people = new JSONObject(readJSONFromAsset());
            JSONArray name = people.getJSONArray("class");
            Log.d(">>>>>>>>>>>>>>>>>person name asset",people.toString());
            for(int i=0;i<jsonArray.length();i++){
                JSONObject json = new JSONObject(jsonArray.get(i).toString());
                JSONArray keyphrase = new JSONArray(json.get("keyPharases").toString());
                Log.d(">>>>>>>number", String.valueOf(keyphrase.length()));
                for(int j=0;j<keyphrase.length();j++){
                    Log.d(">>>>>>>>>>>>>>>>>>>>>>>. keyphrase",keyphrase.get(j).toString());
                    if(keyphrase.get(j).toString().contains("이동민")){
                        text += "이동민-";
                    }
                }
            }
        }catch(Exception e){

        }
        textview.setText(text);
    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("name.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
