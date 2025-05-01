package ru.denis.constellar.tech.vacancy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacancyStatsResponse {
    private int total;
    private int newCount;
    private int viewed;
    private int rejected;
    private int invited;
    private Map<String, Integer> chartData = new HashMap<>();
}
