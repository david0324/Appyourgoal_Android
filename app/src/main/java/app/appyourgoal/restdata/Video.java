package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/5/2015.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

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
    @SerializedName("likes_count")
    @Expose
    private String likesCount;
    @SerializedName("week_likes")
    @Expose
    private String weekLikes;
    @SerializedName("user_commented")
    @Expose
    private Boolean userCommented;
    @SerializedName("user_liked")
    @Expose
    private Boolean userLiked;

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

    /**
     *
     * @return
     * The userCommented
     */
    public Boolean getUserCommented() {
        return userCommented;
    }

    /**
     *
     * @param userCommented
     * The user_commented
     */
    public void setUserCommented(Boolean userCommented) {
        this.userCommented = userCommented;
    }

    /**
     *
     * @return
     * The userLiked
     */
    public Boolean getUserLiked() {
        return userLiked;
    }

    /**
     *
     * @param userLiked
     * The user_liked
     */
    public void setUserLiked(Boolean userLiked) {
        this.userLiked = userLiked;
    }

    public String getWeekLikes() {
        return weekLikes;
    }

    public void setWeekLikes(String weekLikes) {
        this.weekLikes = weekLikes;
    }
}