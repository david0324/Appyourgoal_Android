package app.appyourgoal.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import app.appyourgoal.R;
import app.appyourgoal.restdata.EditDataExample;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by Dragisa on 10/29/2015.
 */
public class EditProfileActivity extends Activity{


    private String LOG = "djevticAPP";
    private static final int SELECT_PICTURE = 1;

    private FontHelper mFontHelper;
    private UserData mUser;
    private EditDataExample mEditData;
    private String mFilePatch = "";

    private RelativeLayout mColorRelativeLayout;
    private EditProfileActivity mActivity;
    private TextView mCancel;
    private TextView mName;
    private TextView mSave;
    private TextView mBorderTapText;
    private TextView mContestRules;
    private TextView mPrivacypolicy;
    private TextView mTermsOfUse;
    private EditText mUserName;
    private EditText mUserCountry;
    private ImageView mUserImage;
    private ImageView mStripe;
    private ProgressBar mProgresBar;

    private int mBackgroundColor = 0;
    private int mStripeColor = 0;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_edit);

        //Font helper for setting custom fonts
        mFontHelper = new FontHelper();

        //Initialize components
        mActivity = this;
        mUser = UserData.getInstance();
        mColorRelativeLayout = (RelativeLayout) findViewById(R.id.profile_edit_relative_layout);
        mCancel = (TextView) findViewById(R.id.profile_edit_cancel);
        mName = (TextView) findViewById(R.id.profile_edit_profile_editor);
        mSave = (TextView) findViewById(R.id.profile_edit_save);
        mBorderTapText = (TextView) findViewById(R.id.profile_edit_border_tap);
        mUserName = (EditText) findViewById(R.id.edit_profile_player_name);
        mUserCountry = (EditText) findViewById(R.id.edit_profile_player_country);
        mUserImage = (ImageView) findViewById(R.id.profile_edit_player_image);
        mStripe = (ImageView) findViewById(R.id.profile_edit_stripe);
        mProgresBar = (ProgressBar) findViewById(R.id.profile_edit_progress_bar);
        mContestRules = (TextView) findViewById(R.id.profile_edit_contest_rules);
        mPrivacypolicy = (TextView) findViewById(R.id.profile_edit_privacy_policy);
        mTermsOfUse = (TextView) findViewById(R.id.profile_edit_terms_of_use);

        //Set background if already chose
        if(mUser.getmColorBackground() != 0){
            mColorRelativeLayout.setBackgroundColor(mUser.getmColorBackground());
            mUserName.setBackgroundColor(mUser.getmColorBackground());
            mUserCountry.setBackgroundColor(mUser.getmColorBackground());
        }
        if(mUser.getmStripeColor() != 0){
            mStripe.setColorFilter(mUser.getmStripeColor());
        }

        //Set fontface
        mFontHelper.setTextViewTypeface(mCancel, getApplicationContext());
        mFontHelper.setTextViewTypeface(mName, getApplicationContext());
        mFontHelper.setTextViewTypeface(mSave, getApplicationContext());
        mFontHelper.setTextViewTypeface(mBorderTapText, getApplicationContext());
        mFontHelper.setTextViewTypeface(mContestRules, getApplicationContext());
        mFontHelper.setTextViewTypeface(mPrivacypolicy, getApplicationContext());
        mFontHelper.setTextViewTypeface(mTermsOfUse, getApplicationContext());

        final AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, mColorRelativeLayout.getSolidColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mColorRelativeLayout.setBackgroundColor(color);
                mUserName.setBackgroundColor(color);
                mUserCountry.setBackgroundColor(color);
                mBackgroundColor = color;
                //mUser.setmColorBackground(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });

        final AmbilWarnaDialog stripeChangeDialog = new AmbilWarnaDialog(this, 0, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mStripe.setColorFilter(color);
                mStripeColor = color;
                //mUser.setmStripeColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
            }
        });

        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        mContestRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://appyourgoal.com/index.php/contest-rules/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        mPrivacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://appyourgoal.com/index.php/privacy-policy/appyourgoal-privacy-policy/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        mTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://appyourgoal.com/index.php/privacy-policy/terms-of-use/terms-of-use/ ";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        //Set on click listeners
        mColorRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        mStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stripeChangeDialog.show();
            }
        });
        
        getData();
    }

    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());
        client.get(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.USER_DETAILS_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in EditProfileActivity");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "EditProfileActivity Response post comment: " + responseString);
                //getData();
                Gson gson = new Gson();
                mEditData = gson.fromJson(responseString, EditDataExample.class);
                fillData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "EditProfileActivity On failure code: " + statusCode);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "EditProfileActivity On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    private void fillData() {
        mUserName.setText(mEditData.getData().getFirstName());
        mUserCountry.setText(mEditData.getData().getNationality());
        Picasso.with(mActivity).load(StaticURL.SERVER_URL+mEditData.getData().getProfilePicture()).error(R.drawable.male_avatar).placeholder(R.drawable.male_avatar).into(mUserImage);
    }

    public void onCancelEditProfile(View v){
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    public void onSaveEditProfile(View v){
        updateUserProfile();
    }

    private void updateUserProfile() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());

        StringEntity entity = null;
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("first_name", mUserName.getText().toString().trim());
            jsonParams.put("last_name", "");
            jsonParams.put("nationality", mUserCountry.getText().toString().trim());
            jsonParams.put("club_name", "");
            entity = new StringEntity(jsonParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.put(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.GET_USER_PROFILES, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in EditProfileActivity");
                mColorRelativeLayout.setVisibility(View.GONE);
                mCancel.setVisibility(View.GONE);
                mSave.setVisibility(View.GONE);
                mProgresBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "EditProfileActivity Response post comment: " + responseString);
                if(!mFilePatch.equals("")) {
                    uploadImage(mFilePatch);
                }else {
                    mColorRelativeLayout.setVisibility(View.VISIBLE);
                    mCancel.setVisibility(View.VISIBLE);
                    mSave.setVisibility(View.VISIBLE);
                    mUser.setmStripeColor(mStripeColor);
                    mUser.setmColorBackground(mBackgroundColor);
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("appyourgoal", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("stripeColor", mStripeColor);
                    editor.putInt("backgroundColor", mBackgroundColor);
                    editor.commit();
                    mProgresBar.setVisibility(View.GONE);
                    mActivity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                    mActivity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "EditProfileActivity On failure code: " + statusCode);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "EditProfileActivity On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgDecodableString;
        String filePath = "";
        try {
            // When an Image is picked
            if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();
                Log.d(LOG,"EditProfile Build.VERSION.SDK_INT:"+Build.VERSION.SDK_INT);
                if(Build.VERSION.SDK_INT <19) {
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = cursor.getString(columnIndex);
                    cursor.close();

                }else{
                    String wholeID = DocumentsContract.getDocumentId(selectedImage);

                    // Split at colon, use second item in the array
                    String id = wholeID.split(":")[1];

                    String[] column = {MediaStore.Images.Media.DATA};

                    // where id is equal to
                    String sel = MediaStore.Images.Media._ID + "=?";

                    Cursor cursor = getContentResolver().
                            query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{id}, null);

                    int columnIndex = cursor.getColumnIndex(column[0]);

                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
                Log.d(LOG, "File path:" + filePath);
                File imgFile = new File(filePath);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(imgFile), null, options);
                int width_tmp = options.outWidth, height_tmp = options.outHeight;
                int scale = 1 ;
                int REQUIRED_SIZE = 400;
                while (true) {
                    if (width_tmp / 2 < REQUIRED_SIZE
                            || height_tmp / 2 < REQUIRED_SIZE)
                        break;
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }

                // decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeStream(new FileInputStream(imgFile), null, o2);//BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    mUserImage.setImageBitmap(myBitmap);
                    mFilePatch = filePath;

                }

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void uploadImage(String filePath) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", mUser.getmTokenType() + " " + mUser.getmToken());
        File myFile = new File(filePath);
        RequestParams params = new RequestParams();
        try {
            params.put("file", myFile);
            params.put("name", getFileName(filePath));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        client.put(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.UPDATE_PROFILE_PICTURE, params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in EditProfileActivity");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                Log.d(LOG, "EditProfileActivity Response post comment: " + responseString);
                mColorRelativeLayout.setVisibility(View.VISIBLE);
                mCancel.setVisibility(View.VISIBLE);
                mSave.setVisibility(View.VISIBLE);
                mProgresBar.setVisibility(View.GONE);
                mActivity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                mActivity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "EditProfileActivity On failure code: " + statusCode);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "EditProfileActivity On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    private String getFileName(String filePath) {
        String[] slices = filePath.split("/");
        return slices[slices.length-1];
    }
}
