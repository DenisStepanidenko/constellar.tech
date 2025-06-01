package ru.denis.constellar.tech.candidate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import ru.denis.constellar.tech.achievement.model.Achievement;
import ru.denis.constellar.tech.repository.model.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String fullName;
    private String position;
    private String skills;
    private String experience;
    private String githubUsername;
    private String about;

    @Lob
    @Column(name = "avatar", columnDefinition = "bytea")
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] avatar;

    private String avatarMimeType;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Repository> repositories = new ArrayList<>();

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Achievement> achievements = new ArrayList<>();

    public Set<String> getSkillsSet() {
        return skills != null ?
                Arrays.stream(skills.split(","))
                        .map(String::trim)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();
    }

    public double getExperienceYears() {
        return switch (experience) {
            case "менее года" -> 0.5;
            case "1-3 года" -> 2;
            case "3-5 лет" -> 4;
            case "более 5 лет" -> 6;
            default -> 0;
        };
    }
}
