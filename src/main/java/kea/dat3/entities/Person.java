package kea.dat3.entities;

import kea.dat3.dto.PersonRequest;
import kea.dat3.security.UserWithPassword;
import kea.dat3.security.dto.LoginRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Person implements UserWithPassword {

    private String email;

    @Id
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    // 72 == Max length of a bcrypt encoded password
    // https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
    @Column(nullable = false, length = 72)
    private String password;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('USER','ADMIN')")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="security_role")
    List<Role> roles = new ArrayList<>();

    public Person(String email, LoginRequest credentials) {
        this.email = email;
        this.username = credentials.getUsername();
        this.password = pwEncoder.encode(credentials.getPassword());
        this.enabled = true;
    }

    @Override
    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public void setPassword(String password) {
        this.password = pwEncoder.encode(password);
    }


    public Person(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = pwEncoder.encode(password);
        this.enabled = true;
    }

    public Person(PersonRequest body) {
        this.email = body.getEmail();
        this.username = body.getUsername();
        this.password = pwEncoder.encode(body.getPassword());
        this.enabled = true;
    }

    public boolean isEnabled() { return this.enabled; }
}