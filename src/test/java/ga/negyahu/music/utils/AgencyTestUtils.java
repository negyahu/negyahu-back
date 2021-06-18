package ga.negyahu.music.utils;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.entity.Agency;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AgencyTestUtils {

    public static AgencyCreateDto agencyCreateDto() {
        return AgencyCreateDto.builder()
            .nameEN("빅히트")
            .name("BIGHIT")
            .email("yangfriendship.dev@gmail.com")
            .bossName("김덕배")
            .businessNumber("111-11-2222")
            .mobile("01011112222")
            .build();
    }

    public static Agency createDefaultAgency(Account account) {
        return Agency.builder()
            .name("빅히트")
            .nameEN("BIGHIT")
            .account(account)
            .bossName("김덕배")
            .businessNumber("111-11-2222")
            .mobile("01011112222")
            .build();
    }

    public static final List<Agency> createAgencies(Account account, int count1, State state1,
        int count2, State state2) {
        List<Agency> results = new ArrayList<>();
        Set<String> names = new HashSet<>();
        while (names.size() < (count1 + count2)) {
            String name = generateRandomString();
            names.add(name);
        }
        String[] namesAsArray = names.stream().toArray(String[]::new);
        int index = count1;
        for (int i = 0; i < count1; i++) {
            Agency agency = createAgencyWithBuilder(account, state1, i, namesAsArray[i]);
            results.add(agency);
        }

        for (int i = count1; i < (count1 + count2); i++) {
            Agency agency = createAgencyWithBuilder(account, state2, i, namesAsArray[i]);
            results.add(agency);
        }

        return results;
    }

    public static final List<Agency> createAgencies(Account account, int count1, State state1) {
        return createAgencies(account, count1, state1, 0, null);
    }

    private static Agency createAgencyWithBuilder(Account account, State state1, int index,
        String s) {
        return Agency.builder()
            .account(account)
            .nameEN(s + "EntEN")
            .name(s + "Ent")
            .bossName(s)
            .mobile(String.format("0101111%04d", index))
            .businessNumber(String.format("000-00-%05d", index))
            .state(state1)
            .build();
    }

    public static final String generateRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}

