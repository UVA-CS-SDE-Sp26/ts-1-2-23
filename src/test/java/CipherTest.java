import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cipher Tests")
public class CipherTest {

    @TempDir
    Path tempDir;

    private Path createKeyFile(String sourceLine, String targetLine) throws IOException {
        Path keyFile = tempDir.resolve("test_key.txt");
        String content = sourceLine + "\n" + targetLine;
        Files.write(keyFile, content.getBytes(StandardCharsets.UTF_8));
        return keyFile;
    }

    private Path createKeyFileWithContent(String content) throws IOException {
        Path keyFile = tempDir.resolve("test_key.txt");
        Files.write(keyFile, content.getBytes(StandardCharsets.UTF_8));
        return keyFile;
    }

    @Nested
    @DisplayName("Happy Path")
    class HappyPathTests {

        @Test
        @DisplayName("Decipher lowercase")
        void testDecipherLowercase() throws Exception {
            Path keyFile = createKeyFile("abcdefghijklmnopqrstuvwxyz", "bcdefghijklmnopqrstuvwxyza");
            Cipher cipher = new Cipher(keyFile.toString());
            assertEquals("a", cipher.decipher("b"));
            assertEquals("hello", cipher.decipher("ifmmp"));
        }

        @Test
        @DisplayName("Decipher uppercase")
        void testDecipherUppercase() throws Exception {
            Path keyFile = createKeyFile("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
                    "BCDEFGHIJKLMNOPQRSTUVWXYZAbcdefghijklmnopqrstuvwxyza");
            Cipher cipher = new Cipher(keyFile.toString());
            assertEquals("HELLO", cipher.decipher("IFMMP"));
        }

        @Test
        @DisplayName("Decipher numbers")
        void testDecipherNumbers() throws Exception {
            Path keyFile = createKeyFile("0123456789", "1234567890");
            Cipher cipher = new Cipher(keyFile.toString());
            assertEquals("0", cipher.decipher("1"));
            assertEquals("9", cipher.decipher("0"));
        }

        @Test
        @DisplayName("Default key path")
        void testDefaultKeyPath() throws Exception {
            Cipher cipher = new Cipher();
            assertNotNull(cipher);
            assertTrue(cipher.isValidKey());
            assertEquals(62, cipher.getMappingCount());
        }
    }

    @Nested
    @DisplayName("Null and Empty Input")
    class NullAndEmptyTests {

        private Cipher cipher;

        @BeforeEach
        void setUp() throws Exception {
            Path keyFile = createKeyFile("abc", "bcd");
            cipher = new Cipher(keyFile.toString());
        }

        @Test
        @DisplayName("Null input returns null")
        void testNullInput() {
            assertNull(cipher.decipher(null));
        }

        @Test
        @DisplayName("Empty input returns empty")
        void testEmptyInput() {
            assertEquals("", cipher.decipher(""));
        }

        @Test
        @DisplayName("Single character")
        void testSingleCharacter() {
            assertEquals("a", cipher.decipher("b"));
        }

        @Test
        @DisplayName("Whitespace only")
        void testWhitespaceOnly() {
            assertEquals("   ", cipher.decipher("   "));
        }
    }

    @Nested
    @DisplayName("Key File Validation")
    class KeyFileValidationTests {

        @Test
        @DisplayName("Missing key file")
        void testMissingKeyFile() {
            CipherException e = assertThrows(CipherException.class, () -> new Cipher("nonexistent/path/key.txt"));
            assertEquals(CipherException.ErrorType.KEY_FILE_NOT_FOUND, e.getErrorType());
        }

        @Test
        @DisplayName("Null key file path")
        void testNullKeyFilePath() {
            CipherException e = assertThrows(CipherException.class, () -> new Cipher(null));
            assertEquals(CipherException.ErrorType.INVALID_PATH, e.getErrorType());
        }

        @Test
        @DisplayName("Empty key file path")
        void testEmptyKeyFilePath() {
            CipherException e = assertThrows(CipherException.class, () -> new Cipher(""));
            assertEquals(CipherException.ErrorType.INVALID_PATH, e.getErrorType());
        }

        @Test
        @DisplayName("Empty key file")
        void testEmptyKeyFile() throws IOException {
            Path keyFile = createKeyFileWithContent("");
            CipherException e = assertThrows(CipherException.class, () -> new Cipher(keyFile.toString()));
            assertEquals(CipherException.ErrorType.KEY_FILE_EMPTY, e.getErrorType());
        }

        @Test
        @DisplayName("Single line key file")
        void testSingleLineKeyFile() throws IOException {
            Path keyFile = createKeyFileWithContent("abcdef");
            CipherException e = assertThrows(CipherException.class, () -> new Cipher(keyFile.toString()));
            assertEquals(CipherException.ErrorType.INVALID_LINE_COUNT, e.getErrorType());
        }

        @Test
        @DisplayName("Too many lines")
        void testTooManyLinesKeyFile() throws IOException {
            Path keyFile = createKeyFileWithContent("abc\nbcd\nextra");
            CipherException e = assertThrows(CipherException.class, () -> new Cipher(keyFile.toString()));
            assertEquals(CipherException.ErrorType.INVALID_LINE_COUNT, e.getErrorType());
        }

        @Test
        @DisplayName("Trailing newline accepted")
        void testTrailingNewlineAccepted() throws Exception {
            Path keyFile = createKeyFileWithContent("abc\nbcd\n");
            Cipher cipher = new Cipher(keyFile.toString());
            assertTrue(cipher.isValidKey());
        }

        @Test
        @DisplayName("Mismatched line lengths")
        void testMismatchedLineLengths() throws IOException {
            Path keyFile = createKeyFile("abcdef", "xyz");
            CipherException e = assertThrows(CipherException.class, () -> new Cipher(keyFile.toString()));
            assertEquals(CipherException.ErrorType.MISMATCHED_LINE_LENGTHS, e.getErrorType());
        }

        @Test
        @DisplayName("Duplicate source characters")
        void testDuplicateSourceCharacters() throws IOException {
            Path keyFile = createKeyFile("aabcd", "bcdef");
            CipherException e = assertThrows(CipherException.class, () -> new Cipher(keyFile.toString()));
            assertEquals(CipherException.ErrorType.DUPLICATE_SOURCE_CHARACTER, e.getErrorType());
        }

        @Test
        @DisplayName("Duplicate target characters")
        void testDuplicateTargetCharacters() throws IOException {
            Path keyFile = createKeyFile("abcde", "bbcde");
            CipherException e = assertThrows(CipherException.class, () -> new Cipher(keyFile.toString()));
            assertEquals(CipherException.ErrorType.DUPLICATE_TARGET_CHARACTER, e.getErrorType());
        }
    }

    @Nested
    @DisplayName("Special Characters")
    class SpecialCharactersTests {

        private Cipher cipher;

        @BeforeEach
        void setUp() throws Exception {
            Path keyFile = createKeyFile("abcdefghijklmnopqrstuvwxyz", "bcdefghijklmnopqrstuvwxyza");
            cipher = new Cipher(keyFile.toString());
        }

        @Test
        @DisplayName("Spaces pass through")
        void testSpacesPassthrough() {
            assertEquals("hello world", cipher.decipher("ifmmp xpsme"));
        }

        @Test
        @DisplayName("Punctuation passes through")
        void testPunctuationPassthrough() {
            assertEquals("hello, world!", cipher.decipher("ifmmp, xpsme!"));
        }

        @Test
        @DisplayName("Unmapped characters pass through")
        void testUnmappedCharactersPassthrough() {
            assertEquals("HELLO", cipher.decipher("HELLO"));
            assertEquals("test123", cipher.decipher("uftu123"));
        }

        @Test
        @DisplayName("Unicode passes through")
        void testUnicodePassthrough() {
            assertEquals("hello 世界", cipher.decipher("ifmmp 世界"));
        }
    }

    @Nested
    @DisplayName("Alternate Key Paths")
    class AlternateKeyPathTests {

        @Test
        @DisplayName("Absolute path")
        void testAbsolutePath() throws Exception {
            Path keyFile = createKeyFile("abc", "bcd");
            Cipher cipher = new Cipher(keyFile.toAbsolutePath().toString());
            assertEquals("a", cipher.decipher("b"));
        }

        @Test
        @DisplayName("Path with spaces")
        void testPathWithSpaces() throws Exception {
            Path subDir = tempDir.resolve("path with spaces");
            Files.createDirectories(subDir);
            Path keyFile = subDir.resolve("key.txt");
            Files.write(keyFile, "abc\nbcd".getBytes(StandardCharsets.UTF_8));
            Cipher cipher = new Cipher(keyFile.toString());
            assertEquals("a", cipher.decipher("b"));
        }
    }

    @Nested
    @DisplayName("Performance")
    class PerformanceTests {

        @Test
        @DisplayName("Very long string")
        void testVeryLongString() throws Exception {
            Path keyFile = createKeyFile("abcdefghijklmnopqrstuvwxyz", "bcdefghijklmnopqrstuvwxyza");
            Cipher cipher = new Cipher(keyFile.toString());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 100000; i++) {
                sb.append("ifmmp ");
            }

            long startTime = System.currentTimeMillis();
            String result = cipher.decipher(sb.toString());
            long endTime = System.currentTimeMillis();

            assertTrue(endTime - startTime < 1000);
            assertTrue(result.startsWith("hello "));
        }
    }

    @Nested
    @DisplayName("CipherException")
    class CipherExceptionTests {

        @Test
        @DisplayName("Error type preserved")
        void testErrorTypePreserved() {
            CipherException e = new CipherException(CipherException.ErrorType.KEY_FILE_NOT_FOUND);
            assertEquals(CipherException.ErrorType.KEY_FILE_NOT_FOUND, e.getErrorType());
        }

        @Test
        @DisplayName("Details preserved")
        void testDetailsPreserved() {
            CipherException e = new CipherException(CipherException.ErrorType.KEY_FILE_NOT_FOUND, "test/path.txt");
            assertEquals("test/path.txt", e.getDetails());
            assertTrue(e.getMessage().contains("test/path.txt"));
        }

        @Test
        @DisplayName("All error types have messages")
        void testAllErrorTypeMessages() {
            for (CipherException.ErrorType type : CipherException.ErrorType.values()) {
                CipherException e = new CipherException(type);
                assertNotNull(e.getMessage());
                assertFalse(e.getMessage().isEmpty());
            }
        }
    }
}
