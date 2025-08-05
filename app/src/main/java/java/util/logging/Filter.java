package java.util.logging;

@FunctionalInterface
/* loaded from: rt.jar:java/util/logging/Filter.class */
public interface Filter {
    boolean isLoggable(LogRecord logRecord);
}
