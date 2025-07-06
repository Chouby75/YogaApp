package com.openclassrooms.starterjwt.services;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    public void testFindAllSessions() {
        List<Session> fakeSessions = new ArrayList<>();
        fakeSessions.add(new Session());
        fakeSessions.add(new Session());

        when(sessionRepository.findAll()).thenReturn(fakeSessions);

        List<Session> result = sessionService.findAll();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testFindSessionById() {
        Session fakeSession = new Session();
        fakeSession.setId(1L);
        fakeSession.setName("Test Session");

        when(sessionRepository.findById(1L)).thenReturn(java.util.Optional.of(fakeSession));
        Session result = sessionService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Session");
    }

    @Test
    public void testCreateSession() {
        Session fakeSession = new Session();
        fakeSession.setName("New Session");

        when(sessionRepository.save(fakeSession)).thenReturn(fakeSession);
        Session result = sessionService.create(fakeSession);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Session");
    }

    @Test
    public void testUpdateSession() {
        Session fakeSession = new Session();
        fakeSession.setId(1L);
        fakeSession.setName("Updated Session");

        when(sessionRepository.save(fakeSession)).thenReturn(fakeSession);
        Session result = sessionService.update(1L, fakeSession);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Session");
    }

    @Test
    public void testDeleteSession() {
        Long sessionId = 1L;

        sessionService.delete(sessionId);
        Mockito.verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    public void testParticipateInSession() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        List<User> users = new ArrayList<>();
        session.setUsers(users);

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        when(sessionRepository.findById(1L)).thenReturn(java.util.Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        sessionService.participate(1L, 1L);

        assertThat(session.getUsers()).contains(user);
    }

    @Test
    public void testNoLongerParticipateInSession() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        users.add(user);
        session.setUsers(users);

        when(sessionRepository.findById(1L)).thenReturn(java.util.Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);

        assertThat(session.getUsers()).doesNotContain(user);
    }

}
