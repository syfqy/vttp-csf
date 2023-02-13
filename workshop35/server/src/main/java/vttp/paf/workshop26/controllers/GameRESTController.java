package vttp.paf.workshop26.controllers;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vttp.paf.workshop26.models.Game;
import vttp.paf.workshop26.services.GameService;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class GameRESTController {

  @Autowired
  GameService gameSvc;

  @GetMapping("/games")
  public ResponseEntity<String> getGames(
    @RequestParam(defaultValue = "25") Integer limit,
    @RequestParam(defaultValue = "0") Integer offset
  ) {
    System.out.println("limit: " + limit);
    System.out.println("offset: " + offset);

    // get games
    boolean isRanked = false;
    List<Game> games = gameSvc.findGames(limit, offset, isRanked);

    // build response
    JsonArrayBuilder gamesArr = Json.createArrayBuilder();
    for (Game g : games) gamesArr.add(g.toJson());

    JsonValue resp = Json
      .createObjectBuilder()
      .add("games", gamesArr.build())
      .add("offset", offset)
      .add("limit", limit)
      .add("total", games.size())
      .add("timestamp", new Timestamp(System.currentTimeMillis()).toString()) // create timestamp on database
      .build();

    System.out.println("response: " + resp);

    return ResponseEntity.ok(resp.toString());
  }

  @GetMapping("/games/rank")
  public ResponseEntity<String> getGamesByNameRanked(
    @RequestParam(defaultValue = "25") Integer limit,
    @RequestParam(defaultValue = "0") Integer offset
  ) {
    System.out.println("limit: " + limit);
    System.out.println("offset: " + offset);

    // get games
    boolean isRanked = true;
    List<Game> games = gameSvc.findGames(limit, offset, isRanked);

    // build response
    JsonArrayBuilder gamesArr = Json.createArrayBuilder();
    for (Game g : games) gamesArr.add(g.toJson());

    JsonValue resp = Json
      .createObjectBuilder()
      .add("games", gamesArr.build())
      .add("offset", offset)
      .add("limit", limit)
      .add("total", games.size())
      .add("timestamp", new Timestamp(System.currentTimeMillis()).toString())
      .build();

    System.out.println("response: " + resp);

    return ResponseEntity.ok(resp.toString());
  }

  @GetMapping("/game/{gameId}")
  public ResponseEntity<String> getGameDetailsById(
    @PathVariable Integer gameId
  ) {
    Optional<Game> game = gameSvc.findGameDetailsById(gameId);

    if (game.isEmpty()) return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body("Cannot find game with id:" + gameId);

    JsonObjectBuilder gameJson = game.get().toJsonObjectBuilder();

    JsonValue resp = gameJson
      .add("timestamp", new Timestamp(System.currentTimeMillis()).toString())
      .build();

    return ResponseEntity.ok(resp.toString());
  }
}
