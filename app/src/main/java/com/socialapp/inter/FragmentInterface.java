package com.socialapp.inter;

import com.socialapp.bean.Team;

public interface FragmentInterface
{
    void goToMainScreen();
    void removeCreateTeamFragment();
    void showChallengeFragment(Team team);
}
