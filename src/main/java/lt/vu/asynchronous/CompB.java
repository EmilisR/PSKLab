package lt.vu.asynchronous;

import lt.vu.usecases.cdi.RescueOrAsync;
import org.apache.deltaspike.core.api.future.Futureable;

import javax.ejb.AsyncResult;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.concurrent.Future;

@ApplicationScoped
public class CompB implements Serializable {

    @Inject
    @RescueOrAsync            // Asinchroninis komponentas negali naudoti @RequestScoped konteksto
    private EntityManager em; // Jei šis komponentas turi dirbti su DB per JPA

    @Futureable
    @Transactional(Transactional.TxType.REQUIRES_NEW) // be šios anotacijos negalėsime gauti @RescueOrAsync EntityManager
    public Future<String> asyncMethod() {
        System.out.println("CompB starts working on a big task...");
        try {
            Thread.sleep(10000); // sleep for 10 seconds
        } catch (InterruptedException e) {
        }
        System.out.println("CompB: big task completed.");
        return new AsyncResult<>("BIG result from component B :)");
    }

}
