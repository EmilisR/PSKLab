package lt.vu.usecases.cdi.dao;

import lt.vu.entities.Account;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

/**
 * Reikia nepamiršti beans.xml faile pridėti:

 <pre>
 {@code
 <alternatives>
    <class>org.apache.deltaspike.jpa.impl.transaction.ContainerManagedTransactionStrategy</class>
 </alternatives>
 }
 </pre>

 */
@Repository
public interface AccountDAO extends EntityRepository<Account, Integer> {

    Account findByUserName(String userName);

}
