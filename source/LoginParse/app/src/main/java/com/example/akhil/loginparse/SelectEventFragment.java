package com.example.akhil.loginparse;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SelectEventFragment extends Fragment implements


        AbsListView.OnItemClickListener {
    private StaggeredGridView mGridView;
    public final static String EXTRA_MESSAGE = "";
    private SampleAdapter mAdapter;
    private ArrayList<String> mData;
    final ArrayList<String> eventnames= new ArrayList<String>();
    final ArrayList<String> eventtypes= new ArrayList<String>();
    final ArrayList<String> eventid= new ArrayList<String>();
    private ProgressDialog proDialog;
    private int Flag=0;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_sgv, container, false);
    }
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ParseUser currentUser = ParseUser.getCurrentUser();
        final String objID = currentUser.getObjectId().toString();

        Flag = 0;
        startLoading(); //Show the loading image

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Audios");
        query.whereEqualTo("userObjID", objID);
        query.addDescendingOrder("eventCounter");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    //s = parseObjects.size();
                    int size = parseObjects.size();
                    String eventname;
                    int i, count =0;
                    for(i = 0; i< size; i++){
                        eventname = parseObjects.get(i).getString("eventName");
                                if(!(eventname.trim() == " " || eventname.trim() == "" || eventname == null)) {
                                    //eventnames.add(i, eventname);
                                    //eventtypes.add(i, parseObjects.get(i).getString("eventType"));
                                    eventid.add(i, eventname + "::" +  parseObjects.get(i).getObjectId().toString() + "::" + parseObjects.get(i).getString("eventType") + "::" + parseObjects.get(i).getString("scheduleDate") + "::" + parseObjects.get(i).getString("scheduleTime"));

                                    count++;
                                }
                        if(count == 10){
                            break;
                        }

                    }
                    //Log.d("mdata1fragment", eventnames.toString());
                    Log.d("mdata1fragment", eventid.toString());


                    mGridView = (StaggeredGridView) getView().findViewById(R.id.grid_view);
                    if (savedInstanceState == null) {
                    }
                    if (mAdapter == null) {
                        mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);
                    }
                    if (mData == null) {
                        mData = eventid;
                    }
                    Log.d("mdataafterfragment", mData.toString());
                    for (String data : mData) {
                        mAdapter.add(data);
                    }

                    mGridView.setAdapter(mAdapter);
                    stopLoading();
                    //mGridView.setOnItemClickListener();
                    /*Log.d("eventcounter", "im here");

                    for(i=0; i< size ; i++){
                        Log.i("eventnames", eventnames.get(i).toString());
                    }*/
                }
                else{
                    e.printStackTrace();
                    stopLoading();
                }
            }
        });

if(!proDialog.isShowing()){
    mGridView.setOnItemClickListener(this);
}

    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //Intent i;
        //i = new Intent("android.intent.action.INVITERECORD");
        //i.putExtra(EXTRA_MESSAGE, Integer.toString(position));
        //startActivity(i);
        String str = String.valueOf(adapterView.getItemAtPosition(position));
        String[] parts = str.split("::");
        //final String eventID = parts[1];

       /* ParseQuery<ParseObject> query = ParseQuery.getQuery("Audios");
        query.whereEqualTo("objectId", eventID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null){

                    Log.d("eventid", eventID);
                }
            else{
                    e.printStackTrace();;
                }
            }
        });*/

        Intent intent = new Intent(getActivity(), Summary.class);
        final Bundle A = new Bundle();
        A.putString("EventName",parts[0]);
        A.putString("EventType",parts[2]);
        A.putString("FromSummary", "yes");
        A.putString("EventID", parts[1]);
        A.putString("EventDate", parts[3]);
        A.putString("EventTime", parts[4]);
        intent.putExtras(A);
        startActivity(intent);


            }

    protected void startLoading() {

        proDialog = new ProgressDialog(getActivity());
        proDialog.setMessage("loading...");
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    protected void stopLoading() {
        proDialog.dismiss();
        proDialog = null;
       // Flag = 1;
        mGridView.setOnItemClickListener(this);

    }
}


