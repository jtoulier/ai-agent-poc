package com.springonly.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipManagerLoginResponse {
    private String relationshipManagerId;
    private String relationshipManagerName;
    private UUID threadId;
}
