package ru.denis.constellar.tech.auth.candidate.controller.impl;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denis.constellar.tech.auth.candidate.controller.AuthCandidateController;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;

@RestController
@RequestMapping("/constellar.tech/api/v1/auth")
public class AuthCandidateControllerImpl implements AuthCandidateController {


    @PostMapping("/register-user")
    public void registerCandidate(@Valid @RequestBody RequestCandidateRegistration requestRegistration, BindingResult bindingResult) {

        return ;
    }

}
