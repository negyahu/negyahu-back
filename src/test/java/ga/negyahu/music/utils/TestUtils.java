package ga.negyahu.music.utils;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.entity.Address;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    public static final String DEFAULT_EMAIL = "yangfriendship.dev@gmail.com";
    public static final String DEFAULT_NAME = "양우정";
    public static final String DEFAULT_NICKNAME = "갓우정";
    public static final String DEFAULT_PASSWORD = "dbwjd123";
    public static final Address DEFAULT_ADDRESS = new Address("02058","서울시 성북구 북악산로 1111","성신여대입구역 5번출구");

    @Autowired
    private AccountService accountService;

    public static final Account createAccount(){
        return Account.builder()
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .password(DEFAULT_PASSWORD)
            .username(DEFAULT_NAME)
            .nickname(DEFAULT_NICKNAME)
            .country("ko-KR")
            .build();
    }

    public static final AccountCreateDto createAccountCreateDto() {
        return AccountCreateDto.builder()
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .username(DEFAULT_NAME)
            .nickname(DEFAULT_NICKNAME)
            .detailAddress(DEFAULT_ADDRESS.getDetailAddress())
            .roadAddress(DEFAULT_ADDRESS.getRoadAddress())
            .zipcode(DEFAULT_ADDRESS.getZipcode())
            .build();
    }

    public Account signUpAccount(Account account){
        return accountService.signUp(account);
    }

}
