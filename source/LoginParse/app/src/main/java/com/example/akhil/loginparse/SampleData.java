package com.example.akhil.loginparse;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SampleData {
//public static final int SAMPLE_DATA_ITEM_COUNT = 6;

    public static ArrayList<String> generateSampleData() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        final String objID = currentUser.getObjectId().toString();


        final ArrayList<String> eventnames= new ArrayList<String>();
        final ArrayList<String> eventtypes= new ArrayList<String>();
        final ArrayList<String> eventid= new ArrayList<String>();

        final ArrayList<String> data = new ArrayList<String>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Audios");
        query.whereEqualTo("userObjID", objID);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
//s = parseObjects.size();
                    int size = parseObjects.size();
                    final int SAMPLE_DATA_ITEM_COUNT = size;

                    int i;
                    for(i = 0; i< size; i++){
                        eventnames.add(i, parseObjects.get(i).getString("eventName"));
                        eventtypes.add(i, parseObjects.get(i).getString("eventType"));
                        eventid.add(i, parseObjects.get(i).getString("objectId"));


                    }
//String[] events = {"Birthday", "Wedding", "Engagement", "Reception", "House Warming", "Anniversary"};
//data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);
                    Log.d("mdata2here1", eventnames.toString());

                    for (i = 0; i < 3; i++) {

                        String event = eventtypes.get(i).toString();

                        if(!event.startsWith("[")) {
                            data.add(event);
                        }
                    }

                }
                else{
                    e.printStackTrace();
                }

            }

        });
        Log.d("mdata2", eventnames.toString());
        Log.d("mdata2here2", data.toString());
        return data;
    }
}