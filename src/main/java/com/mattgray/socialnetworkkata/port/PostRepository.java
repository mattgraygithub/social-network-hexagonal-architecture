package com.mattgray.socialnetworkkata.port;

import com.mattgray.socialnetworkkata.posting.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface PostRepository {
    void addPost(String userName, String post, LocalDateTime time);

    ArrayList<Post> getPosts();
}
