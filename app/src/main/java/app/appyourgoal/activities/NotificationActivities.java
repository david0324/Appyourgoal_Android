package app.appyourgoal.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.adapters.NotificationArrayAdapter;
import app.appyourgoal.restdata.Notification;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 11/14/2015.
 */
public class NotificationActivities extends Activity {

    private String LOG = "djevticAPP";

    private ListView mList;
    private TextView mBack;
    private TextView mCaption;

    private UserData mUser;
    private FontHelper mFontHelper;
    private Notification mNotification;
    private NotificationArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_layout);

        mUser = UserData.getInstance();
        mFontHelper = new FontHelper();

        mList = (ListView) findViewById(R.id.notification_list);
        mBack = (TextView) findViewById(R.id.notification_page_layout_back);
        mCaption = (TextView) findViewById(R.id.notofication_page_layout_notification);

        mFontHelper.setTextViewTypeface(mBack, getApplicationContext());
        mFontHelper.setTextViewTypeface(mCaption, getApplicationContext());

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplication(), MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
            }
        });
        getData();
    }

    public void getData(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());

        client.get(StaticURL.BORNEO_SERVER_URL + StaticURL.NOTIFICATION_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in Posium");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "Response: " + responseString);
                Gson gson = new Gson();
                mNotification = gson.fromJson(responseString, Notification.class);
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
        mAdapter = new NotificationArrayAdapter(getApplicationContext(), R.layout.notifications_layout, mNotification.getData());
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(getApplication(), MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
    }
}
