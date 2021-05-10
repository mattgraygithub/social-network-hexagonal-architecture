package com.mattgray.socialnetworkkata.adapter;

import com.mattgray.socialnetworkkata.port.FolloweeRepository;

import java.util.ArrayList;

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
    public ArrayList<String> getFollowedUsers() {
        return followedUsers;
    }
}
