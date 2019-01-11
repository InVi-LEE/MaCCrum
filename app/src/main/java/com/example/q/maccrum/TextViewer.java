package com.example.q.maccrum;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TextViewer extends AppCompatActivity {
    ArrayList<String> text;
    ArrayList<String> limittext;
    ImageButton right;
    ImageButton process;
    static int num;
    ListView listview;
    EditText editView;
    Button submit;
    int text_position;
    RelativeLayout relative;
    static ArrayAdapter<String> adapter;
    TextView numberShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_viewer);

        Intent i = getIntent();
        text = (ArrayList<String>) i.getSerializableExtra("text");
        if(text==null){
             text = new ArrayList<>();
        }
        limittext = new ArrayList<>();
        for(int j=0;j<text.size();j++){
            limittext.add(text.get(j).split("\n")[0]);
        }
        num = i.getIntExtra("num",0);
        Log.d("tag", text.toString());
        numberShow = findViewById(R.id.textView2);
        numberShow.setText("총 개수 : "+num);

        editView = findViewById(R.id.editview1);
        relative = findViewById(R.id.relativeview);
        submit = findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = editView.getText().toString();
                text.set(text_position,string);
                limittext.set(text_position,string.split("\n")[0]);
                listview.setVisibility(View.VISIBLE);
                relative.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });

        listview = (ListView)findViewById(R.id.listview1);
        adapter = new ArrayAdapter<String>(
                this, //context(액티비티 인스턴스)
                android.R.layout.simple_list_item_1, // 한 줄에 하나의 텍스트 아이템만 보여주는 레이아웃 파일
                // 한 줄에 보여지는 아이템 갯수나 구성을 변경하려면 여기에 새로만든 레이아웃을 지정하면 됩니다.
                limittext  // 데이터가 저장되어 있는 ArrayList 객체
        );
        // 6. ListView 객체에 adapter 객체를 연결합니다.
        listview.setAdapter(adapter);



        // 7. ListView 객체의 특정 아이템 클릭시 처리를 추가합니다.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                // 8. 클릭한 아이템의 문자열을 가져와서
                String selected_item = (String)text.get(position);

                editView.setText(selected_item);

                // 10. 어댑터 객체에 변경 내용을 반영시켜줘야 에러가 발생하지 않습니다.
                listview.setVisibility(View.GONE);
                relative.setVisibility(View.VISIBLE);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                text.remove(position);
                num--;
                adapter.notifyDataSetChanged();
                numberShow.setText("총 개수 : "+num);
                return false;
            }
        });

        right = findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        /* Create an intent that will start the main activity. */
                        Intent mainIntent = new Intent(TextViewer.this,
                                MainActivity.class);
                        mainIntent.putExtra("text",text);
                        mainIntent.putExtra("num", num);
                        mainIntent.putExtra("from",true);

                        //SplashScreen.this.startActivity(mainIntent);
                        startActivity(mainIntent);
                        /* Finish splash activity so user cant go back to it. */
//                        MainActivity.this.finish();
//
//                     /* Apply our splash exit (fade out) and main
//                        entry (fade in) animation transitions. */
                        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                    }
                }, 100);

            }
        });

        ImageButton process = findViewById(R.id.processButton);
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"submit하는 부분 구현하자", Toast.LENGTH_SHORT).show();
                // TODO: 2019-01-11 text 처리해서 단어 요약하는 부분 가져오자.
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        text.remove(text.size()-1);
        overridePendingTransition(R.anim.fadein_left, R.anim.fadeout_left);
    }
}
