package vttp2022.assessment.csf.orderbackend.repositories;

import static vttp2022.assessment.csf.orderbackend.repositories.Queries.*;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import vttp2022.assessment.csf.orderbackend.models.Order;

@Repository
public class OrderRepository {

  @Autowired
  JdbcTemplate jdbcTemplate;

  public boolean createOrder(Order order) {
    return (
      jdbcTemplate.update(
        SQL_INSERT_INTO_ORDERS,
        order.getName(),
        order.getEmail(),
        order.getSize(),
        order.isThickCrust(),
        order.getSauce(),
        order.getToppings().toString(),
        order.getComments()
      ) >
      0
    );
  }

  public List<Order> getOrdersByEmail(String email) {
    final SqlRowSet rs = jdbcTemplate.queryForRowSet(
      SQL_SELECT_ORDERS_BY_EMAIL,
      email
    );

    List<Order> orders = new LinkedList<>();
    while (rs.next()) {
      orders.add(Order.createFromRs(rs));
    }

    return orders;
  }
}
