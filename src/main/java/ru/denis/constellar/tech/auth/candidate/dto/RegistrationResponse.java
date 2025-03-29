package ru.denis.constellar.tech.auth.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {

    private boolean success;

    private Map<String, String> errors;

}
