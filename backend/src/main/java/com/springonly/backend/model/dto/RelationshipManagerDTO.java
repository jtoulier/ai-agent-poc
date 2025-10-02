package com.springonly.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipManagerDTO {

    private String relationshipManagerId;
    private String relationshipManagerName;
    private String password;
    private String threadId;
    private OffsetDateTime writtenAt;
}
