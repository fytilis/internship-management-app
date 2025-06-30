package myy803.springboot.sb_tutorial_7_signup_signin.service;
import myy803.springboot.sb_tutorial_7_signup_signin.dao.LogbookDAO;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Logbook;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LogbookServiceImplTest {
	@Mock
    private LogbookDAO logbookRepository;

    @InjectMocks
    private LogbookServiceImpl logbookService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Logbook log = new Logbook();
        logbookService.save(log);
        verify(logbookRepository).save(log);
    }

    @Test
    void testGetById_Found() {
        Logbook log = new Logbook();
        when(logbookRepository.findById(1L)).thenReturn(Optional.of(log));
        Logbook result = logbookService.getById(1L);
        assertEquals(log, result);
    }

    @Test
    void testGetById_NotFound() {
        when(logbookRepository.findById(99L)).thenReturn(Optional.empty());
        Logbook result = logbookService.getById(99L);
        assertNull(result);
    }

    @Test
    void testDeleteById() {
        logbookService.deleteById(5L);
        verify(logbookRepository).deleteById(5L);
    }

    @Test
    void testGetLogbooksForStudent() {
        Student student = new Student();
        when(logbookRepository.findByStudent(student)).thenReturn(List.of(new Logbook()));
        List<Logbook> result = logbookService.getLogbooksForStudent(student);
        assertEquals(1, result.size());
    }

    @Test
    void testGetEntriesByStudent() {
        Student student = new Student();
        when(logbookRepository.findByStudent(student)).thenReturn(List.of(new Logbook()));
        List<Logbook> result = logbookService.getEntriesByStudent(student);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByStudent() {
        Student student = new Student();
        when(logbookRepository.findByStudent(student)).thenReturn(List.of(new Logbook()));
        List<Logbook> result = logbookService.findByStudent(student);
        assertEquals(1, result.size());
    }

    @Test
    void testSaveEntry_SetsStudentAndDate() {
        Student student = new Student();
        Logbook entry = new Logbook();

        logbookService.saveEntry(entry, student);

        assertEquals(student, entry.getStudent());
        assertNotNull(entry.getDate());
        verify(logbookRepository).save(entry);
    }
}
