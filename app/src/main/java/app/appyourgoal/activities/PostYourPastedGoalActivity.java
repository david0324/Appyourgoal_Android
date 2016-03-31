package app.appyourgoal.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.MonitoringEditText;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 11/7/2015.
 */
public class PostYourPastedGoalActivity extends Activity {

    private String LOG = "djevticAPP";

    private UserData mUser;
    private PostYourPastedGoalActivity mActivity;
    private FontHelper mFontHelper;

    private ImageView mImage;
    private TextView mCancel;
    private TextView mUrl;
    private Button mButton;
    private MonitoringEditText mEditText;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_your_goal_from_web);

        //Font helper for setting custom fonts
        FontHelper mFontHelper = new FontHelper();

        mUser = UserData.getInstance();
        mActivity = this;

        //Initialize fields
        mImage = (ImageView) findViewById(R.id.post_your_goal_image);
        mCancel = (TextView) findViewById(R.id.post_your_goal_cancel);
        mUrl = (TextView) findViewById(R.id.post_your_goal_url);
        mButton = (Button) findViewById(R.id.post_your_goal_button);
        mProgressBar = (ProgressBar) findViewById(R.id.post_your_goal_progress);
        mEditText = (MonitoringEditText) findViewById(R.id.post_your_goal_paste);
        mEditText.setmActivity(this);

        Bundle extras = getIntent().getExtras();
        String value = "";


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
            }
        });

        //Setting font type
        mFontHelper.setTextViewTypeface(mCancel, getApplicationContext());
        mFontHelper.setTextViewTypeface(mUrl, getApplicationContext());
        mFontHelper.setButtonTypeface(mButton, getApplicationContext());
        mFontHelper.setEditTextTypeface(mEditText, getApplicationContext());
    }

    public void onCancelPostGoalVideo(View v){
        Intent main = new Intent(getApplication(), CameraActivity.class);
        startActivity(main);
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(getApplication(), CameraActivity.class);
        startActivity(main);
    }

    public void setYouTubeImage(){
        Picasso.with(this).load(getPictureUrl()).into(mImage);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mEditText.getText().toString()));
                intent.putExtra("force_fullscreen", true);
                startActivity(intent);
                Log.d(LOG, "Video Playing....");
            }
        });
    }

    private String getPictureUrl(){
        String[] slice = mEditText.getText().toString().split("=");
        if(slice.length != 1) {
            return "http://img.youtube.com/vi/" + slice[slice.length - 1] + "/hqdefault.jpg";
        }else{
            slice = mEditText.getText().toString().split("/");
            return "http://img.youtube.com/vi/" + slice[slice.length - 1] + "/hqdefault.jpg";
        }
    }

    public void onResume() {
        super.onResume();
        if(!mEditText.getText().toString().isEmpty()){
            setYouTubeImage();
        }
    }

    private void uploadVideo() {
        String link = mEditText.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "multipart/form-data");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());
        RequestParams params = new RequestParams();
        params.setForceMultipartEntityContentType(true);
        params.put("link", link);

        Log.d(LOG,"Link:"+link);
        Log.d(LOG, "Upload Video: " + params.toString());
        client.post(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.UPLOAD_VIDEO_URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in PostYourGoalActivity");
                mButton.setVisibility(View.GONE);
                mImage.setVisibility(View.GONE);
                mEditText.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "PostYourPostedGoalActivity Response post comment: " + responseString);
                mButton.setVisibility(View.VISIBLE);
                mImage.setVisibility(View.VISIBLE);
                mEditText.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Video posted ", Toast.LENGTH_LONG).show();
                Intent main = new Intent(getApplication(), MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                mButton.setVisibility(View.VISIBLE);
                mImage.setVisibility(View.VISIBLE);
                mEditText.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Unable to post video ", Toast.LENGTH_LONG).show();
                Log.d(LOG, "PostYourPastedGoalActivity ERROR statusCode: " + statusCode);
                String responseString = new String(errorResponse);
                Log.d(LOG, "PostYourPastedGoalActivity ERROR responseString: " + responseString);
                Log.d(LOG, "PostYourPastedGoalActivity ERROR e.getMessage(): " + e.getMessage());
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "PostYourGoalActivity On retry: " + retryNo);
                Toast.makeText(getApplicationContext(), "Retry posting video "+retryNo, Toast.LENGTH_LONG).show();
                // called when request is retried
            }
        });
    }
}