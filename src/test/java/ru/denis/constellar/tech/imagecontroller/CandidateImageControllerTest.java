package ru.denis.constellar.tech.imagecontroller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
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

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateImageControllerTest {

    @Mock
    private CandidateService candidateService;

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateImageController candidateImageController;

    private Candidate testCandidate;
    private MockHttpSession session;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        testCandidate = new Candidate();
        testCandidate.setId(1L);
        testCandidate.setAvatar(new byte[]{1, 2, 3});
        testCandidate.setAvatarMimeType("image/png");

        session = new MockHttpSession();
        response = new MockHttpServletResponse();
    }

    @Test
    void getCandidateAvatar_ValidId_ReturnsAvatar() throws IOException {
        // Arrange
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(testCandidate));

        // Act
        ResponseEntity<byte[]> responseEntity = candidateImageController.getCandidateAvatar(1L, session);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertArrayEquals(new byte[]{1, 2, 3}, responseEntity.getBody());
        assertEquals(MediaType.valueOf("image/png"), responseEntity.getHeaders().getContentType());
    }

    @Test
    void getCandidateAvatar_InvalidId_ThrowsException() {
        // Arrange
        when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            candidateImageController.getCandidateAvatar(99L, session);
        });
    }

    @Test
    void uploadCandidateAvatar_Unauthorized_RedirectsToHome() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile(
                "avatarFile",
                "test.png",
                "image/png",
                new byte[]{1, 2, 3}
        );

        // Act
        candidateImageController.uploadCandidateAvatar(file, null, response);

        // Assert
        assertEquals("http://localhost:8080/home", response.getRedirectedUrl());
    }

    @Test
    void uploadCandidateAvatar_Authorized_UpdatesAvatarAndRedirects() throws IOException {
        // Arrange
        session.setAttribute("candidate", testCandidate);
        MultipartFile file = new MockMultipartFile(
                "avatarFile",
                "test.png",
                "image/png",
                new byte[]{4, 5, 6}
        );

        // Act
        candidateImageController.uploadCandidateAvatar(file, session, response);

        // Assert
        assertArrayEquals(new byte[]{4, 5, 6}, testCandidate.getAvatar());
        assertEquals("image/png", testCandidate.getAvatarMimeType());
        verify(candidateService, times(1)).saveCandidate(testCandidate);
        assertEquals("http://localhost:8080/candidate-personal-account", response.getRedirectedUrl());
    }

    @Test
    void uploadCandidateAvatar_EmptyFile_HandlesGracefully() throws IOException {
        // Arrange
        session.setAttribute("candidate", testCandidate);
        MultipartFile file = new MockMultipartFile(
                "avatarFile",
                "empty.png",
                "image/png",
                new byte[0]
        );

        // Act
        candidateImageController.uploadCandidateAvatar(file, session, response);

        // Assert
        assertArrayEquals(new byte[0], testCandidate.getAvatar());
        assertEquals("image/png", testCandidate.getAvatarMimeType());
    }
}