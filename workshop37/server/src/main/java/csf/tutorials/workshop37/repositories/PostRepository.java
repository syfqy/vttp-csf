package csf.tutorials.workshop37.repositories;

import static csf.tutorials.workshop37.repositories.Queries.*;

import csf.tutorials.workshop37.models.Post;
import java.io.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public boolean createPost(Post post) {
    return (
      jdbcTemplate.update(
        SQL_INSERT_INTO_POSTS,
        post.getPostId(),
        post.getComments(),
        new ByteArrayInputStream(post.getPicture()) // convert byte array into inputstream
      ) >
      0
    );
  }
}
