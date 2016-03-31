package app.appyourgoal.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.adapters.SectionsPagerAdapter;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

public class InitialActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private ImageView mInitPage1;
    private ImageView mInitPage2;

    private Button mLoginButton;
    private Button mSignUpBooton;

    private ProgressBar mProgressBar;
    private LinearLayout mLinearLayout;

    private UserData mUser;
    private InitialActivity mThis;

    private String LOG = "djevticAPP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        mUser = UserData.getInstance();
        mThis = this;
        Log.d("djevticAPP", "mUser.token: " + mUser.getmToken());
        SharedPreferences sharedPref = getApplication().getSharedPreferences("appyourgoal", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        String tokenType = sharedPref.getString("token_type", "");
        String refreshToken = sharedPref.getString("refresh_token", "");
        String expiresIn = sharedPref.getString("expires_in", "");
        String issued = sharedPref.getString("issued", "");
        int stripeColor = sharedPref.getInt("stripeColor", 0);
        int backgroundColor = sharedPref.getInt("backgroundColor", 0);
        Log.d("djevticAPP", "sharedPreferences.token: " + token);
        Log.d("djevticAPP", "sharedPreferences.expiresIn: " + expiresIn);

        //Initialize FontHelper
        FontHelper fontHelper = new FontHelper();

        //Initializing components
        mInitPage1 = (ImageView) findViewById(R.id.initial_paginator_1);
        mInitPage2 = (ImageView) findViewById(R.id.initial_paginator_2);
        mLoginButton = (Button) findViewById(R.id.start_screen_login_button);
        mSignUpBooton = (Button) findViewById(R.id.start_screen_sign_up_button);
        mLinearLayout = (LinearLayout) findViewById(R.id.initial_buttons_linear);
        mProgressBar = (ProgressBar) findViewById(R.id.initial_progress_bar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //mSectionsPagerAdapter.registerCallback(this);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fragmentShown(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if(!token.isEmpty()){
            if(stripeColor != 0){
                mUser.setmStripeColor(stripeColor);
            }
            if (backgroundColor != 0){
                mUser.setmColorBackground(backgroundColor);
            }
            if((new Date()).after(getTime(issued, expiresIn))){
                mLinearLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                performeRefresh(token, refreshToken);
            }else {
                mUser.setmToken(token);
                mUser.setmTokenType(tokenType);
                Intent main = new Intent(getApplication(), MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
            }
        }

        //Setting custom font for components
        fontHelper.setButtonTypeface(mLoginButton, getApplicationContext());
        fontHelper.setButtonTypeface(mSignUpBooton, getApplicationContext());
    }

    private void performeRefresh(String token, String refreshToken) {
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setBasicAuth("Authorization", mUser.getmTokenType()+" "+mUser.getmToken());
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "Bearer " + token);
        RequestParams params = new RequestParams();
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", refreshToken);
        params.put("client_id", "mobileapp");
        params.put("client_secret", "mobileappsecret");
        client.post(StaticURL.SERVER_URL + StaticURL.TOKEN_URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in performeRefresh");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "performeRefresh Response: " + responseString);
                try {
                    JSONObject json = new JSONObject(responseString);
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("appyourgoal", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", json.getString("access_token"));
                    editor.putString("token_type", json.getString("token_type"));
                    editor.putString("expires_in", json.getString("expires_in"));
                    editor.putString("issued", json.getString("issued"));
                    editor.putString("refresh_token", json.getString("refresh_token"));
                    editor.commit();
                    mUser.setmToken(json.getString("access_token"));
                    mUser.setmTokenType(json.getString("token_type"));
                    mUser.setmRefreshToken(json.getString("refresh_token"));
                    mLinearLayout.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    Intent main = new Intent(getApplication(), MainActivity.class);
                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mLinearLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "On performeRefresh failure code: " + statusCode);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                mLinearLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On performeRefresh retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    // @Override
    public void fragmentShown(int position){
        switch (position){
            case 0:
                mInitPage1.setImageResource(R.drawable.initial_paginator_blue);
                mInitPage2.setImageResource(R.drawable.initial_paginator_light_gray);
                break;
            case 1:
                mInitPage1.setImageResource(R.drawable.initial_paginator_light_gray);
                mInitPage2.setImageResource(R.drawable.initial_paginator_blue);
                break;
        }
    }

    public void onLoginClicked(View v){
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    public void onSignUpCLicked(View v){
        Intent login = new Intent(this, SignUpActivity.class);
        startActivity(login);
    }

    private Date getTime(String timeString, String offset){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer delay = Integer.getInteger(offset, 86400);
        Log.d(LOG, "Offset: "+offset+" delay:"+delay);
        delay = delay - 7200; // -2 houres
        Date registrationDate = new Date();
        try {
            registrationDate = input.parse(timeString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(registrationDate);
            calendar.add(Calendar.SECOND, delay);
            registrationDate = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return registrationDate;
    }

}
