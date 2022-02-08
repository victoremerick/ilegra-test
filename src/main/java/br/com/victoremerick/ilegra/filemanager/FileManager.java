package br.com.victoremerick.ilegra.filemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class FileManager {
    private static final String HOME_PATH = "HOMEPATH";
    private static final String INITIAL_PATH_WRITE = "/data/out";
    private static final String INITIAL_PATH_READ = "/data/in";

    public synchronized static Stream<File> getAllFiles(){
        var HomePath = System.getenv(HOME_PATH);
        File folder = new File("C:"+HomePath+INITIAL_PATH_READ);
        File[] listOfFiles = folder.listFiles(new FileFilterImpl());
        return Stream.of(listOfFiles);
    }

    public synchronized static Stream<String> read(File file) throws IOException {
        return Files.lines(file.toPath());
    }

    public synchronized static void write(String fileName, List<String> lines) throws IOException {
        var HomePath = System.getenv(HOME_PATH);
        File file = Path.of("C:", HomePath, INITIAL_PATH_WRITE,fileName).toFile();
        if(!file.exists()) {
            file.createNewFile();
        }
        Files.write(Path.of("C:", HomePath, INITIAL_PATH_WRITE,fileName), lines);
    }
}
