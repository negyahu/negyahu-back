package ga.negyahu.music.dev;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.agency.service.AgencyServiceImpl;
import ga.negyahu.music.artist.dto.ArtistMemberCreateDto;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.service.ArtistMemberService;
import ga.negyahu.music.artist.service.ArtistService;
import ga.negyahu.music.mapstruct.AgencyMapper;
import ga.negyahu.music.mapstruct.artist.ArtistMemberMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Profile({"dev", "prod"})
@Component
@RequiredArgsConstructor
public class DataApplicationRunner implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final AgencyRepository agencyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AgencyServiceImpl agencyService;
    private final ArtistService artistService;
    private final ArtistMemberService artistMemberService;

    private AgencyMapper agencyMapper = AgencyMapper.INSTANCE;
    private ArtistMemberMapper memberMapper = ArtistMemberMapper.INSTANCE;

    private List<Agency> agencies;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!isAction()) {
            return;
        }

        saveTestAccounts();
        saveAgency();
        saveArtistMember();
        clearData();
    }

    private void saveTestAgency() {
        Account agencyBosss = Account.builder()
            .username("킹우정")
            .certifiedEmail(true)
            .isMemberShip(true)
            .password(this.passwordEncoder.encode("dnwjd123"))
            .mobile("01033334444")
            .role(Role.AGENCY)
            .email("agency1@gmail.com")
            .state(State.ACTIVE)
            .build();
        this.accountRepository.save(agencyBosss);

        Agency agency = Agency.builder()
            .state(State.ACTIVE)
            .businessNumber("000-33-55555")
            .bossName("킹우정")
            .mobile("01033334444")
            .nameKR("우정엔터테이먼트")
            .nameEN("YouzhengENT")
            .account(agencyBosss)
            .build();
        this.agencyRepository.save(agency);
    }

    private void saveTestAccounts() {
        Account test1 = Account.builder()
            .username("양우정")
            .email("test1@naver.com")
            .mobile("01011112222")
            .password(passwordEncoder.encode("dnwjd123"))
            .certifiedEmail(true)
            .role(Role.ADMIN)
            .build();
        Account test2 = Account.builder()
            .username("황유정")
            .email("hyu630115@gmail.com")
            .mobile("01022223333")
            .password(passwordEncoder.encode("dbwjd123"))
            .certifiedEmail(true)
            .build();

        accountRepository.save(test1);
        accountRepository.save(test2);
    }

//    private Account createAccount(){
//        return Account.builder()
//            .state()
//    }

    public void saveAgency() {
        AgencyCreateDto bighit = AgencyCreateDto.builder()
            .nameKR("빅히트엔터테이먼트")
            .bossName("방시혁")
            .businessNumber("000-00-0000")
            .email("bighit.ent2@gmail.com")
            .mobile("010-0000-1111")
            .nameEN("BigHitENT")
            .build();
        Agency from = agencyMapper.from(bighit);
        Agency save = this.agencyService.register(from);

        Artist bts = Artist.builder()
            .nameEN("BTS")
            .nameKR("방탄소년단")
            .agency(save)
            .build();
        Artist register = artistService.register(bts, save.getId(), save.getAccount());
        ArtistMemberCreateDto memberCreateDto = ArtistMemberCreateDto.builder()
            .nameKR("양우정")
            .nameEN("youzheng")
            .password("dnwjd123")
            .email("bts_youzheng@gmail.com")
            .build();
        ArtistMember register1 = this.artistMemberService
            .register(bts.getId(), this.memberMapper.from(memberCreateDto));

        AgencyCreateDto edam = AgencyCreateDto.builder()
            .nameKR("이담 엔터테인먼트")
            .bossName("이지은")
            .businessNumber("111-11-1111")
            .email("edam.ent@gmail.com")
            .mobile("010-0000-1111")
            .nameEN("EdamEnt")
            .build();

        AgencyCreateDto forest = AgencyCreateDto.builder()
            .nameKR("이숲 엔터테인먼트")
            .bossName("드루이드")
            .businessNumber("222-22-2222")
            .email("forest.ent@gmail.com")
            .mobile("010-0000-1111")
            .nameEN("ForestEnt")
            .build();

        List<AgencyCreateDto> dtos = List.of(bighit, edam, forest);
    }

    private void saveArtistMember() throws IOException {

    }

    private MultipartFile createMultipart(String artistName) throws IOException {
        String path = new ClassPathResource(
            "/temp_image/" + artistName.toLowerCase(Locale.ROOT) + ".png").getURI().getPath();

        File file = new File(path);
        DiskFileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()),
            false, file.getName(), (int) file.length(), file.getParentFile());
        InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);
        return new CommonsMultipartFile(fileItem);
    }

    private void clearData() {
        this.agencies = null;
    }

    private boolean isAction() {
        return accountRepository.count() == 0;
    }
}
