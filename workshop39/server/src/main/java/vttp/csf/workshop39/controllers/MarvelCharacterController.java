package vttp.csf.workshop39.controllers;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vttp.csf.workshop39.models.MarvelCharacter;
import vttp.csf.workshop39.services.MarvelCharacterService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class MarvelCharacterController {

  @Autowired
  private MarvelCharacterService marvelCharacterSvc;

  @GetMapping(path = "/characters")
  public ResponseEntity<String> getCharacters(
    @RequestParam String nameStartsWith,
    @RequestParam(defaultValue = "20") Integer limit,
    @RequestParam(defaultValue = "0") Integer offset
  ) {
    // get list of characters matching nameStartsWith
    List<MarvelCharacter> characters = marvelCharacterSvc.getCharacters(
      nameStartsWith,
      limit,
      offset
    );

    // List<MarvelCharacter> -> JsonArray
    JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

    // add each MarvelCharacter -> JsonObject
    characters.forEach(c -> arrBuilder.add(c.toJson()));

    JsonArray resp = arrBuilder.build();

    return ResponseEntity.ok(resp.toString());
  }

  @GetMapping(path = "/character/{characterId}")
  public ResponseEntity<String> getCharacterById(
    @PathVariable Integer characterId
  ) {
    System.out.println("Getting character: " + characterId);
    MarvelCharacter character = marvelCharacterSvc.getCharacterById(
      characterId
    );
    // MarvelCharacter -> JsonObject
    JsonObject resp = character.toJson();
    return ResponseEntity.ok(resp.toString());
  }

  @PostMapping(
    path = "/character/{characterId}",
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> postComment(
    @PathVariable Integer characterId,
    @RequestBody String body
  ) {
    // request body
    /*
    {
      ... : ...,
      ... : ...,
      ... : ...,
      comment : some comment here
    }
     */

    // get comment from request body
    JsonReader reader = Json.createReader(new StringReader(body));
    JsonObject json = reader.readObject();
    String comment = json.getString("comment");
    System.out.println(">>> comment: " + comment);

    if (!marvelCharacterSvc.createComment(characterId, comment)) {
      JsonObject resp = Json
        .createObjectBuilder()
        .add("error", "cannot create comment")
        .build();

      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(resp.toString());
    }

    JsonObject resp = Json
      .createObjectBuilder()
      .add("message", "comment created successfully")
      .build();

    return ResponseEntity.ok(resp.toString());
  }
}
