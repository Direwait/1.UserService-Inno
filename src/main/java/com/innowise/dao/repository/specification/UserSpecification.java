package com.innowise.dao.repository.specification;

import com.innowise.dao.model.UserModel;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
public class UserSpecification {

    public static Specification<UserModel> filterByNameOrSurname(String search) {
        return (root, query, cb) -> {
            if (query != null && Long.class != query.getResultType()) {
                root.fetch("cards", JoinType.LEFT);
            }
            if (search == null || search.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("surname")), pattern)
            );
        };
    }
}
