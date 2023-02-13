package csf.tutorials.workshop37.repositories;

public class Queries {

  public static final String SQL_INSERT_INTO_POSTS =
    """
            INSERT INTO posts (post_id, comments, picture)
            VALUES (?, ?, ?)
            """;
}
