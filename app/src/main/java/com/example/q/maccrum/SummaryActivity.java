package com.example.q.maccrum;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    private ShareDialog shareDialog;
    private ImageButton submit;
    private EditText editText;
    private Button copy;

    ArrayList<String> text;
    JSONArray jsonArray;

    TextView textview;

    ArrayList<String> nameList;

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

        copy = (Button)findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    EditText editText = (EditText)findViewById(R.id.edit_text);
                    String k = editText.getText().toString();
                    //클립보드 사용 코드
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("ID",k);
                    clipboardManager.setPrimaryClip(clipData);

                    //복사가 되었다면 토스트메시지 노출
                    Toast.makeText(getApplicationContext(),"요약문이 복사되었습니다.",Toast.LENGTH_SHORT).show();
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

        nameList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = new String();
        try{
            JSONObject people = new JSONObject(readJSONFromAsset());
            JSONArray name = people.getJSONArray("class");
            for(int i=0;i<name.length();i++){
                JSONArray names = name.getJSONObject(i).getJSONArray("name");
                for(int j=0;j<names.length();j++){
                    nameList.add(names.getString(j));
                }

            }
            Log.d(">>>>>>>>>>>>>nameList" , nameList.toString());
            for(int i=0;i<jsonArray.length();i++){
                Log.d(">>>>>>>>>","1");
                JSONObject json = new JSONObject(jsonArray.get(i).toString());
                Log.d(">>>>>>>>>",json.toString());
                JSONArray keyphrase = new JSONArray(json.get("keyPhrases").toString());
                Log.d(">>>>>>>>>","3");
                ArrayList<String> onesummary = new ArrayList<>();

                for(int j=0;j<keyphrase.length();j++){
                    boolean exist = false;
                    for(int k=0;k<nameList.size();k++){
                        if(keyphrase.get(j).toString().contains(nameList.get(k))){
                            onesummary.add(0,nameList.get(k) + " - ");
                            exist = true;
                            break;
                        }
                    }
                    if(exist==false){
                        onesummary.add(keyphrase.get(j).toString()+"\n");
                    }
                }
                for(int j=0;j<onesummary.size();j++){
                    text += onesummary.get(j);
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
