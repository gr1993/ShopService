package park.shop.repository.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import park.shop.domain.member.Member;
import park.shop.repository.member.jpa.SDJpaMemberRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static park.shop.domain.member.QMember.member;

@Repository
@Transactional
public class MemberRepositoryImpl implements MemberRepository{
    private SDJpaMemberRepository sdJpaMemberRepository;

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MemberRepositoryImpl(SDJpaMemberRepository sdJpaMemberRepository, EntityManager em) {
        this.sdJpaMemberRepository = sdJpaMemberRepository;
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public void update(Long memberId, MemberUpdateDto updateDto) {
        Member findMember = em.find(Member.class, memberId);
        findMember.setPassword(updateDto.getPassword());
        findMember.setSalt(updateDto.getSalt());
        findMember.setName(updateDto.getName());
        findMember.setGender(updateDto.getGender().toString());
        findMember.setAddress(updateDto.getAddress());
        findMember.setRole(updateDto.getRole());
        findMember.setIsDelete(updateDto.getIsDelete());
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        Member member = sdJpaMemberRepository.findByLoginId(loginId);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findAll(MemberSearchCond cond) {
        String loginId = cond.getLoginId();
        String name = cond.getName();

        return query
                .select(member)
                .from(member)
                .where(likeLoginId(loginId), likeName(name))
                .fetch();
    }

    private BooleanExpression likeLoginId(String loginId) {
        if(StringUtils.hasText(loginId)) {
            return member.loginId.like("%" + loginId + "%");
        }
        return null;
    }

    private BooleanExpression likeName(String name) {
        if(StringUtils.hasText(name)) {
            return member.name.like("%" + name + "%");
        }
        return null;
    }
}
