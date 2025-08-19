package com.openclassrooms.starterjwt.security.services;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImpl and UserDetailsImpl Tests")
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("encodedPassword123")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Nested
    @DisplayName("UserDetailsServiceImpl Tests")
    class UserDetailsServiceImplTests {

        @Test
        @DisplayName("Should return UserDetails when user exists")
        void shouldReturnUserDetailsWhenUserExists() {
            // Given
            String email = "test@example.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

            // When
            UserDetails result = userDetailsService.loadUserByUsername(email);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(UserDetailsImpl.class);
            
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) result;
            assertThat(userDetailsImpl.getId()).isEqualTo(testUser.getId());
            assertThat(userDetailsImpl.getUsername()).isEqualTo(testUser.getEmail());
            assertThat(userDetailsImpl.getFirstName()).isEqualTo(testUser.getFirstName());
            assertThat(userDetailsImpl.getLastName()).isEqualTo(testUser.getLastName());
            assertThat(userDetailsImpl.getPassword()).isEqualTo(testUser.getPassword());
            
            verify(userRepository).findByEmail(email);
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when user not found")
        void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
            // Given
            String nonExistentEmail = "nonexistent@example.com";
            when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userDetailsService.loadUserByUsername(nonExistentEmail))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("User Not Found with email: " + nonExistentEmail);
            
            verify(userRepository).findByEmail(nonExistentEmail);
        }

        @Test
        @DisplayName("Should handle null email parameter")
        void shouldHandleNullEmailParameter() {
            // Given
            String nullEmail = null;
            when(userRepository.findByEmail(nullEmail)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userDetailsService.loadUserByUsername(nullEmail))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("User Not Found with email: null");
            
            verify(userRepository).findByEmail(nullEmail);
        }

        @Test
        @DisplayName("Should handle empty email parameter")
        void shouldHandleEmptyEmailParameter() {
            // Given
            String emptyEmail = "";
            when(userRepository.findByEmail(emptyEmail)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userDetailsService.loadUserByUsername(emptyEmail))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("User Not Found with email: ");
            
            verify(userRepository).findByEmail(emptyEmail);
        }}

    @Nested
    @DisplayName("UserDetailsImpl Tests")
    class UserDetailsImplTests {

        private UserDetailsImpl userDetails1;
        private UserDetailsImpl userDetails2;

        @BeforeEach
        void setUp() {
            userDetails1 = UserDetailsImpl.builder()
                    .id(1L)
                    .username("test@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .password("password123")
                    .build();

            userDetails2 = UserDetailsImpl.builder()
                    .id(2L)
                    .username("jane@example.com")
                    .firstName("Jane")
                    .lastName("Smith")
                    .password("password456")
                    .build();
        }

        @Test
        @DisplayName("Should return true for isAccountNonExpired")
        void shouldReturnTrueForIsAccountNonExpired() {
            // When & Then
            assertThat(userDetails1.isAccountNonExpired()).isTrue();
            assertThat(userDetails2.isAccountNonExpired()).isTrue();
        }

        @Test
        @DisplayName("Should return true for isAccountNonLocked")
        void shouldReturnTrueForIsAccountNonLocked() {
            // When & Then
            assertThat(userDetails1.isAccountNonLocked()).isTrue();
            assertThat(userDetails2.isAccountNonLocked()).isTrue();
        }

        @Test
        @DisplayName("Should return true for isCredentialsNonExpired")
        void shouldReturnTrueForIsCredentialsNonExpired() {
            // When & Then
            assertThat(userDetails1.isCredentialsNonExpired()).isTrue();
            assertThat(userDetails2.isCredentialsNonExpired()).isTrue();
        }

        @Test
        @DisplayName("Should return true for isEnabled")
        void shouldReturnTrueForIsEnabled() {
            // When & Then
            assertThat(userDetails1.isEnabled()).isTrue();
            assertThat(userDetails2.isEnabled()).isTrue();
        }

        @Test
        @DisplayName("Should return correct username")
        void shouldReturnCorrectUsername() {
            // When & Then
            assertThat(userDetails1.getUsername()).isEqualTo("test@example.com");
            assertThat(userDetails2.getUsername()).isEqualTo("jane@example.com");
        }

        @Test
        @DisplayName("Should return correct password")
        void shouldReturnCorrectPassword() {
            // When & Then
            assertThat(userDetails1.getPassword()).isEqualTo("password123");
            assertThat(userDetails2.getPassword()).isEqualTo("password456");
        }

        @Test
        @DisplayName("Should return correct ID")
        void shouldReturnCorrectId() {
            // When & Then
            assertThat(userDetails1.getId()).isEqualTo(1L);
            assertThat(userDetails2.getId()).isEqualTo(2L);
        }

        @Test
        @DisplayName("Should return correct firstName")
        void shouldReturnCorrectFirstName() {
            // When & Then
            assertThat(userDetails1.getFirstName()).isEqualTo("John");
            assertThat(userDetails2.getFirstName()).isEqualTo("Jane");
        }

        @Test
        @DisplayName("Should return correct lastName")
        void shouldReturnCorrectLastName() {
            // When & Then
            assertThat(userDetails1.getLastName()).isEqualTo("Doe");
            assertThat(userDetails2.getLastName()).isEqualTo("Smith");
        }

        @Nested
        @DisplayName("equals() Tests")
        class EqualsTests {

            @Test
            @DisplayName("Should return true when comparing same instance")
            void shouldReturnTrueWhenComparingSameInstance() {
                // When & Then
                assertThat(userDetails1.equals(userDetails1)).isTrue();
                assertThat(userDetails2.equals(userDetails2)).isTrue();
            }

            @Test
            @DisplayName("Should return false when comparing with null")
            void shouldReturnFalseWhenComparingWithNull() {
                // When & Then
                assertThat(userDetails1.equals(null)).isFalse();
                assertThat(userDetails2.equals(null)).isFalse();
            }

            @Test
            @DisplayName("Should return false when comparing with different class")
            void shouldReturnFalseWhenComparingWithDifferentClass() {
                // Given
                String differentObject = "Not a UserDetailsImpl";

                // When & Then
                assertThat(userDetails1.equals(differentObject)).isFalse();
                assertThat(userDetails2.equals(differentObject)).isFalse();
            }

            @Test
            @DisplayName("Should return true when comparing UserDetailsImpl with same ID")
            void shouldReturnTrueWhenComparingUserDetailsImplWithSameId() {
                // Given
                UserDetailsImpl sameIdAsUserDetails1 = UserDetailsImpl.builder()
                        .id(1L) // Same ID as userDetails1
                        .username("different@email.com")
                        .firstName("Different")
                        .lastName("Name")
                        .password("differentPassword")
                        .build();

                // When & Then
                assertThat(userDetails1.equals(sameIdAsUserDetails1)).isTrue();
                assertThat(sameIdAsUserDetails1.equals(userDetails1)).isTrue();
            }

            @Test
            @DisplayName("Should return false when comparing UserDetailsImpl with different ID")
            void shouldReturnFalseWhenComparingUserDetailsImplWithDifferentId() {
                // When & Then
                assertThat(userDetails1.equals(userDetails2)).isFalse();
                assertThat(userDetails2.equals(userDetails1)).isFalse();
            }

            @Test
            @DisplayName("Should return true when both have null ID")
            void shouldReturnTrueWhenBothHaveNullId() {
                // Given
                UserDetailsImpl nullId1 = UserDetailsImpl.builder()
                        .id(null)
                        .username("test1@example.com")
                        .firstName("Test1")
                        .lastName("User1")
                        .password("password1")
                        .build();

                UserDetailsImpl nullId2 = UserDetailsImpl.builder()
                        .id(null)
                        .username("test2@example.com")
                        .firstName("Test2")
                        .lastName("User2")
                        .password("password2")
                        .build();

                // When & Then
                assertThat(nullId1.equals(nullId2)).isTrue();
                assertThat(nullId2.equals(nullId1)).isTrue();
            }

            @Test
            @DisplayName("Should return false when one has null ID and other doesn't")
            void shouldReturnFalseWhenOneHasNullIdAndOtherDoesnt() {
                // Given
                UserDetailsImpl nullIdUser = UserDetailsImpl.builder()
                        .id(null)
                        .username("test@example.com")
                        .firstName("Test")
                        .lastName("User")
                        .password("password")
                        .build();

                // When & Then
                assertThat(userDetails1.equals(nullIdUser)).isFalse();
                assertThat(nullIdUser.equals(userDetails1)).isFalse();
            }
        }

        @Nested
        @DisplayName("Builder Tests")
        class BuilderTests {

            @Test
            @DisplayName("Should create UserDetailsImpl with all fields using builder")
            void shouldCreateUserDetailsImplWithAllFieldsUsingBuilder() {
                // When
                UserDetailsImpl userDetails = UserDetailsImpl.builder()
                        .id(100L)
                        .username("builder@test.com")
                        .firstName("Builder")
                        .lastName("Test")
                        .password("builderPassword")
                        .build();

                // Then
                assertThat(userDetails).isNotNull();
                assertThat(userDetails.getId()).isEqualTo(100L);
                assertThat(userDetails.getUsername()).isEqualTo("builder@test.com");
                assertThat(userDetails.getFirstName()).isEqualTo("Builder");
                assertThat(userDetails.getLastName()).isEqualTo("Test");
                assertThat(userDetails.getPassword()).isEqualTo("builderPassword");
            }

            @Test
            @DisplayName("Should create UserDetailsImpl with null fields using builder")
            void shouldCreateUserDetailsImplWithNullFieldsUsingBuilder() {
                // When
                UserDetailsImpl userDetails = UserDetailsImpl.builder()
                        .id(null)
                        .username(null)
                        .firstName(null)
                        .lastName(null)
                        .password(null)
                        .build();

                // Then
                assertThat(userDetails).isNotNull();
                assertThat(userDetails.getId()).isNull();
                assertThat(userDetails.getUsername()).isNull();
                assertThat(userDetails.getFirstName()).isNull();
                assertThat(userDetails.getLastName()).isNull();
                assertThat(userDetails.getPassword()).isNull();
            }

            @Test
            @DisplayName("Should create UserDetailsImpl with partial fields using builder")
            void shouldCreateUserDetailsImplWithPartialFieldsUsingBuilder() {
                // When
                UserDetailsImpl userDetails = UserDetailsImpl.builder()
                        .id(50L)
                        .username("partial@test.com")
                        .password("partialPassword")
                        // firstName and lastName not set
                        .build();

                // Then
                assertThat(userDetails).isNotNull();
                assertThat(userDetails.getId()).isEqualTo(50L);
                assertThat(userDetails.getUsername()).isEqualTo("partial@test.com");
                assertThat(userDetails.getPassword()).isEqualTo("partialPassword");
                assertThat(userDetails.getFirstName()).isNull();
                assertThat(userDetails.getLastName()).isNull();
            }
        }

        @Nested
        @DisplayName("Integration with Spring Security")
        class SpringSecurityIntegrationTests {

            @Test
            @DisplayName("Should work as Spring Security UserDetails")
            void shouldWorkAsSpringSecurityUserDetails() {
                // Given
                UserDetails springUserDetails = userDetails1;

                // When & Then - Test all UserDetails interface methods
                assertThat(springUserDetails.getUsername()).isEqualTo("test@example.com");
                assertThat(springUserDetails.getPassword()).isEqualTo("password123");
                assertThat(springUserDetails.isAccountNonExpired()).isTrue();
                assertThat(springUserDetails.isAccountNonLocked()).isTrue();
                assertThat(springUserDetails.isCredentialsNonExpired()).isTrue();
                assertThat(springUserDetails.isEnabled()).isTrue();
                assertThat(springUserDetails.getAuthorities()).isNotNull();
                assertThat(springUserDetails.getAuthorities()).isEmpty(); // Assuming no authorities are set
            }

            @Test
            @DisplayName("Should be usable in Spring Security context")
            void shouldBeUsableInSpringSecurityContext() {
                // Given
                when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

                // When
                UserDetails loadedUserDetails = userDetailsService.loadUserByUsername("test@example.com");

                // Then
                assertThat(loadedUserDetails).isInstanceOf(UserDetailsImpl.class);
                assertThat(loadedUserDetails.getUsername()).isEqualTo("test@example.com");
                assertThat(loadedUserDetails.getPassword()).isEqualTo("encodedPassword123");
                assertThat(loadedUserDetails.isAccountNonExpired()).isTrue();
                assertThat(loadedUserDetails.isAccountNonLocked()).isTrue();
                assertThat(loadedUserDetails.isCredentialsNonExpired()).isTrue();
                assertThat(loadedUserDetails.isEnabled()).isTrue();
            }
        }
    }
}