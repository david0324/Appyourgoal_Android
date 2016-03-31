package app.appyourgoal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.activities.NotificationActivities;
import app.appyourgoal.activities.ProfileActivity;
import app.appyourgoal.adapters.GoalArrayAdapter;
import app.appyourgoal.restdata.Datum;
import app.appyourgoal.restdata.Example;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 10/18/2015.
 */
public class GoalsFragment extends Fragment {

    private static String LOG = "djevticAPP";

    private UserData mUser;
    private Example mGoalData;

    private Spinner mSpiner;
    private ListView mGoalListView;
    private GoalArrayAdapter mAdapter;
    private ImageView mUserIcon;
    private ImageView mBellIcon;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String mSorting = "Most Recent";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goals_layout, container, false);

        mUser = UserData.getInstance();
        //Initializing elements
        mSpiner = (Spinner) view.findViewById(R.id.goal_layout_selection_spiner);
        mGoalListView = (ListView) view.findViewById(R.id.goal_page_layout_list);
        mUserIcon = (ImageView) view.findViewById(R.id.goal_layout_user_icon);
        mBellIcon = (ImageView) view.findViewById(R.id.goal_layout_bell_icon);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.goals_layout_swipe_refresh);
        //Fill up Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.goal_screen_spinner_data, R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpiner.setAdapter(adapter);
        mSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG, "Spiner selected: " + parent.getItemAtPosition(position).toString());
                mSorting = parent.getItemAtPosition(position).toString();
                getData(mSorting);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        mGoalListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (mGoalListView != null && mGoalListView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mGoalListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mGoalListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(mSpiner.getSelectedItem().toString());
            }
        });

        mBellIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivities.class);
                startActivity(intent);
            }
        });

        //getData(mSorting);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(mSorting);
    }

    public void getData(String position){
        String URL = StaticURL.SERVER_URL + StaticURL.GET_ALL_VIDEOS_URL;
        /*if(position.equals("All")){
            URL = StaticURL.SERVER_URL + StaticURL.GET_ALL_VIDEOS_URL;
        }else */if(position.equals("Most Liked")){
            URL = StaticURL.SERVER_URL + StaticURL.GET_FAVORITES_VIDEOS_URL;
        }else if(position.equals("Most Recent")){
            URL = StaticURL.SERVER_URL + StaticURL.GET_RECENT_VIDEOS_URL;
        }
        Log.d(LOG,"GoalsFragment get Data: "+URL);
        Log.d(LOG, "GoalFragment get Data Autorisation token: " + mUser.getmTokenType() + " " + mUser.getmToken());
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());
        RequestParams params = new RequestParams();
        client.get(URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "On Goal success code: " + responseString);
                Gson gson = new Gson();
                mGoalData = gson.fromJson(responseString, Example.class);
                insertData();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "On failure code: " + statusCode);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    private void insertData() {

        mAdapter = new GoalArrayAdapter(getContext(), new ArrayList<Datum>(mGoalData.getData()));
        mGoalListView.setAdapter(mAdapter);
    }
}
