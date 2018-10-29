package com.italomlaino.srg.cli;

import com.italomlaino.srg.analyser.JsonReportAnalyser;
import com.italomlaino.srg.generator.PrintStreamReportGenerator;
import com.italomlaino.srg.model.Analyser;
import com.italomlaino.srg.model.AnalyserException;
import com.italomlaino.srg.model.Report;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class MainCLI {

    private static final String DEFAULT_COMMAND = "sonarqube";

    public static void main(String[] args) throws AnalyserException, FileNotFoundException {
        File projectDir = getProjectDir(args);
        String execCommand = getExecCommand(args);

        Analyser analyser = new JsonReportAnalyser(execCommand);
        Report report = analyser.analyse(projectDir);

        PrintStreamReportGenerator exporter = new PrintStreamReportGenerator();
        exporter.setPrintStream(System.out);
        exporter.generate(report);
    }

    private static String getExecCommand(String[] args) {
        return args.length > 1 ?
                args[1] :
                DEFAULT_COMMAND;
    }

    private static String getCurrentDirectory() {
        return Paths.get("").toAbsolutePath().toString() + File.separator;
    }

    private static File getProjectDir(String[] args) throws FileNotFoundException {
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
