package com.springonly.backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "relationshipManagers", schema = "credits")
public class RelationshipManager {
    @Id
    @Column(length = 16)
    private String relationshipManagerId;

    @Column(length = 64, nullable = false)
    private String relationshipManagerName;

    @Column(length = 256, nullable = false)
    private String password;

    @Column
    private String threadId;

    @Column(nullable = false)
    private OffsetDateTime writtenAt;
}
