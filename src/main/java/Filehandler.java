import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Filehandler {

    private String dataDirectory;

    // default constructor
    public Filehandler() {
        this.dataDirectory = "data";
    }

    // constructor for testing or custom folders
    public Filehandler(String dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    // return all files' names in the data directory, sorted
    public String[] listFiles() throws FileNotFoundException {

        File directory = new File(dataDirectory);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new FileNotFoundException("Data directory not found: " + dataDirectory);
        }

        String[] files = directory.list((dir, name) ->
                new File(dir, name).isFile()
        );

        if (files == null) {
            return new String[0];
        }

        Arrays.sort(files);
        return files;
    }

    // read and return the contents of a file
    public String readFile(String fileName) throws FileNotFoundException {

        File requestedFile = new File(dataDirectory + "/" + fileName);

        if (!requestedFile.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        StringBuilder fileContents = new StringBuilder();
        Scanner fileScanner = new Scanner(requestedFile);

        while (fileScanner.hasNextLine()) {
            fileContents.append(fileScanner.nextLine());
            if (fileScanner.hasNextLine()) {
                fileContents.append("\n");
            }
        }

        fileScanner.close();
        return fileContents.toString();
    }
}
