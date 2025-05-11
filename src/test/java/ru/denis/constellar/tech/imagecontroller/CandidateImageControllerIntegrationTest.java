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
//import ru.denis.constellar.tech.candidate.model.Candidate;
//import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertArrayEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Ignore
//class CandidateImageControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CandidateRepository candidateRepository;
//
//    private Candidate testCandidate;
//    private MockHttpSession session;
//
//    @BeforeEach
//    void setUp() {
//        testCandidate = new Candidate();
//        testCandidate.setId(1L);
//        testCandidate.setAvatar(new byte[]{1, 2, 3});
//        testCandidate.setAvatarMimeType("image/png");
//
//        session = new MockHttpSession();
//    }
//
//    @Test
//    void getCandidateAvatar_IntegrationTest() throws Exception {
//        // Arrange
//        when(candidateRepository.findById(1L)).thenReturn(Optional.of(testCandidate));
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/image/candidate/getAvatar/1"))
//                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "image/png"))
//                .andExpect(content().bytes(new byte[]{1, 2, 3}));
//    }
//
//    @Test
//    void uploadCandidateAvatar_IntegrationTest() throws Exception {
//        // Arrange
//        session.setAttribute("candidate", testCandidate);
//        MockMultipartFile file = new MockMultipartFile(
//                "avatarFile",
//                "test.png",
//                "image/png",
//                new byte[]{4, 5, 6}
//        );
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/image/candidate/upload-avatar")
//                        .file(file)
//                        .session(session))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("http://localhost:8080/candidate-personal-account"));
//
//        assertArrayEquals(new byte[]{4, 5, 6}, testCandidate.getAvatar());
//    }
//}