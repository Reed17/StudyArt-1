package ua.artcode.utils.IO_utils;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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

    public String[] parseFilePaths(String path, String endsWith) throws IOException {
        return Files.walk(Paths.get(path))
                .map(Path::toString)
                .filter(filePath -> filePath.endsWith(endsWith))
                .map(filePath -> new File(path).getAbsolutePath())
                .toArray(String[]::new);
    }


}
