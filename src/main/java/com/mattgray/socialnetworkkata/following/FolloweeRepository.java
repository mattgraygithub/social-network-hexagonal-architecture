package com.mattgray.socialnetworkkata.following;

import java.util.ArrayList;

public interface FolloweeRepository {

    void addFollowee(String userName);

    ArrayList<String> getFollowedUsers();
}
