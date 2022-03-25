package com.ybichel.storage.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class JwtTokenHeader implements Serializable {
    @JsonProperty("kid")
    private String kid;
    @JsonProperty("alg")
    private String alg;
}
