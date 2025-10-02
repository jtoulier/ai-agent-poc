package com.springonly.backend.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private RelationshipManagerResponse relationshipManager;
}
