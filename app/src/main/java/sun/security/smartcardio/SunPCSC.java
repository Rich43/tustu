package sun.security.smartcardio;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactorySpi;

/* loaded from: rt.jar:sun/security/smartcardio/SunPCSC.class */
public final class SunPCSC extends Provider {
    private static final long serialVersionUID = 6168388284028876579L;

    public SunPCSC() {
        super("SunPCSC", 1.8d, "Sun PC/SC provider");
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.smartcardio.SunPCSC.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                SunPCSC.this.put("TerminalFactory.PC/SC", "sun.security.smartcardio.SunPCSC$Factory");
                return null;
            }
        });
    }

    /* loaded from: rt.jar:sun/security/smartcardio/SunPCSC$Factory.class */
    public static final class Factory extends TerminalFactorySpi {
        public Factory(Object obj) throws PCSCException, RuntimeException {
            if (obj != null) {
                throw new IllegalArgumentException("SunPCSC factory does not use parameters");
            }
            PCSC.checkAvailable();
            PCSCTerminals.initContext();
        }

        @Override // javax.smartcardio.TerminalFactorySpi
        protected CardTerminals engineTerminals() {
            return new PCSCTerminals();
        }
    }
}
