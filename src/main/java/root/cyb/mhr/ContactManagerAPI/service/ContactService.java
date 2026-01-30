package root.cyb.mhr.ContactManagerAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.cyb.mhr.ContactManagerAPI.entity.Contact;
import root.cyb.mhr.ContactManagerAPI.repository.ContactProjection;
import root.cyb.mhr.ContactManagerAPI.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact createContact(Contact contact) {
        if (contact.getCreationDate() == null) {
            contact.setCreationDate(java.time.LocalDateTime.now());
        }
        return contactRepository.save(contact);
    }

    public List<Contact> createContacts(List<Contact> contacts) {
        contacts.forEach(c -> {
            if (c.getCreationDate() == null) {
                c.setCreationDate(java.time.LocalDateTime.now());
            }
        });
        return contactRepository.saveAll(contacts);
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> searchByName(String term) {
        return contactRepository.searchByName(term);
    }

    public long countInactiveContacts() {
        return contactRepository.countByIsActiveFalse();
    }

    public List<ContactProjection> searchByCategory(String category) {
        return contactRepository.searchByCategory(category);
    }

    public Page<ContactProjection> getActiveContacts(Pageable pageable) {
        return contactRepository.findByIsActiveTrue(pageable);
    }

    @Transactional
    public void deleteByPhoneNumberPrefix(String prefix) {
        contactRepository.deleteByPhoneNumberStartingWith(prefix);
    }
}
