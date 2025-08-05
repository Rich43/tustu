package javax.tools;

import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.Callable;
import javax.tools.JavaFileManager;

/* loaded from: rt.jar:javax/tools/DocumentationTool.class */
public interface DocumentationTool extends Tool, OptionChecker {

    /* loaded from: rt.jar:javax/tools/DocumentationTool$DocumentationTask.class */
    public interface DocumentationTask extends Callable<Boolean> {
        void setLocale(Locale locale);

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        Boolean call();
    }

    DocumentationTask getTask(Writer writer, JavaFileManager javaFileManager, DiagnosticListener<? super JavaFileObject> diagnosticListener, Class<?> cls, Iterable<String> iterable, Iterable<? extends JavaFileObject> iterable2);

    StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> diagnosticListener, Locale locale, Charset charset);

    /* loaded from: rt.jar:javax/tools/DocumentationTool$Location.class */
    public enum Location implements JavaFileManager.Location {
        DOCUMENTATION_OUTPUT,
        DOCLET_PATH,
        TAGLET_PATH;

        @Override // javax.tools.JavaFileManager.Location
        public String getName() {
            return name();
        }

        @Override // javax.tools.JavaFileManager.Location
        public boolean isOutputLocation() {
            switch (this) {
                case DOCUMENTATION_OUTPUT:
                    return true;
                default:
                    return false;
            }
        }
    }
}
