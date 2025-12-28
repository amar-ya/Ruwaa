package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardsRepository extends JpaRepository<Card, Integer>
{
    @Query("select c from Card c where c.id = :id")
    Optional<Card> findCardById(Integer id);
}
