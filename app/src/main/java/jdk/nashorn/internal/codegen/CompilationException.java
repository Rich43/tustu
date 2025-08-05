package jdk.nashorn.internal.codegen;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/CompilationException.class */
public class CompilationException extends RuntimeException {
    CompilationException(String description) {
        super(description);
    }

    CompilationException(Exception cause) {
        super(cause);
    }
}
