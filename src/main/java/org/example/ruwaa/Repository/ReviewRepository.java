package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>
{
    @Query("select r from Review r where r.id =:id")
    Optional<Review> findReviewById(Integer id);
}
