package com.springonly.backend.service;

import com.springonly.backend.entity.RelationshipManagerEntity;
import com.springonly.backend.mapper.LoginMapper;
import com.springonly.backend.model.dto.RelationshipManagerDTO;
import com.springonly.backend.repository.RelationshipManagerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class LoginService {
    @Inject
    RelationshipManagerRepository relationshipManagerRepository;

    @Inject
    LoginMapper loginMapper;

    public RelationshipManagerDTO login(
        String relationshipManagerId,
        String password
    ) {
        String sql = "relationshipManagerId = ?1 and password = ?2";

        RelationshipManagerEntity relationshipManagerEntity =
            relationshipManagerRepository.login(
                relationshipManagerId,
                password
            );

        return loginMapper.fromEntityToDTO(relationshipManagerEntity);
    }
}
