package ru.denis.constellar.tech.repository.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;
import ru.denis.constellar.tech.repository.jpa.RepositoryJpa;
import ru.denis.constellar.tech.repository.model.Repository;
import ru.denis.constellar.tech.repositoryFile.jpa.RepositoryFileJpa;
import ru.denis.constellar.tech.repositoryFile.model.RepositoryFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryControllerTest {

    @Mock
    private CandidateService candidateService;
    @Mock
    private RepositoryJpa repositoryJpa;
    @Mock
    private RepositoryFileJpa repositoryFileJpa;
    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private RepositoryController repositoryController;

    private Candidate testCandidate;
    private Repository testRepository;
    private RepositoryFile testFile;
    private MockHttpSession session;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        testCandidate = new Candidate();
        testCandidate.setId(1L);
        testCandidate.setUsername("testuser");

        testRepository = new Repository();
        testRepository.setId(1L);
        testRepository.setName("Test Repo");
        testRepository.setCandidate(testCandidate);

        testFile = new RepositoryFile();
        testFile.setId(1L);
        testFile.setFileName("test.txt");
        testFile.setContent("test content".getBytes());
        testFile.setFileType("text/plain");
        testFile.setRepository(testRepository);

        testRepository.setFiles(new ArrayList<>(List.of(testFile)));
        testCandidate.setRepositories(new ArrayList<>(List.of(testRepository)));

        session = new MockHttpSession();
        response = new MockHttpServletResponse();
    }

    @Test
    void getAllRepositoryFiles_ValidRepoId_ReturnsFiles() {
        // Arrange
        when(repositoryJpa.findById(1L)).thenReturn(Optional.of(testRepository));

        // Act
        List<RepositoryFile> result = repositoryController.getAllRepositoryFiles(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("test.txt", result.get(0).getFileName());
    }

    @Test
    void getFileContent_ValidFile_ReturnsContent() {
        // Arrange
        when(repositoryFileJpa.findById(1L)).thenReturn(Optional.of(testFile));

        // Act
        ResponseEntity<byte[]> responseEntity = repositoryController.getFileContent(1L, 1L, session);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("text/plain", responseEntity.getHeaders().getContentType().toString());
        assertArrayEquals("test content".getBytes(), responseEntity.getBody());
    }

    @Test
    void addRepository_Authorized_AddsRepositoryAndRedirects() throws IOException {
        // Arrange
        session.setAttribute("candidate", testCandidate);
        MultipartFile[] files = {
                new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes())
        };
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(testCandidate));

        // Act
        repositoryController.addRepository("New Repo", "Java", "Description", files, session, response);

        // Assert
        verify(repositoryJpa, times(1)).save(any(Repository.class));
        assertEquals("http://localhost:8080/candidate-repository-list", response.getRedirectedUrl());
    }

    @Test
    void getRepository_Authorized_AddsAttributesAndRedirects() throws IOException {
        // Arrange
        session.setAttribute("candidate", testCandidate);
        when(repositoryJpa.findById(1L)).thenReturn(Optional.of(testRepository));

        // Act
        repositoryController.getRepository(1L, 1L, session, response);

        // Assert
        assertEquals(testRepository, session.getAttribute("repository"));
        assertEquals("text", session.getAttribute("contentType"));
        assertEquals("test content", session.getAttribute("fileContent"));
        assertEquals(testFile, session.getAttribute("selectedFile"));
        assertEquals("http://localhost:8080/candidate-repository-page", response.getRedirectedUrl());
    }

    @Test
    void viewPdf_ValidFile_SetsResponseHeaders() throws IOException {
        // Arrange
        RepositoryFile pdfFile = new RepositoryFile();
        pdfFile.setFileName("test.pdf");
        pdfFile.setContent("pdf content".getBytes());
        pdfFile.setFileType("application/pdf");
        when(repositoryFileJpa.findById(1L)).thenReturn(Optional.of(pdfFile));

        // Act
        repositoryController.viewPdf(1L, response);

        // Assert
        assertEquals("application/pdf", response.getContentType());
        assertTrue(response.getHeader("Content-Disposition").contains("inline"));
    }



    @Test
    void deleteFile_Authorized_DeletesFile() throws IOException {
        // Arrange
        session.setAttribute("candidate", testCandidate);
        when(repositoryJpa.findById(1L)).thenReturn(Optional.of(testRepository));

        // Act
        ResponseEntity<?> responseEntity = repositoryController.deleteFile(1L, 1L, session, response);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(repositoryFileJpa, times(1)).deleteById(1L);
    }
}