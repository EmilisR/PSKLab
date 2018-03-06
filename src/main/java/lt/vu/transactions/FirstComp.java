package lt.vu.transactions;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import java.util.Date;

@Named
@RequestScoped
public class FirstComp {
    @Resource
    private TransactionSynchronizationRegistry tx;

    @Inject
    private SecondComp secondComp;

    @PostConstruct
    private void postConstruct() {
        System.out.println(this + " born :)");
    }

    @PreDestroy
    private void preDestroy() {
        System.out.println(this + " about to die :(");
    }

    //---------------------------------

    @Transactional(Transactional.TxType.REQUIRED)
    public String sayHello() {
        return toString() + " | " + new Date() + " | " + " Tx: " + tx.getTransactionKey() +
                " | Second component: " + secondComp.sayHello();
    }

}
