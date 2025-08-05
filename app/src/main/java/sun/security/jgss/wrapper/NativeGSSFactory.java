package sun.security.jgss.wrapper;

import java.io.UnsupportedEncodingException;
import java.security.Provider;
import java.util.Vector;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.GSSExceptionImpl;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.spi.GSSContextSpi;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spi.MechanismFactory;

/* loaded from: rt.jar:sun/security/jgss/wrapper/NativeGSSFactory.class */
public final class NativeGSSFactory implements MechanismFactory {
    GSSLibStub cStub = null;
    private final GSSCaller caller;

    private GSSCredElement getCredFromSubject(GSSNameElement gSSNameElement, boolean z2) throws GSSException {
        Vector vectorSearchSubject = GSSUtil.searchSubject(gSSNameElement, this.cStub.getMech(), z2, GSSCredElement.class);
        if (vectorSearchSubject != null && vectorSearchSubject.isEmpty() && GSSUtil.useSubjectCredsOnly(this.caller)) {
            throw new GSSException(13);
        }
        GSSCredElement gSSCredElement = (vectorSearchSubject == null || vectorSearchSubject.isEmpty()) ? null : (GSSCredElement) vectorSearchSubject.firstElement();
        if (gSSCredElement != null) {
            gSSCredElement.doServicePermCheck();
        }
        return gSSCredElement;
    }

    public NativeGSSFactory(GSSCaller gSSCaller) {
        this.caller = gSSCaller;
    }

    public void setMech(Oid oid) throws GSSException {
        this.cStub = GSSLibStub.getInstance(oid);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSNameSpi getNameElement(String str, Oid oid) throws GSSException {
        byte[] bytes;
        if (str == null) {
            bytes = null;
        } else {
            try {
                bytes = str.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e2) {
                throw new GSSExceptionImpl(11, e2);
            }
        }
        return new GSSNameElement(bytes, oid, this.cStub);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSNameSpi getNameElement(byte[] bArr, Oid oid) throws GSSException {
        return new GSSNameElement(bArr, oid, this.cStub);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSCredentialSpi getCredentialElement(GSSNameSpi gSSNameSpi, int i2, int i3, int i4) throws GSSException {
        GSSNameElement gSSNameElement;
        if (gSSNameSpi != null && !(gSSNameSpi instanceof GSSNameElement)) {
            gSSNameElement = (GSSNameElement) getNameElement(gSSNameSpi.toString(), gSSNameSpi.getStringNameType());
        } else {
            gSSNameElement = (GSSNameElement) gSSNameSpi;
        }
        if (i4 == 0) {
            i4 = 1;
        }
        GSSCredElement credFromSubject = getCredFromSubject(gSSNameElement, i4 == 1);
        if (credFromSubject == null) {
            if (i4 == 1) {
                credFromSubject = new GSSCredElement(gSSNameElement, i2, i4, this.cStub);
            } else if (i4 == 2) {
                if (gSSNameElement == null) {
                    gSSNameElement = GSSNameElement.DEF_ACCEPTOR;
                }
                credFromSubject = new GSSCredElement(gSSNameElement, i3, i4, this.cStub);
            } else {
                throw new GSSException(11, -1, "Unknown usage mode requested");
            }
        }
        return credFromSubject;
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(GSSNameSpi gSSNameSpi, GSSCredentialSpi gSSCredentialSpi, int i2) throws GSSException {
        if (gSSNameSpi == null) {
            throw new GSSException(3);
        }
        if (!(gSSNameSpi instanceof GSSNameElement)) {
            gSSNameSpi = (GSSNameElement) getNameElement(gSSNameSpi.toString(), gSSNameSpi.getStringNameType());
        }
        if (gSSCredentialSpi == null) {
            gSSCredentialSpi = getCredFromSubject(null, true);
        } else if (!(gSSCredentialSpi instanceof GSSCredElement)) {
            throw new GSSException(13);
        }
        return new NativeGSSContext((GSSNameElement) gSSNameSpi, (GSSCredElement) gSSCredentialSpi, i2, this.cStub);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(GSSCredentialSpi gSSCredentialSpi) throws GSSException {
        if (gSSCredentialSpi == null) {
            gSSCredentialSpi = getCredFromSubject(null, false);
        } else if (!(gSSCredentialSpi instanceof GSSCredElement)) {
            throw new GSSException(13);
        }
        return new NativeGSSContext((GSSCredElement) gSSCredentialSpi, this.cStub);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public GSSContextSpi getMechanismContext(byte[] bArr) throws GSSException {
        return this.cStub.importContext(bArr);
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public final Oid getMechanismOid() {
        return this.cStub.getMech();
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public Provider getProvider() {
        return SunNativeProvider.INSTANCE;
    }

    @Override // sun.security.jgss.spi.MechanismFactory
    public Oid[] getNameTypes() throws GSSException {
        return this.cStub.inquireNamesForMech();
    }
}
