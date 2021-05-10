package com.mattgray.socialnetworkkata.port;

import java.util.ArrayList;

public interface FolloweeRepository {

    void addFollowee(String userName);

    ArrayList<String> getFollowedUsers();
}
