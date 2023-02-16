package vttp.csf.workshop39.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp.csf.workshop39.models.MarvelCharacter;
import vttp.csf.workshop39.repositories.CommentRepository;
import vttp.csf.workshop39.repositories.MarvelCharacterCache;

@Service
public class MarvelCharacterService {

  @Autowired
  private MarvelAPIService marvelAPISvc;

  @Autowired
  private MarvelCharacterCache marvelCharacterCache;

  @Autowired
  private CommentRepository commentRepo;

  public List<MarvelCharacter> getCharacters(
    String nameStartsWith,
    Integer limit,
    Integer offset
  ) {
    // retrieve characters from marvel API
    List<MarvelCharacter> characters = marvelAPISvc.getCharactersByName(
      nameStartsWith,
      limit,
      offset
    );

    // cache results in redis
    marvelCharacterCache.saveCharacters(characters);
    return characters;
  }

  public MarvelCharacter getCharacterById(Integer characterId) {
    MarvelCharacter character;

    // retrieve character from redis cache
    Optional<MarvelCharacter> optCharacter = marvelCharacterCache.getCharacterById(
      characterId
    );

    // check if character found
    if (optCharacter.isPresent()) {
      System.out.println("Character: %d found in cache".formatted(characterId));
      character = optCharacter.get();
    } else {
      // if character not found in cache
      System.out.println(
        "Character: %d not found in cache, retrieving data from Marvel API".formatted(
            characterId
          )
      );

      // retrieve character from Marvel API
      List<MarvelCharacter> characters = marvelAPISvc.getCharacterById(
        characterId
      );

      // cache results in redis
      marvelCharacterCache.saveCharacters(characters);

      character = characters.get(0); // get first item in list
    }

    // retrieve list of comments from MongoDB
    Optional<List<String>> optComments = commentRepo.getCommentsByCharacterId(
      characterId
    );

    // check if character has comments
    if (optComments.isPresent()) {
      // set comments
      character.setComments(optComments.get());
    }

    return character;
  }

  public boolean createComment(Integer characterId, String comment) {
    return commentRepo.createComment(characterId, comment);
  }
}
