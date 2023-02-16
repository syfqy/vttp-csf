package vttp.csf.workshop39.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepository {

  @Autowired
  MongoTemplate mongoTemplate;

  private static final String COMMENT_COLLECTION = "comments";

  public Optional<List<String>> getCommentsByCharacterId(Integer characterId) {
    /*
    db.comments.find({
        characterId: 1023
    }).sort({createdAt: -1})
    .limit(10);
     */

    // filter by characterId
    Criteria c = Criteria.where("characterId").is(characterId);
    Query q = Query.query(c);

    // sort by most recent
    Sort sortByMostRecent = Sort.by(Sort.Direction.DESC, "createdAt");
    q.with(sortByMostRecent);

    // limit to 10 most recent comments
    q.limit(10);

    // query results
    List<Document> results = mongoTemplate.find(
      q,
      Document.class,
      COMMENT_COLLECTION
    );

    // if no comments for character, return empty
    if (results.size() < 1) {
      System.out.println(
        "No comments found for character: %d".formatted(characterId)
      );
      return Optional.empty();
    }

    // map Document -> String
    List<String> comments = results
      .stream()
      .map(d -> d.getString("comment"))
      .toList();

    System.out.println(
      "%d comments found for character: %d".formatted(
          comments.size(),
          characterId
        )
    );

    return Optional.of(comments);
  }

  public boolean createComment(Integer characterId, String comment) {
    Document doc = new Document();
    doc.put("characterId", characterId);
    doc.put("comment", comment);
    doc.put("createdAt", new Date());

    Document docInserted = mongoTemplate.insert(doc, COMMENT_COLLECTION);
    System.out.println(">>> Document inserted: " + docInserted);

    // return true if document inserted successfully
    return docInserted != null;
  }
}
