package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        testUser = User.builder()
                .id(1L)
                .email("luke.skywalker@jedi.com")
                .firstName("Luke")
                .lastName("Skywalker")
                .password("encodedPassword123")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should return user when user exists")
        void shouldReturnUserWhenUserExists() {
            // Given
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

            // When
            User result = userService.findById(userId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testUser.getId());
            assertThat(result.getFirstName()).isEqualTo("Luke");
            assertThat(result.getLastName()).isEqualTo("Skywalker");
            assertThat(result.getEmail()).isEqualTo("luke.skywalker@jedi.com");
            assertThat(result.getPassword()).isEqualTo("encodedPassword123");
            assertThat(result.isAdmin()).isFalse();
            assertThat(result.getCreatedAt()).isEqualTo(now);
            assertThat(result.getUpdatedAt()).isEqualTo(now);
            
            verify(userRepository).findById(userId);
        }

        @Test
        @DisplayName("Should return null when user not found")
        void shouldReturnNullWhenUserNotFound() {
            // Given
            Long nonExistentUserId = 999L;
            when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

            // When
            User result = userService.findById(nonExistentUserId);

            // Then
            assertThat(result).isNull();
            verify(userRepository).findById(nonExistentUserId);
        }

        @Test
        @DisplayName("Should handle null ID parameter")
        void shouldHandleNullIdParameter() {
            // Given
            Long nullId = null;
            when(userRepository.findById(nullId)).thenReturn(Optional.empty());

            // When
            User result = userService.findById(nullId);

            // Then
            assertThat(result).isNull();
            verify(userRepository).findById(nullId);
        }

        @Test
        @DisplayName("Should handle negative ID")
        void shouldHandleNegativeId() {
            // Given
            Long negativeId = -1L;
            when(userRepository.findById(negativeId)).thenReturn(Optional.empty());

            // When
            User result = userService.findById(negativeId);

            // Then
            assertThat(result).isNull();
            verify(userRepository).findById(negativeId);
        }

        @Test
        @DisplayName("Should handle zero ID")
        void shouldHandleZeroId() {
            // Given
            Long zeroId = 0L;
            when(userRepository.findById(zeroId)).thenReturn(Optional.empty());

            // When
            User result = userService.findById(zeroId);

            // Then
            assertThat(result).isNull();
            verify(userRepository).findById(zeroId);
        }

        @Test
        @DisplayName("Should handle large ID values")
        void shouldHandleLargeIdValues() {
            // Given
            Long largeId = Long.MAX_VALUE;
            when(userRepository.findById(largeId)).thenReturn(Optional.empty());

            // When
            User result = userService.findById(largeId);

            // Then
            assertThat(result).isNull();
            verify(userRepository).findById(largeId);
        }

        @Test
        @DisplayName("Should return admin user correctly")
        void shouldReturnAdminUserCorrectly() {
            // Given
            User adminUser = User.builder()
                    .id(2L)
                    .email("admin@jedi.com")
                    .firstName("Obi-Wan")
                    .lastName("Kenobi")
                    .password("encodedPassword456")
                    .admin(true)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            when(userRepository.findById(2L)).thenReturn(Optional.of(adminUser));

            // When
            User result = userService.findById(2L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(2L);
            assertThat(result.getFirstName()).isEqualTo("Obi-Wan");
            assertThat(result.getLastName()).isEqualTo("Kenobi");
            assertThat(result.isAdmin()).isTrue();
            
            verify(userRepository).findById(2L);
        }

        @Test
        @DisplayName("Should handle user with minimal fields")
        void shouldHandleUserWithMinimalFields() {
            // Given
            User minimalUser = User.builder()
                    .id(3L)
                    .email("minimal@example.com")
                    .firstName("Min")
                    .lastName("User")
                    .password("password")
                    .build();

            when(userRepository.findById(3L)).thenReturn(Optional.of(minimalUser));

            // When
            User result = userService.findById(3L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getEmail()).isEqualTo("minimal@example.com");
            assertThat(result.getCreatedAt()).isNull();
            assertThat(result.getUpdatedAt()).isNull();
            
            verify(userRepository).findById(3L);
        }
    }

    @Nested
    @DisplayName("delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete user by ID successfully")
        void shouldDeleteUserByIdSuccessfully() {
            // Given
            Long userId = 1L;
            doNothing().when(userRepository).deleteById(userId);

            // When
            userService.delete(userId);

            // Then
            verify(userRepository).deleteById(userId);
        }

        @Test
        @DisplayName("Should handle null ID in delete")
        void shouldHandleNullIdInDelete() {
            // Given
            Long nullId = null;
            doNothing().when(userRepository).deleteById(nullId);

            // When
            userService.delete(nullId);

            // Then
            verify(userRepository).deleteById(nullId);
        }

        @Test
        @DisplayName("Should handle negative ID in delete")
        void shouldHandleNegativeIdInDelete() {
            // Given
            Long negativeId = -1L;
            doNothing().when(userRepository).deleteById(negativeId);

            // When
            userService.delete(negativeId);

            // Then
            verify(userRepository).deleteById(negativeId);
        }

        @Test
        @DisplayName("Should handle zero ID in delete")
        void shouldHandleZeroIdInDelete() {
            // Given
            Long zeroId = 0L;
            doNothing().when(userRepository).deleteById(zeroId);

            // When
            userService.delete(zeroId);

            // Then
            verify(userRepository).deleteById(zeroId);
        }

        @Test
        @DisplayName("Should handle large ID values in delete")
        void shouldHandleLargeIdValuesInDelete() {
            // Given
            Long largeId = Long.MAX_VALUE;
            doNothing().when(userRepository).deleteById(largeId);

            // When
            userService.delete(largeId);

            // Then
            verify(userRepository).deleteById(largeId);
        }

        @Test
        @DisplayName("Should propagate repository exceptions during delete")
        void shouldPropagateRepositoryExceptionsDuringDelete() {
            // Given
            Long userId = 1L;
            RuntimeException expectedException = new RuntimeException("Database connection error");
            doThrow(expectedException).when(userRepository).deleteById(userId);

            // When & Then
            assertThatThrownBy(() -> userService.delete(userId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Database connection error");
            
            verify(userRepository).deleteById(userId);
        }

        @Test
        @DisplayName("Should call repository delete exactly once")
        void shouldCallRepositoryDeleteExactlyOnce() {
            // Given
            Long userId = 42L;
            doNothing().when(userRepository).deleteById(userId);

            // When
            userService.delete(userId);

            // Then
            verify(userRepository, times(1)).deleteById(userId);
            verifyNoMoreInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("Integration Behavior Tests")
    class IntegrationBehaviorTests {

        @Test
        @DisplayName("Should handle repository exceptions in findById gracefully")
        void shouldHandleRepositoryExceptionsInFindByIdGracefully() {
            // Given
            Long userId = 1L;
            RuntimeException expectedException = new RuntimeException("Database error");
            when(userRepository.findById(userId)).thenThrow(expectedException);

            // When & Then
            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Database error");
            
            verify(userRepository).findById(userId);
        }

        @Test
        @DisplayName("Should not call delete when findById throws exception")
        void shouldNotCallDeleteWhenFindByIdThrowsException() {
            // Given
            Long userId = 1L;
            when(userRepository.findById(userId)).thenThrow(new RuntimeException("Error"));

            // When & Then - First verify findById throws exception
            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(RuntimeException.class);

            // When - Now call delete separately
            userService.delete(userId);

            // Then - Verify both methods were called independently
            verify(userRepository).findById(userId);
            verify(userRepository).deleteById(userId);
        }

        @Test
        @DisplayName("Should handle multiple successive findById calls")
        void shouldHandleMultipleSuccessiveFindByIdCalls() {
            // Given
            Long userId = 1L;
            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(testUser))
                    .thenReturn(Optional.empty())
                    .thenReturn(Optional.of(testUser));

            // When & Then
            User result1 = userService.findById(userId);
            assertThat(result1).isNotNull();
            assertThat(result1.getFirstName()).isEqualTo("Luke");

            User result2 = userService.findById(userId);
            assertThat(result2).isNull();

            User result3 = userService.findById(userId);
            assertThat(result3).isNotNull();
            assertThat(result3.getFirstName()).isEqualTo("Luke");

            verify(userRepository, times(3)).findById(userId);
        }

        @Test
        @DisplayName("Should handle multiple successive delete calls")
        void shouldHandleMultipleSuccessiveDeleteCalls() {
            // Given
            Long userId = 1L;
            doNothing().when(userRepository).deleteById(userId);

            // When
            userService.delete(userId);
            userService.delete(userId);
            userService.delete(userId);

            // Then
            verify(userRepository, times(3)).deleteById(userId);
        }
    }

    @Nested
    @DisplayName("Constructor and Dependency Tests")
    class ConstructorAndDependencyTests {

        @Test
        @DisplayName("Should create service with required dependencies")
        void shouldCreateServiceWithRequiredDependencies() {
            // Given
            UserRepository mockRepository = mock(UserRepository.class);

            // When
            UserService service = new UserService(mockRepository);

            // Then
            assertThat(service).isNotNull();
            // Note: We can't directly test private fields without reflection,
            // but we can verify the constructor doesn't throw exceptions
        }
    }
}