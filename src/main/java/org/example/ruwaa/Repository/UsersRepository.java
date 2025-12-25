package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>
{
    @Query("select u from Users u where u.id = :id")
    Users findUserById(Integer id);

    @Query("select u from Users u where u.username = :username")
    Users findUserByUsername(String username);
}
