package ru.denis.constellar.tech.vacancy.model;

import jakarta.persistence.*;
import lombok.*;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.denis.constellar.tech.application.model.Application;
import ru.denis.constellar.tech.employer.model.Employer;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String position;

    private Integer salaryFrom;
    private Integer salaryTo;
    private String currency;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;


    private String skills;


    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType; // FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP


    @Enumerated(EnumType.STRING)
    private WorkSchedule workSchedule; // REMOTE, OFFICE, HYBRID

    private String location;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Application> applications = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;


}
