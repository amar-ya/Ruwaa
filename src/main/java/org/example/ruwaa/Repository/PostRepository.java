package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>
{
    @Query("select m from Post m where m.id =:id")
    Optional<Post> findPostById(Integer id);

    List<Post> findPostByUsers_id(Integer user_id);
}
