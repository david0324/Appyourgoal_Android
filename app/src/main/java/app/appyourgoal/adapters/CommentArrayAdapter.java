package app.appyourgoal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.appyourgoal.R;
import app.appyourgoal.restdata.Comment;
import app.appyourgoal.utils.CircleTransform;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;

/**
 * Created by Dragisa on 11/1/2015.
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {

    private Context mContext;
    private List<Comment> mComments;
    private FontHelper mFontHelper;

    public CommentArrayAdapter(Context context, List<Comment> comments) {
        super(context, R.layout.comment_single_layout, comments);
        Log.d("djevticAPP","CommentArrayAdapter constructor");
        mContext = context;
        mComments = comments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("djevticAPP","CommentArrayAdapter getView for position: "+position);
        ViewHolder viewHolder;

        //Font helper for setting custom fonts
        mFontHelper = new FontHelper();

        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comment_single_layout, parent, false);


            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.commentUserIcon = (ImageView) convertView.findViewById(R.id.comment_single_user_image);
            viewHolder.commentUserName = (TextView) convertView.findViewById(R.id.comment_single_user_name);
            viewHolder.commentUserTime = (TextView) convertView.findViewById(R.id.comment_single_user_time);
            viewHolder.commentUserComment = (TextView) convertView.findViewById(R.id.comment_single_user_comment);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Set fontface
        mFontHelper.setTextViewTypeface(viewHolder.commentUserName, mContext);
        mFontHelper.setTextViewTypeface(viewHolder.commentUserTime, mContext);
        mFontHelper.setTextViewTypeface(viewHolder.commentUserComment, mContext);


        viewHolder.commentUserName.setText(mComments.get(position).getUser().getFirstName());
        viewHolder.commentUserTime.setText(getTime(mComments.get(position).getDate()));
        viewHolder.commentUserComment.setText(mComments.get(position).getCommentText());
        Picasso.with(mContext).load(StaticURL.SERVER_URL+mComments.get(position).getUser().getProfilePicture()).error(R.drawable.male_avatar).transform(new CircleTransform()).into(viewHolder.commentUserIcon);


        return convertView;
    }

    private static class ViewHolder {
        ImageView commentUserIcon;
        TextView commentUserName;
        TextView commentUserTime;
        TextView commentUserComment;
    }

    private String getTime(String timeString){
        Log.d("djevticAPP", "Time string: "+timeString);
        long diffSeconds = 0;
        long diffMinutes = 0;
        long diffHours = 0;
        long diffDays = 0;
        String date = timeString;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat output = new SimpleDateFormat("cccc d MMMM");
        try {
            Date commentDate = input.parse(date);                 // parse input
            Date curentDate = new Date();

            //in milliseconds
            long diff = curentDate.getTime() - (commentDate.getTime()+14400000);

            diffSeconds = diff / 1000 % 60;
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(diffDays > 0){
            return diffDays+"d";
        }else if(diffHours > 0){
            return diffHours+"h";
        }else if(diffMinutes > 0){
            return diffMinutes+"m";
        }else{
            return diffSeconds+"s";
        }
    }
}
