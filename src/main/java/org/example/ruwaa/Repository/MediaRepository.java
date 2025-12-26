package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Post, Integer>
{
    @Query("select m from Post m where m.id =:id")
    Optional<Post> findMediaById(Integer id);
}
