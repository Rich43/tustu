package sun.nio.ch;

import java.io.IOException;
import java.nio.channels.spi.AbstractSelector;

/* loaded from: rt.jar:sun/nio/ch/WindowsSelectorProvider.class */
public class WindowsSelectorProvider extends SelectorProviderImpl {
    @Override // sun.nio.ch.SelectorProviderImpl, java.nio.channels.spi.SelectorProvider
    public AbstractSelector openSelector() throws IOException {
        return new WindowsSelectorImpl(this);
    }
}
