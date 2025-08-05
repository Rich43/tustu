package javax.tools;

import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.Callable;
import javax.annotation.processing.Processor;

/* loaded from: rt.jar:javax/tools/JavaCompiler.class */
public interface JavaCompiler extends Tool, OptionChecker {

    /* loaded from: rt.jar:javax/tools/JavaCompiler$CompilationTask.class */
    public interface CompilationTask extends Callable<Boolean> {
        void setProcessors(Iterable<? extends Processor> iterable);

        void setLocale(Locale locale);

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        Boolean call();
    }

    CompilationTask getTask(Writer writer, JavaFileManager javaFileManager, DiagnosticListener<? super JavaFileObject> diagnosticListener, Iterable<String> iterable, Iterable<String> iterable2, Iterable<? extends JavaFileObject> iterable3);

    StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> diagnosticListener, Locale locale, Charset charset);
}
