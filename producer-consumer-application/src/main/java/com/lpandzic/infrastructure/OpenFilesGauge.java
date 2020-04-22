package com.lpandzic.infrastructure;

import lombok.AllArgsConstructor;

import java.io.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OpenFilesGauge {
    private final Long pid;

    double measure() {
        try {
            return getOpenFilesCount();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private double getOpenFilesCount() throws IOException {
        var process = new ProcessBuilder("/bin/sh", "-c", "lsof -p " + pid + " | wc -l").start();
        try (var inputStream = process.getInputStream()) {
            return Double.parseDouble(
                    new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n")));
        }
    }
}
