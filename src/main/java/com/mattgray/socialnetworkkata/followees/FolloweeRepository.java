package com.mattgray.socialnetworkkata.followees;

import java.util.List;

public interface FolloweeRepository {

    void addFollowee(String userName);

    List<String> getFollowedUsers();
}
