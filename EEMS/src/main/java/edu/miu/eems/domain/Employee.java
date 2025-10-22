package edu.miu.eems.domain;

import java.time.LocalDate;

public record Employee(int id, String name, String title, LocalDate hireDate, double salary, int deptId) {}
