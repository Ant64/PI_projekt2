package pl.piotr.catalog.domain.exception;

public enum ExceptionCode {
    //technical errors
    INTERNAL_SERVER("CS_T_001", "Internal Server Exception", 500),
    ERROR_PARSING_REQUEST("CS_T_002", "Error while parsing request", 401),

    //business errors
    INVALID_LOGIN("CS_B_001", "Provided login '%s' is invalid", 400),
    INVALID_PASSWORD("CS_B_002", "Provided password for login '%s' is invalid", 400),
    LOGIN_NOT_UNIQUE("CS_B_003", "Provided login '%s' is already registered", 400),
    INVALID_TITLE("CS_B_004", "Provided title '%s' is invalid", 400),
    INVALID_CONTENT_RECIPE("CS_B_005", "Provided recipe's content '%s' is invalid", 400),
    INVALID_DISH_DURATION("CS_B_006", "Provided duration time of hours '%s' and minutes '%s' are invalid", 400),
    INVALID_TOKEN("CS_B_007", "Provided token '%s' is invalid", 400),
    INVALID_CREDENTIALS("CS_B_008", "Provided login '%s' or password are invalid", 400),
    INVALID_CONTENT_COMMENT("CS_B_009", "Provided content of comment '%s' is invalid", 400),
    NOT_SUFFICIENT_PERMISSIONS("CS_B_010", "User with login '%s' don't have sufficient permissions", 403),
    NO_SUCH_COMMENT("CS_B_011", "Provided comment id '%s' hasn't been found", 400),
    NO_SUCH_RECIPE("CS_B_012", "Provided recipe id '%s' hasn't been found", 400),
    INVALID_IMAGE_FILENAME("CS_B_013", "Uploaded file can't be parse properly", 400),
    ERROR_PARSING_IMAGE("CS_B_014", "Uploaded file can't be parse properly", 400),
    NO_SUCH_IMAGE("CS_B_015", "Provided image hasn't been found", 400);

    private String code;
    private String detailsPattern;
    private int httpStatus;

    ExceptionCode(String code, String detailPattern, Integer httpStatus) {
        this.code = code;
        this.detailsPattern = detailPattern;
        this.httpStatus = httpStatus;
    }


    public String getCode() {
        return code;
    }

    public String getDetailsPattern() {
        return detailsPattern;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }
}
