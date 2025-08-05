package sun.tracing;

import com.sun.tracing.Provider;
import com.sun.tracing.ProviderFactory;
import java.io.PrintStream;

/* loaded from: rt.jar:sun/tracing/PrintStreamProviderFactory.class */
public class PrintStreamProviderFactory extends ProviderFactory {
    private PrintStream stream;

    public PrintStreamProviderFactory(PrintStream printStream) {
        this.stream = printStream;
    }

    @Override // com.sun.tracing.ProviderFactory
    public <T extends Provider> T createProvider(Class<T> cls) {
        PrintStreamProvider printStreamProvider = new PrintStreamProvider(cls, this.stream);
        printStreamProvider.init();
        return (T) printStreamProvider.newProxyInstance();
    }
}
