package ga.negyahu.music.agency.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyMemberDto;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.agency.repository.AgencyMemberRepository;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.mapstruct.AgencyMemberMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AgencyMemberFetchServiceImpl implements AgencyMemberFetchService {

    private final AgencyMemberRepository agencyMemberRepository;
    private final AgencyRepository agencyRepository;
    private final AgencyMemberMapper agencyMemberMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<AgencyMemberDto> fetchList(Account user, Pageable pageable) {
        Page<AgencyMember> results = this.agencyMemberRepository
            .findAllByBossIdAsPage(user.getId(), pageable);

        List<AgencyMember> content = results.getContent();
        List<AgencyMemberDto> temp = new ArrayList<>(content.size());

        for (AgencyMember member : content) {
            temp.add(this.agencyMemberMapper.toDto(member));
        }
        Page<AgencyMemberDto> page = new PageImpl<>(temp,pageable,results.getTotalElements());
        return page;
    }
}
