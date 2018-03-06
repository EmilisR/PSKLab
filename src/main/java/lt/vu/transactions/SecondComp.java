package lt.vu.transactions;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;

@RequestScoped
public class SecondComp {
    @Resource
    private TransactionSynchronizationRegistry tx;

    @PostConstruct
    private void init() {
        System.out.println(this + " born :)");
    }

    @PreDestroy
    private void preDestroy() {
        System.out.println(this + " about to die :(");
    }

    //---------------------------------

    //@Transactional(Transactional.TxType.REQUIRES_NEW)
    @Transactional(Transactional.TxType.REQUIRED)
    public String sayHello() {
        return toString() + " Tx: " + tx.getTransactionKey();
    }

}
