package com.bedatasolutions.leaseDrop.criteria;

import com.bedatasolutions.leaseDrop.dao.CustomerDao;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class CustomerSpecifications {

    // Dynamic specification for filtering by any column
    public static Specification<CustomerDao> createSpecification(Map<String, Object> filters) {
        return (Root<CustomerDao> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // Start with an empty predicate

            // Iterate over the filters and build the predicate dynamically
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String column = entry.getKey();
                Object value = entry.getValue();

                if (value != null) {
                    if (value instanceof String) {
                        // For String columns, perform a case-insensitive LIKE search
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get(column)),
                                        "%" + ((String) value).toLowerCase() + "%"
                                )
                        );
                    } else if (value instanceof BigDecimal) {
                        // For BigDecimal columns, perform an EQUAL search
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.equal(root.get(column), value)
                        );
                    } else if (value instanceof LocalDate) {
                        // For LocalDate columns, perform an EQUAL search
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.equal(root.get(column), value)
                        );
                    } else if (value instanceof Integer) {
                        // For Integer columns, perform an EQUAL search
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.equal(root.get(column), value)
                        );
                    }
                }
            }

            return predicate; // Return the final predicate
        };
    }
}