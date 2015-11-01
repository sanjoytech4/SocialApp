package com.socialapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.socialapp.async.UserRegistrationTask;
import com.socialapp.bean.Team;
import com.socialapp.fragment.GeoLocationFragment;
import com.socialapp.inter.AsyncInterface;
import com.socialapp.inter.FragmentInterface;
import com.socialapp.utils.LoginInfo;
import com.socialapp.utils.Network;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,FragmentInterface,AsyncInterface
{
    private EditText usernameEdit,passwordEdit,confirmPasswordEdit;
    private String TAG="LoginActivity";
    private String userName;
    private int PASSWORD_MIN_LENGTH=8;
    private CallbackManager callbackManager;
    private LoginInfo loginInfo;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginInfo=new LoginInfo(this);
        setContentView(R.layout.activity_login);
        if(loginInfo.isLoggedIn())
        {
            loadGeoLocationFragment();
        }
        Button login= (Button)findViewById(R.id.login_button);
        login.setOnClickListener(this);
        LoginButton fb= (LoginButton) findViewById(R.id.fb_login_button);
        fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "loginResult.getAccessToken().getUserId() " + loginResult.getAccessToken().getUserId());
                loginInfo.setLoggedInUser(loginResult.getAccessToken().getUserId());
                loginInfo.setAccessToken(loginResult.getAccessToken().getToken());
                loginInfo.updateLoginStatus(true);
                loadGeoLocationFragment();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        });
        usernameEdit= (EditText) findViewById(R.id.login_username);
        passwordEdit= (EditText) findViewById(R.id.login_password);
        confirmPasswordEdit= (EditText) findViewById(R.id.confirm_password);
    }

    private void login()
    {
        Network network=new Network(this);
        if(!network.isAvailable(true))
        {
            return;
        }
        userName=usernameEdit.getText().toString();
        String password=passwordEdit.getText().toString();
        new UserRegistrationTask(this).execute(userName,password);
    }

    /* handle click event */
    public void onClick(View view){
        switch(view.getId())
        {
            case R.id.fb_login_button :
                break;
            case R.id.login_button :
                if(isValidate())
                {
                    login();
                }
                break;
        }
    }

    /* check user has given proper username and password or not */

    private boolean isValidate()
    {
        name=usernameEdit.getText().toString();
        String password=passwordEdit.getText().toString();
        String confirmPassword=confirmPasswordEdit.getText().toString();
        if(!isValidEmail(name))
        {
            usernameEdit.setError(getResources().getString(R.string.valid_username));
            return false;
        }
        else if(password.trim().isEmpty())
        {
            passwordEdit.setError(getResources().getString(R.string.valid_password));
            return false;
        }
        else if(password.trim().length()<PASSWORD_MIN_LENGTH)
        {
            passwordEdit.setError(getResources().getString(R.string.password_length_alert));
            return false;
        }
        else  if(!confirmPassword.equals(password))
        {
            passwordEdit.setError(getResources().getString(R.string.password_confirm_password_same_alert));
            confirmPasswordEdit.setError(getResources().getString(R.string.password_confirm_password_same_alert));
            return false;
        }
        return true;
    }

    private  boolean isValidEmail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private void loadGeoLocationFragment(){
        GeoLocationFragment geoLocationFragment=new GeoLocationFragment();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.login_layout, geoLocationFragment).addToBackStack(geoLocationFragment.getClass().getName());
        tx.commit();
    }

    @Override
    public void goToMainScreen() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void removeCreateTeamFragment() {

    }

    @Override
    public void showChallengeFragment(Team team) {

    }

    @Override
    public void RegistrationResponse(String response) {
        loginInfo.setLoggedInUser(name);
        Toast.makeText(this, "There is no server API, so it is working locally", Toast.LENGTH_LONG).show();
        loadGeoLocationFragment();
    }

    @Override
    public void sendChallengeResponse(String response) {

    }

    @Override
    public void getChallengeListResponse(String response) {

    }

    @Override
    public void createTeamResponse(String response) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //manage login result
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

}
