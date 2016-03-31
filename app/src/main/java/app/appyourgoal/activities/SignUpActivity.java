package app.appyourgoal.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import app.appyourgoal.R;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 9/21/2015.
 */
public class SignUpActivity extends Activity {

    private UserData mUser;

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignUpButton;
    private ProgressBar mProgressBar;
    private LinearLayout mLayout;

    private SignUpActivity mThis;

    private String LOG = "djevticAPP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        //Font helper for custom fonts
        FontHelper fontHelper = new FontHelper();

        mThis = this;

        //Initializing all relevant views on screen
        mUsername = (EditText) findViewById(R.id.sign_up_screen_username);
        mEmail = (EditText) findViewById(R.id.sign_up_screen_email);
        mPassword = (EditText) findViewById(R.id.sign_up_screen_password);
        mSignUpButton = (Button) findViewById(R.id.sign_up_screen_button);
        mProgressBar = (ProgressBar) findViewById(R.id.sign_up_progress_bar);
        mLayout = (LinearLayout) findViewById(R.id.sign_up_layout);

        //Setting up custom fonts to views
        fontHelper.setEditTextTypeface(mUsername, getApplicationContext());
        fontHelper.setEditTextTypeface(mEmail,getApplicationContext());
        fontHelper.setEditTextTypeface(mPassword,getApplicationContext());
        fontHelper.setButtonTypeface(mSignUpButton, getApplicationContext());

        mUser = UserData.getInstance();

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void signUp() {
        Log.d(LOG, "CommentPageActivity get Data: " + StaticURL.SERVER_URL + StaticURL.GET_USER_PROFILES);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Content-Type", "application/json");

        StringEntity entity = null;
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("first_name", mUsername.getText().toString().trim());
            jsonParams.put("last_name", "");
            jsonParams.put("nationality", "");
            jsonParams.put("club_name", "");
            jsonParams.put("email", mEmail.getText().toString().trim());
            jsonParams.put("password", mPassword.getText().toString().trim());
            entity = new StringEntity(jsonParams.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.post(getApplicationContext(), StaticURL.SERVER_URL + StaticURL.GET_USER_PROFILES, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                Log.d(LOG, "Started in CommentPageActivity");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String responseString = new String(response);
                if(statusCode == 200){
                    onLoginClicked();
                }
                Log.d(LOG, "Response: " + responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "On failure code: " + statusCode);
                String responseString = new String(errorResponse);
                Log.d(LOG, "Response: " + responseString);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On retry: " + retryNo);
                // called when request is retried
            }
        });
    }

    private void onLoginClicked(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("grant_type", "password");
        params.put("username", mEmail.getText().toString().trim());
        params.put("password", mPassword.getText().toString().trim());
        params.put("client_id", "mobileapp");
        params.put("client_secret", "mobileappsecret");
        client.post(StaticURL.SERVER_URL+StaticURL.TOKEN_URL, params ,new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                mLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                mUser = UserData.getInstance();
                String responseString = new String(response);
                Log.d(LOG, "Response: " + responseString);
                try {
                    JSONObject json = new JSONObject(responseString);
                    SharedPreferences sharedPref = mThis.getSharedPreferences("appyourgoal", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", json.getString("access_token"));
                    editor.putString("token_type", json.getString("token_type"));
                    editor.putString("expires_in", json.getString("expires_in"));
                    editor.putString("issued", json.getString("issued"));
                    editor.putString("refresh_token", json.getString("refresh_token"));
                    editor.commit();
                    mUser.setmToken(json.getString("access_token"));
                    mUser.setmTokenType(json.getString("token_type"));
                    mUser.setmRefreshToken(json.getString("refresh_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressBar.setVisibility(View.GONE);
                Intent logIn = new Intent(getApplication(), MainActivity.class);
                logIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logIn);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d(LOG, "On failure code: " + statusCode);
                mLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Login error, please contact support", Toast.LENGTH_LONG).show();
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On retry: "+retryNo);
                // called when request is retried
            }
        });
    }
}
