package com.socialapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Places;
import com.socialapp.R;
import com.socialapp.bean.Team;

import java.util.ArrayList;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class TeamAdapter  extends ArrayAdapter<Team> {
    private final Context context;
    private ArrayList<Team> list;

    public TeamAdapter(Context context) {
        super(context, -1);
        this.context = context;
        this.list=new ArrayList<Team>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.adapter_item_team, parent, false);
        }
        TextView teamName=(TextView)convertView.findViewById(R.id.teamName);
        TextView ownerName=(TextView)convertView.findViewById(R.id.ownerName);
        Team team=list.get(position);
        teamName.setText(team.getName());
        ownerName.setText(team.getOwnerName());
        return convertView;
    }

    public void addTeam(Team team){
        this.list.add(team);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    public void clearSearch(){
        this.list.clear();
    }

    public void setTeamList(ArrayList<Team> list)
    {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }
}
