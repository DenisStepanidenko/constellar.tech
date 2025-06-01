package ru.denis.constellar.tech.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.team.dto.TeamFormationResult;
import ru.denis.constellar.tech.team.dto.TeamRequest;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamFormationService {
    private static final double SKILLS_WEIGHT = 0.7;
    private static final double EXPERIENCE_WEIGHT = 0.3;
    private static final double COVERAGE_WEIGHT = 0.6;
    private static final double COMPATIBILITY_WEIGHT = 0.4;

    private final CandidateRepository candidateRepository;


    public TeamFormationResult formTeam(TeamRequest request) {
        // Получаем всех кандидатов, у которых есть хотя бы один нужный навык
        List<Candidate> filteredCandidates = candidateRepository.findAll().stream()
                .filter(c -> !Collections.disjoint(c.getSkillsSet(), request.getRequiredSkills()))
                .collect(Collectors.toList());

        if (filteredCandidates.isEmpty()) {
            return new TeamFormationResult(Collections.emptyList(),
                    Collections.emptySet(), request.getRequiredSkills(), 0.0);
        }

        // Жадный алгоритм формирования команды
        List<Candidate> team = greedySelection(filteredCandidates,
                request.getRequiredSkills(), request.getTeamSize());

        // Локальная оптимизация
        team = localOptimization(team, filteredCandidates, request.getRequiredSkills());

        // Определяем покрытые и непокрытые навыки
        Set<String> coveredSkills = calculateCoveredSkills(team, request.getRequiredSkills());
        Set<String> missingSkills = new HashSet<>(request.getRequiredSkills());
        missingSkills.removeAll(coveredSkills);

        double teamScore = calculateTeamScore(team, request.getRequiredSkills());

        return new TeamFormationResult(team, coveredSkills, missingSkills, teamScore);
    }

    private List<Candidate> greedySelection(List<Candidate> candidates,
                                            Set<String> requiredSkills, int teamSize) {
        List<Candidate> team = new ArrayList<>();
        Set<String> coveredSkills = new HashSet<>();

        while (team.size() < teamSize && !candidates.isEmpty()) {
            Candidate bestCandidate = null;
            double bestScore = -1;
            int bestIndex = -1;

            for (int i = 0; i < candidates.size(); i++) {
                Candidate candidate = candidates.get(i);
                double score = calculateCandidateScore(candidate, team, requiredSkills, coveredSkills);

                if (score > bestScore) {
                    bestScore = score;
                    bestCandidate = candidate;
                    bestIndex = i;
                }
            }

            if (bestCandidate == null) break;

            team.add(bestCandidate);
            coveredSkills.addAll(bestCandidate.getSkillsSet());
            candidates.remove(bestIndex);
        }

        return team;
    }

    private double calculateCandidateScore(Candidate candidate, List<Candidate> team,
                                           Set<String> requiredSkills, Set<String> coveredSkills) {
        // Рассчитываем прирост покрытия навыков
        Set<String> newSkills = new HashSet<>(candidate.getSkillsSet());
        newSkills.retainAll(requiredSkills);
        newSkills.removeAll(coveredSkills);
        double coverageGain = (double) newSkills.size() / requiredSkills.size();

        // Рассчитываем совместимость с текущей командой
        double compatibility = team.stream()
                .mapToDouble(t -> calculateCompatibility(candidate, t))
                .average()
                .orElse(0);

        return COVERAGE_WEIGHT * coverageGain + COMPATIBILITY_WEIGHT * compatibility;
    }

    private double calculateCompatibility(Candidate c1, Candidate c2) {
        // Коэффициент Жаккара для схожести навыков
        Set<String> intersection = new HashSet<>(c1.getSkillsSet());
        intersection.retainAll(c2.getSkillsSet());

        Set<String> union = new HashSet<>(c1.getSkillsSet());
        union.addAll(c2.getSkillsSet());

        double jaccard = union.isEmpty() ? 0 : (double) intersection.size() / union.size();

        // Близость опыта
        double experienceSimilarity = 1 / (1 + Math.abs(c1.getExperienceYears() - c2.getExperienceYears()));

        return SKILLS_WEIGHT * jaccard + EXPERIENCE_WEIGHT * experienceSimilarity;
    }

    private List<Candidate> localOptimization(List<Candidate> team,
                                              List<Candidate> allCandidates, Set<String> requiredSkills) {
        double currentScore = calculateTeamScore(team, requiredSkills);
        boolean improved;
        int iterations = 0;

        do {
            improved = false;

            for (int i = 0; i < team.size() && iterations < 1000; i++) {
                for (Candidate candidate : allCandidates) {
                    if (team.contains(candidate)) continue;

                    List<Candidate> newTeam = new ArrayList<>(team);
                    newTeam.set(i, candidate);

                    double newScore = calculateTeamScore(newTeam, requiredSkills);

                    if (newScore > currentScore) {
                        team = newTeam;
                        currentScore = newScore;
                        improved = true;
                    }
                }
                iterations++;
            }
        } while (improved && iterations < 1000);

        return team;
    }

    private double calculateTeamScore(List<Candidate> team, Set<String> requiredSkills) {
        // Покрытие навыков
        Set<String> coveredSkills = calculateCoveredSkills(team, requiredSkills);
        double coverage = (double) coveredSkills.size() / requiredSkills.size();

        // Совместимость
        double compatibility = 0;
        int pairs = 0;

        for (int i = 0; i < team.size(); i++) {
            for (int j = i + 1; j < team.size(); j++) {
                compatibility += calculateCompatibility(team.get(i), team.get(j));
                pairs++;
            }
        }

        compatibility = pairs > 0 ? compatibility / pairs : 0;

        return COVERAGE_WEIGHT * coverage + COMPATIBILITY_WEIGHT * compatibility;
    }

    private Set<String> calculateCoveredSkills(List<Candidate> team, Set<String> requiredSkills) {
        Set<String> coveredSkills = new HashSet<>();
        for (Candidate member : team) {
            coveredSkills.addAll(member.getSkillsSet());
        }
        coveredSkills.retainAll(requiredSkills);
        return coveredSkills;
    }
}