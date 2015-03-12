package com.example.akhil.loginparse;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class action extends FragmentActivity implements ActionBar.TabListener{

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        //actionBar.setIcon(android.R.color.transparent);
        // actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.set
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFE4E4E4));
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.logo);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
                | Gravity.CENTER_VERTICAL);
        //layoutParams.rightMargin = 40;

        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
        Parse.initialize(this, "0wcbdjeYZE09yoDti4z3G1KGSXbrCmsso0N36lZ3", "ujP59dnSbYwvBtHX0QHdCFjLX9kGIkqtoLN7GwEN");
        ParseFacebookUtils.initialize(getString(R.string.app_id));

        //actionBar.set
        Session session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            makeMeRequest();
        }
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        //mViewPager.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#545454")));
        //actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#545454")));
        //actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#eb460f")));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.fblogin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.action_logout) {
            // Log out the current user
            ParseUser.logOut();
            // Create a new anonymous user
            ParseAnonymousUtils.logIn(null);
            // Update the logged in label info
            //updateLoggedInInfo();
            // Clear the view
            //todoListAdapter.clear();
            // Unpin all the current objects
            //ParseObject
              //      .unpinAllInBackground(TodoListApplication.TODO_GROUP_NAME);
            Intent in =  new Intent(action.this,fblogin.class);
            //in.putExtra("user_name", );
            startActivity(in);

        }
        if(item.getItemId() == R.id.action_about){
            String url = "http://www.yourevent.co/about";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }

        if(item.getItemId() == R.id.action_contact){
            String url = "http://www.yourevent.co/about#contact";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {
                            // Create a JSON object to hold the profile info
                            JSONObject userProfile = new JSONObject();
                            try {
                                // Populate the JSON object
                                userProfile.put("facebookId", user.getId());
                                userProfile.put("name", user.getName());


                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser
                                        .getCurrentUser();
                                currentUser.put("profile", userProfile);
                                currentUser.saveInBackground();

                                // Show the user info
                                updateViewsWithProfileInfo();
                            } catch (JSONException e) {
                                Log.d("MyApp",
                                        "Error parsing returned user data.");
                            }

                        } else if (response.getError() != null) {
                            if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
                                    || (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
                                Log.d("MyApp",
                                        "The facebook session was invalidated.");
                                onLogoutButtonClicked();
                            } else {
                                Log.d("MyApp",
                                        "Some other error: "
                                                + response.getError()
                                                .getErrorMessage());
                            }
                        }
                    }
                });
        request.executeAsync();

    }


    private void updateViewsWithProfileInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.get("profile") != null) {
            JSONObject userProfile = currentUser.getJSONObject("profile");
            try {

                if (userProfile.getString("name") != null) {
                    //   userNameView.setText(userProfile.getString("name"));
                    Toast.makeText(action.this,
                            "Hello " + userProfile.getString("name"), Toast.LENGTH_LONG).show();
                 //   username = userProfile.getString("name");
                    currentUser.setUsername(userProfile.getString("name"));
                    //     Toast.makeText(AndroidVideoCapture.this,
                    //           "Fail in prepareMediaRecorder()!\n - Ended -",
                } else {
                    Toast.makeText(action.this,
                            "Logged In", Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                Log.d("MyApp",
                        "Error parsing saved user data.");
            }

        }
    }


    private void onLogoutButtonClicked() {
        // Log the user out
        ParseUser.logOut();

        // Go to the login view
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, fblogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new LaunchpadSectionFragment();
                    //return new RecentTabView();
                case 1:

                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    return new SelectEventFragment();
                    // return new RecentTabView();

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
		if(position == 0){            
		return "Add Event";

	}
		else{
	return "Recent";
}
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {

        private ParseUser currentUser = ParseUser.getCurrentUser();
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
            final TextView userGreetings = (TextView) rootView.findViewById(R.id.textView2);

            userGreetings.setText("Hello " + currentUser.getUsername().toString() + " !");

            //TextView text =(TextView) getActivity().findViewById(R.id.textViewEventName);
            //final ParseUser currentUser = ParseUser.getCurrentUser();
            //text.setText(currentUser.getUsername());
            setSpinnerContent(rootView);
            final String S;
            // Demonstration of a collection-browsing activity.
            rootView.findViewById(R.id.demo_collection_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(getActivity(), inviterecord.class);
                            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                            final Spinner sp = (Spinner) getActivity().findViewById(R.id.event_type);
                            final String objID = currentUser.getObjectId().toString();
                            final TextView eventName = (TextView) getActivity().findViewById(R.id.eventName);
                            int f=0;
                            if(eventName.getText().toString().trim().equals(""))
                            {
                                eventName.setError( "Event name is required!" );
                                f=1;
                            }
                            else{
                                f=0;
                            }

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Audios");
                            query.whereEqualTo("userObjID", objID);
                            query.addDescendingOrder("eventCounter");
                            query.findInBackground(new FindCallback<ParseObject>() {

                                @Override
                                public void done(List<ParseObject> parseObjects, ParseException e) {
                                    try {
                                        if (e == null) {
                                            Integer s = parseObjects.get(0).getInt("eventCounter") + 1;
                                            final ParseObject event = new ParseObject("Audios");
                                            String selected_text = "none";
                                            if (sp.getSelectedItem() != null) {
                                                selected_text = sp.getSelectedItem().toString();
                                            }
                                            event.put("eventType", selected_text);
                                            selected_text = "none";
                                            if (eventName.getText() != null) {
                                                selected_text = eventName.getText().toString();
                                            }
                                            event.put("eventName", selected_text);
                                            event.put("userObjID", objID);
                                            event.put("eventCounter", s);
                                            event.saveInBackground();
                                            Log.d("eventcounter", s.toString());
                                        }
                                        else{
                                            e.printStackTrace();
                                        }
                                    }
                                    catch(Exception e2){
                                        final ParseObject event = new ParseObject("Audios");
                                        String selected_text = "none";
                                        if (sp.getSelectedItem() != null)
                                        {
                                            selected_text = sp.getSelectedItem().toString();
                                        }
                                        event.put("eventType", selected_text);
                                        selected_text = "none";
                                        if (eventName.getText() != null)
                                        {
                                            selected_text = eventName.getText().toString();
                                        }
                                        event.put("eventName", selected_text);
                                        event.put("userObjID", objID);
                                        event.put("eventCounter", 1);
                                        event.saveInBackground();
                                        //Log.d("eventcounter", s.toString());
                                    }
                                }
                            });
                           // Intent intent = new Intent(getActivity(), inviterecord.class);
                            //Toast.makeText(getAppli = cationContext(), "", Toast.LENGTH_SHORT).show();
                            Bundle A = new Bundle();
                            Spinner k = (Spinner) getActivity().findViewById(R.id.event_type);
                            EditText b = (EditText)getActivity().findViewById(R.id.eventName);
                            A.putString("EventName",b.getText().toString());
                            A.putString("EventType",k.getSelectedItem().toString());
                            A.putString("FromSummary", "no");
                            A.putString("EventID", null);
                            intent.putExtras(A);
                            if(f==0) {
                                startActivity(intent);
                            }


                        }
                    });


            return rootView;
        }
        private void setSpinnerContent( View view )
        {
            Spinner spinner = (Spinner) view.findViewById( R.id.event_type );
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( getActivity(), R.array.event_types , android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter( adapter );
        }

    }


    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ParseUser currentUser = ParseUser.getCurrentUser();
            String welcomeMessage;
            welcomeMessage = "Hello  " + currentUser.getUsername() + ", swipe thru the app ";
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    welcomeMessage);
            return rootView;
        }
    }


}
