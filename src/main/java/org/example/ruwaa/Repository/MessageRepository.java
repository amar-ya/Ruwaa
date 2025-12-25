package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>
{
    @Query("select m from Message m where m.id =:id")
    Message findMessageById(Integer id);
}
