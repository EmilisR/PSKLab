package lt.vu.cachepropagation;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.SynchronizationType;

@Stateful
@RequestScoped
public class Comp2 {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION, synchronization = SynchronizationType.SYNCHRONIZED)
    private EntityManager em;

    //@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String sayHello() {
        return toString() + " Cache instance: " + em.getDelegate();
    }

}
