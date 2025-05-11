package ru.denis.constellar.tech.auth.employer.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.denis.constellar.tech.auth.employer.dto.CompanyRegistrationRequest;
import ru.denis.constellar.tech.auth.employer.dto.RequestCompanyLogin;
import ru.denis.constellar.tech.auth.employer.service.CompanyAuthService;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AuthEmployerControllerImplTest {

    @Mock
    private CompanyAuthService companyAuthService;

    @InjectMocks
    private AuthEmployerControllerImpl authEmployerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authEmployerController).build();
    }

    @Test
    void login_ShouldCallServiceAndRedirect() throws Exception {
        RequestCompanyLogin request = new RequestCompanyLogin("test@example.com", "password");

        mockMvc.perform(post("/constellar.tech/api/v1/auth/employer/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(companyAuthService).loginCompany(any(RequestCompanyLogin.class), any(HttpSession.class), any(HttpServletResponse.class));
    }


    @Test
    void logout_WithEmployerSession_ShouldRemoveAttributeAndRedirect() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employer", new Object());

        MockHttpServletResponse response = mockMvc.perform(post("/constellar.tech/api/v1/auth/employer/logout")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse();


        assertTrue(response.getRedirectedUrl().contains("http://localhost:8080/home"));
    }

    @Test
    void logout_NoSession_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/constellar.tech/api/v1/auth/employer/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8080/home"));
    }
}