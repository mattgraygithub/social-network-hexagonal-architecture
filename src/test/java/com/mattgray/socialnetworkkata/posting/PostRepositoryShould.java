package com.mattgray.socialnetworkkata.posting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static com.mattgray.socialnetworkkata.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class PostRepositoryShould {

    @Mock
    ArrayList<Post> postsMock;

    PostRepository postRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
        postRepository = new InMemoryPostRepository(postsMock);
    }

    @Test
    void addPosts() {
        postRepository.addPost(ALICE_USER_NAME, ALICE_EXAMPLE_POST, AT_12PM);

        verify(postsMock).add(new Post(ALICE_USER_NAME, ALICE_EXAMPLE_POST, AT_12PM));
    }

    @Test
    void getPosts() {
        assertThat(postRepository.getPosts()).isEqualTo(postsMock);
    }
}
