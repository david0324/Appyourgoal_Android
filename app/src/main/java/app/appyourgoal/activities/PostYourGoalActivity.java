package app.appyourgoal.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 10/25/2015.
 */
public class PostYourGoalActivity extends Activity {

    private String LOG = "djevticAPP";

    private UserData mUser;
    private PostYourGoalActivity mActivity;
    private String videoURL;
    private FontHelper mFontHelper;
    private PowerManager.WakeLock mWakeLock;

    private ImageView mImage;
    private TextView mCancel;
    private TextView mUrl;
    private Button mButton;
    private ProgressBar mProgress;
    private TextView mUploadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_your_goal_layout);

        //Font helper for setting custom fonts
        FontHelper mFontHelper = new FontHelper();

        mUser = UserData.getInstance();
        mActivity = this;

        //Initialize fields
        mImage = (ImageView) findViewById(R.id.post_your_goal_image);
        mCancel = (TextView) findViewById(R.id.post_your_goal_cancel);
        mUrl = (TextView) findViewById(R.id.post_your_goal_url);
        mButton = (Button) findViewById(R.id.post_your_goal_button);
        mProgress = (ProgressBar) findViewById(R.id.post_your_goal_progress);
        mProgress.setVisibility(View.GONE);
        mUploadText = (TextView) findViewById(R.id.post_your_goal_progress_text);
        mUploadText.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("file");
        }
        if(!value.equals("")){
            Bitmap bm = ThumbnailUtils.createVideoThumbnail(value, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            mImage.setImageBitmap(bm);
        }

        videoURL = value;

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
    }

    public void onCancelPostGoal(View v){
        Intent main = new Intent(getApplication(), CameraActivity.class);
        startActivity(main);
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(getApplication(), CameraActivity.class);
        startActivity(main);
    }

    private void uploadVideo() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "multipart/form-data");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());
        File myFile = new File(videoURL);
        RequestParams params = new RequestParams();
        try {
            params.put("file", myFile);
            params.put("name", getFileName(videoURL));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        client.post(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.UPLOAD_VIDEO_URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in PostYourGoalActivity");
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My Tag");
                mWakeLock.acquire();
                mProgress.setVisibility(View.VISIBLE);
                mUploadText.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "PostYourGoalActivity Response post comment: " + responseString);
                Toast.makeText(getApplicationContext(), "Video posted ", Toast.LENGTH_LONG).show();
                mWakeLock.release();
                Intent main = new Intent(getApplication(), MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "PostYourGoalActivity On failure code: " + statusCode);
                mWakeLock.release();
                Toast.makeText(getApplicationContext(), "Unable to post video "+getFileName(videoURL)+" due to "+e.getMessage(), Toast.LENGTH_LONG).show();
                mProgress.setVisibility(View.GONE);
                mUploadText.setVisibility(View.GONE);
                mButton.setVisibility(View.VISIBLE);
                if(errorResponse != null) {
                    Log.d(LOG, "PostYourGoalActivity ERROR statusCode: " + statusCode);
                    String responseString = new String(errorResponse);
                    Log.d(LOG, "PostYourGoalActivity ERROR responseString: " + responseString);
                    Log.d(LOG, "PostYourGoalActivity ERROR e.getMessage(): " + e.getMessage());
                }
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "PostYourGoalActivity On retry: " + retryNo);
                Toast.makeText(getApplicationContext(), "Retry uploading video "+retryNo, Toast.LENGTH_LONG).show();
                // called when request is retried
            }
        });
    }

    private String getFileName(String filePath) {
        String[] slices = filePath.split("/");
        return slices[slices.length-1];
    }
}
