package com.springonly.backend.model.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipManagerLoginRequest {
    private String relationshipManagerId;
    private String password;
}
