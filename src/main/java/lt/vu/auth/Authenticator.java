package lt.vu.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lt.vu.entities.AccountGroup;
import lt.vu.usecases.cdi.dao.AccountGroupDAO;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import lt.vu.entities.Account;
import lt.vu.usecases.cdi.dao.AccountDAO;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Named
@ViewScoped
@Slf4j
public class Authenticator implements Serializable {

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private AccountGroupDAO accountGroupDAO;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String surname;

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

    @Transactional
    public void register() {
        try {
            Account account = new Account();
            account.firstName = name;
            account.lastName = surname;
            account.userName = username;
            account.passwordDigest = stringToSha1(password);
            accountDAO.saveAndFlushAndRefresh(account);
            AccountGroup accountGroup = new AccountGroup();
            accountGroup.groupName = "User";
            accountGroup.accountId = account.id;
            accountGroupDAO.create(accountGroup);
        } catch (Exception e) {
            Messages.addGlobalWarn("Registration failed. Please try again.");
            return;
        }
        try {
            Faces.redirect(Faces.getRequestBaseURL()); // arba kitur, kur norime patekti po logout.
        } catch (IOException e) {
            log.error("Authenticator.register(): ", e);
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

    private static String stringToSha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

}