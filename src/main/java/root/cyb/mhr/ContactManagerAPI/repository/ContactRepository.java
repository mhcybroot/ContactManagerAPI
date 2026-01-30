package root.cyb.mhr.ContactManagerAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import root.cyb.mhr.ContactManagerAPI.entity.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT c.firstName as firstName, c.email as email FROM Contact c WHERE c.category = :category")
    List<ContactProjection> searchByCategory(@Param("category") String category);

    Page<ContactProjection> findByIsActiveTrue(Pageable pageable);

    @Transactional
    @Modifying
    void deleteByPhoneNumberStartingWith(String prefix);

    long countByIsActiveFalse();

    @Query("SELECT c FROM Contact c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Contact> searchByName(@Param("term") String term);
}
