package com.springonly.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RelationshipManagerDTO {
    private String relationshipManagerId;
    private String relationshipManagerName;
    private String password;
    private UUID threadId;
    private OffsetDateTime writtenAt;
}
