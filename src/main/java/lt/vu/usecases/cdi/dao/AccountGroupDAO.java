package lt.vu.usecases.cdi.dao;

import lt.vu.entities.AccountGroup;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class AccountGroupDAO {
    @Inject
    private EntityManager em;

    public void create(AccountGroup accountGroup) {
        em.persist(accountGroup);
    }
}