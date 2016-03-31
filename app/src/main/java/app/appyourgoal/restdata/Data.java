package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/4/2015.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("youtube_id")
    @Expose
    private String youtubeId;
    @SerializedName("file")
    @Expose
    private Object file;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("api_endpoint")
    @Expose
    private String apiEndpoint;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = new ArrayList<Comment>();
    @SerializedName("comments_count")
    @Expose
    private String commentsCount;
    @SerializedName("likes_count")
    @Expose
    private String likesCount;
    @SerializedName("week_likes")
    @Expose
    private String weekLikes;
    @SerializedName("user")
    @Expose
    private User_ user;

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

    public Data withVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    /**
     *
     * @return
     * The youtubeId
     */
    public String getYoutubeId() {
        return youtubeId;
    }

    /**
     *
     * @param youtubeId
     * The youtube_id
     */
    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public Data withYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
        return this;
    }

    /**
     *
     * @return
     * The file
     */
    public Object getFile() {
        return file;
    }

    /**
     *
     * @param file
     * The file
     */
    public void setFile(Object file) {
        this.file = file;
    }

    public Data withFile(Object file) {
        this.file = file;
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

    public Data withUserId(String userId) {
        this.userId = userId;
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

    public Data withDate(String date) {
        this.date = date;
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

    public Data withApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
        return this;
    }

    /**
     *
     * @return
     * The comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     *
     * @param comments
     * The comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Data withComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    /**
     *
     * @return
     * The commentsCount
     */
    public String getCommentsCount() {
        return commentsCount;
    }

    /**
     *
     * @param commentsCount
     * The comments_count
     */
    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Data withCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
        return this;
    }

    /**
     *
     * @return
     * The likesCount
     */
    public String getLikesCount() {
        return likesCount;
    }

    /**
     *
     * @param likesCount
     * The likes_count
     */
    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public Data withLikesCount(String likesCount) {
        this.likesCount = likesCount;
        return this;
    }

    /**
     *
     * @return
     * The user
     */
    public User_ getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(User_ user) {
        this.user = user;
    }

    public Data withUser(User_ user) {
        this.user = user;
        return this;
    }

    public String getWeekLikes() {
        return weekLikes;
    }

    public void setWeekLikes(String weekLikes) {
        this.weekLikes = weekLikes;
    }
}