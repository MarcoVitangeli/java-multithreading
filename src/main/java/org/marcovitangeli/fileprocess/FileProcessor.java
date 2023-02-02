package org.marcovitangeli.fileprocess;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileProcessor extends Thread {
    private String fileName;
    private List<String> badWords;
    private Set<BadFileInfo> badFileInfos;

    public FileProcessor(String fileName, List<String> badWords, Set<BadFileInfo> badFileInfos) {
        this.fileName = fileName;
        this.badWords = badWords;
        this.badFileInfos = badFileInfos;
    }

    @Override
    public void run() {
        try {
            File file = new File(this.fileName);
            Scanner scanner = new Scanner(file);
            Set<String> contained = new HashSet<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Arrays.stream(line.split(" "))
                        .forEach(word -> {
                            if (this.badWords.contains(word)) {
                                contained.add(word);
                            }
                        });
            }
            if (!contained.isEmpty()) {
                this.badFileInfos.add(new BadFileInfo(this.fileName, contained.stream().reduce("", (s, s2) -> s2+";"+s)));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
