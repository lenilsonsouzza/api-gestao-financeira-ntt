package com.financeiro.ms1usuarios.application.ports.in;

import com.financeiro.ms1usuarios.domain.model.User;

import java.util.Optional;

public interface FindUserByIdInboundPort {
    Optional<User> findById(Long id);

}
