package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.PermissionDao;
import com.bedatasolutions.leaseDrop.dao.TransactionDao;
import com.bedatasolutions.leaseDrop.dao.UserDao;
import com.bedatasolutions.leaseDrop.dto.PermissionDto;
import com.bedatasolutions.leaseDrop.dto.TransactionDto;
import com.bedatasolutions.leaseDrop.dto.UserDto;
import com.bedatasolutions.leaseDrop.dto.rest.RestPage;
import com.bedatasolutions.leaseDrop.dto.rest.RestPageResponse;
import com.bedatasolutions.leaseDrop.dto.rest.RestSort;
import com.bedatasolutions.leaseDrop.repo.UserRepo;
import com.bedatasolutions.leaseDrop.utils.ClassMapper;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {


    private final UserRepo userRepo;  // Inject the UserRepo to interact with the database
    private final Map<String, Class<?>> COLUMN_TYPE_MAP;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.COLUMN_TYPE_MAP = ClassMapper.buildColumnTypeMap(UserDao.class);
    }


    // Get Permission by ID
    @Transactional
    public UserDto  getUserById(Integer id) {
        UserDao userDao = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return daoToDto(userDao);
    }


    // Method to get a user by ID
    // Update Existing Customer
    @Transactional
    public UserDto update(UserDto userDto) {
        // Find existing customer by ID
        UserDao existingUser = userRepo.getReferenceById(userDto.id());
        if (existingUser.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userDto.id());
        }

        // Save the updated customer
        existingUser.setActionKey(ActionType.UPDATE);
        UserDao updatedCustomer = userRepo.save(dtoToDao(userDto, existingUser));

        // Return the updated CustomerDto
        return daoToDto(updatedCustomer);
    }





    // Method to create a new user
    @Transactional
    public UserDto create(UserDto userDto) {
        UserDao userDao = dtoToDao(userDto,new UserDao()); // Convert DTO to Entity
        userDao.setActionKey(ActionType.CREATE);
        UserDao savedUser = userRepo.save(userDao); // Save entity in the database
        return daoToDto(savedUser);  // Return the saved user as DTO
    }


    // Dynamically build the COLUMN_TYPE_MAP using reflection


    public RestPageResponse<UserDao, UserDto> getAllUsers(RestPage page, RestSort sort,
                                                              Map<String, String> filters) {
        // Define sorting direction
        // Define sorting direction
        org.springframework.data.domain.Sort sortE = sort.direction().equalsIgnoreCase("asc")
                ? org.springframework.data.domain.Sort.by(sort.field()).ascending() : Sort.by(sort.field()).descending();

        // Create a PageRequest with sorting
        PageRequest pageRequest = PageRequest.of(page.pageNumber()-1, page.size(), sortE);


//        logger.info("Input filters: {}", filters);

        // Convert filter values to their appropriate types dynamically
        Map<String, Object> typedFilters = filters.entrySet().stream()
                .filter(entry ->
                        entry.getValue() != null
                                && !entry.getValue().isEmpty()
                                && COLUMN_TYPE_MAP.containsKey(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ClassMapper.convertValue(entry.getValue(), COLUMN_TYPE_MAP.get(entry.getKey()))
                ));
    //        logger.info("Typed filters: {}", typedFilters);


        // Create the dynamic specification using the filters map
        Specification<UserDao> spec = ClassMapper.createSpecification(typedFilters);

        // Fetch the customers with pagination and sorting, applying the filters
        Page<UserDao> customerPage = userRepo.findAll(spec, pageRequest);

        // Convert the CustomerDao to CustomerDto
        List<UserDto> customerDtos = customerPage.getContent().stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());
        return new RestPageResponse<>(customerDtos, customerPage);
    }




    //  Method to delete a user by ID
    // Delete Customer
    @Transactional
    public boolean delete(Integer id) {
        UserDao userDao = userRepo.findById(id).orElse(null);
        if (userDao == null) {
            return false;
        }

        userDao.setActionKey(ActionType.DELETE);
        userRepo.delete(userDao);
        return true;

    }




    public UserDto daoToDto(UserDao userDao) {
        return new UserDto(
                userDao.getId(),
                userDao.getVersion(),
                userDao.getLdapUid(),
                userDao.getName(),
                userDao.getPasswordHash(),
                userDao.getEmail(),
                userDao.getUsername(),
                userDao.getFirstName(),
                userDao.getLastName(),
                userDao.getCompanyEmail(),
                userDao.getContactNumber(),
                userDao.getAddress(),
                userDao.getPackageType(),
                userDao.getAmount(),
                userDao.getPurchaseDate(),
                userDao.getExpireDate(),
                userDao.getRemainingDays(),
                userDao.getFileProcessing()

        );
    }
    public UserDao dtoToDao(UserDto userDto, UserDao userDao) {
        userDao.setId(userDto.id());
        userDao.setVersion(userDto.version());
        userDao.setLdapUid(userDto.ldapUid());
        userDao.setName(userDto.name());
        userDao.setPasswordHash(userDto.passwordHash());
        userDao.setEmail(userDto.email());
        userDao.setUsername(userDto.username());
        userDao.setFirstName(userDto.firstName());
        userDao.setLastName(userDto.lastName());
        userDao.setCompanyEmail(userDto.companyEmail());
        userDao.setContactNumber(userDto.contactNumber());
        userDao.setAddress(userDto.address());
        userDao.setPackageType(userDto.packageType());
        userDao.setAmount(userDto.amount());
        userDao.setPurchaseDate(userDto.purchaseDate());
        userDao.setExpireDate(userDto.expireDate());
        userDao.setRemainingDays(userDto.remainingDays());
        userDao.setFileProcessing(userDto.fileProcessing());

        return userDao;
    }




}
