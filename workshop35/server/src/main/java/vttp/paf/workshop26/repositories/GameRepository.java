package vttp.paf.workshop26.repositories;

import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vttp.paf.workshop26.models.Game;

@Repository
public class GameRepository {

  private static final String GAME_COLLECTION = "games";

  @Autowired
  private MongoTemplate mongoTemplate;

  public List<Game> findGames(Integer limit, Integer offset, boolean isRanked) {
    // partial match on name
    Criteria c = Criteria.where("name").exists(true);
    Query q = Query.query(c);

    // sort by ranking
    if (isRanked) q.with(Sort.by(Sort.Direction.ASC, "ranking"));

    // limit and offset
    q.limit(limit).skip(offset);

    // select fields
    q.fields().include("gid", "name");

    List<Document> results = mongoTemplate.find(
      q,
      Document.class,
      GAME_COLLECTION
    );

    List<Game> games = results
      .stream()
      .map(d -> Game.create(d, false))
      .toList();
    return games;
  }

  public Optional<Game> findGameById(Integer gameId) {
    Criteria c = Criteria.where("gid").is(gameId);
    Query q = Query.query(c);
    q.fields().exclude("_id");

    Document result = mongoTemplate.findOne(q, Document.class, GAME_COLLECTION);
    if (result == null) return Optional.empty();

    Game game = Game.create(result, true);
    return Optional.of(game);
  }
}
