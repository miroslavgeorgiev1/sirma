package org.example.service;

import org.apache.commons.lang3.tuple.Pair;
import org.example.model.EmployeeProject;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class EmployeePairFinder {
    public static Map<Pair<Integer, Integer>, List<EmployeeProject>> findCommonProjects(List<EmployeeProject> projects) {
        Map<Pair<Integer, Integer>, List<EmployeeProject>> commonProjectsMap = new HashMap<>();

        for (EmployeeProject project : projects) {
            for (EmployeeProject other : projects) {
                if (project.getProjectId() == other.getProjectId() && project.getEmpId() != other.getEmpId()) {
                    int daysWorked = (int) DAYS.between(
                            max(project.getDateFrom(), other.getDateFrom()),
                            min(project.getDateTo(), other.getDateTo()));
                    if (daysWorked > 0) {
                        Pair<Integer, Integer> pair = Pair.of(project.getEmpId(), other.getEmpId());
                        commonProjectsMap.computeIfAbsent(pair, k -> new ArrayList<>()).add(new EmployeeProject(
                                project.getEmpId(), project.getProjectId(), project.getDateFrom(), project.getDateTo()
                        ));
                    }
                }
            }
        }
        return commonProjectsMap;
    }

    private static LocalDate min(LocalDate date1, LocalDate date2) {
        return date1.isBefore(date2) ? date1 : date2;
    }

    private static LocalDate max(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }
}
