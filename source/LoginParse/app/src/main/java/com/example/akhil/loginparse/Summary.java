package com.example.akhil.loginparse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class Summary extends Activity {

    SeekBar seek_bar;
    ImageButton play_button, pause_button;
    MediaPlayer player;
    TextView text_shown, timeDisplay;
    Handler seekHandler = new Handler();
    Handler timeHandler = new Handler();
    TextView scheduleDate, scheduleTime;
    String EventName, EventType, FromSummary, eventID, date, time;
    EditText nameevent;
    Spinner spinner;
    private ProgressDialog proDialog;


    private ParseUser user = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

        scheduleDate = (TextView) findViewById(R.id.scheduleDate);
        scheduleTime = (TextView) findViewById(R.id.scheduleTime);
        Intent prev = getIntent();
        Bundle B = prev.getExtras();
        EventName = B.getString("EventName");
        EventType = B.getString("EventType");
        FromSummary = B.getString("FromSummary");

        eventID = B.getString("EventID");
//        Log.d("eventIDtop", eventID);


        nameevent = (EditText) findViewById(R.id.nameofevent);
        nameevent.setText(EventName);

        spinner = (Spinner) findViewById(R.id.typeofevent);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.event_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String myString = EventType; //the value you want the position for

        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(myString);

        spinner.setSelection(spinnerPosition);
        Log.d("ageani", user.getObjectId().toString());
        //Log.d("eventID", eventID);
        if (FromSummary.equals("yes")){
            startLoading();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Audios");
        query.whereEqualTo("objectId", eventID);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    scheduleDate.setText(parseObject.getString("scheduleDate"));
                    scheduleTime.setText(parseObject.getString("scheduleTime"));
                    Log.d("RetriveaudionextErrors", parseObject.getObjectId());
                    ParseFile sound = (ParseFile) parseObject.get("AudioFile");
                    if (sound != null) {
                        sound.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, com.parse.ParseException e1) {
                                if (e1 == null) {
                                    try {
                                        //fileInputStream = new FileInputStream(file);
                                        //fileInputStream.read(data);
                                        //fileInputStream.close();

                                        //convert array of bytes into file
                                        File file;
                                        String outputFile = null;
                                        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myrecording.3gp";
                                        file = new File(outputFile);

                                        FileOutputStream outputStream = new FileOutputStream(file);
                                        outputStream.write(data);
                                        outputStream.close();
                                        getInit();
                                        seekUpdation();
                                        try {
                                            player.prepare();
                                            //stopLoading();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            //stopLoading();

                                        }
                                        Log.d("here", "done");
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                        //stopLoading();
                                    }
                                    //Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    //image.setImageBitmap(bMap);

                        stopLoading();
                                } else {
                                    // something went wronge.p
                                    e1.printStackTrace();
                                    stopLoading();
                                }
                            }
                        });

                    }
                    //ParseObject AudioUpload = new ParseObject("Audios");
                } else {
                    // something went wrong
                    Log.d("audionextErrors", "start");
                    e.printStackTrace();
                    stopLoading();


                }
            }
        });

    }

        else{
            startLoading();
            Log.d("insideaudio", "true");
            ParseQuery<ParseObject> queryAudio = ParseQuery.getQuery("Audios");
            queryAudio.whereEqualTo("userObjID", user.getObjectId().toString());
            queryAudio.addDescendingOrder("eventCounter");
            queryAudio.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        Log.d("RetriveaudionextErrors", parseObjects.get(0).getObjectId());
                        ParseFile sound = (ParseFile) parseObjects.get(0).get("AudioFile");
                        if (sound != null) {
                            sound.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, com.parse.ParseException e1) {
                                    if (e1 == null) {
                                        try {
                                            //fileInputStream = new FileInputStream(file);
                                            //fileInputStream.read(data);
                                            //fileInputStream.close();

                                            //convert array of bytes into file
                                            File file;
                                            String outputFile = null;
                                            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myrecording.3gp";
                                            file = new File(outputFile);

                                            FileOutputStream outputStream = new FileOutputStream(file);
                                            outputStream.write(data);
                                            outputStream.close();
                                            getInit();
                                            seekUpdation();
                                            //timeUpdation();
                                            try {
                                                player.prepare();
                                                stopLoading();

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                stopLoading();

                                            }
                                            Log.d("here", "done");
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                            stopLoading();
                                        }
                                        //Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        //image.setImageBitmap(bMap);


                                    } else {
                                        // something went wronge.p
                                        e1.printStackTrace();
                                        stopLoading();
                                    }
                                }
                            });

                        }
                        //stopLoading();

                        //ParseObject AudioUpload = new ParseObject("Audios");
                    } else {
                        // something went wrong
                        Log.d("audionextErrors", "start");
                        e.printStackTrace();
                        stopLoading();

                    }
                }
            });


        }
        /**
         *
         ParseQuery<ParseObject> queryAudio = ParseQuery.getQuery("Audios");
         queryAudio.whereEqualTo("userObjID", user.getObjectId().toString());
         queryAudio.addDescendingOrder("eventCounter");
         queryAudio.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
        if(e == null) {
         parseObjects.get(0).put("ScheduleDate", scheduleDate.getText().toString());
        parseObjects.get(0).put("ScheduleTime", scheduleTime .getText().toString());

         * }
         */

    }

    public void completed(View view) {
        date = scheduleDate.getText().toString();
        time = scheduleTime.getText().toString();
        Log.d("date", date);
        if (FromSummary.equals("no")) {
            startLoading();
            ParseQuery<ParseObject> queryAudio = ParseQuery.getQuery("Audios");
            queryAudio.whereEqualTo("userObjID", user.getObjectId().toString());
            queryAudio.addDescendingOrder("eventCounter");
            queryAudio.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    if (e == null) {
                        Log.d("datehere", date);
                        Log.d("timehere", time);

                        parseObjects.get(0).put("scheduleDate", date);
                        parseObjects.get(0).put("scheduleTime", time);
                        parseObjects.get(0).put("eventName", nameevent.getText().toString());
                        parseObjects.get(0).put("eventType", spinner.getSelectedItem().toString());
                        try {
                            parseObjects.get(0).save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
stopLoading();

                    } else {
                        e.printStackTrace();
                        stopLoading();
                    }
                }
            });
        }

        else {
            startLoading();
            ParseQuery<ParseObject> queryaudio = ParseQuery.getQuery("Audios");
            queryaudio.whereEqualTo("objectId", eventID);
            queryaudio.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        Log.d("datehere1", date);
                        Log.d("timehere1", time);

                        parseObject.put("scheduleDate", date);
                        parseObject.put("scheduleTime", time);
                        parseObject.put("eventName", nameevent.getText().toString());
                        parseObject.put("eventType", spinner.getSelectedItem().toString());
                        try {
                            parseObject.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        //parseObject.saveInBackground();
                        stopLoading();
                    }
                    else{
                        e.printStackTrace();
                        stopLoading();
                    }

                }
            });
        }

        Intent in =  new Intent(Summary.this,action.class);
        //in.putExtra("user_name", );
        startActivity(in);

    }

    public void backToRecording(View view) {
       /* File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/retrive.3gp");
        File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myrecording.3gp");
        file.renameTo(file2);
        Log.d("rename:", "succesfull");*/
        startLoading();
        ParseQuery<ParseObject> queryAudio = ParseQuery.getQuery("Audios");
        queryAudio.whereEqualTo("userObjID", user.getObjectId().toString());
        queryAudio.addDescendingOrder("eventCounter");
        queryAudio.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                   // Log.d("datehere", date);
                    //Log.d("timehere", time);

//                    parseObjects.get(0).put("scheduleDate", date);
  //                  parseObjects.get(0).put("scheduleTime", time);
                    parseObjects.get(0).put("eventName", nameevent.getText().toString());
                    parseObjects.get(0).put("eventType", spinner.getSelectedItem().toString());
                    try {
                        parseObjects.get(0).save();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    stopLoading();

                } else {
                    e.printStackTrace();
                    stopLoading();
                }
            }
        });
        Intent intent = new Intent(getBaseContext(), inviterecord.class);
        intent.setAction(Intent.ACTION_SEND);
        EventName = nameevent.getText().toString();
        EventType = spinner.getSelectedItem().toString();
        /*Intent prev = getIntent();
        Bundle B = prev.getExtras();
        String EventName=B.getString("EventName");
        String EventType=B.getString("EventType");*/
        Bundle newbundle = new Bundle();
        newbundle.putString("EventName",EventName);
        newbundle.putString("EventType",EventType);
        newbundle.putString("FromSummary", "yes");
        newbundle.putString("EventID", eventID);
        newbundle.putString("EventDate", date);
        newbundle.putString("EventTime", time);

        intent.putExtras(newbundle);
        startActivity(intent);
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            // Do something with the time chosen by the user set in textview

            final TextView time = (TextView) getActivity().findViewById(R.id.scheduleTime);
            //String a= (String)hour;
            time.setText(""+hour+":"+minute);

        }
    }


    public void showTimePickerDialog(View v) {
        // FragmentManager newfrag = new FragmentManager;

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            final TextView date = (TextView) getActivity().findViewById(R.id.scheduleDate);
            date.setText(""+day+"/"+month+"/"+year);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getInit() {
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
        timeDisplay = (TextView) findViewById(R.id.timeDisplay);
        play_button = (ImageButton) findViewById(R.id.play_button);
        pause_button = (ImageButton) findViewById(R.id.pause_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
                play_button.setVisibility(View.GONE);
                pause_button.setVisibility(View.VISIBLE);
            }
        });
        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
                pause_button.setVisibility(View.GONE);
                play_button.setVisibility(View.VISIBLE);
            }
        });
        File mp3File = new File(Environment.getExternalStorageDirectory(), "/myrecording.3gp");
        final Uri mediaUri = Uri.fromFile(mp3File);
        player = MediaPlayer.create(this, mediaUri);
        seek_bar.setMax(player.getDuration());
        showTimeCurrent(player.getDuration());
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try{
                    //player.stop();
                    //player.reset();
                    //player.setDataSource(, mediaUri);
                    player.pause();
                    player.seekTo(0);
                    pause_button.setVisibility(View.GONE);
                    play_button.setVisibility(View.VISIBLE);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });




    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            seekUpdation();


        }
    };
    //Runnable run2 = runOnUiThread()
    private void showTimeCurrent(long currentTime) {
        timeDisplay.setText(getTimeString(currentTime));

// /display current time of song in TextView with forme "hh:mm:ss"
    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    public void seekUpdation() {

        seek_bar.setProgress(player.getCurrentPosition());
        //Log.d("player123", "here" + player.getCurrentPosition());
        //timeDisplay.setText(player.getCurrentPosition());
        //Log.d("player123", timeDisplay.getText().toString());
        long CurrentTime = player.getCurrentPosition();
        showTimeCurrent(CurrentTime);
        seekHandler.postDelayed(run, 1000);


    }
    public void timeUpdation() {



        Log.d("player", "here" + player.getCurrentPosition());
        Log.d("player", timeDisplay.getText().toString());
            timeHandler.postDelayed(run, 1000);


        }

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







