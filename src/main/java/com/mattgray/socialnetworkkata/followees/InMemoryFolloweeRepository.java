package com.mattgray.socialnetworkkata.followees;

import java.util.ArrayList;
import java.util.List;

public class InMemoryFolloweeRepository implements FolloweeRepository {

    private final ArrayList<String> followedUsers;

    public InMemoryFolloweeRepository(ArrayList<String> followedUsers) {
        this.followedUsers = followedUsers;
    }

    @Override
    public void addFollowee(String userName) {
        followedUsers.add(userName);
    }

    @Override
    public List<String> getFollowedUsers() {
        return followedUsers;
    }
}
