package lk.ijse.dep.pos.dao.custom.impl;

import lk.ijse.dep.pos.dao.custom.QueryDAO;
import lk.ijse.dep.pos.entity.CustomEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
public class QueryDAOImpl implements QueryDAO {

    private EntityManager entityManager;

    @Override
    public List<CustomEntity> getOrderDetails() throws Exception {
        return entityManager.createNativeQuery("SELECT o.id,o.date,o.customerId,c.name, Sum(OD.qty *OD.unitPrice) as `Total` from `Order` o INNER JOIN Customer C ON o.customerId = C.id INNER JOIN OrderDetail OD on o.id = OD.orderId GROUP BY o.id",CustomEntity.class).getResultList();
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
