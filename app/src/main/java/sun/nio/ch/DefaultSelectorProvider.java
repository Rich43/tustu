package sun.nio.ch;

import java.nio.channels.spi.SelectorProvider;

/* loaded from: rt.jar:sun/nio/ch/DefaultSelectorProvider.class */
public class DefaultSelectorProvider {
    private DefaultSelectorProvider() {
    }

    public static SelectorProvider create() {
        return new WindowsSelectorProvider();
    }
}
