package ru.denis.constellar.tech.achievement.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.denis.constellar.tech.candidate.model.Candidate;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDate date;
    private String fileName;
    private String fileType;

    @Lob
    @Column(columnDefinition = "bytea")
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] fileContent;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}
