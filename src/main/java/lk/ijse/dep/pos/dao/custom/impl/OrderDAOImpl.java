package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.CrudDAOImpl;
import lk.ijse.dep.pos.dao.custom.OrderDAO;
import lk.ijse.dep.pos.entity.Order;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class OrderDAOImpl extends CrudDAOImpl<Order,String> implements OrderDAO {
    public String lastOrderId() throws SQLException {
        return (String) entityManager.createNativeQuery("SELECT id FROM `Order` ORDER BY id DESC LIMIT 1").getSingleResult();
    }

}

