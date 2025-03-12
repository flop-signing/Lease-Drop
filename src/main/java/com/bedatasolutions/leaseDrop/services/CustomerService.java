package com.bedatasolutions.leaseDrop.services;


import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.CustomerDao;
import com.bedatasolutions.leaseDrop.dto.CustomerDto;
import com.bedatasolutions.leaseDrop.repo.CustomerRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

   //  Get All Customers

    public List<CustomerDto> getAllCustomers() {
        List<CustomerDao> customers = customerRepo.findAll();
        return customers.stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());
    }


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
    public void delete(Integer id) {
        CustomerDao customerDao = customerRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Customer not found with id: " + id));

        customerDao.setActionKey(ActionType.DELETE);
        customerRepo.delete(customerDao);

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
