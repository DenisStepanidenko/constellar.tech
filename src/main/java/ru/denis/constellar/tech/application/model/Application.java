package ru.denis.constellar.tech.application.model;

import jakarta.persistence.*;
import lombok.*;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.vacancy.model.Vacancy;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status; // NEW, VIEWED, INVITED, REJECTED

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    @Column(length = 2000)
    private String coverLetter;


}
