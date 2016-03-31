package app.appyourgoal.utils;

/**
 * Created by Dragisa on 11/3/2015.
 */
public class UserData {

    private static UserData instance = null;
    protected UserData() {
        // Exists only to defeat instantiation.
    }
    public static UserData getInstance() {
        if(instance == null) {
            instance = new UserData();
            instance.setmCommertialShown(false);
        }
        return instance;
    }

    private String mToken;
    private String mTokenType;
    private String mRefreshToken;
    private int mColorBackground = 0;
    private int mStripeColor = 0;

    private boolean mCommertialShown = false;


    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public String getmTokenType() {
        return mTokenType;
    }

    public void setmTokenType(String mTokenType) {
        this.mTokenType = mTokenType;
    }

    public String getmRefreshToken() {
        return mRefreshToken;
    }

    public void setmRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }

    public int getmColorBackground() {
        return mColorBackground;
    }

    public void setmColorBackground(int mColorBackground) {
        this.mColorBackground = mColorBackground;
    }

    public int getmStripeColor() {
        return mStripeColor;
    }

    public void setmStripeColor(int mStripeColor) {
        this.mStripeColor = mStripeColor;
    }

    public boolean ismCommertialShown() {
        return mCommertialShown;
    }

    public void setmCommertialShown(boolean mCommertialShown) {
        this.mCommertialShown = mCommertialShown;
    }
}
