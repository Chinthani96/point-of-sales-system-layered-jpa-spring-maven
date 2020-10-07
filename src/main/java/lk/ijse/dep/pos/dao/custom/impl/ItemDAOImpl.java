package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.CrudDAOImpl;
import lk.ijse.dep.pos.dao.custom.ItemDAO;
import lk.ijse.dep.pos.entity.Item;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class ItemDAOImpl extends CrudDAOImpl<Item, String> implements ItemDAO {
    public String getLastItemId() throws SQLException {
        return (String) entityManager.createNativeQuery("SELECT code FROM Item ORDER BY code DESC LIMIT 1").getSingleResult();
    }
}
