package com.springonly.backend.model.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRelationshipManagerRequest {
    @Size(max = 64)
    private String relationshipManagerName;

    // threadId comes as UUID string (nullable)
    private UUID threadId;

}
