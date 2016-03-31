package app.appyourgoal.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.fragments.PrizeFragment;
import app.appyourgoal.fragments.ProfileGoalsFragment;
import app.appyourgoal.fragments.ProfileMedalsFragment;
import app.appyourgoal.restdata.EditDataExample;
import app.appyourgoal.restdata.Medal;
import app.appyourgoal.restdata.Prize;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 10/26/2015.
 */
public class ProfileActivity extends FragmentActivity {

    private String LOG = "djevticAPP";

    private FontHelper mFontHelper;
    private EditDataExample mEditData;
    private UserData mUser;
    public Prize mPrize;

    private ProfileActivity mActivity;

    private FragmentTabHost mTabHost;
    private RelativeLayout mBackgroundLayout;
    private ScrollView mScrolView;
    private TextView mPlayerName;
    private TextView mPlayerCountry;
    private TextView mProfileBack;
    private TextView mProfileProfile;
    private TextView mProfileEdit;
    private ImageView mProfileImage;
    private ImageView mStripe;
    private View mTab1;
    private View mTab2;
    private View mTab3;

    private String mUserID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_view);
        /*if(getIntent().getExtras() != null){
            mUserID = getIntent().getExtras().getString("userID");
            Log.d(LOG,"Profile userID : "+mUserID);
        }*/

        //Font helper for setting custom fonts
        mFontHelper = new FontHelper();

        mUser = UserData.getInstance();
        mPrize = null;
        mActivity = this;

        //Initializing fields
        mTabHost = (FragmentTabHost) findViewById(R.id.profile_tabhost);
        mBackgroundLayout = (RelativeLayout) findViewById(R.id.profile_view_relative_layout);
        mScrolView = (ScrollView) findViewById(R.id.profile_scroll_view);
        mPlayerName = (TextView) findViewById(R.id.profile_player_name);
        mPlayerCountry = (TextView) findViewById(R.id.profile_player_country);
        mProfileBack = (TextView) findViewById(R.id.profile_back);
        mProfileProfile = (TextView) findViewById(R.id.profile_profile);
        mProfileEdit = (TextView) findViewById(R.id.profile_edit);
        mProfileImage = (ImageView) findViewById(R.id.profile_player_image);
        mStripe = (ImageView) findViewById(R.id.profile_player_stripe);

        //Set fontface
        mFontHelper.setTextViewTypeface(mPlayerCountry, getApplicationContext());
        mFontHelper.setTextViewTypeface(mPlayerName, getApplicationContext());
        mFontHelper.setTextViewTypeface(mProfileBack, getApplicationContext());
        mFontHelper.setTextViewTypeface(mProfileEdit, getApplicationContext());
        mFontHelper.setTextViewTypeface(mProfileProfile, getApplicationContext());

        mTabHost.setup(this, getSupportFragmentManager(), R.id.profile_tabcontent);

        //Setting tabs in tab host
        mTab1 = getTabIndicator(mTabHost.getContext(), "GOALS");
        mTab2 = getTabIndicator(mTabHost.getContext(), "MEDALS");
        mTab3 = getTabIndicator(mTabHost.getContext(), "PRIZE");
        mTabHost.addTab(
                mTabHost.newTabSpec("Goals").setIndicator(mTab1),
                ProfileGoalsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Medals").setIndicator(mTab2),
                ProfileMedalsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Prize").setIndicator(mTab3),
                PrizeFragment.class, null);

        if(!mUserID.equals("")){
            mProfileEdit.setVisibility(View.GONE);
        }
        mTabHost.getTabWidget().setDividerDrawable(null);
    }



    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());
        client.get(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.USER_DETAILS_URL + mUserID, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in EditProfileActivity");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "EditProfileActivity Response post comment: " + responseString);
                Gson gson = new Gson();
                mEditData = gson.fromJson(responseString, EditDataExample.class);
                fillData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "EditProfileActivity On failure code: " + statusCode);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "EditProfileActivity On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    private void fillData() {
        mPlayerName.setText(mEditData.getData().getFirstName());
        mPlayerCountry.setText(mEditData.getData().getNationality());
        Picasso.with(mActivity).load(StaticURL.SERVER_URL + mEditData.getData().getProfilePicture()).error(R.drawable.male_avatar).placeholder(R.drawable.male_avatar).into(mProfileImage);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String arg0) {
                Log.d(LOG, "OnTabChange argument:" + arg0);
                Log.d(LOG, "Number of tabs:" + mTabHost.getTabWidget().getChildCount());
                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    if (tv != null) {
                        tv.setTextColor(Color.WHITE);
                    }
                }
                TextView tv = (TextView) mTabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                if (tv != null) {
                    tv.setTextColor(getResources().getColor(R.color.baby_blue));
                }
                mScrolView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });

        //Setting proper tab names
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            TextView myTextView = (TextView) mTabHost.getTabWidget().getChildTabViewAt(i).findViewById(R.id.textView);
            if (myTextView != null){
                if(i == 0){
                    if(mEditData.getData().getVideos().size() < 3){
                        myTextView.setText(mEditData.getData().getVideos().size()+" GOAL");
                    }else {
                        myTextView.setText(mEditData.getData().getVideos().size() + " GOALS");
                    }
                } else if(i == 1){
                    if(mEditData.getData().getMedals().size() < 3){
                        myTextView.setText(mEditData.getData().getMedals().size()+" MEDAL");
                    }else {
                        myTextView.setText(mEditData.getData().getMedals().size() + " MEDALS");
                    }
                } else if(i == 2){
                    int numOfPrizes = getNumberOfPrizes(mEditData.getData().getMedals());
                    if(numOfPrizes >0) {
                        if (numOfPrizes < 3) {
                            myTextView.setText(numOfPrizes + " PRIZE");
                        } else {
                            myTextView.setText(numOfPrizes + " PRIZES");
                        }
                    }else{
                        mTabHost.getTabWidget().getChildTabViewAt(2).setEnabled(false);
                        mTabHost.getTabWidget().getChildTabViewAt(2).setBackgroundColor(getResources().getColor(R.color.selected_item_bar_color));
                    }
                }
            }
        }
        mScrolView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private int getNumberOfPrizes(List<Medal> medals) {
        int result = 0;
        for (Medal medal : medals) {
            if(medal.getPrize()!=null){
                result = result + 1;
                mPrize = medal.getPrize();
            }
        }
        return result;
    }

    private View getTabIndicator(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        mFontHelper.setTextViewTypeface(tv, getApplicationContext());
        tv.setText(title);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        mTabHost.setCurrentTab(0);
        //mScrolView.fullScroll(ScrollView.FOCUS_DOWN);
        getData();
        //Set background if already chose
        if(mUser.getmColorBackground() != 0){
            mBackgroundLayout.setBackgroundColor(mUser.getmColorBackground());
        }
        if(mUser.getmStripeColor() != 0){
            mStripe.setColorFilter(mUser.getmStripeColor());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTabHost = null;
    }

    public void profileOnBack(View v){
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    public void profileOnEdit(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        mUser.setmTokenType(null);
        mUser.setmToken(null);
        mUser.setmRefreshToken(null);
        mUser = null;
        SharedPreferences sharedPref = getApplication().getSharedPreferences("appyourgoal", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", "");
        editor.putString("token_type", "");
        editor.putString("expires_in", "");
        editor.putString("issued", "");
        editor.putString("refresh_token", "");
        editor.putInt("stripeColor", 0);
        editor.putInt("backgroundColor", 0);
        editor.commit();
        Log.d(LOG, "Commit result is: " + sharedPref.edit().commit());
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_menu_edit:
                        profileOnEdit();
                        return true;
                    case R.id.profile_menu_logout:
                        logOut();
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more_menu, popup.getMenu());
        popup.show();
    }
}
