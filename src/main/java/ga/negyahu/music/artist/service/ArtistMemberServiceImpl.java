package ga.negyahu.music.artist.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.entity.MemberRole;
import ga.negyahu.music.artist.repository.ArtistMemberRepository;
import ga.negyahu.music.artist.repository.ArtistRepository;
import ga.negyahu.music.exception.AgencyNotFoundException;
import ga.negyahu.music.exception.ArtistNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistMemberServiceImpl implements ArtistMemberService {

    private final ArtistMemberRepository artistMemberRepository;
    private final ArtistRepository artistRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @Override
    public ArtistMember fetch(Long memberId) {
        return this.artistMemberRepository.findFirstById(memberId)
            .orElseThrow(() -> {
                throw new ArtistNotFoundException();
            });
    }

    @Override
    public ArtistMember register(Long artistId, ArtistMember member) {
        Artist artist = this.artistRepository.findWithAgencyById(artistId)
            .orElseThrow(() -> {
                throw new ArtistNotFoundException();
                //TODO 이메일 중복 예외 생성, 400 코드 반환하도록 BaseExceptionHandler 설정
            });
        Account save = signUpArtist(member.getAccount());
        member.setArtist(artist);
        member.setAccount(save);
        member.setState(State.ACTIVE);
        member.setMemberRole(MemberRole.ARTIST);
        return this.artistMemberRepository.save(member);
    }

    @Override
    public ArtistMember update(ArtistMember from) {
        // 소속사 & 아티스트 변경일 경우 변경
        changeAgency(from);

        ArtistMember member = this.artistMemberRepository.findFirstById(from.getId())
            .orElseThrow(() -> {
                throw new ArtistNotFoundException();
            });
        this.modelMapper.map(from, member);
        return member;
    }

    private void changeAgency(ArtistMember member) {
        if (member.getArtist().getId() != null && member.getArtist().getId() != null) {
            Artist artist = this.artistRepository.findWithAgencyById(member.getArtist().getId())
                .orElseThrow(() -> {
                    throw new ArtistNotFoundException();
                });
            if (!artist.getAgency().getId().equals(member.getAgency().getId())) {
                throw new AgencyNotFoundException();
            }
            member.setArtist(artist);
        }
    }

    private Account signUpArtist(Account account) {
        // 이메일이 이미 사용중일 경우
        if (this.accountRepository.existsByEmail(account.getEmail())) {
            throw new RuntimeException();
        }
        account.setRole(Role.ARTIST);
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        account.setCertifiedEmail(true);
        return this.accountRepository.save(account);
    }
}
