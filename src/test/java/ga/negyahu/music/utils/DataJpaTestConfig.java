package ga.negyahu.music.utils;

import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.account.service.AccountService;
import ga.negyahu.music.account.service.AccountServiceImpl;
import ga.negyahu.music.security.utils.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class DataJpaTestConfig {

    private static final String secret = "enVpamluIGhlbnhpYW5nZmFuZ3FpeWlxaWUgeWlqaW5naGVubGVpbGUgc2hlbm1lc2hpcWluZ2RvdWJ1eGluZ2dhbiBtYW1hIGJhYmEgamllamllIHdvemhlbmRlaGVubGVpbGUgbWVpeWlzaQo";

    @Autowired
    public AccountRepository accountRepository;

    @Bean
    public JwtTokenProvider provider() {
        return new JwtTokenProvider(secret, 10000L);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public TestUtils testUtils() {
        TestUtils testUtils = new TestUtils();
        return testUtils;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl(accountRepository,
            passwordEncoder(), modelMapper());
    }

}
