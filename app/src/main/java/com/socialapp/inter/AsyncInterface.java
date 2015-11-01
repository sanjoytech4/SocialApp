package com.socialapp.inter;


public interface AsyncInterface
{
    void RegistrationResponse(String response);
    void sendChallengeResponse(String response);
    void getChallengeListResponse(String response);
    void createTeamResponse(String response);
}
