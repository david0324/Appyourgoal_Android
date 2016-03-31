package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/14/2015.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prize {

    @SerializedName("prize_id")
    @Expose
    private String prizeId;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("prize_picture")
    @Expose
    private String prizePicture;
    @SerializedName("prize_notification")
    @Expose
    private String prizeNotification;
    @SerializedName("current_prize")
    @Expose
    private Integer currentPrize;

    /**
     *
     * @return
     * The prizeId
     */
    public String getPrizeId() {
        return prizeId;
    }

    /**
     *
     * @param prizeId
     * The prize_id
     */
    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    /**
     *
     * @return
     * The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     * The start_date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return
     * The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate
     * The end_date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return
     * The prizePicture
     */
    public String getPrizePicture() {
        return prizePicture;
    }

    /**
     *
     * @param prizePicture
     * The prize_picture
     */
    public void setPrizePicture(String prizePicture) {
        this.prizePicture = prizePicture;
    }

    /**
     *
     * @return
     * The prizeNotification
     */
    public String getPrizeNotification() {
        return prizeNotification;
    }

    /**
     *
     * @param prizeNotification
     * The prize_notification
     */
    public void setPrizeNotification(String prizeNotification) {
        this.prizeNotification = prizeNotification;
    }

    /**
     *
     * @return
     * The currentPrize
     */
    public Integer getCurrentPrize() {
        return currentPrize;
    }

    /**
     *
     * @param currentPrize
     * The current_prize
     */
    public void setCurrentPrize(Integer currentPrize) {
        this.currentPrize = currentPrize;
    }

}