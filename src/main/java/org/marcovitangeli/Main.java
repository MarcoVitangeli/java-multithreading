package org.marcovitangeli;

import org.marcovitangeli.fileprocess.BadFileInfo;
import org.marcovitangeli.fileprocess.Orchestrator;

import java.util.List;

/**
 * exercise: loop over all the files in the pwd
 * and process them.
 * process on each file:
 * - read all its data
 * - see if it contains "bad words"
 * NOTE: a word is "bad" if it is in the bad words list
 */

public class Main {
    public static void main(String[] args) {
        Orchestrator orchestrator = new Orchestrator("/home/marco/go/src",
                List.of("GetContacts"));
        List<BadFileInfo> badFiles = orchestrator.processFiles();

        for (int i = 0; i < Math.min(badFiles.size(), 5); i++) {
            System.out.println(badFiles.get(i).toString());
        }
    }
}