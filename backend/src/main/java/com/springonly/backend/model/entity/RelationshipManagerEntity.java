package com.springonly.backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "relationshipManagers", schema = "credits")
public class RelationshipManagerEntity {
    @Id
    @Column(length = 16)
    private String relationshipManagerId;

    @Column(length = 64, nullable = false)
    private String relationshipManagerName;

    @Column(length = 256, nullable = false)
    private String password;

    @Column
    private UUID threadId;

    @Column(nullable = false)
    private OffsetDateTime writtenAt;
}
