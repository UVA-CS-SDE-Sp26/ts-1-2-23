import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Cipher {

    private static final String DEFAULT_KEY_PATH = "ciphers/key.txt";
    private static final long MAX_KEY_FILE_SIZE = 1024 * 1024;
    private static final int MAX_LINE_LENGTH = 10000;

    private final Map<Character, Character> cipherToPlain;
    private final String keyFilePath;
    private String validationError;
    private final boolean keyValid;

    public Cipher() throws CipherException {
        this(DEFAULT_KEY_PATH);
    }

    public Cipher(String keyFilePath) throws CipherException {
        if (keyFilePath == null) {
            throw new CipherException(CipherException.ErrorType.INVALID_PATH, "Path cannot be null");
        }

        String trimmedPath = keyFilePath.trim();
        if (trimmedPath.isEmpty()) {
            throw new CipherException(CipherException.ErrorType.INVALID_PATH, "Path cannot be empty");
        }

        this.keyFilePath = trimmedPath;
        this.cipherToPlain = new HashMap<>();
        this.validationError = null;

        loadKeyFile();
        this.keyValid = validateKeyIntegrity();

        if (!keyValid) {
            throw new CipherException(CipherException.ErrorType.INVALID_LINE_COUNT, validationError);
        }
    }

    public String decipher(String cipheredText) {
        if (cipheredText == null) {
            return null;
        }

        if (cipheredText.isEmpty()) {
            return "";
        }

        StringBuilder plainText = new StringBuilder(cipheredText.length());

        for (int i = 0; i < cipheredText.length(); i++) {
            char cipheredChar = cipheredText.charAt(i);
            Character plainChar = cipherToPlain.get(cipheredChar);
            if (plainChar != null) {
                plainText.append(plainChar);
            } else {
                plainText.append(cipheredChar);
            }
        }

        return plainText.toString();
    }

    public boolean isValidKey() {
        return keyValid;
    }

    public String getValidationError() {
        return validationError;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public int getMappingCount() {
        return cipherToPlain.size();
    }

    private void loadKeyFile() throws CipherException {
        Path path = Paths.get(keyFilePath);
        File file = path.toFile();

        if (!file.exists()) {
            throw new CipherException(CipherException.ErrorType.KEY_FILE_NOT_FOUND, keyFilePath);
        }

        if (!file.canRead()) {
            throw new CipherException(CipherException.ErrorType.KEY_FILE_UNREADABLE, keyFilePath);
        }

        if (file.length() > MAX_KEY_FILE_SIZE) {
            throw new CipherException(CipherException.ErrorType.KEY_FILE_UNREADABLE,
                    "Key file exceeds maximum allowed size of " + MAX_KEY_FILE_SIZE + " bytes");
        }

        if (!file.isFile()) {
            throw new CipherException(CipherException.ErrorType.KEY_FILE_NOT_FOUND,
                    "Path is not a regular file: " + keyFilePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String sourceLine = reader.readLine();
            String targetLine = reader.readLine();
            String extraLine = reader.readLine();

            if (sourceLine == null) {
                throw new CipherException(CipherException.ErrorType.KEY_FILE_EMPTY);
            }

            if (targetLine == null) {
                throw new CipherException(CipherException.ErrorType.INVALID_LINE_COUNT,
                        "Key file must contain 2 lines, found only 1");
            }

            if (extraLine != null && !extraLine.trim().isEmpty()) {
                throw new CipherException(CipherException.ErrorType.INVALID_LINE_COUNT,
                        "Key file must contain exactly 2 lines, found more");
            }

            if (sourceLine.length() > MAX_LINE_LENGTH || targetLine.length() > MAX_LINE_LENGTH) {
                throw new CipherException(CipherException.ErrorType.KEY_FILE_UNREADABLE,
                        "Key file lines exceed maximum allowed length of " + MAX_LINE_LENGTH);
            }

            if (sourceLine.length() != targetLine.length()) {
                throw new CipherException(CipherException.ErrorType.MISMATCHED_LINE_LENGTHS,
                        "Line 1 has " + sourceLine.length() + " chars, Line 2 has " + targetLine.length() + " chars");
            }

            if (sourceLine.isEmpty()) {
                throw new CipherException(CipherException.ErrorType.KEY_FILE_EMPTY, "Key file lines are empty");
            }

            Set<Character> sourceChars = new HashSet<>();
            for (int i = 0; i < sourceLine.length(); i++) {
                char c = sourceLine.charAt(i);
                if (sourceChars.contains(c)) {
                    throw new CipherException(CipherException.ErrorType.DUPLICATE_SOURCE_CHARACTER,
                            "Character '" + c + "' appears multiple times in source line");
                }
                sourceChars.add(c);
            }

            Set<Character> targetChars = new HashSet<>();
            for (int i = 0; i < targetLine.length(); i++) {
                char c = targetLine.charAt(i);
                if (targetChars.contains(c)) {
                    throw new CipherException(CipherException.ErrorType.DUPLICATE_TARGET_CHARACTER,
                            "Character '" + c + "' appears multiple times in target line");
                }
                targetChars.add(c);
            }

            for (int i = 0; i < targetLine.length(); i++) {
                char cipheredChar = targetLine.charAt(i);
                char originalChar = sourceLine.charAt(i);
                cipherToPlain.put(cipheredChar, originalChar);
            }

        } catch (IOException e) {
            throw new CipherException(CipherException.ErrorType.KEY_FILE_UNREADABLE, e.getMessage(), e);
        }
    }

    private boolean validateKeyIntegrity() {
        if (cipherToPlain.isEmpty()) {
            validationError = "Cipher mapping is empty";
            return false;
        }

        Set<Character> values = new HashSet<>(cipherToPlain.values());
        if (values.size() != cipherToPlain.size()) {
            validationError = "Cipher mapping is not bijective";
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Cipher{keyFilePath='" + keyFilePath + "', mappingCount=" + cipherToPlain.size() + ", valid=" + keyValid
                + '}';
    }
}
