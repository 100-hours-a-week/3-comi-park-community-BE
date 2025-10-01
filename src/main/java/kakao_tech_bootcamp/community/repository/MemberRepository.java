package kakao_tech_bootcamp.community.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import kakao_tech_bootcamp.community.entity.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    @PersistenceContext
    EntityManager em;

    public boolean existsEmail(String email) {
        Query query = em.createQuery("select count(m) from member m where m.email = :email")
                .setParameter("email", email);
        int count = (int) query.getSingleResult();
        return count > 0;
    }

    public boolean existsNickname(String nickname) {
        Query query = em.createQuery("select count(m) from member m where nickname = :nickname")
                .setParameter("nickname", nickname);
        int count = (int) query.getSingleResult();
        return count > 0;
    }

    public void save(Member member) {
        em.persist(member);
    }

    public Member findBy(Integer id) {
        return em.find(Member.class, id);
    }

    public void remove(Member member) {
        em.remove(member);
    }
}
