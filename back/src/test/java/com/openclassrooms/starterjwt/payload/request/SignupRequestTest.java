package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SignupRequest Tests")
public class SignupRequestTest {

    private Validator validator;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get email correctly")
        void shouldSetAndGetEmailCorrectly() {
            // Given
            String email = "user@test.com";

            // When
            signupRequest.setEmail(email);

            // Then
            assertThat(signupRequest.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("Should set and get firstName correctly")
        void shouldSetAndGetFirstNameCorrectly() {
            // Given
            String firstName = "Jane";

            // When
            signupRequest.setFirstName(firstName);

            // Then
            assertThat(signupRequest.getFirstName()).isEqualTo(firstName);
        }

        @Test
        @DisplayName("Should set and get lastName correctly")
        void shouldSetAndGetLastNameCorrectly() {
            // Given
            String lastName = "Smith";

            // When
            signupRequest.setLastName(lastName);

            // Then
            assertThat(signupRequest.getLastName()).isEqualTo(lastName);
        }

        @Test
        @DisplayName("Should set and get password correctly")
        void shouldSetAndGetPasswordCorrectly() {
            // Given
            String password = "newPassword123";

            // When
            signupRequest.setPassword(password);

            // Then
            assertThat(signupRequest.getPassword()).isEqualTo(password);
        }

        @Test
        @DisplayName("Should handle null values in setters")
        void shouldHandleNullValuesInSetters() {
            // When
            signupRequest.setEmail(null);
            signupRequest.setFirstName(null);
            signupRequest.setLastName(null);
            signupRequest.setPassword(null);

            // Then
            assertThat(signupRequest.getEmail()).isNull();
            assertThat(signupRequest.getFirstName()).isNull();
            assertThat(signupRequest.getLastName()).isNull();
            assertThat(signupRequest.getPassword()).isNull();
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create SignupRequest with default constructor")
        void shouldCreateSignupRequestWithDefaultConstructor() {
            // When
            SignupRequest request = new SignupRequest();

            // Then
            assertThat(request).isNotNull();
            assertThat(request.getEmail()).isNull();
            assertThat(request.getFirstName()).isNull();
            assertThat(request.getLastName()).isNull();
            assertThat(request.getPassword()).isNull();
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should pass validation with valid data")
        void shouldPassValidationWithValidData() {
            // When
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Nested
        @DisplayName("Email Validation Tests")
        class EmailValidationTests {

        @Nested
        @DisplayName("FirstName Validation Tests")
        class FirstNameValidationTests {

            @Test
            @DisplayName("Should pass validation with firstName at minimum length")
            void shouldPassValidationWithFirstNameAtMinimumLength() {
                // Given
                signupRequest.setFirstName("ABC"); // 3 characters

                // When
                Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

                // Then
                assertThat(violations).isEmpty();
            }

            @Test
            @DisplayName("Should pass validation with firstName at maximum length")
            void shouldPassValidationWithFirstNameAtMaximumLength() {
                // Given
                signupRequest.setFirstName("A".repeat(20)); // 20 characters

                // When
                Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

                // Then
                assertThat(violations).isEmpty();
            }
        }

        @Nested
        @DisplayName("LastName Validation Tests")
        class LastNameValidationTests {

            @Test
            @DisplayName("Should pass validation with lastName at minimum length")
            void shouldPassValidationWithLastNameAtMinimumLength() {
                // Given
                signupRequest.setLastName("ABC"); // 3 characters

                // When
                Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

                // Then
                assertThat(violations).isEmpty();
            }

            @Test
            @DisplayName("Should pass validation with lastName at maximum length")
            void shouldPassValidationWithLastNameAtMaximumLength() {
                // Given
                signupRequest.setLastName("A".repeat(20)); // 20 characters

                // When
                Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

                // Then
                assertThat(violations).isEmpty();
            }
        }

        @Nested
        @DisplayName("Password Validation Tests")
        class PasswordValidationTests {

            @Test
            @DisplayName("Should pass validation with password at minimum length")
            void shouldPassValidationWithPasswordAtMinimumLength() {
                // Given
                signupRequest.setPassword("123456"); // 6 characters

                // When
                Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

                // Then
                assertThat(violations).isEmpty();
            }

            @Test
            @DisplayName("Should pass validation with password at maximum length")
            void shouldPassValidationWithPasswordAtMaximumLength() {
                // Given
                signupRequest.setPassword("A".repeat(40)); // 40 characters

                // When
                Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

                // Then
                assertThat(violations).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("Lombok Generated Methods Tests")
    class LombokGeneratedMethodsTests {

        @Test
        @DisplayName("Should test equals method with same object")
        void shouldTestEqualsMethodWithSameObject() {
            // When & Then
            assertThat(signupRequest.equals(signupRequest)).isTrue();
        }

        @Test
        @DisplayName("Should test equals method with null")
        void shouldTestEqualsMethodWithNull() {
            // When & Then
            assertThat(signupRequest.equals(null)).isFalse();
        }

        @Test
        @DisplayName("Should test equals method with different class")
        void shouldTestEqualsMethodWithDifferentClass() {
            // Given
            String differentObject = "Not a SignupRequest";

            // When & Then
            assertThat(signupRequest.equals(differentObject)).isFalse();
        }

        @Test
        @DisplayName("Should test equals method with identical objects")
        void shouldTestEqualsMethodWithIdenticalObjects() {
            // Given
            SignupRequest other = new SignupRequest();
            other.setEmail("test@example.com");
            other.setFirstName("John");
            other.setLastName("Doe");
            other.setPassword("password123");

            // When & Then
            assertThat(signupRequest.equals(other)).isTrue();
            assertThat(other.equals(signupRequest)).isTrue();
        }

        @Test
        @DisplayName("Should test equals method with different email")
        void shouldTestEqualsMethodWithDifferentEmail() {
            // Given
            SignupRequest other = new SignupRequest();
            other.setEmail("different@example.com");
            other.setFirstName("John");
            other.setLastName("Doe");
            other.setPassword("password123");

            // When & Then
            assertThat(signupRequest.equals(other)).isFalse();
        }

        @Test
        @DisplayName("Should test equals method with different firstName")
        void shouldTestEqualsMethodWithDifferentFirstName() {
            // Given
            SignupRequest other = new SignupRequest();
            other.setEmail("test@example.com");
            other.setFirstName("Jane");
            other.setLastName("Doe");
            other.setPassword("password123");

            // When & Then
            assertThat(signupRequest.equals(other)).isFalse();
        }

        @Test
        @DisplayName("Should test equals method with different lastName")
        void shouldTestEqualsMethodWithDifferentLastName() {
            // Given
            SignupRequest other = new SignupRequest();
            other.setEmail("test@example.com");
            other.setFirstName("John");
            other.setLastName("Smith");
            other.setPassword("password123");

            // When & Then
            assertThat(signupRequest.equals(other)).isFalse();
        }

        @Test
        @DisplayName("Should test equals method with different password")
        void shouldTestEqualsMethodWithDifferentPassword() {
            // Given
            SignupRequest other = new SignupRequest();
            other.setEmail("test@example.com");
            other.setFirstName("John");
            other.setLastName("Doe");
            other.setPassword("differentPassword");

            // When & Then
            assertThat(signupRequest.equals(other)).isFalse();
        }

        @Test
        @DisplayName("Should test equals method with all null fields")
        void shouldTestEqualsMethodWithAllNullFields() {
            // Given
            SignupRequest request1 = new SignupRequest();
            SignupRequest request2 = new SignupRequest();

            // When & Then
            assertThat(request1.equals(request2)).isTrue();
            assertThat(request2.equals(request1)).isTrue();
        }

        @Test
        @DisplayName("Should test hashCode consistency")
        void shouldTestHashCodeConsistency() {
            // Given
            SignupRequest other = new SignupRequest();
            other.setEmail("test@example.com");
            other.setFirstName("John");
            other.setLastName("Doe");
            other.setPassword("password123");

            // When & Then
            assertThat(signupRequest.hashCode()).isEqualTo(other.hashCode());
            
            // Multiple calls should return same hash code
            int hashCode1 = signupRequest.hashCode();
            int hashCode2 = signupRequest.hashCode();
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        @DisplayName("Should test hashCode with different objects")
        void shouldTestHashCodeWithDifferentObjects() {
            // Given
            SignupRequest other = new SignupRequest();
            other.setEmail("different@example.com");
            other.setFirstName("Jane");
            other.setLastName("Smith");
            other.setPassword("differentPassword");

            // When & Then
            assertThat(signupRequest.hashCode()).isNotEqualTo(other.hashCode());
        }

        @Test
        @DisplayName("Should test toString method")
        void shouldTestToStringMethod() {
            // When
            String toString = signupRequest.toString();

            // Then
            assertThat(toString).isNotNull();
            assertThat(toString).contains("SignupRequest");
            assertThat(toString).contains("test@example.com");
            assertThat(toString).contains("John");
            assertThat(toString).contains("Doe");
            assertThat(toString).contains("password123");
        }

        @Test
        @DisplayName("Should test toString method with null fields")
        void shouldTestToStringMethodWithNullFields() {
            // Given
            SignupRequest nullRequest = new SignupRequest();

            // When
            String toString = nullRequest.toString();

            // Then
            assertThat(toString).isNotNull();
            assertThat(toString).contains("SignupRequest");
            assertThat(toString).contains("null");
        }

        @Test
        @DisplayName("Should test canEqual method")
        void shouldTestCanEqualMethod() {
            // Given
            SignupRequest other = new SignupRequest();

            // When & Then
            assertThat(signupRequest.canEqual(other)).isTrue();
            assertThat(signupRequest.canEqual(signupRequest)).isTrue();
            assertThat(signupRequest.canEqual("not a SignupRequest")).isFalse();
            assertThat(signupRequest.canEqual(null)).isFalse();
        }
    }
}}