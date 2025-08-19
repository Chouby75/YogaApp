package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeacherController Tests")
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher1;
    private Teacher teacher2;
    private TeacherDto teacherDto1;
    private TeacherDto teacherDto2;

    @BeforeEach
    void setUp() {
        // Setup test data
        teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacher2 = Teacher.builder()
                .id(2L)
                .lastName("Smith")
                .firstName("Jane")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setLastName("Doe");
        teacherDto1.setFirstName("John");
        teacherDto1.setCreatedAt(LocalDateTime.now());
        teacherDto1.setUpdatedAt(LocalDateTime.now());

        teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setLastName("Smith");
        teacherDto2.setFirstName("Jane");
        teacherDto2.setCreatedAt(LocalDateTime.now());
        teacherDto2.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should return teacher when valid ID is provided")
        void shouldReturnTeacherWhenValidIdProvided() {
            // Given
            String teacherId = "1";
            when(teacherService.findById(1L)).thenReturn(teacher1);
            when(teacherMapper.toDto(teacher1)).thenReturn(teacherDto1);

            // When
            ResponseEntity<?> response = teacherController.findById(teacherId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(teacherDto1);
            verify(teacherService).findById(1L);
            verify(teacherMapper).toDto(teacher1);
        }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        @DisplayName("Should return all teachers when teachers exist")
        void shouldReturnAllTeachersWhenTeachersExist() {
            // Given
            List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
            List<TeacherDto> teacherDtos = Arrays.asList(teacherDto1, teacherDto2);
            when(teacherService.findAll()).thenReturn(teachers);
            when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

            // When
            ResponseEntity<?> response = teacherController.findAll();

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(teacherDtos);
            assertThat((List<?>) response.getBody()).hasSize(2);
            verify(teacherService).findAll();
            verify(teacherMapper).toDto(teachers);
        }

        @Test
        @DisplayName("Should return empty list when no teachers exist")
        void shouldReturnEmptyListWhenNoTeachersExist() {
            // Given
            List<Teacher> emptyTeachers = Collections.emptyList();
            List<TeacherDto> emptyTeacherDtos = Collections.emptyList();
            when(teacherService.findAll()).thenReturn(emptyTeachers);
            when(teacherMapper.toDto(emptyTeachers)).thenReturn(emptyTeacherDtos);

            // When
            ResponseEntity<?> response = teacherController.findAll();

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(emptyTeacherDtos);
            assertThat((List<?>) response.getBody()).isEmpty();
            verify(teacherService).findAll();
            verify(teacherMapper).toDto(emptyTeachers);
        }

        @Test
        @DisplayName("Should return single teacher in list")
        void shouldReturnSingleTeacherInList() {
            // Given
            List<Teacher> singleTeacher = Collections.singletonList(teacher1);
            List<TeacherDto> singleTeacherDto = Collections.singletonList(teacherDto1);
            when(teacherService.findAll()).thenReturn(singleTeacher);
            when(teacherMapper.toDto(singleTeacher)).thenReturn(singleTeacherDto);

            // When
            ResponseEntity<?> response = teacherController.findAll();

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(singleTeacherDto);
            assertThat((List<?>) response.getBody()).hasSize(1);
            verify(teacherService).findAll();
            verify(teacherMapper).toDto(singleTeacher);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create controller with required dependencies")
        void shouldCreateControllerWithRequiredDependencies() {
            // Given
            TeacherService mockService = mock(TeacherService.class);
            TeacherMapper mockMapper = mock(TeacherMapper.class);

            // When
            TeacherController controller = new TeacherController(mockService, mockMapper);

            // Then
            assertThat(controller).isNotNull();
            // Note: We can't directly test private fields without reflection,
            // but we can verify the constructor doesn't throw exceptions
        }
    }

    @Nested
    @DisplayName("Integration Behavior Tests")
    class IntegrationBehaviorTests {

        @Test
        @DisplayName("Should handle service layer exceptions gracefully")
        void shouldHandleServiceLayerExceptionsGracefully() {
            // Given
            String teacherId = "1";
            when(teacherService.findById(1L)).thenThrow(new RuntimeException("Database error"));

            // When & Then
            // The current implementation doesn't handle RuntimeExceptions,
            // so this would propagate up. In a real scenario, you might want
            // to add try-catch blocks for service exceptions
            org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> teacherController.findById(teacherId)
            );
        }

        @Test
        @DisplayName("Should handle mapper exceptions gracefully")
        void shouldHandleMapperExceptionsGracefully() {
            // Given
            String teacherId = "1";
            when(teacherService.findById(1L)).thenReturn(teacher1);
            when(teacherMapper.toDto(teacher1)).thenThrow(new RuntimeException("Mapping error"));

            // When & Then
            org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> teacherController.findById(teacherId)
            );
        }
    }
    }
};