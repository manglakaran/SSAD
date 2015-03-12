package com.example.akhil.loginparse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;


public class inviterecord extends Activity {

    ParseFile mAudio;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null ,EventName, EventType, FromSummary, eventID, eventIDD, datehere, timehere;
    private ImageButton start, stop, play, pause, tick, cancel, next, prev;
    private File file;
    private ViewFlipper viewFlipper;
    private ParseUser  user = ParseUser.getCurrentUser();
    private Button rerecord, saverecording;
    private RelativeLayout bottom;
    private MediaPlayer m = null;
    private int cnt, time;
    private CountDownTimer t1, t2;
    private TextView txtcount, flipper_content_1, flipper_content_2, flipper_content_3;
    private ProgressDialog proDialog;

    Animation animFlipInForeward;
    Animation animFlipOutForeward;
    Animation animFlipInBackward;
    Animation animFlipOutBackward;
    //private ParseObject AudioUpload;
    void addClick() throws ParseException {
       /* try {

            if (m.isPlaying()) {
                m.stop();
            }
        }
        catch(Exception e){
               e.printStackTrace();
           }
*/
        Intent intent = new Intent(getBaseContext(), contacts.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("audio/3gp");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buf = new byte[1024];
        int n;
        assert fis != null;
        try {
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] videoBytes = baos.toByteArray();
        final String videoFileName;
        videoFileName = "audioFile";
        mAudio = new ParseFile(videoFileName, videoBytes);
        mAudio.saveInBackground();

        if (FromSummary.equals("yes")) {
            eventIDD = eventID;
            //datehere =
        startLoading();
        ParseQuery<ParseObject> queryaudio = ParseQuery.getQuery("Audios");
        queryaudio.whereEqualTo("objectId", eventIDD);
        queryaudio.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Log.d("1audionextErrors", parseObject.getObjectId());
                    parseObject.put("AudioName", videoFileName);
                    parseObject.put("AudioFile", mAudio);
                    try {
                        parseObject.save();
                        stopLoading();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                        stopLoading();
                    }

                    //ParseObject AudioUpload = new ParseObject("Audios");
                } else {
                    // something went wrong
                    Log.d("audionextErrors", "start");
                    e.printStackTrace();
                    stopLoading();
                    //Log.d("parseobjects:P", parseObject.getObjectId());
                    //flipper_content_1.setText("no text available");
                    //flipper_content_2.setText("no text available");
                    //flipper_content_3.setText("no text available");

                }
            }
        });
    }

        else {

            ParseQuery<ParseObject> queryAudio = ParseQuery.getQuery("Audios");
            queryAudio.whereEqualTo("userObjID", user.getObjectId().toString());
            queryAudio.addDescendingOrder("eventCounter");
            queryAudio.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if(e == null) {
                        Log.d("1audionextErrors", parseObjects.get(0).getObjectId());
                        parseObjects.get(0).put("AudioName", videoFileName);
                        parseObjects.get(0).put("AudioFile", mAudio);
                        parseObjects.get(0).saveInBackground();

                        //ParseObject AudioUpload = new ParseObject("Audios");
                    }
                    else {
                        // something went wrong
                        Log.d("audionextErrors", "start");
                        e.printStackTrace();
                        //Log.d("parseobjects:P", parseObject.getObjectId());
                        //flipper_content_1.setText("no text available");
                        //flipper_content_2.setText("no text available");
                        //flipper_content_3.setText("no text available");

                    }
                }
            });
        }

        Bundle newbundle = new Bundle();
        newbundle.putString("EventName",EventName);
        newbundle.putString("EventType",EventType);
        newbundle.putString("FromSummary", FromSummary);
        newbundle.putString("EventID", eventID);
        if(FromSummary.equals("yes")){
            newbundle.putString("EventDate", datehere);
            newbundle.putString("EventTime", timehere);
        }
        intent.putExtras(newbundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#545454")));
        actionBar.setDisplayHomeAsUpEnabled(false);
        //actionBar.setTitle("Event Name");
        // Fetch Facebook user info if the session is active
//        Session session = ParseFacebookUtils.getSession();
       //if (session != null && session.isOpened()) {
       //     makeMeRequest();
     //   }

        setContentView(R.layout.voiceinvite);

        start = (ImageButton) findViewById(R.id.recordImageView);
        stop = (ImageButton) findViewById(R.id.stopImageView);
        play = (ImageButton) findViewById(R.id.playImageView);

        pause = (ImageButton) findViewById(R.id.pauseImageView);
        tick = (ImageButton) findViewById(R.id.tickImageView);
        cancel = (ImageButton) findViewById(R.id.cancelImageView);
        viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper1);
        flipper_content_1 = (TextView) findViewById(R.id.flipperContent1);
        flipper_content_2 = (TextView) findViewById(R.id.flipperContent2);
        flipper_content_3 = (TextView) findViewById(R.id.flipperContent3);
        rerecord = (Button) findViewById(R.id.Button);
        saverecording = (Button) findViewById(R.id.Button2);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        bottom.setVisibility(View.INVISIBLE);
        animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
        animFlipOutForeward = AnimationUtils.loadAnimation(this, R.anim.flipout);
        animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
        animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);


       // flipper_content_1.setText("aboveno text available1");
        //flipper_content_2.setText("aboveno text available2");
        //flipper_content_3.setText("noabove text available3");
        if(user.getUsername() !=null){
            //JSONObject userProfile = user.getJSONObject("profile");
       // try {
          //  if(user.getUsername() !=null) {
             /*   flipper_content_1.setText("Hello, i'm " + userProfile.getString("name") + ". We’re blowing up balloons and icing the cake!There’s \na big birthday party and we hope \nyou can make it!");
                flipper_content_2.setText("Hello, i'm " + userProfile.getString("name") + ". Because we have shared so much in our lives by our friendship and love; we invite you to the 25th Wedding Anniversary celebrations of our parents ");
                flipper_content_3.setText("Hello, i'm " + userProfile.getString("name") + ". We would be \npleased to have you join us for a party announcing our engagement ");
            */
            Intent prev = getIntent();
            Bundle B = prev.getExtras();
            EventName=B.getString("EventName");
            EventType=B.getString("EventType");
            FromSummary = B.getString("FromSummary");
            eventID = B.getString("EventID");
            if(FromSummary.equals("yes")){
                datehere = B.getString("EventDate");
                timehere = B.getString("EventTime");
            }



                             ParseQuery<ParseObject> query = ParseQuery.getQuery("SampleText");
                            //I4soQZWszw - Aniversary
                            //dUT7YhOSpf - Birthday
                            query.whereEqualTo("eventType", EventType);
                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (e == null) {

                                        try {
                                            flipper_content_1.setText(parseObject.getJSONArray("textArray").getString(0));
                                            flipper_content_2.setText(parseObject.getJSONArray("textArray").getString(1));
                                            flipper_content_3.setText(parseObject.getJSONArray("textArray").getString(2));
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                        /*flipper_content_1.setText("no text available1");
                                        flipper_content_2.setText("no text available2");
                                        flipper_content_3.setText("no text available3");*/


                                    } else {
                                        // something went wrong
                                        Log.d("nextErrors", "start");
                                        e.printStackTrace();
                                        //Log.d("parseobjects:P", parseObject.getObjectId());
                                        flipper_content_1.setText("no text available");
                                        flipper_content_2.setText("no text available");
                                        flipper_content_3.setText("no text available");

                                    }
                                }
                            });

                //final String eventType = AudioUpload.getString("eventType");

            }
            else{
                flipper_content_1.setText("login to continue");
                flipper_content_2.setText("login to continue");
                flipper_content_3.setText("login to continue");
            }
       // }
        //catch () {
            //Log.d("MyApp",
              //      "Error parsing saved user data.");
       //}

        next = (ImageButton) findViewById(R.id.nextImageView);
        prev = (ImageButton) findViewById(R.id.prevImageView);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeLeft();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeRight();
            }
        });
        //stop.setEnabled(false);
        //play.setEnabled(false);
        txtcount = (TextView) findViewById(R.id.txtCount);
        if(FromSummary.equals("yes")){
            bottom.setVisibility(View.GONE);
            m = null;
            stop.setVisibility(View.GONE);
            play.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
            //txtcount.setVisibility(View.INVISIBLE);
        }


        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/myrecording.3gp";
        file = new File(outputFile);


        saverecording.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                t1.cancel();
                Toast.makeText(getApplicationContext(), "audio confirmed", Toast.LENGTH_LONG).show();
                switch (v.getId()){
                    case R.id.Button2:
                        try {
                            addClick();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        t1 = new CountDownTimer( Long.MAX_VALUE , 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                cnt++;
                String time = new Integer(cnt).toString();

                long millis = cnt;
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds     = seconds % 60;

                txtcount.setText(String.format("%02d:%02d", seconds,millis));

            }

            @Override
            public void onFinish() {            }
        };
        t2 = new CountDownTimer( Long.MAX_VALUE , 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                cnt--;
                String time = new Integer(cnt).toString();

                long millis = cnt;
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds     = seconds % 60;

                txtcount.setText(String.format("%d:%02d", minutes, seconds));

            }

            @Override
            public void onFinish() {            }
        };

    }

    public void start(View view){
        try {
            txtcount.setVisibility(View.VISIBLE);
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);
            myAudioRecorder.prepare();
            myAudioRecorder.start();
            m = null;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        stop.setVisibility(View.VISIBLE);
        start.setVisibility(View.GONE);
        t1.start();
        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

    }

    public void stop(View view){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder  = null;
        //play.setEnabled(true);
        //tick.setVisibility(View.VISIBLE);
        //cancel.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.VISIBLE);

        stop.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);
        //m = new MediaPlayer();
        //m.setDataSource(outputFile);

        //tick.setEnabled(true);
        //cancel.setEnabled(true);
        t1.cancel();
        time = cnt;
        /*Toast.makeText(getApplicationContext(), "Audio recorded successfully",
                Toast.LENGTH_LONG).show();*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }
    public void play(View view) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException{
        //if(m.isPa)
        if( m == null){
        m = new MediaPlayer();
           m.setDataSource(outputFile);
           m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    try{
                        //player.stop();
                        //player.reset();
                        //player.setDataSource(, mediaUri);
                        m.pause();
                        m.seekTo(0);
                        //m = null;
                        pause.setVisibility(View.GONE);
                        play.setVisibility(View.VISIBLE);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

            m.prepare();
        }

        m.start();
        /*if(txtcount.getText() == "00:00") {
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
            //if(m.isPlaying()){
                m.pause();
                t2.cancel();
            //}
            cnt = time;
        }*/
        //t2.start();
        //txtcount.setText("00:00");
        //Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
        pause.setVisibility(View.VISIBLE);
        play.setVisibility(View.GONE);

    }

    public void cancel(View view) {
        File file = new File(outputFile);
        boolean deleted = file.delete();
        //start.setEnabled(true);
        start.setVisibility(View.VISIBLE);
        //cancel.setEnabled(false);
        //tick.setEnabled(false);
        bottom.setVisibility(View.INVISIBLE);
        //cancel.setVisibility(View.INVISIBLE);
        //tick.setVisibility(View.INVISIBLE);
        play.setVisibility(View.GONE);
        pause.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        cnt = 0;
        //t2.cancel();
        txtcount.setText("00:00");
        //Toast.makeText(getApplicationContext(), "audio deleted", Toast.LENGTH_LONG).show();

    }

    /*public void tick(View view) {
       start.setVisibility(View.VISIBLE);
        play.setVisibility(View.GONE);
        pause.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        tick.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "audio confirmed", Toast.LENGTH_LONG).show();
    }*/
    public void pause(View view) {
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
        //t2.cancel();
        if(m.isPlaying()){
            m.pause();

        }
    }
    private void SwipeRight(){
        viewFlipper.setInAnimation(animFlipInBackward);
        viewFlipper.setOutAnimation(animFlipOutBackward);
        viewFlipper.showPrevious();
    }
    public void edit(View view) {
        //1

        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher1);
        switcher.showNext(); //or switcher.showPrevious();
        TextView prev = (TextView) findViewById(R.id.flipperContent1);
        String ans = prev.getText().toString();
        EditText myTV = (EditText) switcher.findViewById(R.id.hidden_edit_view1);
        if(myTV != null) {
            myTV.setText(ans);
        }
        //2
        ViewSwitcher switcher2 = (ViewSwitcher) findViewById(R.id.my_switcher2);
        switcher2.showNext(); //or switcher.showPrevious();
        TextView prev2 = (TextView) findViewById(R.id.flipperContent2);
        String ans2 = prev2.getText().toString();
        Log.v("switch2", ans2);
        EditText myTV2 = (EditText) switcher2.findViewById(R.id.hidden_edit_view2);
        //Log.v("switch2", myTV2.toString());
        if(myTV2 != null) {
            myTV2.setText(ans2);
        }
        //Log.v("test",switcher2.toString());
        //3
        ViewSwitcher switcher3 = (ViewSwitcher) findViewById(R.id.my_switcher3);
        switcher3.showNext(); //or switcher.showPrevious();
        TextView prev3 = (TextView) findViewById(R.id.flipperContent3);
        String ans3 = prev3.getText().toString();
        EditText myTV3 = (EditText) switcher3.findViewById(R.id.hidden_edit_view3);
        if(myTV3 != null) {
            myTV3.setText(ans3);
        }
    }
    private void SwipeLeft(){
        viewFlipper.setInAnimation(animFlipInForeward);
        viewFlipper.setOutAnimation(animFlipOutForeward);
        viewFlipper.showNext();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener
            = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float sensitvity = 50;
            if((e1.getX() - e2.getX()) > sensitvity){
                SwipeLeft();
            }else if((e2.getX() - e1.getX()) > sensitvity){
                SwipeRight();
            }

            return true;
        }

    };

    GestureDetector gestureDetector
            = new GestureDetector(simpleOnGestureListener);
    protected void startLoading() {

        proDialog = new ProgressDialog(this);
        proDialog.setMessage("loading...");
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
        // Flag = 1;

    }

}
