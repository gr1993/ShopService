package park.shop.web.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import park.shop.domain.common.IsDeleteType;
import park.shop.domain.member.LoginType;
import park.shop.domain.member.Member;
import park.shop.domain.member.RoleType;
import park.shop.repository.member.MemberRepository;
import park.shop.web.member.dto.MemberRegisterDto;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void registerMember(MemberRegisterDto memberRegisterDto) {
        Member member = new Member();
        member.setLoginId(memberRegisterDto.getLoginId());
        member.setPassword(memberRegisterDto.getPassword());
        member.setLoginType(LoginType.WEB.toString());
        member.setName(memberRegisterDto.getName());
        member.setGender(memberRegisterDto.getGender().toString());
        member.setAddress(memberRegisterDto.getAddress());
        member.setRole(RoleType.NORMAL.toString());
        member.setIsDelete(IsDeleteType.N.toString());

        memberRepository.save(member);
    }
}
