package com.springonly.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "relationshipManagers", schema = "credits")
@Data
public class RelationshipManagerEntity {
    @Id
    private String relationshipManagerId;

    private String relationshipManagerName;
    private String password;
    private UUID threadId;
    private OffsetDateTime writtenAt;
}
