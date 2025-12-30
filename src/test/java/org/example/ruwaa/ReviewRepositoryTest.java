package org.example.ruwaa;


import org.assertj.core.api.Assertions;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Review;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.ExpertRepository;
import org.example.ruwaa.Repository.ReviewRepository;
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


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    UsersRepository userRepository;

    Users user;
    Expert expert;
    Review review1, review2, review3;
    List<Review> reviews;


    @BeforeEach
    void setUp() {

        user = new Users();
        user.setName("Expert User");
        user.setUsername("expertuser");
        user.setPassword("123");
        user.setEmail("user@email.com");
        user.setPhone("0500000000");
        user.setRole("EXPERT");
        userRepository.save(user);

        expert = new Expert();
        expert.setUsers(user);
        expertRepository.save(expert);

        review1 = new Review();
        review1.setStatus("Pending");
        review1.setExpert(expert);

        review2 = new Review();
        review2.setStatus("Completed");
        review2.setExpert(expert);

        review3 = new Review();
        review3.setStatus("Accepted");
        review3.setExpert(expert);

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
    }


    @Test
    public void findReviewByIdTest() {

        reviewRepository.save(review1);

        Optional<Review> review = reviewRepository.findReviewById(review1.getId());

        Assertions.assertThat(review).isPresent();
        Assertions.assertThat(review.get().getId()).isEqualTo(review1.getId());
    }


    @Test
    public void findFinishedReviewsTest() {
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviews = reviewRepository.findFinishedReviews();

        Assertions.assertThat(reviews).isNotEmpty();
        Assertions.assertThat(reviews.size()).isEqualTo(1);
        Assertions.assertThat(reviews.get(0).getStatus()).isEqualTo("Completed");
    }


    @Test
    public void findUnfinishedReviewsTest() {
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviews = reviewRepository.findUnfinishedReviews();

        Assertions.assertThat(reviews).isNotEmpty();
        Assertions.assertThat(reviews.size()).isEqualTo(1);
        Assertions.assertThat(reviews.get(0).getStatus()).isEqualTo("Accepted");
    }


    @Test
    public void findAllByExpertTest() {
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviews = reviewRepository.findAllByExpert(expert);

        Assertions.assertThat(reviews).isNotEmpty();
        Assertions.assertThat(reviews.size()).isEqualTo(3);
        Assertions.assertThat(reviews.get(0).getExpert().getId()).isEqualTo(expert.getId());
    }

    @Test
    public void findPendingReviewsTest() {
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviews = reviewRepository.findPendingReviews(expert);

        Assertions.assertThat(reviews).isNotEmpty();
        Assertions.assertThat(reviews.size()).isEqualTo(1);

        Assertions.assertThat(reviews.get(0).getStatus()).isEqualTo("Pending");
    }

}
