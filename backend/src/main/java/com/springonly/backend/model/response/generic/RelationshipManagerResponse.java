package com.springonly.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipManagerResponse {
    private String relationshipManagerId;
    private String relationshipManagerName;
    private String threadId;
    private OffsetDateTime writtenAt;
}
