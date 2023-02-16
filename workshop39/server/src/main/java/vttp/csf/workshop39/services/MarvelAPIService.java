package vttp.csf.workshop39.services;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vttp.csf.workshop39.models.MarvelCharacter;

@Service
public class MarvelAPIService {

  @Value("${MARVEL_API_PUBLIC_KEY}")
  private String publicKey;

  @Value("${MARVEL_API_PRIVATE_KEY}")
  private String privateKey;

  private static final String MARVEL_CHARACTERS_URL =
    "https://gateway.marvel.com:443/v1/public/characters";

  private String createUrlWithAuthParams() {
    // create authentication params
    Long ts = System.currentTimeMillis(); // current timestamp
    String signature = "%d%s%s".formatted(ts, privateKey, publicKey);
    String hash = "";

    // create hash from signature
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(signature.getBytes()); // signature -> bytes
      byte[] h = md5.digest(); // hashes bytes
      hash = HexFormat.of().formatHex(h); // bytes -> hex string
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    // build URL with auth params and query params
    final String url = UriComponentsBuilder
      .fromUriString(MARVEL_CHARACTERS_URL)
      .queryParam("ts", ts)
      .queryParam("apikey", publicKey)
      .queryParam("hash", hash)
      .toUriString();

    return url;
  }

  public List<MarvelCharacter> getCharacterById(Integer characterId) {
    String urlWithAuthParams = createUrlWithAuthParams();

    final String url = UriComponentsBuilder
      .fromUriString(urlWithAuthParams)
      .path("/%d".formatted(characterId))
      .toUriString();

    return getCharacters(url);
  }

  public List<MarvelCharacter> getCharactersByName(
    String nameStartsWith,
    Integer limit,
    Integer offset
  ) {
    // build URL with auth params and query params
    String urlWithAuthParams = createUrlWithAuthParams();
    final String url = UriComponentsBuilder
      .fromUriString(urlWithAuthParams)
      .queryParam("nameStartsWith", nameStartsWith)
      .queryParam("limit", limit)
      .queryParam("offset", offset)
      .toUriString();

    return getCharacters(url);
  }

  private List<MarvelCharacter> getCharacters(String url) {
    // create GET request
    RequestEntity req = RequestEntity
      .get(url)
      .accept(MediaType.APPLICATION_JSON)
      .build();

    // Make GET request and receive response
    RestTemplate template = new RestTemplate();
    ResponseEntity<String> resp = template.exchange(req, String.class);

    // String -> JsonObject
    JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
    JsonObject json = reader.readObject();

    // get results JsonArray from json
    JsonArray results = json.getJsonObject("data").getJsonArray("results");

    // map each JsonValue in results array into MarvelCharacter
    List<MarvelCharacter> characters = results
      .stream()
      .map(jv -> jv.asJsonObject()) // JsonValue -> JsonObject
      .map(jo -> MarvelCharacter.createFromJson(jo)) // JsonObject -> MarvelCharacter
      .toList();

    return characters;
  }
}
