package park.shop.repository.member;

import park.shop.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    void update(Long memberId, MemberUpdateDto updateDto);

    Optional<Member> findById(Long id);

    Optional<Member> findByLoginId(String loginId);

    List<Member> findAll(MemberSearchCond cond);
}
