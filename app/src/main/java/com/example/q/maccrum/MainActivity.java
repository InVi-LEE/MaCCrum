package com.example.q.maccrum;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.vikramezhil.droidspeech.DroidSpeech;
//import com.vikramezhil.droidspeech.OnDSListener;
//import com.vikramezhil.droidspeech.OnDSPermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MessageDialogFragment.Listener {

    private final String TAG = "mainactivity";
    Intent intent;
//    DroidSpeech mDroidSpeech;
    TextView textView;
    final int PERMISSION = 1;
    ImageButton start;
    ImageButton stop;
    static boolean isFirst;

    private static final String FRAGMENT_MESSAGE_DIALOG = "message_dialog";

    private static final String STATE_RESULTS = "results";

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private SpeechService mSpeechService;

    private VoiceRecorder mVoiceRecorder;

    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            showStatus(true);
            if (mSpeechService != null) {
                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (mSpeechService != null) {
                mSpeechService.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            showStatus(false);
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
            }
        }

    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mSpeechService = SpeechService.from(binder);
            mSpeechService.addListener(mSpeechServiceListener);
//            mStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mSpeechService = null;
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    private void showPermissionMessageDialog() {
        MessageDialogFragment
                .newInstance(getString(R.string.permission_message))
                .show(getSupportFragmentManager(), FRAGMENT_MESSAGE_DIALOG);
    }

    private void showStatus(final boolean hearingVoice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(">>>>>>>>>>>> hearing : ",String.valueOf(hearingVoice));
//                mStatus.setTextColor(hearingVoice ? mColorHearing : mColorNotHearing);
            }
        });
    }

    @Override
    public void onMessageDialogDismissed() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    private final SpeechService.Listener mSpeechServiceListener =
            new SpeechService.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        mVoiceRecorder.dismiss();
                    }
                    if (textView != null && !TextUtils.isEmpty(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
//                                    String str = textView.getText().toString();
//                                    str += null;
//                                    textView.setText(str);
                                    if(isFirst){
                                        textView.setText(text+" ");
                                        isFirst = false;
                                    }else{
                                        String str = textView.getText().toString();
                                        str += text+" ";
                                        textView.setText(str);
                                    }
                                } else {
//                                    if(isFirst){
//                                        textView.setText(text);
//                                        isFirst = false;
//                                    }
//                                    String str = textView.getText().toString();
//                                    str += text;
//                                    textView.setText(str);
                                }
                            }
                        });
                    }
                }
            };

//    private static class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView text;
//
//        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.item_result, parent, false));
//            text = textView
//        }
//
//    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Resources resources = getResources();
        final Resources.Theme theme = getTheme();
        isFirst = true;
        // Initialize Droid Speech
//        mDroidSpeech = new DroidSpeech(this, null);
//        mDroidSpeech.setOnDroidSpeechListener(this);
//        mDroidSpeech.setShowRecognitionProgressView(true);

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        textView = (TextView)findViewById(R.id.sttResult);

//        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
//        intent.putExtra("android.speech.extra.DICTATION_MODE", true);
//        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
//        start = findViewById(R.id.start);
//        start.setOnClickListener(this);
//
//        stop = findViewById(R.id.stop);
//        stop.setOnClickListener(this);
    }


    @Override
    public void onStart(){
        super.onStart();

        // Prepare Cloud Speech API
        bindService(new Intent(this, SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);
//        isFirst = true;
        // Start listening to voices
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.start:

                // Starting droid speech
//                mDroidSpeech.startDroidSpeechRecognition();

                // Setting the view visibilities when droid speech is running
//                start.setVisibility(View.GONE);
//                stop.setVisibility(View.VISIBLE);
                Log.d(":>>>>>>>>>>> button", "start button is clicked");

                break;

            case R.id.stop:

                // Closing droid speech
//                mDroidSpeech.closeDroidSpeechOperations();
//
//                stop.setVisibility(View.GONE);
//                start.setVisibility(View.VISIBLE);
                Log.d(":>>>>>>>>>>> button", "stop button is clicked");

                break;
        }
    }





    @Override
    public void onPause()
    {
        super.onPause();

//        if(stop.getVisibility() == View.VISIBLE)
//        {
//            stop.performClick();
//        }
    }

    @Override
    public void onStop(){
        // Stop listening to voice
        stopVoiceRecorder();

        // Stop Cloud Speech API
        if(mSpeechService!=null){
            mSpeechService.removeListener(mSpeechServiceListener);
        }
        unbindService(mServiceConnection);
        mSpeechService = null;

        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

//        if(stop.getVisibility() == View.VISIBLE)
//        {
//            stop.performClick();
//        }
    }
    // MARK: OnClickListener Method



    // MARK: DroidSpeechListener Methods

//    @Override
//    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages)
//    {
//        Log.i(TAG, "Current speech language = " + currentSpeechLanguage);
////        Log.i(TAG, "Supported speech languages = " + supportedSpeechLanguages.toString());
//
//        if(supportedSpeechLanguages.contains("ko-KR"))
//        {
//            // Setting the droid speech preferred language as tamil if found
//            mDroidSpeech.setPreferredLanguage("ko-KR");
//
//            // Setting the confirm and retry text in tamil
//            mDroidSpeech.setOneStepVerifyConfirmText("시작되었습니다.");
//            mDroidSpeech.setOneStepVerifyRetryText("시작되었습니다.");
//        }
//    }
//
//    @Override
//    public void onDroidSpeechRmsChanged(float rmsChangedValue)
//    {
//        // Log.i(TAG, "Rms change value = " + rmsChangedValue);
//    }
//
//    @Override
//    public void onDroidSpeechLiveResult(String liveSpeechResult)
//    {
//        Log.i(TAG, "Live speech result = " + liveSpeechResult);
//    }
//
//    @Override
//    public void onDroidSpeechFinalResult(String finalSpeechResult)
//    {
//        // Setting the final speech result
//        if(first){
//            this.textView.setText(finalSpeechResult + "\n");
//            first = false;
//        }else{
//            this.textView.append(finalSpeechResult+"\n");
//        }
//
//        if(mDroidSpeech.getContinuousSpeechRecognition())
//        {
//            int[] colorPallets1 = new int[] {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA};
//            int[] colorPallets2 = new int[] {Color.YELLOW, Color.RED, Color.CYAN, Color.BLUE, Color.GREEN};
//
//            // Setting random color pallets to the recognition progress view
//            mDroidSpeech.setRecognitionProgressViewColors(new Random().nextInt(2) == 0 ? colorPallets1 : colorPallets2);
//        }
//        else
//        {
//            stop.setVisibility(View.GONE);
//            start.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onDroidSpeechClosedByUser()
//    {
//        stop.setVisibility(View.GONE);
//        start.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onDroidSpeechError(String errorMsg)
//    {
//        // Speech error
//        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
//
//        stop.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                // Stop listening
//                stop.performClick();
//            }
//        });
//    }
//
//    // MARK: DroidSpeechPermissionsListener Method
//
//    @Override
//    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny)
//    {
//        if(audioPermissionGiven)
//        {
//            start.post(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    // Start listening
//                    start.performClick();
//                }
//            });
//        }
//        else
//        {
//            if(errorMsgIfAny != null)
//            {
//                // Permissions error
//                Toast.makeText(this, errorMsgIfAny, Toast.LENGTH_LONG).show();
//            }
//
//            stop.post(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    // Stop listening
//                    stop.performClick();
//                }
//            });
//        }
//    }

}

