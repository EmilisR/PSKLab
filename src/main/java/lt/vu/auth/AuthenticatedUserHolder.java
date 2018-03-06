package lt.vu.auth;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lt.vu.entities.Account;
import lt.vu.usecases.cdi.dao.AccountDAO;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("user")
@SessionScoped
@Slf4j
public class AuthenticatedUserHolder implements Serializable {

    @Inject
    private AccountDAO accountDAO;

    @Getter
    private Account authenticatedUser;

    void initUser(String username) {
        authenticatedUser = accountDAO.findByUserName(username);
    }

    @Produces
    @Named("authenticatedUser")
    @LoggedIn
    private Account produceAuthenticatedUser() {
        return authenticatedUser;
    }

    public boolean isAuthenticated() {
        return authenticatedUser != null;
    }

    public boolean isAdmin() {
        return authenticatedUser != null && authenticatedUser.getGroups().contains("Admin");
    }
}
