package stepper.dd.impl.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;

public class FileData {
    private final File file;

    public FileData(String directory) {
        file = new File(directory);
    }

    public FileData(Path path) {
        file = new File(path.toString());
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean delete() {
        return file.delete();
    }

    public boolean addPrefixAndSuffix(String prefix, String suffix) {
        if(prefix == null)
            prefix="";
        if(suffix == null)
            suffix="";
        File newFile = new File(file.getParentFile(),prefix + file.getName() + suffix);
        return file.renameTo(newFile);
    }

    public String getLineFromFile(int lineNumber) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getName()))) {
            String line = null;
            int currentLineNumber = 0;
            while ((line = reader.readLine()) != null) {
                currentLineNumber++;
                if (currentLineNumber == lineNumber) {
                    return line;
                }
            }
            return null;// line number not found
        }
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
