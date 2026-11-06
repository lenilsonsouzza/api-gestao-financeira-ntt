package com.financeiro.ms1usuarios.application.ports.in;

import com.financeiro.ms1usuarios.domain.model.User;

public interface UpdateUserInboundPort {
    User update(Long id, User user);
}
