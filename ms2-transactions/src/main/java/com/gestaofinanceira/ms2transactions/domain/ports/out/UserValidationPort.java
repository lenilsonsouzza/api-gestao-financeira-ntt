package com.gestaofinanceira.ms2transactions.domain.ports.out;

public interface UserValidationPort {
    boolean validateUserExists(Long userId);
}