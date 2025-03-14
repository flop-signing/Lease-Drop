package com.bedatasolutions.leaseDrop.criteria;

import com.bedatasolutions.leaseDrop.dao.CustomerDao;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
public class CustomerSpecifications {

    // Specification for filtering by name
    public static Specification<CustomerDao> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    // Specification for filtering by packageType
    public static Specification<CustomerDao> hasPackageType(String packageType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("packageType")), "%" + packageType.toLowerCase() + "%");
    }

    // Specification for filtering by amount
    public static Specification<CustomerDao> hasAmount(BigDecimal amount) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("amount"), amount);
    }

    // Specification for filtering by purchaseDate
    public static Specification<CustomerDao> hasPurchaseDate(LocalDate purchaseDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("purchaseDate"), purchaseDate);
    }

    // Specification for filtering by expireDate
    public static Specification<CustomerDao> hasExpireDate(LocalDate expireDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("expireDate"), expireDate);
    }

    // Specification for filtering by remainingDays
    public static Specification<CustomerDao> hasRemainingDays(Integer remainingDays) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("remainingDays"), remainingDays);
    }

    // Specification for filtering by fileProcessing
    public static Specification<CustomerDao> hasFileProcessing(Integer fileProcessing) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("fileProcessing"), fileProcessing);
    }
}*/



public class CustomerSpecifications {

    // Generic Specification for any field
    public static Specification<CustomerDao> hasField(String fieldName, Object fieldValue) {
        return (Root<CustomerDao> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (fieldValue instanceof String) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldName)), "%" + fieldValue.toString().toLowerCase() + "%");
            } else if (fieldValue instanceof BigDecimal) {
                return criteriaBuilder.equal(root.get(fieldName), fieldValue);
            } else if (fieldValue instanceof LocalDate) {
                return criteriaBuilder.equal(root.get(fieldName), fieldValue);
            } else if (fieldValue instanceof Integer) {
                return criteriaBuilder.equal(root.get(fieldName), fieldValue);
            } else {
                return criteriaBuilder.isNull(root.get(fieldName)); // Handle null case
            }
        };
    }
}
