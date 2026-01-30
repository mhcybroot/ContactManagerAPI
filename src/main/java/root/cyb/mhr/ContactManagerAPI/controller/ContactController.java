package root.cyb.mhr.ContactManagerAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import root.cyb.mhr.ContactManagerAPI.entity.Contact;
import root.cyb.mhr.ContactManagerAPI.repository.ContactProjection;
import root.cyb.mhr.ContactManagerAPI.service.ContactService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        Contact created = contactService.createContact(contact);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        return contact.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Contact>> searchByName(@RequestParam String term) {
        return ResponseEntity.ok(contactService.searchByName(term));
    }

    @GetMapping("/count-inactive")
    public ResponseEntity<Long> countInactiveContacts() {
        return ResponseEntity.ok(contactService.countInactiveContacts());
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<ContactProjection>> searchByCategory(@RequestParam("name") String category) {
        return ResponseEntity.ok(contactService.searchByCategory(category));
    }

    @GetMapping("/active/page")
    public ResponseEntity<Page<ContactProjection>> getActiveContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName,asc") String sort) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        return ResponseEntity.ok(contactService.getActiveContacts(pageable));
    }

    @DeleteMapping("/delete-by-prefix")
    public ResponseEntity<Void> deleteByPrefix(@RequestParam String prefix) {
        contactService.deleteByPhoneNumberPrefix(prefix);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/mass")
    public ResponseEntity<List<Contact>> createContacts(@RequestBody List<Contact> contacts) {
        return ResponseEntity.ok(contactService.createContacts(contacts));
    }
}
