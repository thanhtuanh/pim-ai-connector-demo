package com.mybookstore.productintelligence.repository;

import com.mybookstore.productintelligence.model.BookMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookMetadataRepository extends JpaRepository<BookMetadata, Long> {
    Optional<BookMetadata> findByBookId(Long bookId);
}