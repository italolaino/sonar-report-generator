package com.italomlaino.srg.cli;

import com.italomlaino.srg.analyser.GradleTaskReportAnalyser;
import com.italomlaino.srg.generator.PrintStreamReportGenerator;
import com.italomlaino.srg.model.Analyser;
import com.italomlaino.srg.model.AnalyserException;
import com.italomlaino.srg.model.Report;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;

public class Cli {

    private static final String DEFAULT_TASK_NAME = "sonarqube";

    public void run(String[] args) throws AnalyserException, FileNotFoundException {
        System.out.println("Verifying arguments...");

        File projectDir = getProjectDir(args);
        String taskCommand = getTaskCommand(args);

        System.out.println(String.format("Analysing project %s...", projectDir.getAbsolutePath()));

        Analyser analyser = new GradleTaskReportAnalyser(taskCommand);
        Report report = analyser.analyse(projectDir);


        System.out.println("Generating report...");

        PrintStreamReportGenerator exporter = new PrintStreamReportGenerator(System.out);
        exporter.generate(report);
    }

    private String getTaskCommand(String[] args) {
        return args.length > 1 ?
                String.join(" ",Arrays.copyOfRange(args, 1, args.length)) :
                DEFAULT_TASK_NAME;
    }

    private String getCurrentDirectory() {
        return Paths.get("").toAbsolutePath().toString() + File.separator;
    }

    private File getProjectDir(String[] args) throws FileNotFoundException {
        String path = args.length > 0 ?
                args[0] :
                getCurrentDirectory();

        File dir = new File(path);
        if (!dir.exists()) {
            throw new FileNotFoundException("Invalid project directory");
        }

        return dir;
    }
}
