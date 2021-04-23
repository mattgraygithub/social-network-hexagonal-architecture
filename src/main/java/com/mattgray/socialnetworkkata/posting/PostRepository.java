package com.mattgray.socialnetworkkata.posting;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface PostRepository {
    void addPost(String userName, String post, LocalDateTime time);

    ArrayList<Post> getPosts();
}
