public class CipherException extends Exception {

    public enum ErrorType {
        KEY_FILE_NOT_FOUND,
        KEY_FILE_UNREADABLE,
        KEY_FILE_EMPTY,
        INVALID_LINE_COUNT,
        MISMATCHED_LINE_LENGTHS,
        DUPLICATE_SOURCE_CHARACTER,
        DUPLICATE_TARGET_CHARACTER,
        INVALID_PATH,
        CHARACTER_NOT_MAPPED
    }

    private final ErrorType errorType;
    private final String details;

    public CipherException(ErrorType errorType) {
        super(getMessageForType(errorType, null));
        this.errorType = errorType;
        this.details = null;
    }

    public CipherException(ErrorType errorType, String details) {
        super(getMessageForType(errorType, details));
        this.errorType = errorType;
        this.details = details;
    }

    public CipherException(ErrorType errorType, Throwable cause) {
        super(getMessageForType(errorType, null), cause);
        this.errorType = errorType;
        this.details = null;
    }

    public CipherException(ErrorType errorType, String details, Throwable cause) {
        super(getMessageForType(errorType, details), cause);
        this.errorType = errorType;
        this.details = details;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getDetails() {
        return details;
    }

    private static String getMessageForType(ErrorType type, String details) {
        String baseMessage;
        switch (type) {
            case KEY_FILE_NOT_FOUND:
                baseMessage = "Cipher key file not found";
                break;
            case KEY_FILE_UNREADABLE:
                baseMessage = "Cipher key file cannot be read";
                break;
            case KEY_FILE_EMPTY:
                baseMessage = "Cipher key file is empty";
                break;
            case INVALID_LINE_COUNT:
                baseMessage = "Cipher key file must contain exactly 2 lines";
                break;
            case MISMATCHED_LINE_LENGTHS:
                baseMessage = "Cipher key lines must have equal length";
                break;
            case DUPLICATE_SOURCE_CHARACTER:
                baseMessage = "Duplicate character in source (first) line of key";
                break;
            case DUPLICATE_TARGET_CHARACTER:
                baseMessage = "Duplicate character in target (second) line of key";
                break;
            case INVALID_PATH:
                baseMessage = "Key file path is null or empty";
                break;
            case CHARACTER_NOT_MAPPED:
                baseMessage = "Character not found in cipher key mapping";
                break;
            default:
                baseMessage = "Unknown cipher error";
        }

        if (details != null && !details.isEmpty()) {
            return baseMessage + ": " + details;
        }
        return baseMessage;
    }
}
