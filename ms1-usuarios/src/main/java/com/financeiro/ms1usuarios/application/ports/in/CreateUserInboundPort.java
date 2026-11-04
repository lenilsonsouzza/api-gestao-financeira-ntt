package com.financeiro.ms1usuarios.application.ports.in;

import com.financeiro.ms1usuarios.domain.model.User;

public interface CreateUserInboundPort   {
    User create(User user);
}
