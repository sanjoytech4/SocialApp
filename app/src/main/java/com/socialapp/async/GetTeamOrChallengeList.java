package com.socialapp.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.socialapp.R;
import com.socialapp.bean.Challenge;
import com.socialapp.bean.Team;
import com.socialapp.constants.Constants;
import com.socialapp.inter.AsyncInterface;
import com.socialapp.utils.LoginInfo;
import com.socialapp.utils.ProgressDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Sanjoy on 01-11-2015.
 */
public class GetTeamOrChallengeList extends AsyncTask<String,Void,String> implements Constants
{
    private String TAG=SendChallengeAsync.class.getSimpleName();
    private Context context;
    ProgressDialog progress;
    private AsyncInterface asyncInterface;
    private Team team;
    private boolean isChallenge;
    private LoginInfo loginInfo;

    public GetTeamOrChallengeList(Context context, Team team)
    {
        this.context=context;
        this.asyncInterface= (AsyncInterface) context;
        this.team=team;
        isChallenge=true;
    }

    public GetTeamOrChallengeList(Context context)
    {
        this.context=context;
        this.asyncInterface= (AsyncInterface) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(context,context.getResources().getString(R.string.send_challenge_message));
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progress.show();
    }
    @Override
    protected String doInBackground(String... param) {
        try
        {
            URL obj = new URL(getUrl());
            loginInfo=new LoginInfo(context);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            con.setRequestMethod(API_REQUEST_METHOD_GET);

            con.setRequestProperty(HEADER_CLIENT_ID, HEADER_CLIENT_ID_VALUE);
            con.setRequestProperty(HEADER_CLIENT_SECRET, HEADER_CLIENT_SECRET_VALUE);
            con.setRequestProperty(HEADER_ORGANIZATION, HEADER_ORGANIZATION_VALUE);
            if(isChallenge) {
                con.setRequestProperty(TEAM_ID, team.getId());
            }
            else {
                con.setRequestProperty(LOCALITY, loginInfo.getLocality());
            }

            //timeout after 10 seconds
            con.setConnectTimeout(timeoutInMillliseconds);
            con.setReadTimeout(timeoutInMillliseconds);

            int responseCode = con.getResponseCode();
            Log.i(TAG, "Response Code : " + responseCode);


            BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();
            String result=response.toString();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(progress!=null)
        {
            progress.dismiss();
        }
        if(isChallenge)
        asyncInterface.getChallengeListResponse(result);
        else
            asyncInterface.getTeamListResponse(result);
    }

    private String getUrl()
    {
        String url="";
        if(isChallenge)
         url= Uri.parse(baseURL)
                .buildUpon()
                .appendEncodedPath(GET_CHALLENGE)
                .build()
                .toString();
        else {
            url= Uri.parse(baseURL)
                    .buildUpon()
                    .appendEncodedPath(GET_TEAM)
                    .build()
                    .toString();
        }
        return url;
    }

    private String getQuery(HashMap<String,String> params)
    {
        Set<String> keys=params.keySet();
        JSONObject object=new JSONObject();
        for (String key: keys)
        {
            try
            {
                object.put(key,params.get(key));
            }
            catch(Exception ex)
            {
                Log.i(TAG,"getQuery :: Exception "+ex.getMessage());
            }
        }

        Log.i(TAG," login request "+object.toString());
        return object.toString();
    }
}
