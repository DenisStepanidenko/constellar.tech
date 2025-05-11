package ru.denis.constellar.tech.employer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
import ru.denis.constellar.tech.employer.controller.EmployerController;
import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
import ru.denis.constellar.tech.employer.model.Employer;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployerControllerTest {

    @Mock
    private EmployerJpa employerJpa;

    @InjectMocks
    private EmployerController employerController;

    private Employer createFullTestEmployer() {
        return Employer.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .companyName("Test Company")
                .companyDescription("Test Description")
                .website("test.com")
                .industry("IT")
                .phoneNumber("1234567890")
                .address("Test Address")
                .inn("1234567890")
                .kpp("987654321")
                .contactPerson("John Doe")
                .contactPosition("HR Manager")
                .vacancies(Collections.emptyList())
                .build();
    }

    private Employer createEmptyTestEmployer() {
        return Employer.builder()
                .companyName("")
                .companyDescription("")
                .website("")
                .industry("")
                .phoneNumber("")
                .address("")
                .inn("")
                .kpp("")
                .contactPerson("")
                .contactPosition("")
                .build();
    }

    @Test
    void updateEmployer_UnauthorizedAccess_ThrowsException() {
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Employer updateEmployer = createEmptyTestEmployer();

        assertThrows(UnauthorizedAccessException.class, () -> {
            employerController.updateEmployer(updateEmployer, session, response);
        });
    }

    @Test
    void updateEmployer_ValidUpdate_UpdatesAllFieldsAndRedirects() throws IOException {
        // Arrange
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Employer existingEmployer = createFullTestEmployer();
        session.setAttribute("employer", existingEmployer);

        Employer updateEmployer = createFullTestEmployer();
        updateEmployer.setCompanyName("Updated Company");
        updateEmployer.setCompanyDescription("Updated Description");
        updateEmployer.setWebsite("updated.com");
        updateEmployer.setIndustry("Finance");
        updateEmployer.setPhoneNumber("0987654321");
        updateEmployer.setAddress("Updated Address");
        updateEmployer.setInn("0987654321");
        updateEmployer.setKpp("123456789");
        updateEmployer.setContactPerson("Jane Smith");
        updateEmployer.setContactPosition("Recruitment Lead");

        when(employerJpa.save(any(Employer.class))).thenReturn(existingEmployer);
        when(employerJpa.findById(1L)).thenReturn(Optional.of(existingEmployer));

        // Act
        employerController.updateEmployer(updateEmployer, session, response);

        // Assert
        assertEquals("Updated Company", existingEmployer.getCompanyName());
        assertEquals("Updated Description", existingEmployer.getCompanyDescription());
        assertEquals("updated.com", existingEmployer.getWebsite());
        assertEquals("Finance", existingEmployer.getIndustry());
        assertEquals("0987654321", existingEmployer.getPhoneNumber());
        assertEquals("Updated Address", existingEmployer.getAddress());
        assertEquals("0987654321", existingEmployer.getInn());
        assertEquals("987654321", existingEmployer.getKpp());
        assertEquals("Jane Smith", existingEmployer.getContactPerson());
        assertEquals("Recruitment Lead", existingEmployer.getContactPosition());

        verify(employerJpa, times(1)).save(existingEmployer);
        verify(employerJpa, times(1)).flush();
        verify(employerJpa, times(1)).findById(1L);

        assertEquals("http://localhost:8080/company-personal-account-home", response.getRedirectedUrl());
    }

    @Test
    void updateEmployer_BlankFields_DoesNotUpdateOriginalValues() throws IOException {
        // Arrange
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Employer existingEmployer = createFullTestEmployer();
        session.setAttribute("employer", existingEmployer);

        Employer updateEmployer = createEmptyTestEmployer();

        when(employerJpa.save(any(Employer.class))).thenReturn(existingEmployer);
        when(employerJpa.findById(1L)).thenReturn(Optional.of(existingEmployer));

        // Act
        employerController.updateEmployer(updateEmployer, session, response);

        // Assert
        assertEquals("Test Company", existingEmployer.getCompanyName());
        assertEquals("Test Description", existingEmployer.getCompanyDescription());
        assertEquals("test.com", existingEmployer.getWebsite());
        assertEquals("IT", existingEmployer.getIndustry());
        assertEquals("1234567890", existingEmployer.getPhoneNumber());
        assertEquals("Test Address", existingEmployer.getAddress());
        assertEquals("1234567890", existingEmployer.getInn());
        assertEquals("987654321", existingEmployer.getKpp());
        assertEquals("John Doe", existingEmployer.getContactPerson());
        assertEquals("HR Manager", existingEmployer.getContactPosition());
    }

    @Test
    void updateEmployer_PartialUpdate_UpdatesOnlyProvidedFields() throws IOException {
        // Arrange
        MockHttpSession session = new MockHttpSession();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Employer existingEmployer = createFullTestEmployer();
        session.setAttribute("employer", existingEmployer);

        Employer updateEmployer = createFullTestEmployer();
        updateEmployer.setCompanyName("New Company Name");
        updateEmployer.setPhoneNumber("5555555555");
        updateEmployer.setContactPerson("New Contact");
        // Остальные поля остаются как в createFullTestEmployer()

        when(employerJpa.save(any(Employer.class))).thenReturn(existingEmployer);
        when(employerJpa.findById(1L)).thenReturn(Optional.of(existingEmployer));

        // Act
        employerController.updateEmployer(updateEmployer, session, response);

        // Assert
        assertEquals("New Company Name", existingEmployer.getCompanyName());
        assertEquals("5555555555", existingEmployer.getPhoneNumber());
        assertEquals("New Contact", existingEmployer.getContactPerson());

        // Проверяем, что остальные поля не изменились
        assertEquals("Test Description", existingEmployer.getCompanyDescription());
        assertEquals("test.com", existingEmployer.getWebsite());
        assertEquals("IT", existingEmployer.getIndustry());
        assertEquals("Test Address", existingEmployer.getAddress());
        assertEquals("1234567890", existingEmployer.getInn());
        assertEquals("987654321", existingEmployer.getKpp());
        assertEquals("HR Manager", existingEmployer.getContactPosition());
    }
}