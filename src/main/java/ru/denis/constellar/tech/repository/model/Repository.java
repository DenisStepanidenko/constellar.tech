package ru.denis.constellar.tech.repository.model;

import jakarta.persistence.*;
import lombok.*;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.repositoryFile.model.RepositoryFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String primaryLanguage;
    private Integer stars;
    private LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RepositoryFile> files = new ArrayList<>();
}
