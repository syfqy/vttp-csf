package vttp2022.assessment.csf.orderbackend.controllers;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping(path = "api/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderRestController {

  @Autowired
  OrderService orderService;

  @PostMapping
  public ResponseEntity<String> createOrder(@RequestBody String orderForm) {
    JsonReader reader = Json.createReader(new StringReader(orderForm));
    JsonObject json = reader.readObject();

    Order order = new Order();
    order.setName(json.getString("name"));
    order.setEmail(json.getString("email"));
    order.setSize(json.getInt("pizzaSize"));
    order.setSauce(json.getString("pizzaSauce"));
    order.setThickCrust(json.getString("base").trim().toLowerCase() == "thick");
    String toppingsStr = json.getString("pizzaToppings");
    List<String> toppings = Arrays.asList(toppingsStr.split("\\s*,\\s*"));
    order.setToppings(toppings);
    order.setComments(json.getString("comments"));

    orderService.createOrder(order);

    JsonObject resp = Json
      .createObjectBuilder()
      .add(
        "message",
        "orderId: %s created successfully".formatted(order.getOrderId())
      )
      .build();

    return ResponseEntity.ok(resp.toString());
  }

  @GetMapping("/{email}/all")
  public ResponseEntity<String> getOrdersByEmail(@PathVariable String email) {
    List<OrderSummary> summaries = orderService.getOrdersByEmail(email);

    if (summaries == null) {
      System.out.println(">>> No orders yet");
      return ResponseEntity.ok(Json.createArrayBuilder().build().toString());
    }

    System.out.println(">>> %d orders found".formatted(summaries.size()));
    JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
    summaries.forEach(s -> arrBuilder.add(s.toJson()));

    return ResponseEntity.ok(arrBuilder.build().toString());
  }
}
