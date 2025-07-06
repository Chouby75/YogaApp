package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import com.openclassrooms.starterjwt.models.Teacher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    
    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;
    
    @Test
    public void testFindAllTeachers() {
        List<Teacher> fakeTeachers = new ArrayList<>();
        fakeTeachers.add(new Teacher());
        fakeTeachers.add(new Teacher());

        when(teacherRepository.findAll()).thenReturn(fakeTeachers);

        List<Teacher> result = teacherService.findAll();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testFindTeacherById() {
        Teacher fakeTeacher = new Teacher();
        fakeTeacher.setId(1L);
        fakeTeacher.setFirstName("Obi-Wan");
        fakeTeacher.setLastName("Kenobi");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(fakeTeacher));
        Teacher result = teacherService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Obi-Wan");
}};
