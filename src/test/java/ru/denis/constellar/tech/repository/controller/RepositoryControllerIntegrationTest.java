//package ru.denis.constellar.tech.repository.controller;
//
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import ru.denis.constellar.tech.candidate.model.Candidate;
//import ru.denis.constellar.tech.repository.model.Repository;
//import ru.denis.constellar.tech.repository.jpa.RepositoryJpa;
//import ru.denis.constellar.tech.repositoryFile.model.RepositoryFile;
//import ru.denis.constellar.tech.repositoryFile.jpa.RepositoryFileJpa;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Ignore
//class RepositoryControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RepositoryJpa repositoryJpa;
//    @MockBean
//    private RepositoryFileJpa repositoryFileJpa;
//
//    private Candidate testCandidate;
//    private Repository testRepository;
//    private RepositoryFile testFile;
//    private MockHttpSession session;
//
//    @BeforeEach
//    void setUp() {
//        testCandidate = new Candidate();
//        testCandidate.setId(1L);
//        testCandidate.setUsername("testuser");
//
//        testRepository = new Repository();
//        testRepository.setId(1L);
//        testRepository.setName("Test Repo");
//        testRepository.setCandidate(testCandidate);
//
//        testFile = new RepositoryFile();
//        testFile.setId(1L);
//        testFile.setFileName("test.txt");
//        testFile.setContent("test content".getBytes());
//        testFile.setFileType("text/plain");
//        testFile.setRepository(testRepository);
//
//        testRepository.setFiles(List.of(testFile));
//        testCandidate.setRepositories(List.of(testRepository));
//
//        session = new MockHttpSession();
//        session.setAttribute("candidate", testCandidate);
//    }
//
//    @Test
//    void getAllRepositoryFiles_IntegrationTest() throws Exception {
//        // Arrange
//        when(repositoryJpa.findById(1L)).thenReturn(Optional.of(testRepository));
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/constellar.tech/api/v1/repository/1/files")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].fileName").value("test.txt"));
//    }
//
//    @Test
//    void addRepository_IntegrationTest() throws Exception {
//        // Arrange
//        MockMultipartFile file = new MockMultipartFile(
//                "files",
//                "test.txt",
//                "text/plain",
//                "content".getBytes()
//        );
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/constellar.tech/api/v1/repository/add")
//                        .file(file)
//                        .param("name", "New Repo")
//                        .param("primaryLanguage", "Java")
//                        .param("description", "Description")
//                        .session(session))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("http://localhost:8080/candidate-repository-list"));
//    }
//
//    @Test
//    void downloadPdf_IntegrationTest() throws Exception {
//        // Arrange
//        when(repositoryFileJpa.findById(1L)).thenReturn(Optional.of(testFile));
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/constellar.tech/api/v1/repository/download-file/1")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\"; filename*=UTF-8''test.txt"))
//                .andExpect(content().bytes("test content".getBytes()));
//    }
//}