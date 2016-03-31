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
import app.appyourgoal.restdata.Datum;
import app.appyourgoal.utils.CircleTransform;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 11/5/2015.
 */
public class GoalArrayAdapter extends ArrayAdapter<Datum> {

    private Context mContext;
    private List<Datum> mData;

    private UserData mUser;

    private String LOG = "djevticAPP";

    private boolean clickerBlock = false;

    public GoalArrayAdapter(Context context, List<Datum> data) {
        super(context, R.layout.goals_single_layout, data);
        Log.d("djevticAPP", "GoalArrayAdapter constructor");
        mContext = context;
        mData = data;
        mUser = UserData.getInstance();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("djevticAPP","CommentArrayAdapter getView for position: "+position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.goals_single_layout, parent, false);


            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.goalVideoPreview = (ImageView) convertView.findViewById(R.id.goal_single_layout_video);
            viewHolder.goalUserImage = (ImageView) convertView.findViewById(R.id.goal_layout_single_user_image_round);
            viewHolder.goalUserName = (TextView) convertView.findViewById(R.id.goal_single_layout_username);
            viewHolder.goalNumberOfComments = (TextView) convertView.findViewById(R.id.goal_single_layout_number_of_comments);
            viewHolder.goalNumberOfWeekLikes = (TextView) convertView.findViewById(R.id.goal_layout_week_number_of_stars);
            viewHolder.goalNumberOfStars = (TextView) convertView.findViewById(R.id.goal_single_layout_number_of_stars);
            viewHolder.goalSmallStar = (ImageView) convertView.findViewById(R.id.goal_single_layout_small_star);
            viewHolder.goalBigStar = (ImageView) convertView.findViewById(R.id.goal_single_layout_star);
            viewHolder.goalCommentIcon = (ImageView) convertView.findViewById(R.id.goal_single_layout_comment_icon);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.d(LOG,"mData.get(position).getLikesCount(): "+mData.get(position).getLikesCount());
        String likes = mData.get(position).getLikesCount();
        Integer likesCOunt = Integer.getInteger(likes);
        viewHolder.goalUserName.setText(mData.get(position).getUser().getFirstName() + " " + mData.get(position).getUser().getLastName());
        Picasso.with(mContext).load("http://img.youtube.com/vi/" + mData.get(position).getYoutubeId() + "/0.jpg").into(viewHolder.goalVideoPreview);
        viewHolder.goalNumberOfComments.setText(mData.get(position).getComments().size() + "");
        if(mData.get(position).getLikesCount() == null){
            viewHolder.goalNumberOfStars.setText(mData.get(position).getLikesNo()+"");
        } else {
            viewHolder.goalNumberOfStars.setText(mData.get(position).getLikesCount());
        }
        Picasso.with(mContext).load(StaticURL.SERVER_URL+mData.get(position).getUser().getProfilePicture()).error(R.drawable.male_avatar).placeholder(R.drawable.male_avatar).transform(new CircleTransform()).into(viewHolder.goalUserImage);
        if(mData.get(position).isUserLiked()){
            viewHolder.goalNumberOfStars.setTextColor(Color.parseColor("#fff1ea65"));
            viewHolder.goalSmallStar.setImageResource(R.drawable.yellow_star_small);
            viewHolder.goalBigStar.setImageResource(R.drawable.image_star_yellow);
            viewHolder.goalNumberOfWeekLikes.setTextColor(Color.parseColor("#fff1ea65"));
            viewHolder.goalNumberOfWeekLikes.setBackgroundResource(R.drawable.text_box_border);
        }else{
            viewHolder.goalNumberOfStars.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.goalSmallStar.setImageResource(R.drawable.white_small_star);
            viewHolder.goalBigStar.setImageResource(R.drawable.image_star_grey);
            viewHolder.goalNumberOfWeekLikes.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.goalNumberOfWeekLikes.setBackgroundResource(R.drawable.tex_box_border_white);
        }
        if(mData.get(position).isUserCommented()){
            viewHolder.goalNumberOfComments.setTextColor(Color.parseColor("#fff1ea65"));
            viewHolder.goalCommentIcon.setImageResource(R.drawable.comment_cloud_small_selected);
        }else{
            viewHolder.goalNumberOfComments.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.goalCommentIcon.setImageResource(R.drawable.comment_cloud_small);
        }

        viewHolder.goalNumberOfWeekLikes.setText(mData.get(position).getWeekLikes());


        viewHolder.goalUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        final String videoId = mData.get(position).getVideoId();
        boolean isLiked = mData.get(position).isUserLiked();
        final boolean isCommented = mData.get(position).isUserCommented();
        viewHolder.goalBigStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mData.get(position).isUserLiked() && !clickerBlock) {
                    clickerBlock = true;
                    likeVideo(videoId, viewHolder.goalBigStar, viewHolder.goalSmallStar, viewHolder.goalNumberOfStars, viewHolder.goalNumberOfWeekLikes, position, viewHolder);
                }
            }
        });

        final String videoID = mData.get(position).getVideoId();
        viewHolder.goalVideoPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentPageActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("videoid", videoID);
                mBundle.putBoolean("isLiked", mData.get(position).isUserLiked());
                mBundle.putBoolean("isCommented", isCommented);
                intent.putExtras(mBundle);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private void likeVideo(String videoId, final ImageView starImage, final ImageView goalSmallStar, final TextView goalNumberOfStars, final TextView goalNumberOfWeekLikes, final int position, final ViewHolder viewHolder) {
        Log.d(LOG, "CommentPageActivity get Data: " + StaticURL.SERVER_URL + StaticURL.LIKE_VIDEO_URL);
        Log.d(LOG,"CommentPageActivity get Data Autorisation token: "+mUser.getmTokenType()+" "+mUser.getmToken());
        final AsyncHttpClient client = new AsyncHttpClient();
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
                goalSmallStar.setImageResource(R.drawable.yellow_star_small);
                goalNumberOfStars.setTextColor(Color.parseColor("#fff1ea65"));
                goalNumberOfStars.setText(setNumberOfStars(goalNumberOfStars));
                goalNumberOfWeekLikes.setTextColor(Color.parseColor("#fff1ea65"));
                goalNumberOfWeekLikes.setBackgroundResource(R.drawable.text_box_border);
                goalNumberOfWeekLikes.setText(setNumberOfStars(goalNumberOfWeekLikes));
                mData.get(position).setUserLiked(true);
                clickerBlock = false;
                addLikeToArray(position, goalNumberOfStars.getText().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "On failure code: " + statusCode);
                clickerBlock = false;
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                clickerBlock = false;
                Log.d(LOG, "On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    private String setNumberOfStars(TextView text) {
        String result = "";
        int temp = Integer.parseInt(text.getText().toString());
        Log.d(LOG,"SetNumberOfStars: "+temp);
        temp = temp + 1;
        result = String.valueOf(temp);
        Log.d(LOG,"SetNumberOfStars: "+result);
        return result;
    }

    private void addLikeToArray(int position, String numberOfLikes) {
        mData.get(position).setUserLiked(true);
        mData.get(position).setLikesCount(numberOfLikes);
    }

    private static class ViewHolder {
        ImageView goalVideoPreview;
        ImageView goalUserImage;
        TextView goalUserName;
        TextView goalNumberOfComments;
        TextView goalNumberOfWeekLikes;
        ImageView goalCommentIcon;
        TextView goalNumberOfStars;
        ImageView goalSmallStar;
        ImageView goalBigStar;
    }
}
