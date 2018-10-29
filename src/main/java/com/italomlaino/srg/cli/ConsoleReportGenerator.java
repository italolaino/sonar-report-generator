package com.italomlaino.srg.cli;

import com.italomlaino.srg.model.Issue;
import com.italomlaino.srg.model.Report;
import com.italomlaino.srg.model.ReportGenerator;

import java.io.File;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleReportGenerator implements ReportGenerator {

    private static final String NEWEST_ISSUES_HEADER = "Newest issues";
    private static final String OLD_ISSUES_HEADER = "Old issues";
    private static final String ROW_FORMAT = "[%s] %s [%s]\n%s:%s\n\n";
    private static final String SEPARATOR = "----------------------------------";
    private static final String HEADER_FORMAT = "%s\n\n%s [%d]:\n\n";

    private PrintStream printStream;

    public void generate(Report report) {
        StringBuilder sb = new StringBuilder();

        List<Issue> newIssues = getIssues(report, true);

        generateHeader(sb, newIssues, NEWEST_ISSUES_HEADER);
        generateRows(sb, newIssues);

        List<Issue> oldIssues = getIssues(report, false);

        generateHeader(sb, oldIssues, OLD_ISSUES_HEADER);
        generateRows(sb, oldIssues);

        print(sb);
    }

    private void print(StringBuilder sb) {
        if (printStream != null) {
            printStream.println(sb);
        }
    }

    private void generateHeader(StringBuilder sb, List<Issue> issues, String issueHeader) {
        sb.append(String.format(
                HEADER_FORMAT, SEPARATOR, issueHeader,
                issues.size()));
    }

    private void generateRows(StringBuilder sb, List<Issue> newIssues) {
        newIssues.forEach(
                issue -> sb.append(generateRow(issue))
        );
    }

    private List<Issue> getIssues(Report report, boolean isNew) {
        return report
                .getIssues()
                .stream()
                .filter(issue -> issue.isNew() == isNew)
                .sorted(Comparator.comparing(Issue::getCreationDate))
                .collect(Collectors.toList());
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

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }
}