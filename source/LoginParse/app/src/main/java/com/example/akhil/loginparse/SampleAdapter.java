package com.example.akhil.loginparse;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightTextView;

import java.util.ArrayList;
/***
 * ADAPTER
 */
public class SampleAdapter extends ArrayAdapter<String> {
    private static final String TAG = "SampleAdapter";
    private ProgressDialog proDialog;

    static class ViewHolder {
        DynamicHeightTextView txtLineOne;
        DynamicHeightTextView objID;

    }

    private final LayoutInflater mLayoutInflater;
    private final ArrayList<Integer> mBackgroundColors;

    public SampleAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.drawable.cardlibs_anniversary);
        mBackgroundColors.add(R.drawable.cardlibs_birthday);
        mBackgroundColors.add(R.drawable.cardlibs_engagement);
        mBackgroundColors.add(R.drawable.cardlibs_housewarming);
	    mBackgroundColors.add(R.drawable.cardlibs_reception);
        mBackgroundColors.add(R.drawable.cardlibs_wedding);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
            //vh.objID = (DynamicHeightTextView) convertView.findViewById(R.id.objID_line);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String str = getItem(position);
        String[] parts = str.split("::");
        String eventtype = parts[2];
        Integer i = 1;
        if(eventtype.equals("Birthday")){
            i = 1;
        }
        else if(eventtype.equals("Anniversary")){
            i = 0;

        }
        else if(eventtype.equals("Reception")){
            i = 4;
        }
        else if(eventtype.equals("Wedding")){
            i = 5;
        }
        else if(eventtype.equals("Engagement")){
            i = 2;
        }
        else if(eventtype.equals("HouseWarming")){
            i = 3;
        }
        Log.d("ivaluehere", i.toString());
       convertView.setBackgroundResource(mBackgroundColors.get(i));
        /*startLoading();
        vh.objID.setHeightRatio(0.000);
        vh.objID.setText(getItem(position));
        String objid = getItem(position);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Audios");
        query.whereEqualTo("objectId", objid);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {*/

        vh.txtLineOne.setText(parts[0]+ "\n" + parts[2] );
        vh.txtLineOne.setHeightRatio(0.962);
        Log.d("mdatamdata", parts[1]);

                /*stopLoading();
            }
        });*/


        return convertView;
    }

/*    protected void startLoading() {

        proDialog = new ProgressDialog(getContext());
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
*/
}
