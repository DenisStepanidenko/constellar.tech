package ru.denis.constellar.tech.vacancy.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.denis.constellar.tech.vacancy.model.EmploymentType;
import ru.denis.constellar.tech.vacancy.model.ExperienceLevel;
import ru.denis.constellar.tech.vacancy.model.WorkSchedule;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class VacancyDto {
    @NotBlank(message = "Название вакансии не может быть пустым")
    @Size(max = 100, message = "Название вакансии должно быть не длиннее 100 символов")
    private String title;

    @NotBlank(message = "Описание вакансии не может быть пустым")
    @Size(max = 5000, message = "Описание вакансии должно быть не длиннее 5000 символов")
    private String description;

    @NotBlank(message = "Должность не может быть пустой")
    @Size(max = 100, message = "Должность должна быть не длиннее 100 символов")
    private String position;

    @NotNull(message = "Минимальная зарплата не может быть пустой")
    @Min(value = 0, message = "Минимальная зарплата не может быть отрицательной")
    private Integer salaryFrom;

    @NotNull(message = "Максимальная зарплата не может быть пустой")
    @Min(value = 0, message = "Максимальная зарплата не может быть отрицательной")
    private Integer salaryTo;

    @NotBlank(message = "Валюта не может быть пустой")
    private String currency;

    @NotNull(message = "Уровень опыта не может быть пустым")
    private ExperienceLevel experienceLevel;

    private String skills;

    @NotNull(message = "Тип занятости не может быть пустым")
    private EmploymentType employmentType;

    @NotNull(message = "График работы не может быть пустым")
    private WorkSchedule workSchedule;

    @NotBlank(message = "Локация не может быть пустой")
    @Size(max = 100, message = "Локация должна быть не длиннее 100 символов")
    private String location;


}
