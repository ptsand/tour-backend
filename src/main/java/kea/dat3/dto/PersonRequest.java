package kea.dat3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kea.dat3.entities.Person;
import kea.dat3.security.UserWithPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {

    @NotBlank
    @Size(min = UserWithPassword.USER_NAME_MIN_SIZE, max = UserWithPassword.USER_NAME_MAX_SIZE)
    private String username;

    @NotBlank
    @Size(max = UserWithPassword.EMAIL_MAX_SIZE)
    @Email
    private String email;

    @NotBlank
    @Size(min = UserWithPassword.PASSWORD_MIN_SIZE, max = UserWithPassword.PASSWORD_MAX_SIZE)
    private String password;

    public PersonRequest(Person person) {
        this.email = person.getEmail();
        this.username = person.getUsername();
        this.password = person.getPassword();
    }
}
