package com.mattgray.socialnetworkkata.following;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryFolloweeRepository that = (InMemoryFolloweeRepository) o;
        return Objects.equals(followedUsers, that.followedUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followedUsers);
    }
}
