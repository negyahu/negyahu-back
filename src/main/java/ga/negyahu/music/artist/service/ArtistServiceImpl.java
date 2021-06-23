package ga.negyahu.music.artist.service;

import ga.negyahu.music.ScrollPageable;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.repository.ArtistMemberRepository;
import ga.negyahu.music.artist.repository.ArtistRepository;
import ga.negyahu.music.exception.ArtistNotFoundException;
import ga.negyahu.music.fileupload.entity.ArtistUpload;
import ga.negyahu.music.fileupload.repository.ArtistFileUploadRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMemberRepository artistMemberRepository;
    private final AccountRepository accountRepository;
    private final ArtistFileUploadRepository artistFileUploadRepository;
    private final AgencyRepository agencyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Artist register(Artist artist, Long agencyId, Account user) {
        Agency agency = findAgencyByIdAndAccount(agencyId, user);
        if (artist.getProfileImage() != null && artist.getProfileImage().getId() != null) {
            Long id = artist.getProfileImage().getId();
            ArtistUpload findImage = null;
            try {
                findImage = artistFileUploadRepository.findById(id).get();
            } catch (NoSuchElementException e) {

            }
            if (findImage != null) {
                artist.setProfileImage(findImage);
            }
        }

        artist.setAgency(agency);
        Artist save = this.artistRepository.save(artist);
        return save;
    }

    private Agency findAgencyByIdAndAccount(Long agencyId, Account user) {
        return this.agencyRepository.findByIdAndAccount(agencyId, user)
            .orElseThrow(() -> {
                    throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.");
                }
            );
    }

    @Transactional
    @Override
    public List<Artist> fetchList(ScrollPageable pageable) {
        return this.artistRepository.findAllByPageable(pageable);
    }

    @Override
    public List<ArtistMember> createMembers(Long artistId,
        List<ArtistMember> artistMembers) {
        Artist artist = this.artistRepository.findWithAgencyById(artistId)
            .orElseThrow(() -> {
                throw new ArtistNotFoundException();
            });
        signUpMembers(artistMembers);
        // Member 의 Account 가 생성된다
        Agency agency = artist.getAgency();
        artistMembers.stream()
            .forEach(am -> {
                am.setArtist(artist);
            });
        return this.artistMemberRepository.saveAll(artistMembers);
    }

    public void signUpMembers(List<ArtistMember> members) {
        for (ArtistMember member : members) {
            try {
                Account account = member.getAccount();
                account.setRole(Role.ARTIST);
                account.setPassword(this.passwordEncoder.encode(account.getPassword()));
                account.setState(State.ACTIVE);
                account.setCertifiedEmail(true);
                Account save = this.accountRepository.save(account);
                member.setAccount(save);
            } catch (Exception e) {
                // 계정생성에 실패한 멤버는 일단 등록하지 않는다.
                members.remove(member);
            }
        }
    }


    @Override
    public void checkIsAdmin(Account user, Long agencyId) {
        this.agencyRepository.findByIdAndAccount(agencyId, user)
            .orElseThrow(() -> {
                throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.");
            })
        ;
    }

}
