package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer>
{
    @Query("select e from Expert e where e.id = :id")
    Optional<Expert> findExpertById(Integer id);

    @Query("select e from Expert e where e.category = :category order by e.review_count desc")
    Optional<Expert> findMostActiveExpert(String category);
}
