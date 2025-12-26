package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Integer> {
}
