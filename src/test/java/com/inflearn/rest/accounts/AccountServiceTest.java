package com.inflearn.rest.accounts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {
    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Test
    @DisplayName("인증된 유저가 주어졌을 때 제대로 인증 서비스에 저장되는지 테스트")
    void findByUserName() {
        /* given */
        final String email = "huisam@naver.com";
        final String password = "hwijin";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.repository.save(account);

        /* when */
        final UserDetails userDetails = service.loadUserByUsername(email);

        /* then */
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("인증되지 않는 유저가 주어졌을 때 예외를 던지는지 테스트")
    void findByUserNameFail() {
        /* given */
        final String email = "huisam@naver.com";

        /* when, then */
        final UsernameNotFoundException usernameNotFoundException = assertThrows(
                UsernameNotFoundException.class, () -> service.loadUserByUsername(email));
        assertThat(usernameNotFoundException.getMessage()).contains(email);
    }

    @AfterEach
    void tearDown() {
        this.repository.deleteAll();
    }
}