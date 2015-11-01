package com.socialapp.constants;

import android.os.Environment;

public interface Constants {

    String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/socialApp";
    String name="name";
    String value="value";
    String login="login";
    String password="password";
    String TEAM="team";
    String TEAM_ID="teamId";
    String LOCALITY="locality";
    String CHALLENGE_ID="challengeId";
    String CHALLENGE_NAME="challengeName";

    int REQUEST_ICON_CAPTURE=1;

   int timeoutInMillliseconds=10000;

    String baseURL= "";
    String login_api="user/registration";
    String CREATE_TEAM="/create_team";
    String SEND_CHALLENGE="/send_challenge";
    String GET_TEAM="/get_team_list";
    String GET_CHALLENGE="/get_challenge_list";
    String API_REQUEST_METHOD_POST="POST";
    String API_REQUEST_METHOD_GET="GET";

    String HEADER_CLIENT_ID="dummy_client_id";
    String HEADER_CLIENT_SECRET="client_secret";
    String HEADER_ORGANIZATION="organization";
    String HEADER_CLIENT_ID_VALUE="SocialApp";
    String HEADER_CLIENT_SECRET_VALUE="Testing";
    String HEADER_ORGANIZATION_VALUE="SocialEvening";

}
