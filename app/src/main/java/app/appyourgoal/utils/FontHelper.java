package app.appyourgoal.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Dragisa on 10/18/2015.
 */
public class FontHelper{

    private Typeface normalFont;
    private Typeface boldFont;

    public void setTextViewTypeface(TextView textView, Context context) {
        if(textView != null) {
            if(textView.getTypeface() != null && textView.getTypeface().isBold()) {
                textView.setTypeface(getBoldFont(context));
            } else {
                textView.setTypeface(getNormalFont(context));
            }
        }
    }

    public void setEditTextTypeface(EditText editText, Context context) {
        if(editText != null) {
            if(editText.getTypeface() != null && editText.getTypeface().isBold()) {
                editText.setTypeface(getBoldFont(context));
            } else {
                editText.setTypeface(getNormalFont(context));
            }
        }
    }

    public void setButtonTypeface(Button button, Context context) {
        if(button != null) {
            if(button.getTypeface() != null && button.getTypeface().isBold()) {
                button.setTypeface(getBoldFont(context));
            } else {
                button.setTypeface(getNormalFont(context));
            }
        }
    }

    private Typeface getNormalFont(Context context) {
        if(normalFont == null) {
            normalFont = Typeface.createFromAsset(context.getAssets(),"fonts/din_mediumalternate.ttf");
        }
        return this.normalFont;
    }

    private Typeface getBoldFont(Context context) {
        if(boldFont == null) {
            boldFont = Typeface.createFromAsset(context.getAssets(),"fonts/din_mediumalternate.ttf");
        }
        return this.boldFont;
    }
}
