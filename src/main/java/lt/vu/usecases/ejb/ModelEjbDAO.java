package lt.vu.usecases.ejb;

import lt.vu.entities.Model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.SynchronizationType;
import java.util.List;

@Stateless
public class ModelEjbDAO {
    @PersistenceContext(synchronization = SynchronizationType.UNSYNCHRONIZED)
    private EntityManager em;

    public void create(Model model) {
        em.persist(model);
    }

    public List<Model> getAllModels() {
        return em.createNamedQuery("Model.findAll", Model.class).getResultList();
    }
}
