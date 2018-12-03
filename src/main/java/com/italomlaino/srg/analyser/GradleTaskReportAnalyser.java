package com.italomlaino.srg.analyser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.italomlaino.srg.model.Analyser;
import com.italomlaino.srg.model.AnalyserException;
import com.italomlaino.srg.model.Report;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GradleTaskReportAnalyser implements Analyser {

    private static final String DEFAULT_JSON_REPORT_PATH = "build/sonar/report.json";

    private static final String NON_WINDOWS_GRADLEW_BIN = "./gradlew";
    private static final String WINDOWS_GRADLEW_BIN = "cmd /c gradlew.bat";

    private static final String OS_NAME_PROPERTY = "os.name";
    private static final String OS_NAME_PROPERTY_WINDOWS_VALUE = "Windows";

    private static final String COMMAND_PARAMETERS = "-Dsonar.issuesReport.json.enable=true -Dsonar.report.export.path=report.json";

    private static final String FULL_COMMAND_FORMAT = "%s %s %s";

    private final String taskName;

    public GradleTaskReportAnalyser(String taskName) {
        this.taskName = taskName;
    }

    private String loadReport(File projectDir)
            throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(projectDir.getAbsolutePath(), DEFAULT_JSON_REPORT_PATH));

        return new String(bytes, StandardCharsets.UTF_8);
    }

    private Report parseReport(String contents)
            throws IOException {
        return new ObjectMapper().readValue(contents, Report.class);
    }

    private void runSonar(File projectDir)
            throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(getFullCommand());
        processBuilder.directory(projectDir);
        processBuilder.inheritIO();

        Process process = processBuilder.start();
        process.waitFor();
    }

    private String[] getFullCommand() {
        String osValue = System.getProperty(OS_NAME_PROPERTY);

        if (osValue.contains(OS_NAME_PROPERTY_WINDOWS_VALUE)) {
            return String.format(FULL_COMMAND_FORMAT, WINDOWS_GRADLEW_BIN, taskName, COMMAND_PARAMETERS).split(" ");
        } else {
            return String.format(FULL_COMMAND_FORMAT, NON_WINDOWS_GRADLEW_BIN, taskName, COMMAND_PARAMETERS).split(" ");
        }
    }

    @Override
    public Report analyse(File projectDir) throws AnalyserException {
        try {
            cleanReport(projectDir);
            runSonar(projectDir);

            String contents = loadReport(projectDir);

            return parseReport(contents);

        } catch (IOException | InterruptedException e) {
            throw new AnalyserException(e);
        }
    }

    private void cleanReport(File projectDir) {
        File file = Paths.get(projectDir.getAbsolutePath(), DEFAULT_JSON_REPORT_PATH).toFile();
        file.delete();
    }
}
