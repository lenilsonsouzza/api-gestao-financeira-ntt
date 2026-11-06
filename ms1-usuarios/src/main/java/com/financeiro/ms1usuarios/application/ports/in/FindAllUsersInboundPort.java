package com.financeiro.ms1usuarios.application.ports.in;

import com.financeiro.ms1usuarios.domain.model.User;

import java.util.List;

public interface FindAllUsersInboundPort {
    List<User> findAll();
}
