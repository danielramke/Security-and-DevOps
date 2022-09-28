package de.lyth.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModifyCartRequest {

    @JsonProperty
    private String username;

    @JsonProperty
    private long itemID;

    @JsonProperty
    private int quantity;

}
