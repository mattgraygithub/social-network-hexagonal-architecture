package com.mattgray.socialnetworkkata.followees;

import java.util.ArrayList;

public class InMemoryFolloweeRepository implements FolloweeRepository {

    private final ArrayList<String> followedUsers = new ArrayList<>();

    @Override
    public void addFollowee(String userName) {
        followedUsers.add(userName);
    }
}
