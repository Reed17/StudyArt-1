package ua.artcode.utils.IO_utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by v21k on 15.04.17.
 */
@Component
public class CommonIOUtils {


    public PrintStream redirectSystemOut(ByteArrayOutputStream baos) {
        PrintStream oldSystemOut = System.out;
        System.setOut(new PrintStream(baos));
        return oldSystemOut;
    }

    public String resetSystemOut(ByteArrayOutputStream redirectedSystemOut, PrintStream systemOutOld) {
        System.out.flush();
        System.setOut(systemOutOld);
        return redirectedSystemOut.toString();
    }

    public PrintStream redirectSystemErr(ByteArrayOutputStream baos) {
        PrintStream oldSystemOut = System.out;
        System.setOut(new PrintStream(baos));
        return oldSystemOut;
    }

    public String resetSystemErr(ByteArrayOutputStream redirectedSystemOut, PrintStream systemOutOld) {
        System.out.flush();
        System.setOut(systemOutOld);
        return redirectedSystemOut.toString();
    }

    public String[] parseFilePaths(String path, String endsWith) throws IOException {
        return Files.walk(Paths.get(path))
                .map(Path::toString)
                .filter(filePath -> filePath.endsWith(endsWith))
                .toArray(String[]::new);
    }


    public void deleteAndWrite(String path, String content) throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(path)) {
            pw.write(content);
        }
    }


}
