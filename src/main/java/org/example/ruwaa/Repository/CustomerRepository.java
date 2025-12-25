package org.example.ruwaa.Repository;

import org.example.ruwaa.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>
{
    @Query("select c from Customer c where c.id = :id")
    Customer findCustomerById(Integer id);
}
