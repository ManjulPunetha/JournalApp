package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<User> getUsersForSentimentAnalysis() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // Build conditions (Predicates)
        Predicate emailExists = cb.isNotNull(root.get("email"));
        Predicate emailNotEmpty = cb.notEqual(root.get("email"), "");
        Predicate sentimentTrue = cb.equal(root.get("sentimentAnalysis"), true);

        // Combine with AND
        query.where(cb.and(emailExists, emailNotEmpty, sentimentTrue));

        return entityManager.createQuery(query).getResultList();
    }
}
