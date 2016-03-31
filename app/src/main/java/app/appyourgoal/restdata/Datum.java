package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/4/2015.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Comparable<Datum>{

    @SerializedName("winner_id")
    @Expose
    private String winnerId;
    @SerializedName("video_id")
    @Expose
    private String videoId;
    @SerializedName("week_start")
    @Expose
    private String weekStart;
    @SerializedName("week_end")
    @Expose
    private String weekEnd;
    @SerializedName("likes_no")
    @Expose
    private Integer likesNo;
    @SerializedName("prize")
    @Expose
    private Prize prize;
    @SerializedName("prize_user_image")
    @Expose
    private String prizeUserImage;
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
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = new ArrayList<Comment>();
    @SerializedName("user")
    @Expose
    private User_ user;
    @SerializedName("likes_count")
    @Expose
    private String likesCount;
    @SerializedName("week_likes")
    @Expose
    private String weekLikes;
    @SerializedName("user_commented")
    @Expose
    private boolean userCommented;
    @SerializedName("user_liked")
    @Expose
    private boolean userLiked;

    /**
     *
     * @return
     * The winnerId
     */
    public String getWinnerId() {
        return winnerId;
    }

    /**
     *
     * @param winnerId
     * The winner_id
     */
    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public Datum withWinnerId(String winnerId) {
        this.winnerId = winnerId;
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

    public Datum withVideoId(String videoId) {
        this.videoId = videoId;
        return this;
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

    public Datum withWeekStart(String weekStart) {
        this.weekStart = weekStart;
        return this;
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

    public Datum withWeekEnd(String weekEnd) {
        this.weekEnd = weekEnd;
        return this;
    }

    /**
     *
     * @return
     * The likesNo
     */
    public int getLikesNo() {
        return likesNo;
    }

    /**
     *
     * @param likesNo
     * The likes_no
     */
    public void setLikesNo(String likesNo) {
        this.likesNo = Integer.getInteger(likesNo);
    }

    public Datum withLikesNo(String likesNo) {
        this.likesNo = Integer.getInteger(likesNo);
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

    public Datum withYoutubeId(String youtubeId) {
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

    public Datum withFile(Object file) {
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

    public Datum withUserId(String userId) {
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

    public Datum withDate(String date) {
        this.date = date;
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

    public Datum withComments(List<Comment> comments) {
        this.comments = comments;
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

    public Datum withUser(User_ user) {
        this.user = user;
        return this;
    }

    /**
     *
     * @return
     * The userCommented
     */
    public boolean isUserCommented() {
        return userCommented;
    }

    /**
     *
     * @param userCommented
     * The user_commented
     */
    public void setUserCommented(boolean userCommented) {
        this.userCommented = userCommented;
    }

    public Datum withUserCommented(boolean userCommented) {
        this.userCommented = userCommented;
        return this;
    }

    /**
     *
     * @return
     * The userLiked
     */
    public boolean isUserLiked() {
        return userLiked;
    }

    /**
     *
     * @param userLiked
     * The user_liked
     */
    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public Datum withUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
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

    public Prize getPrize() {
        return prize;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    public String getPrizeUserImage() {
        return prizeUserImage;
    }

    public void setPrizeUserImage(String prizeUserImage) {
        this.prizeUserImage = prizeUserImage;
    }

    @Override
    public int compareTo(Datum another) {
        int local = this.getLikesNo();
        int other = another.getLikesNo();
        if(local > other)
        {
            return -1;
        }
        else if(local < other)
        {
            return 1;
        }
        else
        {
            return 0;
        }

    }

    public String getWeekLikes() {
        return weekLikes;
    }

    public void setWeekLikes(String weekLikes) {
        this.weekLikes = weekLikes;
    }
}
