package lk.ijse.dep.pos.business.custom.impl;

import lk.ijse.dep.pos.business.custom.CustomerBO;
import lk.ijse.dep.pos.dao.custom.CustomerDAO;
import lk.ijse.dep.pos.db.JPAUtil;
import lk.ijse.dep.pos.entity.Customer;
import lk.ijse.dep.pos.util.CustomerTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerBOImpl implements CustomerBO {
    @Autowired
    private CustomerDAO customerDAO;
    public List<CustomerTM> getAllCustomers(){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(entityManager);

        List<Customer> allCustomers = null;
        List<CustomerTM> customerTMS = new ArrayList<>();
        try {
            entityManager.getTransaction().begin();
            allCustomers = customerDAO.findAll();
            entityManager.getTransaction().commit();
        } catch (SQLException e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
        for (Customer customer : allCustomers) {
            customerTMS.add(new CustomerTM(customer.getId(),customer.getName(),customer.getAddress()));
        }
        return customerTMS;
    }

    public void saveCustomer(String id, String name, String address) throws SQLException {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(entityManager);
        try {
            entityManager.getTransaction().begin();
            customerDAO.save(new Customer(id,name,address));
            entityManager.getTransaction().commit();
        } catch (Throwable t) {
            entityManager.getTransaction().rollback();
            throw t;
        }

    }

    public void updateCustomer(String id, String name, String address) throws SQLException {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(entityManager);
        try {
            entityManager.getTransaction().begin();
            customerDAO.update(new Customer(id, name, address));
            entityManager.getTransaction().commit();
        } catch (Throwable t) {
            entityManager.getTransaction().rollback();
            throw t;
        }
    }

    public void deleteCustomer(String id) throws SQLException {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(entityManager);
        try {
            entityManager.getTransaction().begin();
            customerDAO.delete(id);
            entityManager.getTransaction().commit();
        } catch (Throwable t) {
            entityManager.getTransaction().rollback();
            throw t;
        }
    }
    public String generateNewCustomerId(){
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(entityManager);

        try {
            entityManager.getTransaction().begin();
            String lastCustomerId = customerDAO.getLastCustomerId();
            entityManager.getTransaction().commit();
            int lastNumber = Integer.parseInt(lastCustomerId.substring(1, 4));
            if (lastNumber==0) {
//                lastNumber++;
                return "C001";
            } else if (lastNumber<9) {
                lastNumber++;
                return "C00" +lastNumber;
            } else if (lastNumber<99) {
                lastNumber++;
                return "C0" +lastNumber;
            }
            else{
                lastNumber++;
                return "C" +lastNumber;
            }
        } catch (SQLException e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }
}
