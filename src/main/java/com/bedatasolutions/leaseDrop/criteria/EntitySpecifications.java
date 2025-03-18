//package com.bedatasolutions.leaseDrop.criteria;
//
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import org.springframework.data.jpa.domain.Specification;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.Map;
//
//public class EntitySpecifications {
//
//    public static <T> Specification<T> createSpecification(Map<String, Object> filters) {
//        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
//            Predicate predicate = criteriaBuilder.conjunction(); // Start with an empty predicate
//
//            // Iterate over the filters and build the predicate dynamically
//            for (Map.Entry<String, Object> entry : filters.entrySet()) {
//                String column = entry.getKey();
//                Object value = entry.getValue();
//
//                if (value != null) {
//                    if (value instanceof String) {
//                        // Case-insensitive LIKE search for String fields
//                        predicate = criteriaBuilder.and(predicate,
//                                criteriaBuilder.like(
//                                        criteriaBuilder.lower(root.get(column)),
//                                        "%" + ((String) value).toLowerCase() + "%"
//                                )
//                        );
//                    } else if (value instanceof BigDecimal) {
//                        // EQUAL search for BigDecimal fields
//                        predicate = criteriaBuilder.and(predicate,
//                                criteriaBuilder.equal(root.get(column), value)
//                        );
//                    } else if (value instanceof LocalDate) {
//                        // EQUAL search for LocalDate fields
//                        predicate = criteriaBuilder.and(predicate,
//                                criteriaBuilder.equal(root.get(column), value)
//                        );
//                    } else if (value instanceof Integer) {
//                        // EQUAL search for Integer fields
//                        predicate = criteriaBuilder.and(predicate,
//                                criteriaBuilder.equal(root.get(column), value)
//                        );
//                    } else if (value instanceof Long) {
//                        // EQUAL search for Long fields
//                        predicate = criteriaBuilder.and(predicate,
//                                criteriaBuilder.equal(root.get(column), value)
//                        );
//                    } else if (value instanceof Double) {
//                        // EQUAL search for Double fields
//                        predicate = criteriaBuilder.and(predicate,
//                                criteriaBuilder.equal(root.get(column), value)
//                        );
//                    } else if (value instanceof Boolean) {
//                        // EQUAL search for Boolean fields
//                        predicate = criteriaBuilder.and(predicate,
//                                criteriaBuilder.equal(root.get(column), value)
//                        );
//                    } else {
//                        throw new IllegalArgumentException("Unsupported filter type: " + value.getClass().getSimpleName());
//                    }
//                }
//            }
//
//            return predicate; // Return the final predicate
//        };
//    }
//}