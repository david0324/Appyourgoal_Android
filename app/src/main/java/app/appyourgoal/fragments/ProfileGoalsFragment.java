package app.appyourgoal.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.adapters.VideoArrayAdapter;
import app.appyourgoal.restdata.EditDataExample;
import app.appyourgoal.restdata.Video;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 10/27/2015.
 */
public class ProfileGoalsFragment extends Fragment {

    private String LOG = "djevticAPP";

    private UserData mUser;
    private EditDataExample mEditData;

    private ListView mGoalListView;
    private VideoArrayAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_goal_layout, container, false);

        mUser = UserData.getInstance();
        //Initializing elements
        mGoalListView = (ListView) view.findViewById(R.id.goal_profle_page_layout_list);
        /*mGoalListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());
        client.get(getActivity(), StaticURL.SERVER_URL + StaticURL.USER_DETAILS_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "On Goal success code: " + responseString);
                Gson gson = new Gson();
                mEditData = gson.fromJson(responseString, EditDataExample.class);
                insertData();
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

        mAdapter = new VideoArrayAdapter(getContext(), new ArrayList<Video>(mEditData.getData().getVideos()), mEditData);
        mGoalListView.setAdapter(mAdapter);
    }

}
