package park.shop.web.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import park.shop.common.util.EncryptUtil;
import park.shop.domain.common.IsDeleteType;
import park.shop.domain.member.LoginType;
import park.shop.domain.member.Member;
import park.shop.domain.member.RoleType;
import park.shop.repository.member.MemberRepository;
import park.shop.repository.member.MemberUpdateDto;
import park.shop.web.member.dto.MemberInfoDto;
import park.shop.web.member.dto.MemberRegisterDto;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public void registerMember(MemberRegisterDto memberRegisterDto) {
        Member member = new Member();
        member.setLoginId(memberRegisterDto.getLoginId());

        String pwd = memberRegisterDto.getPassword();
        String salt = EncryptUtil.getSalt();
        String encryptedPwd = EncryptUtil.getEncrypt(pwd, salt);
        member.setPassword(encryptedPwd);
        member.setSalt(salt);

        member.setLoginType(LoginType.WEB.toString());
        member.setName(memberRegisterDto.getName());
        member.setGender(memberRegisterDto.getGender().toString());
        member.setAddress(memberRegisterDto.getAddress());
        member.setRole(RoleType.NORMAL.toString());
        member.setIsDelete(IsDeleteType.N.toString());

        memberRepository.save(member);
    }

    public Member login(String loginId, String password) {
        Member findMember = memberRepository.findByLoginId(loginId).orElse(null);
        if(findMember == null) {
            return null;
        }

        String encryptedPwd = EncryptUtil.getEncrypt(password, findMember.getSalt());
        if (!encryptedPwd.equals(findMember.getPassword())) {
            return null;
        }
        return findMember;
    }

    public void updateMember(Long id, MemberInfoDto memberInfoDto) {
        MemberUpdateDto updateDto = new MemberUpdateDto();
        Member findMember = memberRepository.findById(id).orElse(null);

        if (findMember == null) {
            throw new IllegalStateException();
        }

        if (StringUtils.hasText(memberInfoDto.getPassword())) {
            String pwd = memberInfoDto.getPassword();
            String salt = EncryptUtil.getSalt();
            String encryptedPwd = EncryptUtil.getEncrypt(pwd, salt);
            updateDto.setPassword(encryptedPwd);
            updateDto.setSalt(salt);
        } else {
            updateDto.setPassword(findMember.getPassword());
            updateDto.setSalt(findMember.getSalt());
        }

        updateDto.setName(memberInfoDto.getName());
        updateDto.setGender(memberInfoDto.getGender());
        updateDto.setAddress(memberInfoDto.getAddress());
        updateDto.setRole(findMember.getRole());
        updateDto.setIsDelete(findMember.getIsDelete());

        memberRepository.update(id, updateDto);
    }
}
