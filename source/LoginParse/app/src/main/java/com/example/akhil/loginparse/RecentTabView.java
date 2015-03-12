package com.example.akhil.loginparse;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RecentTabView extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// TODO Auto-generated method stub
        ParseUser currentUser = ParseUser.getCurrentUser();
        final String objID = currentUser.getObjectId().toString();


        final ArrayList eventnames= new ArrayList<String>();
        final ArrayList eventtypes= new ArrayList<String>();
        final ArrayList eventid= new ArrayList<String>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Audios");
        query.whereEqualTo("userObjID", objID);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e == null){
                    //s = parseObjects.size();
                    int size = parseObjects.size();

                    int i;
                    for(i = 0; i< size; i++){
                        eventnames.add(i, parseObjects.get(i).getString("eventName"));
                        eventtypes.add(i, parseObjects.get(i).getString("eventtype"));
                        eventid.add(i, parseObjects.get(i).getString("objectId"));


                    }
                    Log.d("eventcounter", "im here");
                for(i=0; i< size ; i++){
                    Log.i("eventnames", eventnames.get(i).toString());
                }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
        return inflater.inflate(R.layout.recent_layout, container,false);
    }


}