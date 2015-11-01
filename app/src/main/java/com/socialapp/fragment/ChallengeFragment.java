package com.socialapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.socialapp.R;
import com.socialapp.async.SendChallengeAsync;
import com.socialapp.bean.Challenge;
import com.socialapp.bean.Team;
import com.socialapp.constants.Constants;
import com.socialapp.db.DatabaseHelper;
import com.socialapp.inter.FragmentInterface;
import com.socialapp.utils.Network;

import java.util.ArrayList;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class ChallengeFragment extends Fragment implements ViewPager.OnPageChangeListener,Constants
{
    private FragmentInterface fragmentInterface;
    private Team team;
    private ViewPager viewPager;
    private ArrayList<Challenge> challenges=new ArrayList<Challenge>();
    private  ChallengePageAdapter adapter;
    private Challenge selectedChallenge;
    private String TAG=ChallengeFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface= (FragmentInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_challenge, container, false);
        viewPager= (ViewPager) rootView.findViewById(R.id.viewPager);
        adapter=new ChallengePageAdapter(getActivity(),challenges);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        getTeam();
        getChallenge();
        return rootView;
    }

    private void getTeam()
    {
        Bundle bundle=getArguments();
        if(bundle!=null) {
            team = (Team) bundle.getSerializable(TEAM);
        }
    }

    private void getChallenge()
    {
        if(team!=null)
        {
            DatabaseHelper helper=DatabaseHelper.getInstance(getActivity());
            challenges=helper.getChallengeList(team.getLocalId());
            if(challenges.size()==0)
            {
                getDummyChallenge();
            }
            adapter.updateChallengeList(challenges);
        }
    }

    private  void getDummyChallenge()
    {
        DatabaseHelper helper=DatabaseHelper.getInstance(getActivity());
        Challenge challenge1=new Challenge();
        challenge1.setTeamId(team.getLocalId());
        challenge1.setName("Challenge 1");
        challenge1.setDescription("Description for Challenge 1");
        challenge1.setDate("31/12/2015");
        challenge1.setCreatedBy("System");

        Challenge challenge2=new Challenge();
        challenge2.setTeamId(team.getLocalId());
        challenge2.setName("Challenge 2");
        challenge2.setDescription("Description for Challenge 2");
        challenge2.setDate("31/12/2016");
        challenge2.setCreatedBy("System");

        Challenge challenge3=new Challenge();
        challenge3.setTeamId(team.getLocalId());
        challenge3.setName("Challenge 3");
        challenge3.setDescription("Description for Challenge 3");
        challenge3.setDate("25/12/2015");
        challenge3.setCreatedBy("System");

        helper.insertChallenge(challenge1);
        helper.insertChallenge(challenge2);
        helper.insertChallenge(challenge3);

        challenges.add(challenge1);
        challenges.add(challenge2);
        challenges.add(challenge3);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectedChallenge=challenges.get(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class ChallengePageAdapter extends PagerAdapter implements View.OnClickListener
    {
        private ArrayList<Challenge> challenges=new ArrayList<Challenge>();
        private Context context;
        public ChallengePageAdapter(Context context,ArrayList<Challenge> challenges)
        {
            this.context=context;
            this.challenges=challenges;
        }

        @Override
        public int getCount() {
            return challenges.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            View view = getActivity().getLayoutInflater().inflate(R.layout.adapter_challenge_item, container,false);
            TextView challengeName= (TextView) view.findViewById(R.id.challenge_name);
            TextView challengeOwner= (TextView) view.findViewById(R.id.challenge_owner);
            TextView challengeDesc= (TextView) view.findViewById(R.id.challenge_desc);
            TextView challengeDate= (TextView) view.findViewById(R.id.challenge_date);
            Challenge challenge=challenges.get(position);

            challengeName.setText(challenge.getName());
            challengeDesc.setText(challenge.getDescription());
            challengeOwner.setText(context.getResources().getString(R.string.created_by)+" : "+challenge.getCreatedBy());
            challengeDate.setText(context.getResources().getString(R.string.event_date)+" : "+challenge.getDate());

            Button SendChallenge=(Button)view.findViewById(R.id.send_challenge);
            SendChallenge.setOnClickListener(this);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
        public void updateChallengeList(ArrayList<Challenge> list)
        {
            challenges.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.send_challenge :
                    Log.i(TAG,"selectedChallenge "+selectedChallenge);

                    if(selectedChallenge==null ) {
                        selectedChallenge = challenges.get(viewPager.getCurrentItem());
                    }
                    if(new Network(getActivity()).isAvailable(true))
                    {
                        new SendChallengeAsync(getActivity(),selectedChallenge).execute();
                    }
            }
        }
    }


}
