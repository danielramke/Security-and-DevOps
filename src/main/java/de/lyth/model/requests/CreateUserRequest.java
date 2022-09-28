package de.lyth.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserRequest {

    @JsonProperty
    private String name;

    @JsonProperty
    private String password;

    @JsonProperty
    private String confirmPassword;

}
