package kea.dat3.security;

import kea.dat3.entities.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface UserWithPassword {

    public static final int USER_NAME_MIN_SIZE = 3;
    public static final int USER_NAME_MAX_SIZE = 20;

    public static final int EMAIL_MAX_SIZE = 50;

    public static final int PASSWORD_MIN_SIZE = 6;
    public static final int PASSWORD_MAX_SIZE = 40;
    static final PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    void setPassword(String password);

    List<Role> getRoles();

    void addRole(Role role);

    String getUsername();

    String getEmail();

    String getPassword();

    static PasswordEncoder getPasswordEncoder(){
        return pwEncoder;
    };

    boolean isEnabled();
}
