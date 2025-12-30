package org.example.ruwaa;

import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Expert;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.ExpertRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.example.ruwaa.Service.ExpertService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExpertServiceTest {

    @InjectMocks
    ExpertService expertService;

    @Mock
    ExpertRepository expertRepository;

    @Mock
    UsersRepository userRepository;

    Expert expert1, expert2;
    Users user1, user2;
    List<Expert> experts;

    @BeforeEach
    void setUp() {

        user1 = new Users();
        user1.setId(1);
        user1.setName("User One");
        user1.setUsername("user1");
        user1.setPassword("123");
        user1.setEmail("user1@email.com");
        user1.setPhone("0500000000");
        user1.setRole("EXPERT");
        user1.setBalance(0.0);
        user1.setCreatedAt(LocalDateTime.now());

        user2 = new Users();
        user2.setName("User Two");
        user2.setUsername("user2");
        user2.setPassword("456");
        user2.setEmail("user2@email.com");
        user2.setPhone("0511111111");
        user2.setRole("EXPERT");

        expert1 = new Expert();
        expert1.setId(1);
        expert1.setUsers(user1);

        expert2 = new Expert();
        expert2.setId(2);
        expert2.setUsers(user2);

        experts = new ArrayList<>();
        experts.add(expert1);
        experts.add(expert2);
    }


    @Test
    public void getAllExpertsTest() {

        when(expertRepository.findAll()).thenReturn(experts);

        List<Expert> result = expertService.getAll();

        assertEquals(2, result.size());
        verify(expertRepository, times(1)).findAll();
    }

    @Test
    public void updateExpertTest() {

        when(expertRepository.findExpertById(expert1.getId()))
                .thenReturn(Optional.of(expert1));

        expertService.update(expert1.getId(), user2);

        assertEquals("user2", expert1.getUsers().getUsername());
        assertEquals("456", expert1.getUsers().getPassword());
        assertEquals("user2@email.com", expert1.getUsers().getEmail());
        assertEquals("0511111111", expert1.getUsers().getPhone());
        assertEquals("User Two", expert1.getUsers().getName());

        verify(expertRepository, times(1)).findExpertById(expert1.getId());
        verify(expertRepository, times(1)).save(expert1);
    }


    @Test
    public void deleteExpertTest() {

        when(expertRepository.findExpertById(expert1.getId()))
                .thenReturn(Optional.of(expert1));

        expertService.delete(expert1.getId());

        verify(expertRepository, times(1)).findExpertById(expert1.getId());
        verify(expertRepository, times(1)).delete(expert1);
    }


    @Test
    public void setAvailableTest() {

        expert1.setIsAvailable(false);

        when(expertRepository.findExpertByUsername(user1.getUsername()))
                .thenReturn(Optional.of(expert1));

        expertService.setAvailable(user1.getUsername());

        assertTrue(expert1.getIsAvailable());
        verify(expertRepository, times(1))
                .findExpertByUsername(user1.getUsername());
        verify(expertRepository, times(1)).save(expert1);
    }


    @Test
    public void setBusyTest() {

        expert1.setIsAvailable(true);

        when(expertRepository.findExpertByUsername(user1.getUsername()))
                .thenReturn(Optional.of(expert1));

        ApiException exception = assertThrows(
                ApiException.class,
                () -> expertService.setAvailable(user1.getUsername())
        );

        assertEquals("status already available", exception.getMessage());
        verify(expertRepository, times(1))
                .findExpertByUsername(user1.getUsername());
        verify(expertRepository, times(0)).save(expert1);
    }


    @Test
    void activateExpert_alreadyActive() {
        expert1.setIsActive(true);
        when(expertRepository.findExpertById(expert1.getId())).thenReturn(Optional.of(expert1));

        ApiException exception = assertThrows(ApiException.class,
                () -> expertService.activateExpert(expert1.getId()));

        assertEquals("this expert is already active", exception.getMessage());
        verify(expertRepository, never()).save(any());
    }


}
