package com.mattgray.socialnetworkkata.followees;

import java.util.ArrayList;

public interface FolloweeRepository {

    void addFollowee(String userName);

    ArrayList<String> getFollowedUsers();
}
