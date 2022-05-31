package kea.dat3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiderRequest {

    @NotNull
    private Long teamId;
    @NotBlank
    private String name;
    @NotBlank
    private String country;
    private int totalTimeMs;
    private int mountainPoints;
    private int sprintPoints;

}