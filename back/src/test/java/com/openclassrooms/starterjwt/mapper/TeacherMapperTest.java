package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TeacherMapperTest {

    @InjectMocks
    private TeacherMapperImpl teacherMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;
    private LocalDateTime testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDateTime.of(2025, 7, 11, 10, 30, 0);
        
        // Setup Teacher entity
        teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(testDate)
                .updatedAt(testDate)
                .build();

        // Setup TeacherDto
        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(testDate);
        teacherDto.setUpdatedAt(testDate);
    }

    @Test
    void shouldMapTeacherDtoToEntity() {
        // When
        Teacher result = teacherMapper.toEntity(teacherDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(teacherDto.getId());
        assertThat(result.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(teacherDto.getLastName());
        assertThat(result.getCreatedAt()).isEqualTo(teacherDto.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(teacherDto.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherEntityToDto() {
        // When
        TeacherDto result = teacherMapper.toDto(teacher);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(teacher.getId());
        assertThat(result.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(result.getLastName()).isEqualTo(teacher.getLastName());
        assertThat(result.getCreatedAt()).isEqualTo(teacher.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(teacher.getUpdatedAt());
    }

    @Test
    void shouldReturnNullWhenMappingNullDtoToEntity() {
        // When
        Teacher result = teacherMapper.toEntity((TeacherDto) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullWhenMappingNullEntityToDto() {
        // When
        TeacherDto result = teacherMapper.toDto((Teacher) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldMapTeacherDtoListToEntityList() {
        // Given
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setCreatedAt(testDate);
        dto1.setUpdatedAt(testDate);

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");
        dto2.setCreatedAt(testDate);
        dto2.setUpdatedAt(testDate);

        List<TeacherDto> dtoList = Arrays.asList(dto1, dto2);

        // When
        List<Teacher> result = teacherMapper.toEntity(dtoList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        Teacher teacher1 = result.get(0);
        assertThat(teacher1.getId()).isEqualTo(dto1.getId());
        assertThat(teacher1.getFirstName()).isEqualTo(dto1.getFirstName());
        assertThat(teacher1.getLastName()).isEqualTo(dto1.getLastName());
        assertThat(teacher1.getCreatedAt()).isEqualTo(dto1.getCreatedAt());
        assertThat(teacher1.getUpdatedAt()).isEqualTo(dto1.getUpdatedAt());

        Teacher teacher2 = result.get(1);
        assertThat(teacher2.getId()).isEqualTo(dto2.getId());
        assertThat(teacher2.getFirstName()).isEqualTo(dto2.getFirstName());
        assertThat(teacher2.getLastName()).isEqualTo(dto2.getLastName());
        assertThat(teacher2.getCreatedAt()).isEqualTo(dto2.getCreatedAt());
        assertThat(teacher2.getUpdatedAt()).isEqualTo(dto2.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherEntityListToDtoList() {
        // Given
        Teacher entity1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(testDate)
                .updatedAt(testDate)
                .build();

        Teacher entity2 = Teacher.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(testDate)
                .updatedAt(testDate)
                .build();

        List<Teacher> entityList = Arrays.asList(entity1, entity2);

        // When
        List<TeacherDto> result = teacherMapper.toDto(entityList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        TeacherDto dto1 = result.get(0);
        assertThat(dto1.getId()).isEqualTo(entity1.getId());
        assertThat(dto1.getFirstName()).isEqualTo(entity1.getFirstName());
        assertThat(dto1.getLastName()).isEqualTo(entity1.getLastName());
        assertThat(dto1.getCreatedAt()).isEqualTo(entity1.getCreatedAt());
        assertThat(dto1.getUpdatedAt()).isEqualTo(entity1.getUpdatedAt());

        TeacherDto dto2 = result.get(1);
        assertThat(dto2.getId()).isEqualTo(entity2.getId());
        assertThat(dto2.getFirstName()).isEqualTo(entity2.getFirstName());
        assertThat(dto2.getLastName()).isEqualTo(entity2.getLastName());
        assertThat(dto2.getCreatedAt()).isEqualTo(entity2.getCreatedAt());
        assertThat(dto2.getUpdatedAt()).isEqualTo(entity2.getUpdatedAt());
    }

    @Test
    void shouldReturnNullWhenMappingNullDtoListToEntityList() {
        // When
        List<Teacher> result = teacherMapper.toEntity((List<TeacherDto>) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnNullWhenMappingNullEntityListToDtoList() {
        // When
        List<TeacherDto> result = teacherMapper.toDto((List<Teacher>) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldMapEmptyDtoListToEmptyEntityList() {
        // Given
        List<TeacherDto> emptyDtoList = Arrays.asList();

        // When
        List<Teacher> result = teacherMapper.toEntity(emptyDtoList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMapEmptyEntityListToEmptyDtoList() {
        // Given
        List<Teacher> emptyEntityList = Arrays.asList();

        // When
        List<TeacherDto> result = teacherMapper.toDto(emptyEntityList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMapDtoWithNullFieldsToEntity() {
        // Given
        TeacherDto dtoWithNulls = new TeacherDto();
        dtoWithNulls.setId(1L);
        // firstName, lastName, createdAt, updatedAt are null

        // When
        Teacher result = teacherMapper.toEntity(dtoWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isNull();
        assertThat(result.getLastName()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    void shouldMapEntityWithNullFieldsToDto() {
        // Given
        Teacher entityWithNulls = Teacher.builder()
                .id(1L)
                // firstName, lastName, createdAt, updatedAt are null
                .build();

        // When
        TeacherDto result = teacherMapper.toDto(entityWithNulls);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isNull();
        assertThat(result.getLastName()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    void shouldMapDtoWithSpecialCharactersInNames() {
        // Given
        TeacherDto dtoWithSpecialChars = new TeacherDto();
        dtoWithSpecialChars.setId(1L);
        dtoWithSpecialChars.setFirstName("José");
        dtoWithSpecialChars.setLastName("García-López");
        dtoWithSpecialChars.setCreatedAt(testDate);
        dtoWithSpecialChars.setUpdatedAt(testDate);

        // When
        Teacher result = teacherMapper.toEntity(dtoWithSpecialChars);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("José");
        assertThat(result.getLastName()).isEqualTo("García-López");
    }

    @Test
    void shouldMapEntityWithSpecialCharactersInNames() {
        // Given
        Teacher entityWithSpecialChars = Teacher.builder()
                .id(1L)
                .firstName("José")
                .lastName("García-López")
                .createdAt(testDate)
                .updatedAt(testDate)
                .build();

        // When
        TeacherDto result = teacherMapper.toDto(entityWithSpecialChars);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("José");
        assertThat(result.getLastName()).isEqualTo("García-López");
    }
}