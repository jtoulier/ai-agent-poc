package com.springonly.backend.model.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipManagerLoginResponse {
    private String relationshipManagerId;
    private String relationshipManagerName;
    private String threadId;
}
