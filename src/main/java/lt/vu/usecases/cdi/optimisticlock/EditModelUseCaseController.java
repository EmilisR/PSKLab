package lt.vu.usecases.cdi.optimisticlock;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lt.vu.entities.Brand;
import lt.vu.entities.Model;
import lt.vu.entities.Factory;
import lt.vu.usecases.cdi.dao.BrandDAO;
import lt.vu.usecases.cdi.dao.ModelDAO;
import lt.vu.usecases.cdi.dao.FactoryDAO;
import org.hibernate.Hibernate;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@Slf4j
public class EditModelUseCaseController implements Serializable {

    /*
     * Būsena, kurią saugome ViewScoped kontekste:
     */
    @Getter private Model selectedModel;
    @Getter private Model conflictingModel;
    @Getter private List<Model> allModels;
    @Getter private List<Factory> allUniversities;
    @Getter private List<Brand> allBrands;

    /*
     * DAO:
     */
    @Inject private ModelDAO modelDAO;
    @Inject private BrandDAO brandDAO;
    @Inject private FactoryDAO factoryDAO;

    @PostConstruct
    public void init() {
        reloadAll();
    }

    public void prepareForEditing(Model model) {
        selectedModel = model;
        conflictingModel = null;
    }

    @Transactional
    public void updateSelectedModel() {
        try {
            modelDAO.updateAndFlush(selectedModel);
            reloadAll();
        } catch (OptimisticLockException ole) {
            conflictingModel = modelDAO.findById(selectedModel.getId());
            // Pavyzdys, kaip inicializuoti LAZY ryšį, jei jo reikia HTML puslapyje:
            Hibernate.initialize(conflictingModel.getBrandList());
            // Pranešam PrimeFaces dialogui, kad užsidaryti dar negalima:
            RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
        }
    }

    @Transactional
    public void overwriteModel() {
        selectedModel.setOptLockVersion(conflictingModel.getOptLockVersion());
        updateSelectedModel();
    }

    public void reloadAll() {
        allModels = modelDAO.getAllModels();
        allUniversities = factoryDAO.getAllUniversities();
        allBrands = brandDAO.getAllBrands();
    }
}
