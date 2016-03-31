package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/5/2015.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medal {

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
    @SerializedName("prize_user_image")
    @Expose
    private String prizeUserImage;
    @SerializedName("prize")
    @Expose
    private Prize prize;
    @SerializedName("week_start")
    @Expose
    private String weekStart;
    @SerializedName("week_end")
    @Expose
    private String weekEnd;
    @SerializedName("week_likes")
    @Expose
    private String weekLikes;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("comments_count")
    @Expose
    private Integer commentsCount;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
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
     * The weekStart
     */
    public String getWeekStart() {
        return weekStart;
    }

    /**
     *
     * @param weekStart
     * The week_start
     */
    public void setWeekStart(String weekStart) {
        this.weekStart = weekStart;
    }

    /**
     *
     * @return
     * The weekEnd
     */
    public String getWeekEnd() {
        return weekEnd;
    }

    /**
     *
     * @param weekEnd
     * The week_end
     */
    public void setWeekEnd(String weekEnd) {
        this.weekEnd = weekEnd;
    }

    /**
     *
     * @return
     * The weekLikes
     */
    public String getWeekLikes() {
        return weekLikes;
    }

    /**
     *
     * @param weekLikes
     * The week_likes
     */
    public void setWeekLikes(String weekLikes) {
        this.weekLikes = weekLikes;
    }

    /**
     *
     * @return
     * The place
     */
    public String getPlace() {
        return place;
    }

    /**
     *
     * @param place
     * The place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     *
     * @return
     * The commentsCount
     */
    public Integer getCommentsCount() {
        return commentsCount;
    }

    /**
     *
     * @param commentsCount
     * The comments_count
     */
    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    /**
     *
     * @return
     * The likesCount
     */
    public Integer getLikesCount() {
        return likesCount;
    }

    /**
     *
     * @param likesCount
     * The likes_count
     */
    public void setLikesCount(Integer likesCount) {
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

    public String getPrizeUserImage() {
        return prizeUserImage;
    }

    public void setPrizeUserImage(String prizeUserImage) {
        this.prizeUserImage = prizeUserImage;
    }

    public Prize getPrize() {
        return prize;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }
}
