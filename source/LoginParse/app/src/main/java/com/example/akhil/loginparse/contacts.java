package com.example.akhil.loginparse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class contacts extends Activity implements Comparable<contacts> {


    ArrayAdapter<contact> adapter;
    private ParseUser user = ParseUser.getCurrentUser();
    ArrayList selectedcontacts = new ArrayList<String>();
    ArrayList names = new ArrayList<contact>();
    ArrayList previousContacts = new ArrayList<String>();
    String EventName, EventType, FromSummary, eventID, datehere, timehere;
    private ProgressDialog proDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#545454")));
        actionBar.setDisplayHomeAsUpEnabled(false);
        Intent prev = getIntent();
        Bundle B=prev.getExtras();
        EventName=B.getString("EventName");
        EventType=B.getString("EventType");
        FromSummary = B.getString("FromSummary");
        eventID = B.getString("EventID");
        if(FromSummary.equals("yes")){
            datehere = B.getString("EventDate");
            timehere = B.getString("EventTime");
        }
    /*    if(FromSummary.equals("yes")){
        startLoading();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Audios");
        query.whereEqualTo("objectId", eventID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
try{
                if (e == null) {


                    previousContacts.addAll(parseObject.getList("selectedContacts"));

                    Log.d("previousContacts", previousContacts.toString());
                    stopLoading();
                } else {
                    e.printStackTrace();
                    stopLoading();
                }
            }
catch (Exception e3){
    e3.printStackTrace();
}
            }
        });


        }
*/

       // ArrayList names = new ArrayList<contact>();
        EditText inputSearch;
        LayoutInflater inflater;
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_contacts);
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                //String id = cur.getString(
                //        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //TextView temp = new TextView(this);
                //temp.setText(name);

                //EditText temp = (EditText) findViewById(R.id.contactname);
                //temp.setText(name);
                //System.out.print(name);
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //System.out.print(name);
                    int f=0;

                    if (FromSummary.equals("yes")){

                        //Iterator<String> it = previousContacts.iterator();

                        for(int i=0;i<previousContacts.size();i++)
                        {

                            String obj = previousContacts.get(i).toString();

                            if (obj.equals(number)){

                                f=1;

                            }

                        }

                    }

                    contact con;

                    if (f==0) {

                        con = new contact(name, number, false);

                    }

                    else {

                        con = new contact(name, number, true);

                    }

                    names.add(con);
                }
                //return names;
            }

            contact[] arrname = new contact[names.size()];
            //contact variable = arrname[1];
           /* Toast.makeText(getApplicationContext(),
                    arrname[0].name,
                    Toast.LENGTH_LONG).show();*/
            names.toArray(arrname);
            //Toast.makeText(this,dsf[1],Toast.LENGTH_SHORT);
            Arrays.sort(arrname);
            Collections.sort(names, new Comparator<contact>(){
                @Override
                public int compare(contact a, contact b)
                {
                    return a.name.compareTo(b.name);
                }
            });
            ListView tem = (ListView) findViewById(R.id.contactnames);
            inputSearch = (EditText) findViewById(R.id.inputSearch);
            //adapter = new ArrayAdapter<contact>(this, android.R.layout.simple_list_item_1,
            //        android.R.id.text1, arrname);
            adapter = new CustomAdapter(this, R.layout.contact_info, names);
            //CustomAdapter adapter = new CustomAdapter(this, R.layout.activity_fetch_contacts
            //              , names);
            tem.setAdapter(adapter);



            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    contacts.this.adapter.getFilter().filter(cs);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
            //setListAdapter(adapter);
        }


        Button next2 = (Button) findViewById(R.id.NextStep);
        next2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

if(FromSummary.equals("no")) {
    startLoading();
    Log.d("contacts", selectedcontacts.toString());
    ParseQuery<ParseObject> queryAudio = ParseQuery.getQuery("Audios");
    queryAudio.whereEqualTo("userObjID", user.getObjectId().toString());
    queryAudio.addDescendingOrder("eventCounter");
    queryAudio.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> parseObjects, ParseException e) {
            if (e == null) {
                Log.d("1audionextErrors", parseObjects.get(0).getObjectId());
                parseObjects.get(0).put("selectedContacts", selectedcontacts);
                parseObjects.get(0).saveInBackground();
                stopLoading();

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
                else{
    startLoading();
    ParseQuery<ParseObject> queryaudio = ParseQuery.getQuery("Audios");
    queryaudio.whereEqualTo("objectId", eventID);
    queryaudio.getFirstInBackground(new GetCallback<ParseObject>() {
        @Override
        public void done(ParseObject parseObject, ParseException e) {
            if (e == null) {
                Log.d("1audionextErrors", parseObject.getObjectId());
                parseObject.put("selectedContacts", selectedcontacts);
                parseObject.saveInBackground();
stopLoading();
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
                // Log.w("abc",B.getString("EventName").toString());*/

                Intent intent = new Intent(contacts.this, Summary.class);
                Bundle Bundlename = new Bundle();
                Bundlename.putString("EventName",EventName);
                Bundlename.putString("EventType",EventType);
                Bundlename.putString("FromSummary", FromSummary);
                Bundlename.putString("EventID", eventID);
                if(FromSummary == "yes"){
                    Bundlename.putString("EventDate", datehere);
                    Bundlename.putString("EventTime", timehere);
                }
                intent.putExtras(Bundlename);
                startActivity(intent);


            }
        });
    }
    //here is checkbox code

    private class CustomAdapter extends ArrayAdapter<contact> {

        private ArrayList<contact> contactlist;
        private ArrayList<contact> filtered;
        public CustomAdapter(Context context, int textViewResourceId, ArrayList<contact> contactlist) {
            super(context, textViewResourceId, contactlist);
            this.filtered = contactlist;
            this.contactlist = new ArrayList<contact>();
            this.contactlist.addAll(contactlist);
        }
        private class ViewHolder {
            //TextView code;
            CheckBox name;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.contact_info, null);

                holder = new ViewHolder();
                //  holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        contact contactselected = (contact) cb.getTag();
                        /*Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG
                        ).show();*/
                        contactselected.selected = true;
                        selectedcontacts.add(contactselected.number);
                        //country.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            contact country = filtered.get(position);
            //holder.code.setText(country.name);
            holder.name.setText(country.name);
            holder.name.setChecked(country.selected);
            holder.name.setTag(country);

            return convertView;

        }

        //checkbox filter
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence cons) {
                FilterResults filterResults = new FilterResults();
                ArrayList<contact> tempList = new ArrayList<contact>();
                contactlist = names;
                if(cons != null && contactlist != null) {
                    int length = contactlist.size();
                    Log.v("contactlist size-", String.valueOf(length));
                    int i=0;
                    while(i<length){
                        contact item = contactlist.get(i);
                        if(item.name.toLowerCase().contains(cons)) {
                            tempList.add(item);
                        }
                        i++;
                    }
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                return filterResults;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence cons, FilterResults results){
                filtered = (ArrayList<contact>)results.values;
                if(results.count>0){
                    notifyDataSetChanged();
                    clear();
                    Log.v("Filter size-", String.valueOf(filtered.size()));
                    for(int i = 0, l = filtered.size(); i < l; i++)
                        add(filtered.get(i));
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        @Override
        public Filter getFilter() {
            return myFilter;
        }

    }

    //here it finishes

    /*class MySalaryComp implements Comparator<ArrayList<String>> {

        @Override
        public int compare(ArrayList<String> e1, ArrayList<String> e2) {
            return e1.get(1).compareTo(e2.get(1));
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contacts, menu);
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

    @Override
    public int compareTo(contacts another) {
        return 0;
    }


    /*public static Map<String> getPhonebook(Context context)
    {
        Map<String> result = new HashMap<String>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(cursor.moveToNext())
        {
            int name_id = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String name = cursor.getString(name_id);
            result.put(name);
        }
        return result;
    }*/

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
