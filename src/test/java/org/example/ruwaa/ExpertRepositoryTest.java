package org.example.ruwaa;

import org.assertj.core.api.Assertions;
import org.example.ruwaa.Model.Categories;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.ExpertRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpertRepositoryTest {

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    UsersRepository userRepository;


    private Categories categories;
    private Expert expert1, expert2;
    private Users user1, user2;

    @BeforeEach
    void setUp() {
        categories = new Categories();
        categories.setName("sport");

        user1 = new Users();
        user1.setName("Expert One");
        user1.setUsername("expertuser1");
        user1.setPassword("123");
        user1.setEmail("user1@email.com");
        user1.setPhone("0500000001");
        user1.setRole("EXPERT");
        userRepository.save(user1);

        expert1 = new Expert();
        expert1.setUsers(user1);
        expert1.setReview_count(10);
        expert1.setCategory(categories);
        expertRepository.save(expert1);

        user2 = new Users();
        user2.setName("Expert Two");
        user2.setUsername("expertuser2");
        user2.setPassword("123");
        user2.setEmail("user2@email.com");
        user2.setPhone("0500000002");
        user2.setRole("EXPERT");
        userRepository.save(user2);

        expert2 = new Expert();
        expert2.setUsers(user2);
        expert2.setReview_count(5);
        expert2.setCategory(categories);
        expertRepository.save(expert2);
    }


    @Test
    void findExpertByIdTest() {
        expertRepository.save(expert1);
        Optional<Expert> expert = expertRepository.findExpertById(expert1.getId());


        assertThat(expert).isPresent();
        assertThat(expert.get().getId()).isEqualTo(expert1.getId());
    }





}
