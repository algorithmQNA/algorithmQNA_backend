package algorithm_QnA_community.algorithm_QnA_community.repository;

import algorithm_QnA_community.algorithm_QnA_community.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : algorithm_QnA_community.algorithm_QnA_community.repository
 * fileName       : MemberRepository
 * author         : solmin
 * date           : 2023/05/01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/01        solmin       최초 생성 (JPA 테스트용)
 * 2023/05/10        solmin       findByEmail 추가
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    List<Member> findByName(String name);
}
