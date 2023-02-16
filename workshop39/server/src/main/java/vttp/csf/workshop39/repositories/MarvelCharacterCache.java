package vttp.csf.workshop39.repositories;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import vttp.csf.workshop39.models.MarvelCharacter;

@Repository
public class MarvelCharacterCache {

  @Autowired
  @Qualifier("MARVEL_CACHE")
  RedisTemplate<String, String> redisTemplate;

  public void saveCharacters(List<MarvelCharacter> characters) {
    for (MarvelCharacter c : characters) {
      redisTemplate
        .opsForValue()
        .set(
          c.getCharacterId().toString(),
          c.toJson().toString(), // MarvelCharacter -> JsonObject -> String
          Duration.ofHours(1) // cache result for 1 hr
        );
    }
  }

  public Optional<MarvelCharacter> getCharacterById(Integer characterId) {
    String key = characterId.toString();
    String value = redisTemplate.opsForValue().get(key);

    // if character not found, return empty object
    if (value == null) return Optional.empty();

    // if character found, String -> JsonObject -> MarvelCharacter
    JsonReader reader = Json.createReader(new StringReader(value));
    JsonObject json = reader.readObject();
    MarvelCharacter character = MarvelCharacter.createFromCache(json);
    return Optional.of(character);
  }
}
