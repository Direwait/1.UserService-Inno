package com.innowise.dao.repository.specification;

import com.innowise.dao.model.CardModel;
import org.springframework.data.jpa.domain.Specification;


public class CardSpecification {

    public static Specification<CardModel> filterByNumber(String number) {
        return (root, query, cb) -> {
            if (number == null || number.isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + number.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("number")), pattern);
        };
    }
}
