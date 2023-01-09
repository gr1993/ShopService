package park.shop.repository.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import park.shop.domain.member.GenderType;
import park.shop.domain.member.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        //given
        Member member = createDumpMember("1", GenderType.M);

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void update() {
        //given
        Member member = createDumpMember("1", GenderType.M);
        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();

        //when
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setAddress("주소2");
        updateDto.setGender(GenderType.F);
        updateDto.setPassword("test2");
        updateDto.setName("박강린2");
        updateDto.setRole("판매자");
        updateDto.setIsDelete("A");
        memberRepository.update(memberId, updateDto);

        //then
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getAddress()).isEqualTo(updateDto.getAddress());
        assertThat(findMember.getGender()).isEqualTo(updateDto.getGender());
        assertThat(findMember.getPassword()).isEqualTo(updateDto.getPassword());
        assertThat(findMember.getName()).isEqualTo(updateDto.getName());
        assertThat(findMember.getRole()).isEqualTo(updateDto.getRole());
        assertThat(findMember.getIsDelete()).isEqualTo(updateDto.getIsDelete());
    }

    @Test
    void findMembers() {
        //given
        Member member1 = createDumpMember("1", GenderType.M);
        Member member2 = createDumpMember("2", GenderType.F);
        Member member3 = createDumpMember("3", GenderType.M);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //둘 다 없음 검증
        findTest(null, null, member1, member2, member3);
        findTest("", null, member1, member2, member3);

        //loginId 검증
        findTest("est", null, member1, member2, member3);
        findTest("2", null, member2);

        //name 검증
        findTest(null, "강림", member1, member2, member3);
        findTest(null, "박강림3", member3);

        //둘 다 있음 검증
        findTest("test3", "박강림3", member3);
    }

    void findTest(String loginId, String name, Member... members) {
        List<Member> result = memberRepository.findAll(new MemberSearchCond(loginId, name));
        assertThat(result).containsExactly(members);
    }

    Member createDumpMember(String addText, GenderType gender) {
        Member member = new Member();
        member.setLoginId("test" + addText);
        member.setPassword("pw" + addText);
        member.setName("박강림" + addText);
        member.setRole("일반" + addText);
        member.setGender(gender.toString());
        member.setLoginType("web" + addText);
        member.setAddress("주소" + addText);

        return member;
    }
}