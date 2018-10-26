package com.srg.model;

import java.io.File;

public interface Analyser {

    Report analyse(File projectDir) throws AnalyserException;
}
