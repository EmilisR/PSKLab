package lt.vu.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Serializable;

@Named
@ViewScoped
@Slf4j
public class Authenticator implements Serializable {

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    private String originalRequestURI;

    @Inject
    private Provider<AuthenticatedUserHolder> authenticatedUserHolderProvider;

    @PostConstruct
    public void postConstruct() {
        originalRequestURI = Faces.getRequestURIWithQueryString();

        if (originalRequestURI == null || originalRequestURI.matches(".*/login\\.xhtml")) {
            originalRequestURI = Faces.getRequestBaseURL(); // arba kitoks adresas, kurį atversime po sėkmingo login
        }
    }

    public void loginAndRedirect() {
        // Nutraukiame egzistavusią sesiją ir sukuriame naują:
        Faces.invalidateSession();
        Faces.getSession(true);

        try {
            Faces.login(username, password);
            authenticatedUserHolderProvider.get().initUser(username);
        } catch (ServletException e) {
            Messages.addGlobalWarn("Wrong user name or password. Please try again.");
            return;
        } finally {
            password = null;
        }

        try {
            Faces.redirect(originalRequestURI);
        } catch (IOException e) {
            log.error("Authenticator.loginAndRedirect(): ", e);
        }
    }

    public void logoutAndRedirect() {
        // logout() kvietimo nereikia.
        Faces.invalidateSession();

        try {
            Faces.redirect(Faces.getRequestBaseURL()); // arba kitur, kur norime patekti po logout.
        } catch (IOException e) {
            log.error("Authenticator.logoutAndRedirect(): ", e);
        }
    }

}