package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer>
{
    @Query("select e from Expert e where e.id = :id")
    Expert findExpertById(Integer id);
}
