package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer>
{
    @Query("select m from Media m where m.id =:id")
    Media findMediaById(Integer id);
}
