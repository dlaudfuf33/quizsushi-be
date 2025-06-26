package com.cmdlee.quizsushi.admin.common;

import com.cmdlee.quizsushi.admin.domain.model.AdminMember;
import com.cmdlee.quizsushi.admin.domain.model.enums.AdminRole;
import com.cmdlee.quizsushi.admin.repository.AdminMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RootAdminInitializer implements CommandLineRunner {

    private final AdminMemberRepository adminMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RootAdminProperties properties;

    @Override
    public void run(String... args) {
        String rootId = properties.getId();
        String rawPassword = properties.getPassword();

        if (!adminMemberRepository.existsByRole(AdminRole.valueOf("ROOT"))) {
            AdminMember root = AdminMember.builder()
                    .alias("ROOT 관리자 계정")
                    .username(rootId)
                    .password(passwordEncoder.encode(rawPassword))
                    .role(AdminRole.valueOf("ROOT"))
                    .build();
            adminMemberRepository.save(root);
            log.info("최초 ROOT 관리자 계정이 생성되었습니다.");
        }
    }
}