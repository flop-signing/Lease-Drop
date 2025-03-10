package com.bedatasolutions.leaseDrop.services;


import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.ContactUsDao;
import com.bedatasolutions.leaseDrop.dao.DocumentDao;
import com.bedatasolutions.leaseDrop.dto.ContactUsDto;
import com.bedatasolutions.leaseDrop.repo.ContactUsRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactUsService {
    private final ContactUsRepo contactUsRepo;

    public ContactUsService(ContactUsRepo contactUsRepo) {
        this.contactUsRepo = contactUsRepo;
    }

    // Method to get all contact inquiries
    public List<ContactUsDto> getAllContacts() {
        List<ContactUsDao> contactList = contactUsRepo.findAll();
        return contactList.stream()
                .map(this::daoToDto)
                .collect(Collectors.toList());
    }

    // Method to get a contact inquiry by ID
    public Optional<ContactUsDto> getContactById(Integer id) {
        Optional<ContactUsDao> contactOptional = contactUsRepo.findById(id);
        return contactOptional.map(this::daoToDto);
    }

    // Method to create a new contact inquiry
    @Transactional
    public ContactUsDto create(ContactUsDto contactUsDto) {
        ContactUsDao contactDao = dtoToDao(contactUsDto, new ContactUsDao());
        contactDao.setActionKey(ActionType.CREATE);
        ContactUsDao savedContact = contactUsRepo.save(contactDao);
        return daoToDto(savedContact);
    }

    // Method to update an existing contact inquiry
    @Transactional
    public ContactUsDto update(ContactUsDto contactUsDto) {
        ContactUsDao existingContact = contactUsRepo.getReferenceById(contactUsDto.id());
        if (existingContact.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact inquiry not found with id: " + contactUsDto.id());
        }

        existingContact.setActionKey(ActionType.UPDATE);
        ContactUsDao updatedContact = contactUsRepo.save(dtoToDao(contactUsDto, existingContact));
        return daoToDto(updatedContact);
    }

    // Method to delete a contact inquiry by ID
    @Transactional
    public void delete(Integer id) {
       ContactUsDao contactUsDao=contactUsRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
       contactUsDao.setActionKey(ActionType.DELETE);
       contactUsRepo.delete(contactUsDao);
    }

    // Convert DAO to DTO
    public ContactUsDto daoToDto(ContactUsDao contactDao) {
        return new ContactUsDto(
                contactDao.getId(),
                contactDao.getVersion(),
                contactDao.getName(),
                contactDao.getEmail(),
                contactDao.getPhone(),
                contactDao.getSubject(),
                contactDao.getMessage(),
                contactDao.getStatus(),
                contactDao.getResponse()
        );
    }

    // Convert DTO to DAO
    public ContactUsDao dtoToDao(ContactUsDto contactDto, ContactUsDao contactDao) {
        contactDao.setId(contactDto.id());
        contactDao.setVersion(contactDto.version());
        contactDao.setName(contactDto.name());
        contactDao.setEmail(contactDto.email());
        contactDao.setPhone(contactDto.phone());
        contactDao.setSubject(contactDto.subject());
        contactDao.setMessage(contactDto.message());
        contactDao.setStatus(contactDto.status());
        contactDao.setResponse(contactDto.response());
        return contactDao;
    }


}
