package pl.piotr.catalog.domain.exception;

public class DomainException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public DomainException(ExceptionCode exceptionCode, Object... variables) {
        super(String.format(exceptionCode.getDetailsPattern(), variables));
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
