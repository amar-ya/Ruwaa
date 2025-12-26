package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>
{
    @Query("select c from Customer c where c.id = :id")
    Optional<Customer> findCustomerById(Integer id);
}
