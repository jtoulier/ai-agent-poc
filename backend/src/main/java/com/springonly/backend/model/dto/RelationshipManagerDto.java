package com.springonly.backend.model.dto;

import lombok.*;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipManagerDto {
    private String relationshipManagerId;
    private String relationshipManagerName;
    private String password;
    private String threadId;
    private OffsetDateTime writtenAt;
}
