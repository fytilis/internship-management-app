package myy803.springboot.sb_tutorial_7_signup_signin.dao;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommitteeDAOTest {

    @Test
    void testExistsByUsername() {
        CommitteeDAO committeeDAO = mock(CommitteeDAO.class);
        when(committeeDAO.existsByUsername("john")).thenReturn(true);

        assertTrue(committeeDAO.existsByUsername("john"));
        verify(committeeDAO).existsByUsername("john");
    }
}