package ga.negyahu.music.artist.service;

import ga.negyahu.music.ScrollPageable;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.repository.ArtistMemberRepository;
import ga.negyahu.music.artist.repository.ArtistRepository;
import ga.negyahu.music.fileupload.entity.ArtistFileUpload;
import ga.negyahu.music.fileupload.repository.ArtistFileUploadRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMemberRepository artistMemberRepository;
    private final AccountRepository accountRepository;
    private final ArtistFileUploadRepository artistFileUploadRepository;
    private final AgencyRepository agencyRepository;

    @Override
    public Artist register(Artist artist, Long agencyId, Account user) {
        Agency agency = this.agencyRepository.findByIdAndAccount(agencyId, user)
            .orElseThrow(() -> {
                    throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.");
                }
            );
        Long id = artist.getProfileImage().getId();
        ArtistFileUpload findImage = null;
        try {
            findImage = artistFileUploadRepository.findById(id).get();
        } catch (NoSuchElementException e) {

        }
        if (findImage != null) {
            artist.setProfileImage(findImage);
        }
        artist.setAgency(agency);
        Artist save = this.artistRepository.save(artist);
        return save;
    }

    @Transactional
    @Override
    public List<Artist> fetchList(ScrollPageable pageable) {
        return this.artistRepository.findAllByPageable(pageable);
    }

    private void registerMembers(List<ArtistMember> members, final Artist artist) {
        String[] emails = members.stream()
            .map(a -> a.getAccount().getEmail())
            .distinct()
            .toArray(String[]::new);
        List<Account> accounts = this.accountRepository.findAllByEmailIn(emails);

        members.stream()
            .forEach(a -> {
                for (Account account : accounts) {
                    if (a.getAccount().getEmail().equals(account.getEmail())) {
                        a.setAccount(account);
                        a.setAgency(artist.getAgency());
                        a.setArtist(artist);
                    }
                }
            });
        for (ArtistMember member : members) {
            try {
                ArtistMember added = addMember(member);
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public ArtistMember addMember(ArtistMember member) {
        return this.artistMemberRepository.save(member);
    }


}
