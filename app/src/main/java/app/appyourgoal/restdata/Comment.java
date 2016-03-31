package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/4/2015.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Comment {

    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("comment_text")
    @Expose
    private String commentText;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("api_endpoint")
    @Expose
    private String apiEndpoint;
    @SerializedName("user")
    @Expose
    private User user;

    private Date properDate;

    /**
     *
     * @return
     * The commentId
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     *
     * @param commentId
     * The comment_id
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public Comment withCommentId(String commentId) {
        this.commentId = commentId;
        return this;
    }

    /**
     *
     * @return
     * The videoId
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     *
     * @param videoId
     * The video_id
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Comment withVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    /**
     *
     * @return
     * The commentText
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     *
     * @param commentText
     * The comment_text
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Comment withCommentText(String commentText) {
        this.commentText = commentText;
        return this;
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

    public Comment withDate(String date) {
        this.date = date;
        return this;
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

    public Comment withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     *
     * @return
     * The apiEndpoint
     */
    public String getApiEndpoint() {
        return apiEndpoint;
    }

    /**
     *
     * @param apiEndpoint
     * The api_endpoint
     */
    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public Comment withApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
        return this;
    }

    /**
     *
     * @return
     * The user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    public Comment withUser(User user) {
        this.user = user;
        return this;
    }

    public Date getProperDate() {
        return properDate;
    }

    public void setProperDate(Date properDate) {
        this.properDate = properDate;
    }
}