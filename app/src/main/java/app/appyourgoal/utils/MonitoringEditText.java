package app.appyourgoal.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

import app.appyourgoal.activities.PostYourPastedGoalActivity;

/**
 * Created by Dragisa on 11/7/2015.
 */
public class MonitoringEditText  extends EditText {

    private final Context context;
    private PostYourPastedGoalActivity mActivity;

    /*
        Just the constructors to create a new EditText...
     */
    public MonitoringEditText(Context context) {
        super(context);
        this.context = context;
    }

    public MonitoringEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MonitoringEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /**
     * <p>This is where the "magic" happens.</p>
     * <p>The menu used to cut/copy/paste is a normal ContextMenu, which allows us to
     *  overwrite the consuming method and react on the different events.</p>
     * @see <a href="http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3_r1/android/widget/TextView.java#TextView.onTextContextMenuItem%28int%29">Original Implementation</a>
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        // Do your thing:
        boolean consumed = super.onTextContextMenuItem(id);
        // React:
        switch (id){
            case android.R.id.cut:
                onTextCut();
                break;
            case android.R.id.paste:
                mActivity.setYouTubeImage();
                break;
            case android.R.id.copy:
                onTextCopy();
        }
        return consumed;
    }

    /**
     * Text was cut from this EditText.
     */
    public void onTextCut(){
        Toast.makeText(context, "Cut!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Text was copied from this EditText.
     */
    public void onTextCopy(){
        Toast.makeText(context, "Copy!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Text was pasted into the EditText.
     */
    public void onTextPaste(){
        Toast.makeText(context, "Paste!", Toast.LENGTH_SHORT).show();
    }

    public PostYourPastedGoalActivity getmActivity() {
        return mActivity;
    }

    public void setmActivity(PostYourPastedGoalActivity mActivity) {
        this.mActivity = mActivity;
    }
}