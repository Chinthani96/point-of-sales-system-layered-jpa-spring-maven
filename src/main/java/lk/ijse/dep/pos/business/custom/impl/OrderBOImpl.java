package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.OrderBO;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.dao.custom.OrderDAO;
import lk.ijse.dep.pos.dao.custom.OrderDetailDAO;
import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.db.JPAUtil;
import lk.ijse.dep.pos.entity.*;
import lk.ijse.dep.pos.util.CustomerTM;
import lk.ijse.dep.pos.util.SearchOrderTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderBOImpl implements OrderBO {
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderDetailDAO orderDetailDAO;
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private QueryDAO queryDAO;

    public void saveOrder(String id, Date date, CustomerTM customer){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        orderDAO.setEntityManager(entityManager);
        try {
            entityManager.getTransaction().begin();
            orderDAO.save(new Order(id,date,new Customer(customer.getCustomerId(), customer.getCustomerName(), customer.getCustomerAddress())));
            entityManager.getTransaction().commit();
        } catch (SQLException e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void saveOrderDetail(String orderId, String itemCode, int qty, double unitPrice){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        orderDetailDAO.setEntityManager(entityManager);
        itemDAO.setEntityManager(entityManager);

        try {
            entityManager.getTransaction().begin();
            orderDetailDAO.save(new OrderDetail(orderId,itemCode,qty,BigDecimal.valueOf(unitPrice)));
            Item item = itemDAO.find(itemCode);
            item.setQtyOnHand(item.getQtyOnHand()-qty);
            entityManager.getTransaction().commit();
        } catch (SQLException e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public String generateNewOrderId(){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        orderDAO.setEntityManager(entityManager);

        try {
            entityManager.getTransaction().begin();
            String lastOrderId = orderDAO.lastOrderId();
            entityManager.getTransaction().commit();
            int lastNumber = Integer.parseInt(lastOrderId.substring(2, 5));
            if (lastNumber<=0) {
                lastNumber++;
                return "OD001";
            } else if (lastNumber<9) {
                lastNumber++;
                return "OD00" +lastNumber;
            } else if (lastNumber<99) {
                lastNumber++;
                return "OD0" +lastNumber;
            }
            else{
                lastNumber++;
                return "OD" +lastNumber;
            }
        } catch (SQLException e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }

    public List<SearchOrderTM> getOrderDetails(){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        queryDAO.setEntityManager(entityManager);

        List<CustomEntity> orderDetails = null;
        List<SearchOrderTM> searchOrderTMS = new ArrayList<>();
        try {
            entityManager.getTransaction().begin();
            orderDetails = queryDAO.getOrderDetails();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
        for (CustomEntity orderDetail : orderDetails) {
            searchOrderTMS.add(new SearchOrderTM(orderDetail.getOrderId(),orderDetail.getOrderDate().toString(),orderDetail.getCustomerId(),orderDetail.getCustomerName(),orderDetail.getTotal().doubleValue()));
            return searchOrderTMS;
        }
        return null;
    }
}
