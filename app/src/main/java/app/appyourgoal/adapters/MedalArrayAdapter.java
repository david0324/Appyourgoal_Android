package app.appyourgoal.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import app.appyourgoal.R;
import app.appyourgoal.activities.CommentPageActivity;
import app.appyourgoal.activities.ProfileActivity;
import app.appyourgoal.restdata.EditDataExample;
import app.appyourgoal.restdata.Medal;
import app.appyourgoal.utils.CircleTransform;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 11/7/2015.
 */
public class MedalArrayAdapter extends ArrayAdapter<Medal> {

    private Context mContext;
    private List<Medal> mData;
    private EditDataExample mUserData;

    private UserData mUser;

    private String LOG = "djevticAPP";

    public MedalArrayAdapter(Context context, List<Medal> data, EditDataExample userData) {
        super(context, R.layout.medals_single_layout, data);
        Log.d("djevticAPP", "GoalArrayAdapter constructor");
        mContext = context;
        mData = data;
        mUser = UserData.getInstance();
        mUserData = userData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("djevticAPP","CommentArrayAdapter getView for position: "+position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.medals_single_layout, parent, false);


            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.medalVideoPreview = (ImageView) convertView.findViewById(R.id.medals_single_layout_video);
            viewHolder.medalUserImage = (ImageView) convertView.findViewById(R.id.medals_layout_single_user_image_round);
            viewHolder.medalImage = (ImageView) convertView.findViewById(R.id.medal_single_layout_medal);
            viewHolder.medalUserName = (TextView) convertView.findViewById(R.id.medals_single_layout_username);
            viewHolder.medalNumberOfComments = (TextView) convertView.findViewById(R.id.medals_single_layout_number_of_comments);
            viewHolder.medalWeekNumberOfLikes = (TextView) convertView.findViewById(R.id.medals_single_layout_week_number_of_stars);
            viewHolder.medalNumberOfStars = (TextView) convertView.findViewById(R.id.medals_single_layout_number_of_stars);
            viewHolder.medalSmallStar = (ImageView) convertView.findViewById(R.id.medals_single_layout_small_star);
            viewHolder.medalBigStar = (ImageView) convertView.findViewById(R.id.medals_single_layout_star);
            viewHolder.medalCommentCloud = (ImageView) convertView.findViewById(R.id.medal_single_layout_comment_cloud);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.medalUserName.setText(mUserData.getData().getFirstName());
        Picasso.with(mContext).load("http://img.youtube.com/vi/" + mData.get(position).getYoutubeId() + "/0.jpg").into(viewHolder.medalVideoPreview);
        viewHolder.medalNumberOfComments.setText(mData.get(position).getCommentsCount() + "");
        viewHolder.medalNumberOfStars.setText(mData.get(position).getLikesCount() + "");
        Picasso.with(mContext).load(StaticURL.SERVER_URL+mUserData.getData().getProfilePicture()).transform(new CircleTransform()).into(viewHolder.medalUserImage);
        if(mData.get(position).getPlace()!=null) {
            if (mData.get(position).getPlace().equals("1")) {
                viewHolder.medalImage.setImageResource(R.drawable.gold_medal);
            } else if (mData.get(position).getPlace().equals("2")) {
                viewHolder.medalImage.setImageResource(R.drawable.silver_medal);
            } else if (mData.get(position).getPlace().equals("3")) {
                viewHolder.medalImage.setImageResource(R.drawable.bronze_medal);
            }
        }

        if(mData.get(position).getUserLiked()!= null && mData.get(position).getUserLiked()){
            viewHolder.medalNumberOfStars.setTextColor(Color.parseColor("#fff1ea65"));
            viewHolder.medalSmallStar.setImageResource(R.drawable.yellow_star_small);
            viewHolder.medalBigStar.setImageResource(R.drawable.image_star_yellow);
            viewHolder.medalWeekNumberOfLikes.setTextColor(Color.parseColor("#fff1ea65"));
            viewHolder.medalWeekNumberOfLikes.setBackgroundResource(R.drawable.text_box_border);
        }else{
            viewHolder.medalNumberOfStars.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.medalSmallStar.setImageResource(R.drawable.white_small_star);
            viewHolder.medalBigStar.setImageResource(R.drawable.image_star_grey);
            viewHolder.medalWeekNumberOfLikes.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.medalWeekNumberOfLikes.setBackgroundResource(R.drawable.tex_box_border_white);
        }
        if(mData.get(position).getUserCommented()!= null && mData.get(position).getUserCommented()){
            viewHolder.medalNumberOfComments.setTextColor(Color.parseColor("#fff1ea65"));
            viewHolder.medalCommentCloud.setImageResource(R.drawable.comment_cloud_small_selected);
        }else{
            viewHolder.medalNumberOfComments.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.medalCommentCloud.setImageResource(R.drawable.comment_cloud_small);
        }

        viewHolder.medalUserImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
            }
        });

        viewHolder.medalWeekNumberOfLikes.setText(mData.get(position).getWeekLikes());

        final String videoId = mData.get(position).getVideoId();

        final String videoID = mData.get(position).getVideoId();
        final boolean isLiked = (mData.get(position).getUserLiked()!= null && mData.get(position).getUserLiked());
        final boolean isCommented = (mData.get(position).getUserCommented()!= null && mData.get(position).getUserCommented());
        viewHolder.medalVideoPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentPageActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("videoid", videoID);
                mBundle.putBoolean("isLiked", isLiked);
                mBundle.putBoolean("isCommented", isCommented);
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
            }
        });

        /*viewHolder.medalBigStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLiked) {
                    likeVideo(videoId, viewHolder.medalBigStar);
                }
            }
        });*/

        return convertView;
    }

    private void likeVideo(String videoId, final ImageView starImage) {
        Log.d(LOG, "CommentPageActivity get Data: " + StaticURL.SERVER_URL + StaticURL.LIKE_VIDEO_URL);
        Log.d(LOG,"CommentPageActivity get Data Autorisation token: "+mUser.getmTokenType()+" "+mUser.getmToken());
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization",mUser.getmTokenType()+" "+mUser.getmToken());

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

        client.post(mContext, StaticURL.SERVER_URL + StaticURL.LIKE_VIDEO_URL, entity, "application/json", new AsyncHttpResponseHandler() {

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

    private static class ViewHolder {
        ImageView medalVideoPreview;
        ImageView medalUserImage;
        ImageView medalImage;
        TextView medalUserName;
        TextView medalNumberOfComments;
        TextView medalWeekNumberOfLikes;
        TextView medalNumberOfStars;
        ImageView medalSmallStar;
        ImageView medalBigStar;
        ImageView medalCommentCloud;
    }
}
