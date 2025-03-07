package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.TransactionDao;
import com.bedatasolutions.leaseDrop.dao.UserDao;
import com.bedatasolutions.leaseDrop.dto.TransactionDto;
import com.bedatasolutions.leaseDrop.dto.UserDto;
import com.bedatasolutions.leaseDrop.repo.UserRepo;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {


    private final UserRepo userRepo;  // Inject the UserRepo to interact with the database

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Method to get all users
    public List<UserDto> getAllUsers() {
        List<UserDao> userList = userRepo.findAll(); // Retrieves all users
        return userList.stream()
                .map(this::daoToDto)  // Convert to DTOs before returning
                .collect(Collectors.toList());
    }

    // Method to get a user by ID
    public Optional<UserDto> getUserById(Integer idUsersKey) {
        Optional<UserDao> userOptional = userRepo.findById(idUsersKey); // Find user by ID
        return userOptional.map(this::daoToDto);  // If found, return as DTO
    }

    // Method to create a new user
    @Transactional
    public UserDto create(UserDto userDto) {
        UserDao userDao = dtoToDao(userDto,new UserDao()); // Convert DTO to Entity
        userDao.setActionKey(ActionType.CREATE);
        UserDao savedUser = userRepo.save(userDao); // Save entity in the database
        return daoToDto(savedUser);  // Return the saved user as DTO
    }

    // Method to update an existing user
    @Transactional
    public UserDto update(UserDto userDto) {
        UserDao existingUser = userRepo.getReferenceById(userDto.id());

        if (existingUser.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " +userDto.id());
        }

        // Save updated transaction back to the database
        UserDao updatedUser = userRepo.save(dtoToDao(userDto, existingUser));
        return daoToDto(updatedUser); // Return the
    }

    // Method to delete a user by ID
    @Transactional
    public void delete(Integer idUsersKey) {
        if (userRepo.existsById(idUsersKey)) {
            userRepo.deleteById(idUsersKey); // Delete the user
        } else {
            throw new RuntimeException("User not found with ID " + idUsersKey);  // Handle case where user is not found
        }
    }



    public UserDto daoToDto(UserDao userDao) {
        return new UserDto(
                userDao.getId(),
                userDao.getVersion(),
                userDao.getLdapUid(),
                userDao.getPasswordHash(),
                userDao.getEmail(),
                userDao.getUsername(),
                userDao.getFirstName(),
                userDao.getLastName(),
                userDao.getCompanyEmail(),
                userDao.getContactNumber(),
                userDao.getAddress()

        );
    }
    public UserDao dtoToDao(UserDto userDto, UserDao userDao) {
        userDao.setId(userDto.id());
        userDao.setVersion(userDto.version());
        userDao.setLdapUid(userDto.ldapUid());
        userDao.setPasswordHash(userDto.passwordHash());
        userDao.setEmail(userDto.email());
        userDao.setUsername(userDto.username());
        userDao.setFirstName(userDto.firstName());
        userDao.setLastName(userDto.lastName());
        userDao.setCompanyEmail(userDto.companyEmail());
        userDao.setContactNumber(userDto.contactNumber());
        userDao.setAddress(userDto.address());

        return userDao;
    }




}
