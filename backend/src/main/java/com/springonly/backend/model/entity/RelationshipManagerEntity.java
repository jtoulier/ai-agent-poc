package com.springonly.backend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
