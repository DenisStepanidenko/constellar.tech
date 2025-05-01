package ru.denis.constellar.tech.vacancy.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class VacancyDetailsDto {

    private Long id;
    private String title;
    private String position;
    private Integer salaryFrom;
    private Integer salaryTo;
    private String currency;
    private String experienceLevel;
    private String employmentType;
    private String workSchedule;
    private String location;
    private String description;
    private String skills;
    private LocalDateTime createdAt;
    private boolean active;


    private int totalResponses;
    private int newResponses;
    private int inReviewResponses;
    private int rejectedResponses;
    private int acceptedResponses;


}
