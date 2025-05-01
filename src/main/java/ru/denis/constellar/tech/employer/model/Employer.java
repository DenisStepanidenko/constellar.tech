package ru.denis.constellar.tech.employer.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.denis.constellar.tech.vacancy.model.Vacancy;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    private String companyName;

    private String companyDescription;
    private String website;
    private String industry;
    private String phoneNumber;
    private String address;
    private String inn;
    private String kpp;
    private String contactPerson;
    private String contactPosition;

    @Lob
    @Column(name = "logoFile", columnDefinition = "bytea")
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] avatar;

    private String avatarType;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Vacancy> vacancies = new ArrayList<>();
}
