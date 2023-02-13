package vttp.paf.workshop26.models;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import org.bson.Document;

public class Game {

  private Integer gid;
  private String name;
  private Integer year;
  private Integer ranking;
  //   private Double average;
  private Integer usersRated;
  private String url;
  private String image;

  public Integer getGid() {
    return gid;
  }

  public void setGid(Integer gid) {
    this.gid = gid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Integer getRanking() {
    return ranking;
  }

  public void setRanking(Integer ranking) {
    this.ranking = ranking;
  }

  public Integer getUsersRated() {
    return usersRated;
  }

  public void setUsersRated(Integer usersRated) {
    this.usersRated = usersRated;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public static Game create(Document d, boolean isFull) {
    Game g = new Game();
    g.setGid(d.getInteger("gid"));
    g.setName(d.getString("name"));
    if (isFull) {
      g.setYear(d.getInteger("year"));
      g.setRanking(d.getInteger("ranking"));
      g.setUsersRated(d.getInteger("users_rated"));
      g.setUrl(d.getString("url"));
      g.setImage(d.getString("image"));
    }
    return g;
  }

  public JsonValue toJson() {
    return Json.createObjectBuilder().add("gid", gid).add("name", name).build();
  }

  public JsonObjectBuilder toJsonObjectBuilder() {
    return Json
      .createObjectBuilder()
      .add("gid", gid)
      .add("name", name)
      .add("year", year)
      .add("ranking", ranking)
      .add("users_rated", usersRated)
      .add("url", url)
      .add("thumbnail", image);
  }
}
