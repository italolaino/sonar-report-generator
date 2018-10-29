package com.italomlaino.srg.cli;

import com.italomlaino.srg.model.Issue;
import com.italomlaino.srg.model.Report;
import com.italomlaino.srg.model.ReportGenerator;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleReportGenerator implements ReportGenerator {

    private static final String NEWEST_ISSUES_HEADER = "Newest issues";
    private static final String OLD_ISSUES_HEADER = "Old issues";
    private static final String ROW_FORMAT = "[%s] %s [%s]\n%s:%s\n\n";
    private static final String SEPARATOR = "----------------------------------";
    private static final String HEADER_FORMAT = "%s\n\n%s [%d]:\n\n";

    public void generate(Report report) {
        StringBuilder sb = new StringBuilder();

        List<Issue> newIssues = report
                .getIssues()
                .stream()
                .filter(Issue::isNew)
                .sorted(Comparator.comparing(Issue::getCreationDate))
                .collect(Collectors.toList());

        sb.append(String.format(
                HEADER_FORMAT, SEPARATOR, NEWEST_ISSUES_HEADER,
                newIssues.size()));

        newIssues.forEach(
                issue -> sb.append(generateRow(issue))
        );

        List<Issue> oldIssues = report
                .getIssues()
                .stream()
                .filter(issue -> !issue.isNew())
                .sorted(Comparator.comparing(Issue::getCreationDate))
                .collect(Collectors.toList());

        sb.append(String.format(
                HEADER_FORMAT, SEPARATOR, OLD_ISSUES_HEADER,
                oldIssues.size()));

        oldIssues.forEach(
                issue -> sb.append(generateRow(issue))
        );

        System.out.println(sb);
    }

    private String generateRow(Issue issue) {
        return String.format(
                ROW_FORMAT,
                issue.getSeverity(),
                issue.getMessage(),
                issue.isNew() ? "*" : issue.getCreationDate(),
                File.separatorChar + issue.getComponent().replace(':', File.separatorChar),
                issue.getLine());
    }
}