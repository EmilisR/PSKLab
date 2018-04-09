package lt.vu.usecases.cdi.security;

import lombok.Getter;
import lt.vu.auth.AdministratorArbaKrantai;
import lt.vu.auth.UserLoggedInArbaRagai;

import javax.enterprise.inject.Model;

@Model
public class SecureBean {

    @Getter
    private String result = "Press the button!!!";

    @UserLoggedInArbaRagai
    public void tikPrisijungusiems() {
        result = "Users only method was called.";
    }

    @AdministratorArbaKrantai
    public void tikAdminams() {
        result = "Admins only method was called.";
    }
}
