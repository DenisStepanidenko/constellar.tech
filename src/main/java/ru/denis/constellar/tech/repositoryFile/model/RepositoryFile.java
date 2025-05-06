package ru.denis.constellar.tech.repositoryFile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.denis.constellar.tech.repository.model.Repository;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob
    @Column(columnDefinition = "bytea")
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "repository_id")
    @JsonIgnore
    private Repository repository;
}