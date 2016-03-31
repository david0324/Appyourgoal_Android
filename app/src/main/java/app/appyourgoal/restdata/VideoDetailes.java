package app.appyourgoal.restdata;

/**
 * Created by Dragisa on 11/4/2015.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoDetailes {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     *
     * @return
     * The result
     */
    public String getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(String result) {
        this.result = result;
    }

    public VideoDetailes withResult(String result) {
        this.result = result;
        return this;
    }

    /**
     *
     * @return
     * The status
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public VideoDetailes withStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public VideoDetailes withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    public VideoDetailes withData(Data data) {
        this.data = data;
        return this;
    }

}