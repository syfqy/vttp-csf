package csf.tutorials.workshop37.services;

import csf.tutorials.workshop37.models.Post;
import csf.tutorials.workshop37.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  @Autowired
  private PostRepository postRepository;

  public boolean createPost(Post post) {
    return postRepository.createPost(post);
  }
}
