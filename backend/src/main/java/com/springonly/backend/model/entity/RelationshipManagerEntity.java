package com.springonly.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(schema = "credits", name = "relationshipManagers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipManagerEntity {
    @Id
    private String relationshipManagerId;
    private String relationshipManagerName;
    private String password;
    private String threadId;
    private OffsetDateTime writtenAt;
}
