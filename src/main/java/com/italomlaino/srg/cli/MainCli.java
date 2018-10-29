package com.italomlaino.srg.cli;

import com.italomlaino.srg.model.AnalyserException;

import java.io.FileNotFoundException;

public class MainCli {

    public static void main(String[] args) throws AnalyserException, FileNotFoundException {
        Cli cli = new Cli();
        cli.run(args);
    }
}
