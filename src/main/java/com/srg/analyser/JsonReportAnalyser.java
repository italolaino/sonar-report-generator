package com.srg.analyser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srg.model.Analyser;
import com.srg.model.AnalyserException;
import com.srg.model.Report;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonReportAnalyser implements Analyser {

    private static final String DEFAULT_JSON_REPORT_PATH = "build/sonar/report.json";

    private static final String NON_WINDOWS_GRADLEW_BIN = "./gradlew";
    private static final String WINDOWS_GRADLEW_BIN = "gradlew.bat";

    private static final String OS_NAME_PROPERTY = "os.name";
    private static final String OS_NAME_PROPERTY_WINDOWS_VALUE = "Windows";

    private static final String COMMAND_PARAMETERS = "-Dsonar.issuesReport.json.enable=true -Dsonar.report.export.path=report.json";

    private final String execCommand;

    public JsonReportAnalyser(String execCommand) {
        this.execCommand = execCommand;
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

    private void runSonar(File path, String execCommand)
            throws IOException, InterruptedException {
        Runtime.getRuntime()
                .exec(getFullCommand(execCommand), null, path)
                .waitFor();
    }

    private String getFullCommand(String execCommand) {
        String osValue = System.getProperty(OS_NAME_PROPERTY);

        if (osValue.contains(OS_NAME_PROPERTY_WINDOWS_VALUE)) {
            return WINDOWS_GRADLEW_BIN + " " + execCommand + " " + COMMAND_PARAMETERS;
        } else {
            return NON_WINDOWS_GRADLEW_BIN + " " + execCommand + " " + COMMAND_PARAMETERS;
        }
    }

    @Override
    public Report analyse(File projectDir) throws AnalyserException {
        try {
            runSonar(projectDir, execCommand);

            String contents = loadReport(projectDir);

            return parseReport(contents);

        } catch (IOException | InterruptedException e) {
            throw new AnalyserException("Invalid execution command", e);
        }
    }
}
