package park.shop.repository.member.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import park.shop.domain.Member;

public interface SDJpaMemberRepository extends JpaRepository<Member, Long> {
}
