package csf.tutorials.workshop37.controllers;

import csf.tutorials.workshop37.models.Post;
import csf.tutorials.workshop37.services.PostService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api")
public class PostController {

  @Autowired
  private PostService postService;

  @PostMapping(
    path = "/post",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> createPost(
    @RequestPart String comments,
    @RequestPart MultipartFile picture
  ) {
    // map request body to Post
    Post post = new Post();
    post.setPostId(UUID.randomUUID().toString().substring(0, 8));
    post.setComments(comments);
    try {
      post.setPicture(picture.getBytes()); // convert to byte array
    } catch (IOException e) {
      e.printStackTrace();
    }

    // save post to db
    if (!postService.createPost(post)) {
      // if cannot save post, return HTTP 500 status with error message
      JsonObject err = Json
        .createObjectBuilder()
        .add("message", "cannot create post")
        .build();
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(err.toString());
    }

    JsonObject resp = Json
      .createObjectBuilder()
      .add(
        "message",
        "post: %s created successfully".formatted(post.getPostId())
      )
      .build();

    return ResponseEntity.ok(resp.toString());
  }
}
