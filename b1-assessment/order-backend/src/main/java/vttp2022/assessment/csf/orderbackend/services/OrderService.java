package vttp2022.assessment.csf.orderbackend.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.OrderRepository;

@Service
public class OrderService {

  @Autowired
  private PricingService priceSvc;

  @Autowired
  private OrderRepository orderRepo;

  // POST /api/order
  // Create a new order by inserting into orders table in pizzafactory database
  // IMPORTANT: Do not change the method's signature
  public void createOrder(Order order) {
    orderRepo.createOrder(order);
  }

  // GET /api/order/<email>/all
  // Get a list of orders for email from orders table in pizzafactory database
  // IMPORTANT: Do not change the method's signature
  public List<OrderSummary> getOrdersByEmail(String email) {
    // Use priceSvc to calculate the total cost of an order
    List<Order> orders = orderRepo.getOrdersByEmail(email);
    if (orders.size() < 1) {
      return null;
    }

    List<OrderSummary> summaries = orders
      .stream()
      .map(order -> {
        OrderSummary summary = new OrderSummary();
        summary.setOrderId(order.getOrderId());
        summary.setName(order.getName());
        summary.setEmail(order.getEmail());

        Float sizePrice = priceSvc.size(order.getSize());
        Float saucePrice = priceSvc.sauce(order.getSauce());
        Float crustPrice = order.isThickCrust()
          ? priceSvc.thickCrust()
          : priceSvc.thinCrust();
        Float toppingsPrice = (float) order
          .getToppings()
          .stream()
          .mapToDouble(t -> priceSvc.topping(t))
          .sum();

        Float total = sizePrice + saucePrice + crustPrice + toppingsPrice;
        summary.setAmount(total);
        return summary;
      })
      .toList();

    return summaries;
  }
}
