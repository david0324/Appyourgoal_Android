package app.appyourgoal.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

import app.appyourgoal.R;
import app.appyourgoal.fragments.BlankFragment;
import app.appyourgoal.fragments.GoalsFragment;
import app.appyourgoal.fragments.PodiumFragment;

/**
 * Created by Dragisa on 10/18/2015.
 */
public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity_layout);

        //Initilaize elements
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String arg0) {
                if (arg0.equals("Uploads")) {
                    Intent camera = new Intent(getApplication(), CameraActivity.class);
                    startActivity(camera);
                }
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

            }
        });

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        //Remove devider
        mTabHost.getTabWidget().setDividerDrawable(null);

        //Setting tabs in tab host
        mTabHost.addTab(
                mTabHost.newTabSpec("Goals").setIndicator(getTabIndicator(mTabHost.getContext(), "GOALS")),
                GoalsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Podium").setIndicator(getTabIndicator(mTabHost.getContext(), "PODIUM")),
                PodiumFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Uploads").setIndicator(getTabIndicator(mTabHost.getContext(), "UPLOADS")),
                BlankFragment.class, null);
        mTabHost.setCurrentTab(1);
    }

    private View getTabIndicator(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(mTabHost.getCurrentTab() == 2){
            mTabHost.setCurrentTab(1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTabHost = null;
    }
}
