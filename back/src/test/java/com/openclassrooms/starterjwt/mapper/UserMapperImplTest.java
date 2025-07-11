package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserMapperImplTest {

    private UserMapperImpl userMapper;
    private User user;
    private UserDto userDto;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapperImpl();
        now = LocalDateTime.now();

        // Setup User entity
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .password("password123")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Setup UserDto
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john.doe@test.com");
        userDto.setPassword("password123");
        userDto.setAdmin(false);
        userDto.setCreatedAt(now);
        userDto.setUpdatedAt(now);
    }

    @Test
    public void testToDto_Success() {
        // When
        UserDto result = userMapper.toDto(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.isAdmin()).isEqualTo(user.isAdmin());
        assertThat(result.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    public void testToDto_WithAdminUser() {
        // Given
        user = User.builder()
                .id(2L)
                .firstName("Admin")
                .lastName("User")
                .email("admin@test.com")
                .password("adminpass")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        UserDto result = userMapper.toDto(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getFirstName()).isEqualTo("Admin");
        assertThat(result.getLastName()).isEqualTo("User");
        assertThat(result.getEmail()).isEqualTo("admin@test.com");
        assertThat(result.isAdmin()).isTrue();
    }

    @Test
    public void testToEntity_Success() {
        // When
        User result = userMapper.toEntity(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(result.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(result.isAdmin()).isEqualTo(userDto.isAdmin());
        assertThat(result.getCreatedAt()).isEqualTo(userDto.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(userDto.getUpdatedAt());
    }

    @Test
    public void testToEntity_WithNullDto() {
        // When
        User result = userMapper.toEntity((UserDto) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testToEntity_WithAdminDto() {
        // Given
        userDto.setId(2L);
        userDto.setFirstName("Admin");
        userDto.setLastName("User");
        userDto.setEmail("admin@test.com");
        userDto.setPassword("adminpass");
        userDto.setAdmin(true);

        // When
        User result = userMapper.toEntity(userDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getFirstName()).isEqualTo("Admin");
        assertThat(result.getLastName()).isEqualTo("User");
        assertThat(result.getEmail()).isEqualTo("admin@test.com");
        assertThat(result.isAdmin()).isTrue();
    }

    @Test
    public void testToDtoList_Success() {
        // Given
        User user2 = User.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@test.com")
                .password("password456")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<User> userList = Arrays.asList(user, user2);

        // When
        List<UserDto> result = userMapper.toDto(userList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        UserDto firstDto = result.get(0);
        assertThat(firstDto.getId()).isEqualTo(1L);
        assertThat(firstDto.getFirstName()).isEqualTo("John");
        assertThat(firstDto.getLastName()).isEqualTo("Doe");
        assertThat(firstDto.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(firstDto.isAdmin()).isFalse();
        
        UserDto secondDto = result.get(1);
        assertThat(secondDto.getId()).isEqualTo(2L);
        assertThat(secondDto.getFirstName()).isEqualTo("Jane");
        assertThat(secondDto.getLastName()).isEqualTo("Smith");
        assertThat(secondDto.getEmail()).isEqualTo("jane.smith@test.com");
        assertThat(secondDto.isAdmin()).isTrue();
    }

    @Test
    public void testToDtoList_WithNullList() {
        // When
        List<UserDto> result = userMapper.toDto((List<User>) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testToDtoList_WithEmptyList() {
        // When
        List<UserDto> result = userMapper.toDto(Collections.emptyList());

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void testToDtoList_WithNullElementInList() {
        // Given
        List<User> userList = Arrays.asList(user, null);

        // When
        List<UserDto> result = userMapper.toDto(userList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isNotNull();
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1)).isNull();
    }

    @Test
    public void testToEntityList_Success() {
        // Given
        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Smith");
        userDto2.setEmail("jane.smith@test.com");
        userDto2.setPassword("password456");
        userDto2.setAdmin(true);
        userDto2.setCreatedAt(now);
        userDto2.setUpdatedAt(now);

        List<UserDto> userDtoList = Arrays.asList(userDto, userDto2);

        // When
        List<User> result = userMapper.toEntity(userDtoList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        
        User firstUser = result.get(0);
        assertThat(firstUser.getId()).isEqualTo(1L);
        assertThat(firstUser.getFirstName()).isEqualTo("John");
        assertThat(firstUser.getLastName()).isEqualTo("Doe");
        assertThat(firstUser.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(firstUser.isAdmin()).isFalse();
        
        User secondUser = result.get(1);
        assertThat(secondUser.getId()).isEqualTo(2L);
        assertThat(secondUser.getFirstName()).isEqualTo("Jane");
        assertThat(secondUser.getLastName()).isEqualTo("Smith");
        assertThat(secondUser.getEmail()).isEqualTo("jane.smith@test.com");
        assertThat(secondUser.isAdmin()).isTrue();
    }

    @Test
    public void testToEntityList_WithNullList() {
        // When
        List<User> result = userMapper.toEntity((List<UserDto>) null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void testToEntityList_WithEmptyList() {
        // When
        List<User> result = userMapper.toEntity(Collections.emptyList());

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void testToEntityList_WithNullElementInList() {
        // Given
        List<UserDto> userDtoList = Arrays.asList(userDto, null);

        // When
        List<User> result = userMapper.toEntity(userDtoList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isNotNull();
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1)).isNull();
    }

    @Test
    public void testBidirectionalMapping_EntityToDto() {
        // When
        UserDto dto = userMapper.toDto(user);
        User entityFromDto = userMapper.toEntity(dto);

        // Then
        assertThat(entityFromDto).isNotNull();
        assertThat(entityFromDto.getId()).isEqualTo(user.getId());
        assertThat(entityFromDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(entityFromDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(entityFromDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(entityFromDto.getPassword()).isEqualTo(user.getPassword());
        assertThat(entityFromDto.isAdmin()).isEqualTo(user.isAdmin());
        assertThat(entityFromDto.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(entityFromDto.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    public void testBidirectionalMapping_DtoToEntity() {
        // When
        User entity = userMapper.toEntity(userDto);
        UserDto dtoFromEntity = userMapper.toDto(entity);

        // Then
        assertThat(dtoFromEntity).isNotNull();
        assertThat(dtoFromEntity.getId()).isEqualTo(userDto.getId());
        assertThat(dtoFromEntity.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(dtoFromEntity.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(dtoFromEntity.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(dtoFromEntity.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(dtoFromEntity.isAdmin()).isEqualTo(userDto.isAdmin());
        assertThat(dtoFromEntity.getCreatedAt()).isEqualTo(userDto.getCreatedAt());
        assertThat(dtoFromEntity.getUpdatedAt()).isEqualTo(userDto.getUpdatedAt());
    }

    @Test
    public void testBidirectionalMapping_Lists() {
        // Given
        List<User> userList = Arrays.asList(user);

        // When
        List<UserDto> dtoList = userMapper.toDto(userList);
        List<User> entityListFromDto = userMapper.toEntity(dtoList);

        // Then
        assertThat(entityListFromDto).isNotNull();
        assertThat(entityListFromDto).hasSize(1);
        
        User resultUser = entityListFromDto.get(0);
        assertThat(resultUser.getId()).isEqualTo(user.getId());
        assertThat(resultUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(resultUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(resultUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(resultUser.isAdmin()).isEqualTo(user.isAdmin());
    }

    @Test
    public void testMappingWithSpecialCharacters() {
        // Given
        user = User.builder()
                .id(1L)
                .firstName("José")
                .lastName("García-López")
                .email("jose.garcia+test@example.com")
                .password("pássw0rd!@#")
                .admin(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        UserDto result = userMapper.toDto(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("José");
        assertThat(result.getLastName()).isEqualTo("García-López");
        assertThat(result.getEmail()).isEqualTo("jose.garcia+test@example.com");
        assertThat(result.getPassword()).isEqualTo("pássw0rd!@#");
    }
}