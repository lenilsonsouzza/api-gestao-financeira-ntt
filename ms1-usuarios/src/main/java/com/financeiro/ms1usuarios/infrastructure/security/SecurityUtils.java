package com.financeiro.ms1usuarios.infrastructure.security;

import com.financeiro.ms1usuarios.domain.model.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Utilizador não autenticado.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return ((User) principal).getId();
        }

        else if (principal instanceof Long) {
            return (Long) principal;
        }

        throw new IllegalStateException("Tipo de Principal inesperado no contexto de segurança: " + principal.getClass());
    }

    public static void verifyIsOwner(Long targetUserId) {
        Long currentUserId = getAuthenticatedUserId();
        if (!currentUserId.equals(targetUserId)) {
            throw new AccessDeniedException("Acesso negado: Você só pode atualizar ou excluir o seu próprio usuário.");
        }
    }
}