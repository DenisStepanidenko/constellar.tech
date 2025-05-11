package ru.denis.constellar.tech.imagecontroller;

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
import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
import ru.denis.constellar.tech.employer.model.Employer;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployerImageControllerTest {

    @Mock
    private EmployerJpa employerJpa;

    @InjectMocks
    private EmployerImageController employerImageController;

    private Employer testEmployer;
    private MockHttpSession session;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        testEmployer = new Employer();
        testEmployer.setId(1L);
        testEmployer.setAvatar(new byte[]{1, 2, 3});
        testEmployer.setAvatarType("image/png");

        session = new MockHttpSession();
        response = new MockHttpServletResponse();
    }

    @Test
    void getEmployerAvatar_Authorized_ReturnsAvatar() {
        // Arrange
        session.setAttribute("employer", testEmployer);

        // Act
        ResponseEntity<byte[]> responseEntity = employerImageController.getEmployerAvatar(session);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertArrayEquals(new byte[]{1, 2, 3}, responseEntity.getBody());
        assertEquals(MediaType.valueOf("image/png"), responseEntity.getHeaders().getContentType());
    }

    @Test
    void getEmployerAvatar_Unauthorized_ReturnsForbidden() {
        // Act
        ResponseEntity<byte[]> responseEntity = employerImageController.getEmployerAvatar(null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void uploadEmployerAvatar_Authorized_UpdatesLogoAndRedirects() throws IOException {
        // Arrange
        session.setAttribute("employer", testEmployer);
        MultipartFile file = new MockMultipartFile(
                "logoFile",
                "logo.png",
                "image/png",
                new byte[]{4, 5, 6}
        );
        when(employerJpa.findById(1L)).thenReturn(Optional.of(testEmployer));

        // Act
        employerImageController.uploadEmployerAvatar(file, session, response);

        // Assert
        assertArrayEquals(new byte[]{4, 5, 6}, testEmployer.getAvatar());
        assertEquals("image/png", testEmployer.getAvatarType());
        verify(employerJpa, times(1)).save(testEmployer);
        verify(employerJpa, times(1)).flush();
        assertEquals("http://localhost:8080/company-personal-account-home", response.getRedirectedUrl());
    }

    @Test
    void uploadEmployerAvatar_Unauthorized_RedirectsToHome() throws IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile(
                "logoFile",
                "logo.png",
                "image/png",
                new byte[]{4, 5, 6}
        );

        // Act
        employerImageController.uploadEmployerAvatar(file, null, response);

        // Assert
        assertEquals("http://localhost:8080/home", response.getRedirectedUrl());
        verify(employerJpa, never()).save(any());
    }

    @Test
    void uploadEmployerAvatar_EmptyFile_HandlesGracefully() throws IOException {
        // Arrange
        session.setAttribute("employer", testEmployer);
        MultipartFile file = new MockMultipartFile(
                "logoFile",
                "empty.png",
                "image/png",
                new byte[0]
        );
        when(employerJpa.findById(1L)).thenReturn(Optional.of(testEmployer));

        // Act
        employerImageController.uploadEmployerAvatar(file, session, response);

        // Assert
        assertArrayEquals(new byte[0], testEmployer.getAvatar());
        assertEquals("image/png", testEmployer.getAvatarType());
    }
}