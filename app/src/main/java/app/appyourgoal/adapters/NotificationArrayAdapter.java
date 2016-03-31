package app.appyourgoal.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.appyourgoal.R;
import app.appyourgoal.restdata.NotificationBody;
import app.appyourgoal.utils.FontHelper;

/**
 * Created by Dragisa on 11/14/2015.
 */
public class NotificationArrayAdapter extends ArrayAdapter<NotificationBody> {

    private FontHelper mFontHelper;
    private Context mContext;
    private List<NotificationBody> mNotification;

    public NotificationArrayAdapter(Context context, int resource, List<NotificationBody> notifications) {
        super(context, R.layout.single_notification, notifications);
        mContext = context;
        mNotification = notifications;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("djevticAPP", "CommentArrayAdapter getView for position: " + position);
        ViewHolder viewHolder;

        //Font helper for setting custom fonts
        mFontHelper = new FontHelper();
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_notification, parent, false);


            viewHolder = new ViewHolder();
            viewHolder.commentUserTime = (TextView) convertView.findViewById(R.id.single_notification_time);
            viewHolder.commentUserComment = (TextView) convertView.findViewById(R.id.single_notification_text);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        mFontHelper.setTextViewTypeface(viewHolder.commentUserTime, mContext);
        mFontHelper.setTextViewTypeface(viewHolder.commentUserComment, mContext);

        viewHolder.commentUserComment.setText(mNotification.get(position).getNotificationText());
        viewHolder.commentUserTime.setText(getTime(mNotification.get(position).getDate()));

        return convertView;
    }

    private static class ViewHolder {
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
            if(diffDays>1){
                return diffDays+" days ago";
            }else{
                return diffDays+" day ago";
            }
        }else if(diffHours > 0){
            if(diffHours>1){
                return diffHours+" hours ago";
            }else{
                return diffHours+" hour ago";
            }
        }else if(diffMinutes > 0){
            if(diffHours>1){
                return diffMinutes+" minutes ago";
            }else{
                return diffMinutes+" minute ago";
            }
        }else{
            if(diffHours>1){
                return diffSeconds+" seconds ago";
            }else{
                return diffSeconds+" seconds ago";
            }
        }
    }
}
