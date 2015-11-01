package com.socialapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.socialapp.bean.Team;
import com.socialapp.constants.Constants;
import com.socialapp.fragment.ChallengeFragment;
import com.socialapp.fragment.CreateTeamFragment;
import com.socialapp.fragment.OthersAroundYou;
import com.socialapp.inter.AsyncInterface;
import com.socialapp.inter.FragmentInterface;


public class MainActivity extends AppCompatActivity implements FragmentInterface,View.OnClickListener,Constants,AsyncInterface {
    private CreateTeamFragment createTeamFragment;
    private OthersAroundYou othersAroundYou;
    private ChallengeFragment challengeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button othersAroundYou= (Button) findViewById(R.id.others_around_you);
        othersAroundYou.setOnClickListener(this);
        Button team_mission= (Button) findViewById(R.id.team_mission);
        team_mission.setOnClickListener(this);
        Button rewards= (Button) findViewById(R.id.rewards);
        rewards.setOnClickListener(this);
        showCreateTeamFragment();
    }

    @Override
    public void goToMainScreen() {

    }

    @Override
    public void removeCreateTeamFragment() {
        if(createTeamFragment!=null)
        {
            getSupportFragmentManager().beginTransaction().remove(createTeamFragment).commit();
        }
    }

    @Override
    public void showChallengeFragment(Team team) {
        challengeFragment=new ChallengeFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(TEAM,team);
        challengeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.mainLayout,challengeFragment).commit();
    }

    private void showCreateTeamFragment(){
        createTeamFragment=new CreateTeamFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mainLayout,createTeamFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.others_around_you :
                showOthersAroundYou();
                break;
            case R.id.team_mission:
                break;
            case R.id.rewards:
                break;
        }
    }

    private void showOthersAroundYou()
    {
        othersAroundYou=new OthersAroundYou();
        getSupportFragmentManager().beginTransaction().add(R.id.mainLayout,othersAroundYou).commit();
    }

    @Override
    public void RegistrationResponse(String response) {

    }

    @Override
    public void sendChallengeResponse(String response) {
        Toast.makeText(this,"There is no server API, so it is working locally",Toast.LENGTH_LONG).show();
    }

    @Override
    public void getChallengeListResponse(String response) {

    }

    @Override
    public void createTeamResponse(String response) {

    }
/**
    * Dispatch incoming result to the correct fragment.
    *
            * @param requestCode
    * @param resultCode
    * @param data
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            if(data==null || data.getAction()==null)
            {
                return;
            }

        }

        switch ((requestCode))
        {

            case Constants.REQUEST_ICON_CAPTURE :
                if(resultCode == RESULT_OK)
                {
                    String imagepath;
                    //if data is not null;image chosen from gallary and need to be copied to orc image location;uri saved to be used in summary fragment
                    if(data!=null) {
                         imagepath = getRealPathFromURI(data.getData());
                    }
                    else
                        imagepath= null;
                    Log.i("TAG","imagepath "+imagepath);
                    createTeamFragment.updateTeamIcon(imagepath);
                }
                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
