package com.bedatasolutions.leaseDrop.services;


import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.criteria.CustomerSpecifications;
import com.bedatasolutions.leaseDrop.dao.CustomerDao;
import com.bedatasolutions.leaseDrop.dto.CustomerDto;
import com.bedatasolutions.leaseDrop.repo.CustomerRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import com.bedatasolutions.leaseDrop.criteria.FilterHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;

    }

    // Create Customer
    @Transactional
    public CustomerDto create(CustomerDto customerDto) {
        // Convert DTO to DAO (CustomerDao)
        CustomerDao customerDao = dtoToDao(customerDto, new CustomerDao());
        customerDao.setActionKey(ActionType.CREATE);

        // Save the customer and return the saved DTO
        CustomerDao savedCustomer = customerRepo.save(customerDao);
        return daoToDto(savedCustomer);
    }


   /* public Map<String, Object> getAllCustomers(Integer page, Integer size, String field, String direction) {
        // Set default values if null
        if (page == null || page < 1) page = 1; // Default to page 1
        if (size == null || size <= 0) size = 10; // Default to size 10
        if (field == null || field.trim().isEmpty()) field = "id"; // Default to `id`
        if (direction == null || direction.trim().isEmpty()) direction = "desc"; // Default to descending

        // Define sorting direction
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Create a PageRequest with sorting
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortDirection, field));

        // Fetch the customers with pagination and sorting
        Page<CustomerDao> customerPage = customerRepo.findAll(pageRequest);

        // Convert the CustomerDao to CustomerDto
        List<CustomerDto> customerDtos = customerPage.getContent().stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());

        // Prepare the response with pagination details
        Map<String, Object> response = new HashMap<>();
        response.put("payload", customerDtos);

        Map<String, Object> header = new HashMap<>();
        header.put("pageNo", page);
        header.put("totalPages", customerPage.getTotalPages());
        header.put("pageSize", size);
        header.put("totalElements", customerPage.getTotalElements());

        response.put("header", header);

        return response;
    }


*/

/*


    public Map<String, Object> getAllCustomers(Integer page, Integer size, String field, String direction,
                                               String name, String packageType, BigDecimal amount,
                                               LocalDate purchaseDate, LocalDate expireDate,
                                               Integer remainingDays, Integer fileProcessing) {

        // Set default values if null
        if (page == null || page < 1) page = 1; // Default to page 1
        if (size == null || size <= 0) size = 10; // Default to size 10
        if (field == null || field.trim().isEmpty()) field = "id"; // Default to `id`
        if (direction == null || direction.trim().isEmpty()) direction = "desc"; // Default to descending

        // Define sorting direction
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Create a PageRequest with sorting
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortDirection, field));

        // Create Specification for filtering (name, packageType, amount, purchaseDate, expireDate, remainingDays, fileProcessing)
        Specification<CustomerDao> spec = Specification.where(null);

        // Add filters dynamically based on input
        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and(CustomerSpecifications.hasName(name)); // Apply name filter if present
        }

        if (packageType != null && !packageType.trim().isEmpty()) {
            spec = spec.and(CustomerSpecifications.hasPackageType(packageType)); // Apply packageType filter if present
        }

        if (amount != null) {
            spec = spec.and(CustomerSpecifications.hasAmount(amount)); // Apply amount filter if present
        }

        if (purchaseDate != null) {
            spec = spec.and(CustomerSpecifications.hasPurchaseDate(purchaseDate)); // Apply purchaseDate filter if present
        }

        if (expireDate != null) {
            spec = spec.and(CustomerSpecifications.hasExpireDate(expireDate)); // Apply expireDate filter if present
        }

        if (remainingDays != null) {
            spec = spec.and(CustomerSpecifications.hasRemainingDays(remainingDays)); // Apply remainingDays filter if present
        }

        if (fileProcessing != null) {
            spec = spec.and(CustomerSpecifications.hasFileProcessing(fileProcessing)); // Apply fileProcessing filter if present
        }

        // Fetch the customers with pagination and sorting, applying the filters
        Page<CustomerDao> customerPage = customerRepo.findAll(spec, pageRequest);

        // Convert the CustomerDao to CustomerDto
        List<CustomerDto> customerDtos = customerPage.getContent().stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());

        // Prepare the response with pagination details
        Map<String, Object> response = new HashMap<>();
        response.put("payload", customerDtos);

        Map<String, Object> header = new HashMap<>();
        header.put("pageNo", page);
        header.put("totalPages", customerPage.getTotalPages());
        header.put("pageSize", size);
        header.put("totalElements", customerPage.getTotalElements());

        response.put("header", header);

        return response;
    }
*/


    public Map<String, Object> getAllCustomers(Integer page, Integer size, String field, String direction,
                                               Map<String, Object> filterParams) {

        // Set default values if null
        if (page == null || page < 1) page = 1; // Default to page 1
        if (size == null || size <= 0) size = 10; // Default to size 10
        if (field == null || field.trim().isEmpty()) field = "id"; // Default to `id`
        if (direction == null || direction.trim().isEmpty()) direction = "desc"; // Default to descending

        // Define sorting direction
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Create a PageRequest with sorting
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortDirection, field));

        // Create Specification for filtering dynamically
        Specification<CustomerDao> spec = Specification.where(null);

        // Use reflection to dynamically access fields of CustomerDao
        Field[] fields = CustomerDao.class.getDeclaredFields();

        // Dynamically check and apply filters for non-null values from the filterParams map
        for (Field fieldObj : fields) {
            fieldObj.setAccessible(true); // Make private fields accessible

            // Get the corresponding value from the filterParams map
            Object value = filterParams.get(fieldObj.getName());

            // If the value is not null, dynamically build the specification and apply the filter
            if (value != null) {
                spec = spec.and(buildSpecification(fieldObj.getName(), value)); // Dynamically apply the filter
            }
        }

        // Fetch the customers with pagination and sorting, applying the filters
        Page<CustomerDao> customerPage = customerRepo.findAll(spec, pageRequest);

        // Convert the CustomerDao to CustomerDto
        List<CustomerDto> customerDtos = customerPage.getContent().stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());

        // Prepare the response with pagination details
        Map<String, Object> response = new HashMap<>();
        response.put("payload", customerDtos);

        Map<String, Object> header = new HashMap<>();
        header.put("pageNo", page);
        header.put("totalPages", customerPage.getTotalPages());
        header.put("pageSize", size);
        header.put("totalElements", customerPage.getTotalElements());

        response.put("header", header);

        return response;
    }

    private Specification<CustomerDao> buildSpecification(String fieldName, Object fieldValue) {
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





    // Get a single Customer
    public CustomerDto getCustomerById(Integer id) {
        CustomerDao customerDao = customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return daoToDto(customerDao);
    }


    // Update Existing Customer
    @Transactional
    public CustomerDto update(CustomerDto customerDto) {
        // Find existing customer by ID
        CustomerDao existingCustomer = customerRepo.getReferenceById(customerDto.id());
        if (existingCustomer.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id: " + customerDto.id());
        }

        // Save the updated customer
        existingCustomer.setActionKey(ActionType.UPDATE);
        CustomerDao updatedCustomer = customerRepo.save(dtoToDao(customerDto, existingCustomer));

        // Return the updated CustomerDto
        return daoToDto(updatedCustomer);
    }


    // Delete Customer
    @Transactional
    public boolean delete(Integer id) {
        CustomerDao customerDao = customerRepo.findById(id).orElse(null);
        if (customerDao == null) {
            return false;
        }

        customerDao.setActionKey(ActionType.DELETE);
        customerRepo.delete(customerDao);
        return true;

    }

    public CustomerDto daoToDto(CustomerDao customerDao) {
        return new CustomerDto(
                customerDao.getId(),
                customerDao.getVersion(),
                customerDao.getName(),
                customerDao.getPackageType(),
                customerDao.getAmount(),
                customerDao.getPurchaseDate(),
                customerDao.getExpireDate(),
                customerDao.getRemainingDays(),
                customerDao.getFileProcessing()
        );
    }

    // Convert DTO to DAO
    public CustomerDao dtoToDao(CustomerDto customerDto, CustomerDao customerDao) {
        customerDao.setVersion(customerDto.version());
        customerDao.setName(customerDto.name());
        customerDao.setPackageType(customerDto.packageType());
        customerDao.setAmount(customerDto.amount());
        customerDao.setPurchaseDate(customerDto.purchaseDate());
        customerDao.setExpireDate(customerDto.expireDate());
        customerDao.setRemainingDays(customerDto.remainingDays());
        customerDao.setFileProcessing(customerDto.fileProcessing());
        return customerDao;
    }


}
