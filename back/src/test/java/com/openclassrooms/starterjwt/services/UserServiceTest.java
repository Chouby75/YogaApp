package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindById() {
        User fakeUser = new User();
        fakeUser.setId(1L);
        fakeUser.setFirstName("Luke");
        fakeUser.setLastName("Skywalker");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(fakeUser));
        User result = userService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Luke");
    }

    @Test
    public void testDelete() {
        Long userId = 1L;

        userService.delete(userId);

        org.mockito.Mockito.verify(userRepository).deleteById(userId);
    }
}
