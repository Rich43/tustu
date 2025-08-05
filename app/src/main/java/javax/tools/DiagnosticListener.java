package javax.tools;

/* loaded from: rt.jar:javax/tools/DiagnosticListener.class */
public interface DiagnosticListener<S> {
    void report(Diagnostic<? extends S> diagnostic);
}
