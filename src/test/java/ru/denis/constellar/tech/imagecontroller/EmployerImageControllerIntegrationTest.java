//package ru.denis.constellar.tech.imagecontroller;
//
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import ru.denis.constellar.tech.employer.model.Employer;
//import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Ignore
//class EmployerImageControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private EmployerJpa employerJpa;
//
//    private Employer testEmployer;
//    private MockHttpSession session;
//
//    @BeforeEach
//    void setUp() {
//        testEmployer = new Employer();
//        testEmployer.setId(1L);
//        testEmployer.setAvatar(new byte[]{1, 2, 3});
//        testEmployer.setAvatarType("image/png");
//
//        session = new MockHttpSession();
//    }
//
//    @Test
//    void getEmployerAvatar_IntegrationTest() throws Exception {
//        // Arrange
//        session.setAttribute("employer", testEmployer);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/image/employer/getAvatar")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "image/png"))
//                .andExpect(content().bytes(new byte[]{1, 2, 3}));
//    }
//
//    @Test
//    void uploadEmployerLogo_IntegrationTest() throws Exception {
//        // Arrange
//        session.setAttribute("employer", testEmployer);
//        MockMultipartFile file = new MockMultipartFile(
//                "logoFile",
//                "logo.png",
//                "image/png",
//                new byte[]{4, 5, 6}
//        );
//        when(employerJpa.findById(1L)).thenReturn(Optional.of(testEmployer));
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/employer/upload-logo")
//                        .file(file)
//                        .session(session))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("http://localhost:8080/company-personal-account-home"));
//    }
//}