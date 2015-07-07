package com.teched.smartread;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private LoginButton loginButton;
    private Button gLogin;
    private TextView Logo;
    private ImageView LogoImage;
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress = false;
    private boolean mSignInClicked = false;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.fbLogin);
        gLogin = (Button) findViewById(R.id.gLogin);
        Logo = (TextView) findViewById(R.id.LogoText);
        LogoImage = (ImageView) findViewById(R.id.aboutImage);
        List<String> permissions = new ArrayList<>();
        permissions.add("email");
        permissions.add("public_profile");
        loginButton.setReadPermissions(permissions);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final LoginResult loginResult2 = loginResult;
                loginButton.setVisibility(View.GONE);
                gLogin.setVisibility(View.GONE);
                DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
                Logo.animate().alpha(1.0f).setDuration(750);
                LogoImage.animate().y(displayMetrics.heightPixels / 2 - Math.round(100 * displayMetrics.density)).scaleXBy(0.60f).scaleYBy(0.60f).setDuration(750)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult2.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject user, GraphResponse response) {
                                                try {
                                                    String image_value = "https://graph.facebook.com/" + user.getString("id") + "/picture?type=large";
                                                    String coverPicUrl = user.getJSONObject("cover").getString("source");
                                                    Transfer(user.getString("name"), user.getString("email"), image_value, coverPicUrl);
                                                } catch (Exception ignored) {
                                                }
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,link,email,cover");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                Snackbar.make(findViewById(R.id.loginScreen), R.string.no_connection, Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    mSignInClicked = true;
                    mGoogleApiClient.connect();
                }
                else
                    Snackbar.make(findViewById(R.id.loginScreen), R.string.no_connection, Snackbar.LENGTH_SHORT)
                            .show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            if (mSignInClicked && result.hasResolution()) {
                try {
                    result.startResolutionForResult(this, RC_SIGN_IN);
                    mIntentInProgress = true;
                } catch (IntentSender.SendIntentException e) {
                    mIntentInProgress = false;
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        loginButton.setVisibility(View.GONE);
        gLogin.setVisibility(View.GONE);
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        Logo.animate().alpha(1.0f).setDuration(750);
        LogoImage.animate().y(displayMetrics.heightPixels / 2 - Math.round(100 * displayMetrics.density)).scaleXBy(0.60f).scaleYBy(0.60f).setDuration(750)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                        Transfer(currentPerson.getName().getGivenName() + " " + currentPerson.getName().getFamilyName(), Plus.AccountApi.getAccountName(mGoogleApiClient), currentPerson.getImage().getUrl().subSequence(0, currentPerson.getImage().getUrl().length() - 2).toString() + "400", currentPerson.getCover().getCoverPhoto().getUrl());
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }
    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.reconnect();
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void WritePreferences() {
        SharedPreferences prefs = this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);
        if (prefs.getBoolean("firstTime", true)) {
            prefs.edit().putBoolean("firstTime", false).apply();
        }
    }
    public void Transfer(String user, String email, String ProfilePic, String ProfileCover)
    {
        PackageManager m = getPackageManager();
        String s = getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir+"/app_book";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        WritePreferences();
        try {
            JSONObject jsonObject = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_user.php?email=" + email));
            if (jsonObject.getInt("success")==1) {

            }
            else {
                JSONObject jsonObject1 = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/create_user.php?name=" + user + "&email=" + email + "&profileUrl=" + ProfilePic));
                jsonObject1 = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/add_book_user.php?email=" + email + "&book=" + "1"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        try {
            JSONObject books = new JSONObject(JsonClass.getJSON("http://php-smartread.rhcloud.com/get_books.php?email=" + email));
            JSONArray booksArray= books.getJSONArray("books");
            for (int i = 0; i<booksArray.length();i++) {
                JsonClass.DownloadBook(s, booksArray.getString(i) + ".pdf");
                JsonClass.DownloadBook(s,booksArray.getString(i)+".json");
            }
        } catch (Exception e) { e.printStackTrace(); }
        SharedPreferences prefs = this.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);
        prefs.edit().putString("ProfileName",user).apply();
        prefs.edit().putString("Email",email).apply();
        prefs.edit().putString("ProfilePicture", ProfilePic).apply();
        prefs.edit().putString("ProfileCover", ProfileCover).apply();
        Intent startScreen = new Intent(getApplicationContext(),MainActivity.class);
        startScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getBaseContext().startActivity(startScreen);
        finish();
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}