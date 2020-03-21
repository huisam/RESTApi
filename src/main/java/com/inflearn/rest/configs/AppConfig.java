package com.inflearn.rest.configs;

import com.inflearn.rest.accounts.Account;
import com.inflearn.rest.accounts.AccountRole;
import com.inflearn.rest.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService service;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                final Account huisam = Account.builder()
                        .email("huisam@naver.com")
                        .password("huisam")
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();

                service.save(huisam);
            }
        };
    }
}
