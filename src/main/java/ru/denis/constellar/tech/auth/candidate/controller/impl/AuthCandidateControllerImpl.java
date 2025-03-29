package ru.denis.constellar.tech.auth.candidate.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denis.constellar.tech.auth.candidate.controller.AuthCandidateController;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.auth.candidate.service.AuthCandidateService;

@RestController
@RequestMapping("/constellar.tech/api/v1/auth")
@RequiredArgsConstructor
public class AuthCandidateControllerImpl implements AuthCandidateController {

    private final AuthCandidateService authService;

    @PostMapping("/register-user")
    public void registerCandidate(@Valid @RequestBody RequestCandidateRegistration requestRegistration) {

        authService.registerCandidate(requestRegistration);
    }

}
