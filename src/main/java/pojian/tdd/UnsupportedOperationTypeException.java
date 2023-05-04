package pojian.tdd;

public class UnsupportedOperationTypeException extends RuntimeException {

    private String value;

    private Class<?> type;

    public UnsupportedOperationTypeException(String value, Class<?> type) {
    }

    public String getValue() {
        return value;
    }

    public Class<?> getType() {
        return type;
    }
}
