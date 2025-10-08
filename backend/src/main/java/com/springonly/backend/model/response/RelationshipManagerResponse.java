package com.springonly.backend.model.response;

import lombok.*;
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
