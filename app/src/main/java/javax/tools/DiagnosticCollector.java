package javax.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: rt.jar:javax/tools/DiagnosticCollector.class */
public final class DiagnosticCollector<S> implements DiagnosticListener<S> {
    private List<Diagnostic<? extends S>> diagnostics = Collections.synchronizedList(new ArrayList());

    @Override // javax.tools.DiagnosticListener
    public void report(Diagnostic<? extends S> diagnostic) {
        diagnostic.getClass();
        this.diagnostics.add(diagnostic);
    }

    public List<Diagnostic<? extends S>> getDiagnostics() {
        return Collections.unmodifiableList(this.diagnostics);
    }
}
