package app.appyourgoal.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;
import app.appyourgoal.R;
import app.appyourgoal.utils.FontHelper;
import app.appyourgoal.utils.StaticURL;
import app.appyourgoal.utils.UserData;

/**
 * Created by Dragisa on 9/21/2015.
 */
public class LoginActivity extends Activity {

    private final static String LOG = "djevticAPP";

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mLoginProblemLink;
    private Button mSignUpButton;
    private LoginButton mFacebookLink;
    private ProgressBar mProgressBar;
    private LinearLayout mLayout;

    private UserData mUser;
    private LoginActivity mThis;

    //Facebook
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login_layout);

        //Font helper for setting custom fonts
        FontHelper fontHelper = new FontHelper();

        mThis = this;


        //Initializing all relevant views
        mEmail = (EditText) findViewById(R.id.login_screen_email);
        mPassword = (EditText) findViewById(R.id.login_screen_password);
        mLoginButton = (Button) findViewById(R.id.login_screen_login_button);
        mLoginProblemLink = (TextView) findViewById(R.id.login_screen_login_problem_link);
        mSignUpButton = (Button) findViewById(R.id.login_screen_sign_up_button);
        mFacebookLink = (LoginButton) findViewById(R.id.login_screen_facebook);
        mLayout = (LinearLayout) findViewById(R.id.login_screen_linear_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);

        //Setting custom font to views
        fontHelper.setEditTextTypeface(mEmail,getApplicationContext());
        fontHelper.setEditTextTypeface(mPassword,getApplicationContext());
        fontHelper.setButtonTypeface(mLoginButton,getApplicationContext());
        fontHelper.setTextViewTypeface(mLoginProblemLink,getApplicationContext());
        fontHelper.setButtonTypeface(mSignUpButton,getApplicationContext());
        fontHelper.setTextViewTypeface(mFacebookLink,getApplicationContext());

        //Button click listeners
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked();
            }
        });//Facebook login
        mFacebookLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookClicked();
            }
        });

        // Callback registration
        mFacebookLink.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void onFacebookClicked() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "app.appyourgoal",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("grant_type", "password");
        params.put("first_name", mEmail.getText().toString().trim());
        params.put("account_typ", "facebook");
        params.put("client_id", "mobileapp");
        params.put("client_secret", "mobileappsecret");
        client.post(StaticURL.SERVER_URL+StaticURL.TOKEN_URL, params ,new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                mLayout.setVisibility(View.GONE);
                mSignUpButton.setVisibility(View.GONE);
                mFacebookLink.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                mUser = UserData.getInstance();
                String responseString = new String(response);
                Log.d(LOG, "Response: " + responseString);
                try {
                    JSONObject json = new JSONObject(responseString);
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("appyourgoal", Context.MODE_PRIVATE);
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
                Log.d(LOG, "On failure code: "+statusCode);
                String responseString = new String(errorResponse);
                Log.d(LOG, "On failure code: "+responseString);
                mLayout.setVisibility(View.VISIBLE);
                mSignUpButton.setVisibility(View.VISIBLE);
                mFacebookLink.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Login error, please try again", Toast.LENGTH_LONG).show();
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On retry: "+retryNo);
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
                mSignUpButton.setVisibility(View.GONE);
                mFacebookLink.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                mUser = UserData.getInstance();
                String responseString = new String(response);
                Log.d(LOG, "Response: " + responseString);
                try {
                    JSONObject json = new JSONObject(responseString);
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("appyourgoal", Context.MODE_PRIVATE);
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
                Log.d(LOG, "On failure code: "+statusCode);
                mLayout.setVisibility(View.VISIBLE);
                mSignUpButton.setVisibility(View.VISIBLE);
                mFacebookLink.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Login error, please try again", Toast.LENGTH_LONG).show();
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                Log.d(LOG, "On retry: "+retryNo);
                // called when request is retried
            }
        });
        //Intent logIn = new Intent(this, MainActivity.class);
        //startActivity(logIn);
    }

    public void onSignUpClicked(View v){
        Intent signUp = new Intent(this, SignUpActivity.class);
        startActivity(signUp);
    }
}
