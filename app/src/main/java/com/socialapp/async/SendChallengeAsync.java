package com.socialapp.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.socialapp.R;
import com.socialapp.bean.Challenge;
import com.socialapp.constants.Constants;
import com.socialapp.inter.AsyncInterface;
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
public class SendChallengeAsync extends AsyncTask<String,Void,String> implements Constants
{
    private String TAG=SendChallengeAsync.class.getSimpleName();
    private Context context;
    ProgressDialog progress;
    private AsyncInterface asyncInterface;
    private Challenge challenge;

    public SendChallengeAsync(Context context,Challenge challenge)
    {
        this.context=context;
        this.asyncInterface= (AsyncInterface) context;
        this.challenge=challenge;
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
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Content-Type","application/json; charset=utf-8");

            con.setRequestMethod(API_REQUEST_METHOD_POST);

            con.setRequestProperty(HEADER_CLIENT_ID, HEADER_CLIENT_ID_VALUE);
            con.setRequestProperty(HEADER_CLIENT_SECRET, HEADER_CLIENT_SECRET_VALUE);
            con.setRequestProperty(HEADER_ORGANIZATION, HEADER_ORGANIZATION_VALUE);

            //timeout after 10 seconds
            con.setConnectTimeout(timeoutInMillliseconds);
            con.setReadTimeout(timeoutInMillliseconds);

            HashMap<String,String> params = new HashMap<String,String>();
            params.put(Constants.TEAM_ID, challenge.getTeamId()+"");
            params.put(Constants.CHALLENGE_ID, challenge.getLocalId()+"");
            params.put(Constants.CHALLENGE_NAME, challenge.getName()+"");

            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();

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
        asyncInterface.sendChallengeResponse(result);
    }

    private String getUrl()
    {
        String url = Uri.parse(baseURL)
                .buildUpon()
                .appendEncodedPath(SEND_CHALLENGE)
                .build()
                .toString();
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
