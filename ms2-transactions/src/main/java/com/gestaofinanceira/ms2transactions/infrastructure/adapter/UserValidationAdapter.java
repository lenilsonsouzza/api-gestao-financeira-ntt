package com.gestaofinanceira.ms2transactions.infrastructure.adapter;

import com.gestaofinanceira.ms2transactions.domain.ports.out.UserValidationPort;
import com.gestaofinanceira.ms2transactions.infrastructure.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidationAdapter implements UserValidationPort {

    private final UserClient userClient;

    @Override
    public boolean validateUserExists(Long userId) {
        try {
            Object user = userClient.getUserById(userId);
            return user != null;
        } catch (Exception e) {

            return false;
        }
    }
}
