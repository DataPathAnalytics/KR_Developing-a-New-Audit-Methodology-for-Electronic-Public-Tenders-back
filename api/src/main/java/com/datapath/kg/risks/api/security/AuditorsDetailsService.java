package com.datapath.kg.risks.api.security;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.repository.AuditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class AuditorsDetailsService implements UserDetailsService {

    @Autowired
    private AuditorRepository auditorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        AuditorEntity auditor = auditorRepository.findByEmail(username);

        if (isNull(auditor)) throw new UsernameNotFoundException(username);

        return User
                .builder()
                .username(auditor.getEmail())
                .password(auditor.getPassword())
                .disabled(auditor.isDisabled())
                .accountLocked(auditor.isAccountLocked())

                .authorities(auditor.getPermissions().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                        .collect(Collectors.toList()))

                .build();
    }

}