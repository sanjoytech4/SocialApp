package com.socialapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.socialapp.R;
import com.socialapp.async.CreateTeamAsync;
import com.socialapp.bean.Team;
import com.socialapp.constants.Constants;
import com.socialapp.db.DatabaseHelper;
import com.socialapp.inter.FragmentInterface;
import com.socialapp.utils.LoginInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class CreateTeamFragment extends Fragment implements View.OnClickListener,Constants
{
    private FragmentInterface fragmentInterface;
    private EditText teamName;
    private String teamIconPath,name;
    private String TAG=CreateTeamFragment.class.getSimpleName();
    private ImageView icon;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface= (FragmentInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_create_team, container, false);
        Button layout= (Button) rootView.findViewById(R.id.skip_create_team);
        layout.setOnClickListener(this);
        LinearLayout takeIcon= (LinearLayout) rootView.findViewById(R.id.take_icon);
        takeIcon.setOnClickListener(this);

        icon= (ImageView) rootView.findViewById(R.id.icon);
        Button createTeam= (Button) rootView.findViewById(R.id.create_team);
        createTeam.setOnClickListener(this);
        teamName= (EditText) rootView.findViewById(R.id.create_team_name);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.skip_create_team :
                fragmentInterface.removeCreateTeamFragment();
                break;
            case R.id.create_team :
                if(isValidate())
                {
                    Team team= createTeam();
                    DatabaseHelper helper=DatabaseHelper.getInstance(getActivity());
                    long id=helper.insertTeam( team);
                    team.setLocalId((int)id);
                    new CreateTeamAsync(getActivity(),team).execute();
                }
                break;
            case R.id.take_icon:
                showPhotoChooser();
                break;
        }

    }

    private boolean isValidate()
    {
        name=teamName.getText().toString();
        if(name.trim().isEmpty())
        {
            teamName.setError(getResources().getString(R.string.valid_teamName));
            return false;
        }
        else if(teamIconPath==null)
        {
            showLoginInfoDialog(getResources().getString(R.string.valid_teamIcon));
            return false;
        }
        return true;
    }

    /* show user alert for Team icon */
    private void showLoginInfoDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getString(R.string.app_close_dialog_button_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private Team createTeam()
    {
        LoginInfo info=new LoginInfo(getActivity());
        Team team=new Team();
        team.setIconLocalPath(teamIconPath);
        team.setName(name);
        team.setOwnerName(info.getLoggedInUser());
        return  team;
    }

    private void showPhotoChooser() {
        File file=getOutputMediaFile();
        teamIconPath=file.getPath();
        Uri outputFileUri = Uri.fromFile(file);
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_PICK);
        pickIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        String pickTitle = getResources().getString(R.string.choose_photo);
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[]{takePhotoIntent}
                );

        getActivity(). startActivityForResult(chooserIntent, REQUEST_ICON_CAPTURE);
    }

    public void updateTeamIcon(String teamIconPath)
    {
        if(teamIconPath!=null) {
            this.teamIconPath = teamIconPath;
        }
        Bitmap bitmap=BitmapFactory.decodeFile(this.teamIconPath);
        Log.i("TAG","imagepath "+teamIconPath);
        Log.i("TAG","imagepath bitmap "+bitmap);
        icon.setImageBitmap(bitmap);
    }

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(DATA_PATH);
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");
        return mediaFile;
    }
}
