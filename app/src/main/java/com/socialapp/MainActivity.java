package com.socialapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
}
