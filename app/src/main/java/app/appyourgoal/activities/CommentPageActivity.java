package app.appyourgoal.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import app.appyourgoal.R;
import app.appyourgoal.adapters.CommentArrayAdapter;
import app.appyourgoal.restdata.Comment;
import app.appyourgoal.restdata.VideoDetailes;
import app.appyourgoal.utils.CircleTransform;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 11/1/2015.
 */
public class CommentPageActivity extends Activity {

    private UserData mUser;
    private VideoDetailes mVideoDetailes;
    private String mVideoKey;
    private CommentPageActivity mActivity;
    private FontHelper mFontHelper;

    //Components
    private ListView commentListView;
    private EditText commentEditText;
    private Button commentButton;
    private ImageView commentImage;
    private ImageView userImage;
    private ImageView shareVideo;
    private TextView userName;
    private ImageView commentSmallStar;
    private TextView commentNumberOfStars;
    private TextView commentNumberOfWeekLikes;
    private TextView commentNumberOfComments;
    private ImageView commentCloudImage;
    private TextView commentPageBack;
    private TextView commentPageGoal;
    private ImageView commentPagePlay;
    private ImageView commentStar;

    private CommentArrayAdapter mAdapter;
    private boolean isLiked;
    private boolean isCommented;
    private boolean iskeybordHiden = true;

    private String LOG = "djevticAPP";

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() != null){
            mVideoKey = getIntent().getExtras().getString("videoid");
            isLiked = getIntent().getExtras().getBoolean("isLiked");
            isCommented = getIntent().getExtras().getBoolean("isCommented");
            Log.d(LOG,"mVideoKey : "+mVideoKey);
        }

        setContentView(R.layout.comment_page_layout);

        //Font helper for setting custom fonts
        mFontHelper = new FontHelper();

        //Initialise components
        mActivity = this;
        commentListView = (ListView) findViewById(R.id.comment_page_layout_list);
        commentEditText = (EditText) findViewById(R.id.comment_page_layout_comment_edit);
        commentButton = (Button) findViewById(R.id.comment_page_layout_post_comment_button);
        commentImage = (ImageView) findViewById(R.id.podium_layout_upper_video);
        userImage = (ImageView) findViewById(R.id.comment_layout_user_image_round);
        userName = (TextView) findViewById(R.id.comment_layout_username);
        commentStar = (ImageView) findViewById(R.id.comment_layout_star);
        commentSmallStar = (ImageView) findViewById(R.id.comment_layout_stars_small);
        commentNumberOfStars = (TextView) findViewById(R.id.comment_layout_number_of_stars);
        commentNumberOfWeekLikes = (TextView) findViewById(R.id.comment_layout_week_number_of_stars);
        commentNumberOfComments = (TextView) findViewById(R.id.comment_layout_number_of_comments);
        commentCloudImage = (ImageView) findViewById(R.id.comment_layout_cloud);
        shareVideo = (ImageView) findViewById(R.id.comment_page_layout_share);
        commentPageBack = (TextView) findViewById(R.id.comment_page_layout_back);
        commentPageGoal = (TextView) findViewById(R.id.comment_page_layout_goal);
        commentPagePlay = (ImageView) findViewById(R.id.comment_page_play_button);

        //Set fontface
        mFontHelper.setTextViewTypeface(commentEditText, getApplicationContext());
        mFontHelper.setTextViewTypeface(commentButton, getApplicationContext());
        mFontHelper.setTextViewTypeface(userName, getApplicationContext());
        mFontHelper.setTextViewTypeface(commentNumberOfStars, getApplicationContext());
        mFontHelper.setTextViewTypeface(commentNumberOfComments, getApplicationContext());
        mFontHelper.setTextViewTypeface(commentPageBack, getApplicationContext());
        mFontHelper.setTextViewTypeface(commentPageGoal, getApplicationContext());

        //Preparing view
        //commentButton.setVisibility(View.GONE);

        //On edit click listener
        commentEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //commentButton.setVisibility(View.VISIBLE);
                commentEditText.setFocusableInTouchMode(true);
                commentEditText.setFocusable(true);
                commentEditText.requestFocus();
                //commentListView.setVisibility(View.GONE);
                showSoftKeybord(mActivity);
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!commentEditText.getText().toString().isEmpty()) {
                    commentGoal();
                    commentEditText.setFocusable(false);
                    commentEditText.setText("");
                    commentEditText.clearFocus();
                    hideSoftKeyboard(mActivity, commentEditText);
                }
                /*commentButton.setVisibility(View.GONE);
                commentEditText.setFocusable(false);
                commentEditText.setText("");
                commentListView.setVisibility(View.VISIBLE);
                commentEditText.clearFocus();
                hideSoftKeyboard(mActivity, commentEditText);*/
            }
        });

        mUser = UserData.getInstance();
        getData();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(LOG, "on Config changed newConfig.keyboard: " + newConfig.keyboard);
        Log.d(LOG, "on Config changed newConfig.keyboardHidden: "+newConfig.keyboardHidden);

        // Checks whether a hardware keyboard is available
        if (newConfig.keyboard == Configuration.KEYBOARDHIDDEN_NO) {
            Log.d(LOG, "Keybord Hiden? ");
            commentButton.setVisibility(View.VISIBLE);
            commentEditText.setFocusableInTouchMode(true);
            commentEditText.setFocusable(true);
            commentEditText.requestFocus();
            commentListView.setVisibility(View.GONE);
        } else if (newConfig.keyboard == Configuration.KEYBOARDHIDDEN_YES) {
            Log.d(LOG, "Keybord Shown? ");
            commentButton.setVisibility(View.GONE);
            commentEditText.setFocusable(false);
            commentEditText.setText("");
            commentListView.setVisibility(View.VISIBLE);
            commentEditText.clearFocus();
        }
    }

    public void onCancelCommentPage(View v){
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    public void onCommentUserIconCLicked(View v){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void getData(){
        Log.d(LOG, "CommentPageActivity get Data: " + StaticURL.SERVER_URL + StaticURL.UPLOAD_VIDEO_URL + "/" + mVideoKey);
        Log.d(LOG, "CommentPageActivity get Data Autorisation token: " + mUser.getmTokenType() + " " + mUser.getmToken());
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());

        client.get(StaticURL.SERVER_URL + StaticURL.UPLOAD_VIDEO_URL + "/" + mVideoKey, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in CommentPageActivity");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "Response: " + responseString);
                Gson gson = new Gson();
                mVideoDetailes = gson.fromJson(responseString, VideoDetailes.class);
                fillData();
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

    private void fillData(){

        Picasso.with(this).load("http://img.youtube.com/vi/" + mVideoDetailes.getData().getYoutubeId() + "/0.jpg").into(commentImage);
        commentPagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mVideoDetailes.getData().getYoutubeId()));
                intent.putExtra("force_fullscreen", true);
                startActivity(intent);
                Log.d(LOG, "Video Playing....");
            }
        });
        shareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("AppYourGoal", "http://www.youtube.com/watch?v="+mVideoDetailes.getData().getYoutubeId());
                clipboard.setPrimaryClip(clip);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain"); // might be text, sound, whatever
                share.putExtra(Intent.EXTRA_SUBJECT, "http://www.youtube.com/watch?v="+mVideoDetailes.getData().getYoutubeId());
                startActivity(Intent.createChooser(share, "Share"));
            }
        });
        userImage = (ImageView) findViewById(R.id.comment_layout_user_image_round);
        Picasso.with(getApplicationContext()).load(StaticURL.SERVER_URL+mVideoDetailes.getData().getUser().getProfilePicture()).error(R.drawable.male_avatar).placeholder(R.drawable.male_avatar).transform(new CircleTransform()).into(userImage);
        userName.setText(mVideoDetailes.getData().getUser().getFirstName() + " " + mVideoDetailes.getData().getUser().getLastName());
        //commentSmallStar = (ImageView) findViewById(R.id.comment_layout_star);
        commentNumberOfStars.setText(mVideoDetailes.getData().getLikesCount());
        commentNumberOfComments.setText(mVideoDetailes.getData().getCommentsCount());
        commentNumberOfWeekLikes.setText(mVideoDetailes.getData().getWeekLikes());
        if(isLiked){
            commentSmallStar.setImageResource(R.drawable.yellow_star_small);
            commentStar.setImageResource(R.drawable.yellow_star_big);
            commentNumberOfStars.setTextColor(Color.parseColor("#fff1ea65"));
            commentNumberOfWeekLikes.setTextColor(Color.parseColor("#fff1ea65"));
            commentNumberOfWeekLikes.setBackgroundResource(R.drawable.text_box_border);
        }
        if (isCommented){
            commentCloudImage.setImageResource(R.drawable.comment_cloud_small_selected);
            commentNumberOfComments.setTextColor(Color.parseColor("#fff1ea65"));
        }
        for(int i = 0;mVideoDetailes.getData().getComments().size()>i;i++){
            mVideoDetailes.getData().getComments().get(i).setProperDate(formatDate(mVideoDetailes.getData().getComments().get(i).getDate()));
        }
        ArrayList<Comment> comments = new ArrayList<>();
        comments.addAll(mVideoDetailes.getData().getComments());

        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment p1, Comment p2) {
                if (p1.getProperDate().before(p2.getProperDate())) {
                    return 1;
                } else if (p1.getProperDate().after(p2.getProperDate())) {
                    return -1;
                } else {
                    return 0;
                }
            }

        });
        final String videoId = mVideoDetailes.getData().getVideoId();
        commentStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLiked){
                    likeVideo(videoId, commentStar);
                }
            }
        });

        mAdapter = new CommentArrayAdapter(getApplicationContext(), comments);
        commentListView.setAdapter(mAdapter);
    }

    private void likeVideo(String videoId, final ImageView starImage) {
        Log.d(LOG, "CommentPageActivity get Data: " + StaticURL.SERVER_URL + StaticURL.LIKE_VIDEO_URL);
        Log.d(LOG, "CommentPageActivity get Data Autorisation token: " + mUser.getmTokenType() + " " + mUser.getmToken());
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());

        StringEntity entity = null;
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("video_id", videoId);
            entity = new StringEntity(jsonParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.LIKE_VIDEO_URL, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in CommentPageActivity");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "Response: " + responseString);
                starImage.setImageResource(R.drawable.image_star_yellow);
                commentSmallStar.setImageResource(R.drawable.yellow_star_small);
                commentNumberOfStars.setText((Integer.parseInt(mVideoDetailes.getData().getLikesCount()) + 1) + "");
                commentNumberOfStars.setTextColor(Color.parseColor("#fff1ea65"));
                isLiked = true;
                getData();
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

    private Date formatDate(String dateString) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date commentDate = null;
        try {
            commentDate = input.parse(dateString);                 // parse input
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return commentDate;
    }

    private void commentGoal(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());

        StringEntity entity = null;
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("video_id", mVideoKey);
            jsonParams.put("comment_text", commentEditText.getText());
            entity = new StringEntity(jsonParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.COMMENT_VIDEO_URL, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in CommentPageActivity");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "Response post comment: " + responseString);
                commentCloudImage.setImageResource(R.drawable.comment_cloud_small_selected);
                commentNumberOfComments.setTextColor(Color.parseColor("#fff1ea65"));
                commentNumberOfComments.setText((mVideoDetailes.getData().getComments().size()+1)+"");
                getData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                String responseString = new String(errorResponse);
                Log.d(LOG, "On failure code: " + statusCode+ "Response message: "+responseString);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    private String getNumberOfComments(TextView text) {
        String result = "";
        int number = Integer.getInteger(text.getText().toString());
        result = String.valueOf(number++);
        return result;
    }

    public void hideSoftKeyboard(Activity activity, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        iskeybordHiden = true;

    }

    public void showSoftKeybord(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        iskeybordHiden = false;
    }

    private String setNumberOfLikes(TextView view){
        String result = "";
        int numberOfLikes = Integer.getInteger(view.getText().toString());
        result = String.valueOf(numberOfLikes++);
        return result;
    }
}
