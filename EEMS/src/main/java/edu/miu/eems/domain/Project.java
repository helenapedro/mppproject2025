package edu.miu.eems.domain;

import java.time.LocalDate;
public record Project(int id, String name, String description,
                      LocalDate startDate, LocalDate endDate,
                      double budget, ProjectStatus status, int deptId) {}
