package app.appyourgoal.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.activities.CommentPageActivity;
import app.appyourgoal.activities.InitialActivity;
import app.appyourgoal.activities.NotificationActivities;
import app.appyourgoal.activities.ProfileActivity;
import app.appyourgoal.restdata.Datum;
import app.appyourgoal.restdata.Podium;
import app.appyourgoal.restdata.PrizeNotification;
import app.appyourgoal.utils.CircleTransform;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 10/18/2015.
 */
public class PodiumFragment extends Fragment {

    private final static String  FILE_PATCH = "";

    private UserData mUser;
    private Podium mPodium;
    private PrizeNotification mPrize;
    private FontHelper mFontHelper;

    private Spinner mSpiner;
    private ImageView mUserIcon;
    private ImageView mBellIcon;
    private ImageView mUpperVideo;
    private ImageView mGoldenUserRoundImage;
    private TextView mGoldenUsername;
    private TextView mGoldenStarsGot;
    private TextView mGoldenWeekLikes;
    private TextView mGoldenCommentsGot;
    private ImageView mGoldenCommentsCloud;
    private ImageView mBigStar;
    private ImageView mSilverUserVideoImage;
    private ImageView mSilverUserRoundImage;
    private TextView mSilverUserStarsGot;
    private TextView mSilverWeekLikes;
    private TextView mSilverUserName;
    private TextView mSilverUserComments;
    private ImageView mSilverUserCommentsCloud;
    private ImageView mBronzeUserVideoImage;
    private ImageView mBronzeUserRoundImage;
    private TextView mBronzeUserStarsGot;
    private TextView mBronzeUserName;
    private TextView mBronzeWeekLikes;
    private TextView mBronzeUserComments;
    private ImageView mBronzeUserCommentsCloud;
    private ImageView mGoldSmallStar;
    private ImageView mSilverSmallStar;
    private ImageView mBronzeSmalStar;

    private String LOG = "djevticAPP";

    public int REQUIRED_SIZE = 800;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onViewCreated(savedInstanceState);
        View view = inflater.inflate(R.layout.podium_layout, container, false);
        //Font helper for setting custom fonts
        mFontHelper = new FontHelper();
        mUser = UserData.getInstance();

        mSpiner = (Spinner) view.findViewById(R.id.podium_layout_selection_spiner);
        mUserIcon = (ImageView) view.findViewById(R.id.podium_layout_user_icon);
        mBellIcon = (ImageView) view.findViewById(R.id.podium_layout_bell_icon);
        mUpperVideo = (ImageView) view.findViewById(R.id.podium_layout_upper_video);
        mGoldenUserRoundImage = (ImageView) view.findViewById(R.id.podium_layout_user_image_round);
        mGoldenUsername = (TextView) view.findViewById(R.id.podium_layout_username);
        mGoldenStarsGot = (TextView) view.findViewById(R.id.podium_layout_number_of_stars);
        mGoldenCommentsGot = (TextView) view.findViewById(R.id.podium_layout_number_of_comments);
        mGoldenWeekLikes = (TextView) view.findViewById(R.id.podium_layout_week_number_of_stars);
        mGoldenCommentsCloud = (ImageView) view.findViewById(R.id.podium_layout_comment_cloud_gold);
        mBigStar = (ImageView) view.findViewById(R.id.podium_layout_star);
        mSilverUserName = (TextView) view.findViewById(R.id.podium_layout_username_silver);
        mSilverUserVideoImage = (ImageView) view.findViewById(R.id.podium_layout_silver_video);
        mSilverUserRoundImage = (ImageView) view.findViewById(R.id.podium_layout_user_image_silver);
        mSilverUserStarsGot = (TextView) view.findViewById(R.id.podium_layout_number_of_stars_silver);
        mSilverWeekLikes = (TextView) view.findViewById(R.id.podium_layout_week_number_of_stars_silver);
        mSilverUserComments = (TextView) view.findViewById(R.id.podium_layout_number_of_comments_silver);
        mSilverUserCommentsCloud =  (ImageView) view.findViewById(R.id.podium_layout_comment_cloud_silver);
        mBronzeUserVideoImage = (ImageView) view.findViewById(R.id.podium_layout_bronze_video);
        mBronzeUserName = (TextView) view.findViewById(R.id.podium_layout_username_bronze);
        mBronzeUserRoundImage = (ImageView) view.findViewById(R.id.podium_layout_user_image_bronze);
        mBronzeUserStarsGot = (TextView) view.findViewById(R.id.podium_layout_number_of_stars_bronze);
        mBronzeUserComments = (TextView) view.findViewById(R.id.podium_layout_number_of_comments_bronze);
        mBronzeWeekLikes = (TextView) view.findViewById(R.id.podium_layout_week_number_of_stars_bronze);
        mBronzeUserCommentsCloud =  (ImageView) view.findViewById(R.id.podium_layout_comment_cloud_bronze);
        mGoldSmallStar = (ImageView) view.findViewById(R.id.podium_gold_small_star);
        mSilverSmallStar = (ImageView) view.findViewById(R.id.podium_silver_small_star);
        mBronzeSmalStar = (ImageView) view.findViewById(R.id.podium_bronze_small_star);

        //Set fontface
        mFontHelper.setTextViewTypeface(mGoldenUsername, getContext());
        mFontHelper.setTextViewTypeface(mGoldenStarsGot, getContext());
        mFontHelper.setTextViewTypeface(mGoldenCommentsGot, getContext());
        mFontHelper.setTextViewTypeface(mSilverUserName, getContext());
        mFontHelper.setTextViewTypeface(mSilverUserStarsGot, getContext());
        mFontHelper.setTextViewTypeface(mSilverUserComments, getContext());
        mFontHelper.setTextViewTypeface(mBronzeUserName, getContext());
        mFontHelper.setTextViewTypeface(mBronzeUserStarsGot, getContext());
        mFontHelper.setTextViewTypeface(mBronzeUserComments, getContext());

        //Fill up Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.podium_screen_spinner_data, R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpiner.setAdapter(adapter);

        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        if (!mUser.ismCommertialShown()){
            getPrizeShowing();
        }

        mBellIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivities.class);
                startActivity(intent);
            }
        });

        //getData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        Log.d("djevticAPP","Image URL is: "+imageUrl+ " and also destinationFile: "+destinationFile+" formated URL is:"+(imageUrl).replace(" ", "%20"));
        URL url = new URL((imageUrl).replace(" ", "%20"));
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + FILE_PATCH + "/"+destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... link) {
            Log.d(LOG, "DownloadImageTask : "+link[0]+" and "+link[1]);
            return download_Image(link[0], link[1]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            showPrizeNotification(result);
        }


        private Bitmap download_Image(String imageUrl, String destinationFile) {

            try {
                saveImage(imageUrl, destinationFile);
                File imgFile = new File(Environment.getExternalStorageDirectory() + "/" + FILE_PATCH + "/"+destinationFile);
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o);

                // Find the correct scale value. It should be the power of 2.

                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1 ;
                while (true) {
                    if (width_tmp / 2 < REQUIRED_SIZE
                            || height_tmp / 2 < REQUIRED_SIZE)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }

                // decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                if (imgFile.exists()) {
                    return BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    private void showPrizeNotification(Bitmap myBitmap) {
        if(myBitmap!=null && getActivity()!=null) {
            final Dialog settingsDialog = new Dialog(getActivity());
            settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            settingsDialog.setContentView(getActivity().getLayoutInflater().inflate(R.layout.prize_dialog_layout
                    , null));
            settingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ImageView prizeImage = (ImageView) settingsDialog.findViewById(R.id.prize_dialog_image);
            //Picasso.with(getActivity()).load(new File(Environment.getExternalStorageDirectory() + "/" + FILE_PATCH + "/prize.jpg")).into(prizeImage);
            prizeImage.setImageBitmap(myBitmap);
            prizeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settingsDialog.dismiss();
                }
            });
            settingsDialog.show();
            mUser.setmCommertialShown(true);
        }
    }

    private void getPrizeShowing(){
        Log.d(LOG,"Show Prize");
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setBasicAuth("Authorization", mUser.getmTokenType()+" "+mUser.getmToken());
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());

        client.get(StaticURL.BORNEO_SERVER_URL + StaticURL.PRIZE_URL, new AsyncHttpResponseHandler() {

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
                mPrize = gson.fromJson(responseString, PrizeNotification.class);
                String[] list = {StaticURL.BORNEO_SERVER_URL+mPrize.getData().getPrizePicture(),"prize.jpg"};
                Log.d(LOG,"onSucsess: "+ StaticURL.BORNEO_SERVER_URL+mPrize.getData().getPrizePicture());
                new DownloadImagesTask().execute(list);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                String responseString = new String(errorResponse);
                Log.d(LOG, "On failure code: " + statusCode+" failure resonse:"+responseString);
                Toast.makeText(getContext(), "Something went wrong with communication to server : "+responseString, Toast.LENGTH_LONG)
                        .show();
                logOut();
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    public void getData(){
        Log.d(LOG,"PodiumFragment get Data: "+StaticURL.SERVER_URL + StaticURL.GET_WINNERS_URL);
        Log.d(LOG,"PodiumFragment get Data Autorisation token: "+mUser.getmTokenType()+" "+mUser.getmToken());
        AsyncHttpClient client = new AsyncHttpClient();
        //client.setBasicAuth("Authorization", mUser.getmTokenType()+" "+mUser.getmToken());
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization",mUser.getmTokenType()+" "+mUser.getmToken());

        client.get(StaticURL.BORNEO_SERVER_URL + StaticURL.GET_WINNERS_URL, new AsyncHttpResponseHandler() {

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
                mPodium = gson.fromJson(responseString,Podium.class);
                insertData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                String responseString = new String(errorResponse);
                Log.d(LOG, "On failure code: " + statusCode+" failure resonse:"+responseString);
                Toast.makeText(getContext(), "Something went wrong with communication to server : "+responseString, Toast.LENGTH_LONG)
                        .show();
                logOut();
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    private void insertData(){
        final List<Datum> datums = mPodium.getData();
        Collections.sort(datums);
        //Collections.sort(datums, new CustomComparator());
        for (int i = 0;i < datums.size();i++) {
            if(i==0){
                final String videoID = datums.get(i).getVideoId();
                Picasso.with(getActivity()).load("http://img.youtube.com/vi/" + datums.get(i).getYoutubeId()+"/0.jpg").into(mUpperVideo);
                final boolean isLiked = datums.get(i).isUserLiked();
                final boolean isCommented = datums.get(i).isUserCommented();
                mUpperVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CommentPageActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("videoid", videoID);
                        mBundle.putBoolean("isLiked", isLiked);
                        mBundle.putBoolean("isCommented", isCommented);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });
                mGoldenCommentsGot.setText(datums.get(i).getComments().size()+"");
                mGoldenStarsGot.setText(datums.get(i).getLikesNo()+"");
                if(datums.get(i).isUserLiked()){
                    mGoldSmallStar.setImageResource(R.drawable.yellow_star_small);
                    mGoldenStarsGot.setTextColor(Color.parseColor("#FFf1ea65"));
                    mGoldenWeekLikes.setText(datums.get(i).getWeekLikes());
                    mGoldenWeekLikes.setTextColor(Color.parseColor("#FFf1ea65"));
                    mGoldenWeekLikes.setBackgroundResource(R.drawable.text_box_border);
                }
                if(datums.get(i).isUserCommented()){
                    mGoldenCommentsCloud.setImageResource(R.drawable.comment_cloud_small_selected);
                    mGoldenCommentsGot.setTextColor(Color.parseColor("#fff1ea65"));
                }
                mGoldenUsername.setText(datums.get(i).getUser().getFirstName());
            }else if(i==1){
                final String videoID = datums.get(i).getVideoId();
                Picasso.with(getActivity()).load("http://img.youtube.com/vi/" + datums.get(i).getYoutubeId()+"/0.jpg").into(mSilverUserVideoImage);
                final boolean isLiked = datums.get(i).isUserLiked();
                final boolean isCommented = datums.get(i).isUserCommented();
                mSilverUserVideoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CommentPageActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("videoid", videoID);
                        mBundle.putBoolean("isLiked", isLiked);
                        mBundle.putBoolean("isCommented", isCommented);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });
                mSilverUserComments.setText(datums.get(i).getComments().size()+"");
                mSilverUserStarsGot.setText(datums.get(i).getLikesNo()+"");
                if(datums.get(i).isUserLiked()){
                    mSilverSmallStar.setImageResource(R.drawable.yellow_star_small);
                    mSilverUserStarsGot.setTextColor(Color.parseColor("#fff1ea65"));
                    mSilverWeekLikes.setText(datums.get(i).getWeekLikes());
                    mSilverWeekLikes.setTextColor(Color.parseColor("#fff1ea65"));
                    mSilverWeekLikes.setBackgroundResource(R.drawable.text_box_border);
                }
                if(datums.get(i).isUserCommented()){
                    mSilverUserCommentsCloud.setImageResource(R.drawable.comment_cloud_small_selected);
                    mSilverUserComments.setTextColor(Color.parseColor("#fff1ea65"));
                }
                mSilverUserName.setText(datums.get(i).getUser().getFirstName());
            }else{
                final String videoID = datums.get(i).getVideoId();
                Picasso.with(getActivity()).load("http://img.youtube.com/vi/" + datums.get(i).getYoutubeId()+"/0.jpg").into(mBronzeUserVideoImage);
                final boolean isLiked = datums.get(i).isUserLiked();
                final boolean isCommented = datums.get(i).isUserCommented();
                mBronzeUserVideoImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CommentPageActivity.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("videoid", videoID);
                        mBundle.putBoolean("isLiked", isLiked);
                        mBundle.putBoolean("isCommented", isCommented);
                        intent.putExtras(mBundle);
                        startActivity(intent);
                    }
                });
                mBronzeUserComments.setText(datums.get(i).getComments().size()+"");
                mBronzeUserStarsGot.setText(datums.get(i).getLikesNo() + "");
                if(datums.get(i).isUserLiked()){
                    mBronzeSmalStar.setImageResource(R.drawable.yellow_star_small);
                    mBronzeUserStarsGot.setTextColor(Color.parseColor("#fff1ea65"));
                    mBronzeWeekLikes.setText(datums.get(i).getWeekLikes());
                    mBronzeWeekLikes.setTextColor(Color.parseColor("#fff1ea65"));
                    mBronzeWeekLikes.setBackgroundResource(R.drawable.text_box_border);
                }
                if(datums.get(i).isUserCommented()){
                    mBronzeUserCommentsCloud.setImageResource(R.drawable.comment_cloud_small_selected);
                    mBronzeUserComments.setTextColor(Color.parseColor("#fff1ea65"));
                }
                mBronzeUserName.setText(datums.get(i).getUser().getFirstName());
                Picasso.with(getContext()).load(StaticURL.SERVER_URL + datums.get(0).getUser().getProfilePicture()).error(R.drawable.male_avatar).placeholder(R.drawable.male_avatar).transform(new CircleTransform()).into(mGoldenUserRoundImage);
                Picasso.with(getContext()).load(StaticURL.SERVER_URL + datums.get(1).getUser().getProfilePicture()).error(R.drawable.male_avatar).placeholder(R.drawable.male_avatar).transform(new CircleTransform()).into(mSilverUserRoundImage);
                Picasso.with(getContext()).load(StaticURL.SERVER_URL + datums.get(2).getUser().getProfilePicture()).error(R.drawable.male_avatar).placeholder(R.drawable.male_avatar).transform(new CircleTransform()).into(mBronzeUserRoundImage);
            }
        }
    };

    private void logOut() {
        mUser.setmTokenType(null);
        mUser.setmToken(null);
        mUser.setmRefreshToken(null);
        mUser = null;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("appyourgoal", Context.MODE_PRIVATE);
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
        Intent intent = new Intent(getActivity(), InitialActivity.class);
        startActivity(intent);
    }

    public class CustomComparator implements Comparator<Datum> {
        @Override
        public int compare(Datum o1, Datum o2) {
            return o1.compareTo(o2);
        }
    }
}
