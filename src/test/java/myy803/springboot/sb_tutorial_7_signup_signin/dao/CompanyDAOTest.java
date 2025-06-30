package myy803.springboot.sb_tutorial_7_signup_signin.dao;


import myy803.springboot.sb_tutorial_7_signup_signin.model.Company;
import myy803.springboot.sb_tutorial_7_signup_signin.model.Role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyDAOTest {

    @Autowired
    private CompanyDAO companyDAO;

    @Autowired
    private RoleDAO roleDAO; 

    private Role createDefaultRole() {
        Role role = new Role();
        role.setName("ROLE_COMPANY");
        return roleDAO.save(role);
    }

    @Test
    void testFindByUsername() {
        Role role = createDefaultRole();

        Company company = new Company();
        company.setUsername("companyX");
        company.setFullName("Company X");
        company.setPassword("1234");
        company.setEnabled(true);
        company.setRole(role); 

        companyDAO.save(company);

        Company found = companyDAO.findByUsername("companyX");
        assertNotNull(found);
        assertEquals("Company X", found.getFullName());
    }

    @Test
    void testExistsByUsername() {
        Role role = createDefaultRole();

        Company company = new Company();
        company.setUsername("companyY");
        company.setFullName("Company Y");
        company.setPassword("abc123");
        company.setEnabled(true);
        company.setRole(role); 

        companyDAO.save(company);

        assertTrue(companyDAO.existsByUsername("companyY"));
        assertFalse(companyDAO.existsByUsername("unknown"));
    }
}
