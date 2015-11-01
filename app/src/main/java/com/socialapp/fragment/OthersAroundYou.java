package com.socialapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.socialapp.R;
import com.socialapp.adapter.TeamAdapter;
import com.socialapp.async.GetTeamOrChallengeList;
import com.socialapp.bean.Team;
import com.socialapp.db.DatabaseHelper;
import com.socialapp.inter.FragmentInterface;

import java.util.ArrayList;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class OthersAroundYou  extends Fragment implements AdapterView.OnItemClickListener
{
    private FragmentInterface fragmentInterface;
    private TeamAdapter adapter;
    private ArrayList<Team> teamList;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface= (FragmentInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_others_around_you, container, false);
        ListView listView= (ListView) rootView.findViewById(R.id.people_around_you_list);
        listView.setOnItemClickListener(this);
        teamList=new ArrayList<Team>();
        adapter=new TeamAdapter(getActivity());
        listView.setAdapter(adapter);
        getTeamList();
        return rootView;
    }

    private void getTeamList()
    {
        DatabaseHelper helper=DatabaseHelper.getInstance(getActivity());
        teamList=helper.getTeamList();
        adapter.setTeamList(teamList);

        if(teamList.size()==0)
        {
            Toast.makeText(getActivity(), "There is no Created team locally", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Team team=teamList.get(position);
        fragmentInterface.showChallengeFragment(team);
    }
}
