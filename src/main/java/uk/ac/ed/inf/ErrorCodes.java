package uk.ac.ed.inf;

/**
 * Enum used to store error codes for the most potentially frequent appearing errors
 */
public enum ErrorCodes {
    INVALID_ARGS(2),
    INVALID_URL(3),
    READ_WRITE_FILE(4),
    JSON_PROCESS(5);

    public final int code;

    ErrorCodes(int code){this.code = code;}

}
