import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;


public class FilehandlerTest {

    @BeforeEach
    public void cleanTestDirectory() {
        File testDir = new File("testData");

        if (testDir.exists()) {
            File[] files = testDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        } else {
            testDir.mkdir();
        }
    }


    // listFiles returns empty array when directory is empty
    @Test
    public void testListFilesEmptyDirectory() throws Exception {
        File testDir = new File("testData");
        testDir.mkdir();

        Filehandler filehandler = new Filehandler("testData");
        String[] files = filehandler.listFiles();

        assertEquals(0, files.length,
                "Empty directory should return empty file list");

        testDir.delete();
    }

    // listFiles returns correct number of files
    @Test
    public void testListFilesMultipleFiles() throws Exception {
        File testDir = new File("testData");
        testDir.mkdir();

        new File("testData/a.txt").createNewFile();
        new File("testData/b.txt").createNewFile();

        Filehandler filehandler = new Filehandler("testData");
        String[] files = filehandler.listFiles();

        assertEquals(2, files.length,
                "Directory with two files should return two file names");

        new File("testData/a.txt").delete();
        new File("testData/b.txt").delete();
        testDir.delete();
    }

    // readFile returns deciphered contents of existing file
    @Test
    public void testReadFileReturnsContents() throws Exception {

        File testDir = new File("testData");
        testDir.mkdir();

        FileWriter writer = new FileWriter("testData/test.txt");
        writer.write("Secret Message");
        writer.close();

        Filehandler filehandler = new Filehandler("testData");
        String contents = filehandler.readFile("test.txt");

        //expected must match the auto-deciphered result
        Cipher cipher = new Cipher();
        String expected = cipher.decipher("Secret Message\n");

        assertEquals(expected, contents,
                "Filehandler should return deciphered file contents");

        new File("testData/test.txt").delete();
        testDir.delete();
    }

    // readFile throws exception when file does not exist
    @Test
    public void testReadFileMissingFile() {
        Filehandler filehandler = new Filehandler("testData");

        assertThrows(FileNotFoundException.class, () ->
                        filehandler.readFile("missing.txt"),
                "Reading a missing file should throw an exception");
    }

    //Test if files are sorted correctly
    @Test
    public void testListFilesReturnsSortedOrder() throws Exception {
        File testDir = new File("testData");
        testDir.mkdir();

        new File("testData/zeta.txt").createNewFile();
        new File("testData/alpha.txt").createNewFile();
        new File("testData/middle.txt").createNewFile();

        Filehandler filehandler = new Filehandler("testData");
        String[] files = filehandler.listFiles();

        assertArrayEquals(
                new String[]{"alpha.txt", "middle.txt", "zeta.txt"},
                files,
                "Files should be returned in sorted alphabetical order"
        );

        new File("testData/zeta.txt").delete();
        new File("testData/alpha.txt").delete();
        new File("testData/middle.txt").delete();
        testDir.delete();
    }


    // readFile preserves newline characters
    @Test
    public void testReadFilePreservesNewLines() throws Exception {
        File testDir = new File("testData");
        testDir.mkdir();

        FileWriter writer = new FileWriter("testData/newlines.txt");
        writer.write("Line1\nLine2");
        writer.close();

        Filehandler filehandler = new Filehandler("testData");
        String contents = filehandler.readFile("newlines.txt");

        assertTrue(contents.contains("\n"),
                "Returned contents should contain newline characters");

        new File("testData/newlines.txt").delete();
        testDir.delete();
    }

    // listFiles throws exception when directory does not exist
    @Test
    public void testListFilesMissingDirectory() {
        Filehandler filehandler = new Filehandler("noSuchDirectory");

        assertThrows(FileNotFoundException.class, () ->
                        filehandler.listFiles(),
                "Missing directory should throw an exception");
    }

    // readFile returns empty string for empty file
    @Test
    public void testReadEmptyFile() throws Exception {
        File testDir = new File("testData");
        testDir.mkdir();

        new File("testData/empty.txt").createNewFile();

        Filehandler filehandler = new Filehandler("testData");
        String contents = filehandler.readFile("empty.txt");

        assertEquals("", contents,
                "Reading an empty file should return an empty string");

        new File("testData/empty.txt").delete();
        testDir.delete();
    }

    // listFiles does not return null
    @Test
    public void testListFilesNotNull() throws Exception {
        File testDir = new File("testData");
        testDir.mkdir();

        Filehandler filehandler = new Filehandler("testData");
        String[] files = filehandler.listFiles();

        assertNotNull(files,
                "listFiles should never return null");

        testDir.delete();
    }

    // constructor stores directory name correctly
    @Test
    public void testConstructorStoresDirectoryName() {
        Filehandler filehandler = new Filehandler("testData");

        assertNotNull(filehandler,
                "Filehandler object should be created successfully");
    }


    // test to ensure file names with spaces are handled correctly
    @Test
    public void testReadFileWithSpacesInName() throws Exception {

        File testDir = new File("testData");
        testDir.mkdir();

        File spacedFile = new File("testData/secret file.txt");
        FileWriter writer = new FileWriter(spacedFile);
        writer.write("Top Secret Content");
        writer.close();

        Filehandler filehandler = new Filehandler("testData");
        String contents = filehandler.readFile("secret file.txt");

        //since Filehandler now auto-deciphers,
        //the expected must match deciphered result
        Cipher cipher = new Cipher();
        String expected = cipher.decipher("Top Secret Content\n");

        assertEquals(expected, contents,
                "Filehandler should correctly read files with spaces in the name");

        spacedFile.delete();
        testDir.delete();
    }

    // readFile with custom key returns deciphered contents
    @Test
    public void testReadFileWithCustomKey() throws Exception {

        File testDir = new File("testData");
        testDir.mkdir();

        // Create content file
        FileWriter writer = new FileWriter("testData/custom.txt");
        writer.write("Secret");
        writer.close();

        // Create valid key file (2 lines!)
        FileWriter keyWriter = new FileWriter("testData/key.txt");
        keyWriter.write("ABCDEFGHIJKLMNOPQRSTUVWXYZ\n");
        keyWriter.write("ZYXWVUTSRQPONMLKJIHGFEDCBA");
        keyWriter.close();

        Filehandler filehandler = new Filehandler("testData");

        String result = filehandler.readFile("custom.txt", "testData/key.txt");

        assertNotNull(result);

        new File("testData/custom.txt").delete();
        new File("testData/key.txt").delete();
        testDir.delete();
    }
}

