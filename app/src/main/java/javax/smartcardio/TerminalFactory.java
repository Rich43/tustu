package javax.smartcardio;

import java.security.AccessController;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.util.Collections;
import java.util.List;
import javax.smartcardio.CardTerminals;
import org.icepdf.core.pobjects.graphics.Separation;
import sun.security.action.GetPropertyAction;
import sun.security.jca.GetInstance;

/* loaded from: rt.jar:javax/smartcardio/TerminalFactory.class */
public final class TerminalFactory {
    private static final String PROP_NAME = "javax.smartcardio.TerminalFactory.DefaultType";
    private static final String defaultType;
    private static final TerminalFactory defaultFactory;
    private final TerminalFactorySpi spi;
    private final Provider provider;
    private final String type;

    static {
        String strTrim = ((String) AccessController.doPrivileged(new GetPropertyAction(PROP_NAME, "PC/SC"))).trim();
        TerminalFactory terminalFactory = null;
        try {
            terminalFactory = getInstance(strTrim, null);
        } catch (Exception e2) {
        }
        if (terminalFactory == null) {
            try {
                strTrim = "PC/SC";
                Provider provider = Security.getProvider("SunPCSC");
                if (provider == null) {
                    provider = (Provider) Class.forName("sun.security.smartcardio.SunPCSC").newInstance();
                }
                terminalFactory = getInstance(strTrim, (Object) null, provider);
            } catch (Exception e3) {
            }
        }
        if (terminalFactory == null) {
            strTrim = "None";
            terminalFactory = new TerminalFactory(NoneFactorySpi.INSTANCE, NoneProvider.INSTANCE, "None");
        }
        defaultType = strTrim;
        defaultFactory = terminalFactory;
    }

    /* loaded from: rt.jar:javax/smartcardio/TerminalFactory$NoneProvider.class */
    private static final class NoneProvider extends Provider {
        private static final long serialVersionUID = 2745808869881593918L;
        static final Provider INSTANCE = new NoneProvider();

        private NoneProvider() {
            super("None", 1.0d, Separation.COLORANT_NONE);
        }
    }

    /* loaded from: rt.jar:javax/smartcardio/TerminalFactory$NoneFactorySpi.class */
    private static final class NoneFactorySpi extends TerminalFactorySpi {
        static final TerminalFactorySpi INSTANCE = new NoneFactorySpi();

        private NoneFactorySpi() {
        }

        @Override // javax.smartcardio.TerminalFactorySpi
        protected CardTerminals engineTerminals() {
            return NoneCardTerminals.INSTANCE;
        }
    }

    /* loaded from: rt.jar:javax/smartcardio/TerminalFactory$NoneCardTerminals.class */
    private static final class NoneCardTerminals extends CardTerminals {
        static final CardTerminals INSTANCE = new NoneCardTerminals();

        private NoneCardTerminals() {
        }

        @Override // javax.smartcardio.CardTerminals
        public List<CardTerminal> list(CardTerminals.State state) throws CardException {
            if (state == null) {
                throw new NullPointerException();
            }
            return Collections.emptyList();
        }

        @Override // javax.smartcardio.CardTerminals
        public boolean waitForChange(long j2) throws CardException {
            throw new IllegalStateException("no terminals");
        }
    }

    private TerminalFactory(TerminalFactorySpi terminalFactorySpi, Provider provider, String str) {
        this.spi = terminalFactorySpi;
        this.provider = provider;
        this.type = str;
    }

    public static String getDefaultType() {
        return defaultType;
    }

    public static TerminalFactory getDefault() {
        return defaultFactory;
    }

    public static TerminalFactory getInstance(String str, Object obj) throws NoSuchAlgorithmException {
        GetInstance.Instance getInstance = GetInstance.getInstance("TerminalFactory", (Class<?>) TerminalFactorySpi.class, str, obj);
        return new TerminalFactory((TerminalFactorySpi) getInstance.impl, getInstance.provider, str);
    }

    public static TerminalFactory getInstance(String str, Object obj, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance getInstance = GetInstance.getInstance("TerminalFactory", (Class<?>) TerminalFactorySpi.class, str, obj, str2);
        return new TerminalFactory((TerminalFactorySpi) getInstance.impl, getInstance.provider, str);
    }

    public static TerminalFactory getInstance(String str, Object obj, Provider provider) throws NoSuchAlgorithmException {
        GetInstance.Instance getInstance = GetInstance.getInstance("TerminalFactory", (Class<?>) TerminalFactorySpi.class, str, obj, provider);
        return new TerminalFactory((TerminalFactorySpi) getInstance.impl, getInstance.provider, str);
    }

    public Provider getProvider() {
        return this.provider;
    }

    public String getType() {
        return this.type;
    }

    public CardTerminals terminals() {
        return this.spi.engineTerminals();
    }

    public String toString() {
        return "TerminalFactory for type " + this.type + " from provider " + this.provider.getName();
    }
}
