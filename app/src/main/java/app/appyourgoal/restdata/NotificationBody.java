package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/14/2015.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationBody {
    @SerializedName("notification_id")
    @Expose
    private String notificationId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("notification_text")
    @Expose
    private String notificationText;
    @SerializedName("date")
    @Expose
    private String date;

    /**
     *
     * @return
     * The notificationId
     */
    public String getNotificationId() {
        return notificationId;
    }

    /**
     *
     * @param notificationId
     * The notification_id
     */
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The notificationText
     */
    public String getNotificationText() {
        return notificationText;
    }

    /**
     *
     * @param notificationText
     * The notification_text
     */
    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

}
