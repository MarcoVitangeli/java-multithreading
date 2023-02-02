package org.marcovitangeli.fileprocess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Orchestrator {
    final int MAX_DEPTH = 999;
    private String pwd;
    // not used yet, will be passed to FileProcessor
    private Set<BadFileInfo> badFiles;
    private List<String> badWords;

    public Orchestrator(String pwd, List<String> badWords) {
        this.pwd = pwd;
        this.badWords = badWords;
        // concurrent safe set
        this.badFiles = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public List<BadFileInfo> processFiles() throws RuntimeException {
        try(final Stream<Path> paths = Files.find(Paths.get(this.pwd), MAX_DEPTH, (p, bfa) -> !bfa.isDirectory())) {
            Stream<FileProcessor> fileProcessorStream = paths
                    .map(Path::toFile)
                    .map(file -> new FileProcessor(file.getAbsolutePath(), this.badWords, this.badFiles));

            List<FileProcessor> fileProcessors = new ArrayList<>();

            this.healthCheck();
            fileProcessorStream.forEach(fileProcessor -> {
                fileProcessor.start();
                fileProcessors.add(fileProcessor);
            });
            for (FileProcessor fileProcessor : fileProcessors) {
                fileProcessor.join();
            }

            return new ArrayList<>(this.badFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException ignored) {
            return null;
        }
    }

    public void healthCheck() {
        Runnable runnable = () -> {
            for (int i = 0; i < 10000000; i++) {
                if (i % 1000 == 0) {
                    System.out.println(Thread.activeCount());
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }
}
