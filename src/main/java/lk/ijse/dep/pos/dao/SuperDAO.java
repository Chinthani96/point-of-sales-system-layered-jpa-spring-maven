package lk.ijse.dep.pos.dao;

import javax.persistence.EntityManager;
import java.io.Serializable;

public interface SuperDAO extends Serializable {
    public void setEntityManager(EntityManager entityManager);
}
