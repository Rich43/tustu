package sun.security.krb5;

import java.io.IOException;
import java.util.Arrays;
import javax.security.auth.kerberos.KeyTab;
import sun.security.jgss.krb5.Krb5Util;
import sun.security.krb5.internal.HostAddresses;
import sun.security.krb5.internal.KDCOptions;
import sun.security.krb5.internal.KRBError;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.PAData;
import sun.security.krb5.internal.crypto.EType;

/* loaded from: rt.jar:sun/security/krb5/KrbAsReqBuilder.class */
public final class KrbAsReqBuilder {
    private KDCOptions options;
    private PrincipalName cname;
    private PrincipalName refCname;
    private PrincipalName sname;
    private KerberosTime from;
    private KerberosTime till;
    private KerberosTime rtime;
    private HostAddresses addresses;
    private final char[] password;
    private final KeyTab ktab;
    private PAData[] paList;
    private KrbAsReq req;
    private KrbAsRep rep;
    private State state;

    /* loaded from: rt.jar:sun/security/krb5/KrbAsReqBuilder$State.class */
    private enum State {
        INIT,
        REQ_OK,
        DESTROYED
    }

    private void init(PrincipalName principalName) throws KrbException {
        this.cname = principalName;
        this.refCname = principalName;
        this.state = State.INIT;
    }

    public KrbAsReqBuilder(PrincipalName principalName, KeyTab keyTab) throws KrbException {
        init(principalName);
        this.ktab = keyTab;
        this.password = null;
    }

    public KrbAsReqBuilder(PrincipalName principalName, char[] cArr) throws KrbException {
        init(principalName);
        this.password = (char[]) cArr.clone();
        this.ktab = null;
    }

    public EncryptionKey[] getKeys(boolean z2) throws KrbException {
        checkState(z2 ? State.REQ_OK : State.INIT, "Cannot get keys");
        if (this.password != null) {
            int[] defaults = EType.getDefaults("default_tkt_enctypes");
            EncryptionKey[] encryptionKeyArr = new EncryptionKey[defaults.length];
            String salt = null;
            for (int i2 = 0; i2 < defaults.length; i2++) {
                try {
                    PAData.SaltAndParams saltAndParams = PAData.getSaltAndParams(defaults[i2], this.paList);
                    if (saltAndParams != null) {
                        if (defaults[i2] != 23 && saltAndParams.salt != null) {
                            salt = saltAndParams.salt;
                        }
                        encryptionKeyArr[i2] = EncryptionKey.acquireSecretKey(this.cname, this.password, defaults[i2], saltAndParams);
                    }
                } catch (IOException e2) {
                    KrbException krbException = new KrbException(Krb5.ASN1_PARSE_ERROR);
                    krbException.initCause(e2);
                    throw krbException;
                }
            }
            if (salt == null) {
                salt = this.cname.getSalt();
            }
            for (int i3 = 0; i3 < defaults.length; i3++) {
                if (encryptionKeyArr[i3] == null) {
                    encryptionKeyArr[i3] = EncryptionKey.acquireSecretKey(this.password, salt, defaults[i3], (byte[]) null);
                }
            }
            return encryptionKeyArr;
        }
        throw new IllegalStateException("Required password not provided");
    }

    public void setOptions(KDCOptions kDCOptions) {
        checkState(State.INIT, "Cannot specify options");
        this.options = kDCOptions;
    }

    public void setTill(KerberosTime kerberosTime) {
        checkState(State.INIT, "Cannot specify till");
        this.till = kerberosTime;
    }

    public void setRTime(KerberosTime kerberosTime) {
        checkState(State.INIT, "Cannot specify rtime");
        this.rtime = kerberosTime;
    }

    public void setTarget(PrincipalName principalName) {
        checkState(State.INIT, "Cannot specify target");
        this.sname = principalName;
    }

    public void setAddresses(HostAddresses hostAddresses) {
        checkState(State.INIT, "Cannot specify addresses");
        this.addresses = hostAddresses;
    }

    private KrbAsReq build(EncryptionKey encryptionKey, ReferralsState referralsState) throws IOException, ArrayIndexOutOfBoundsException, KrbException {
        int[] defaults;
        PAData[] pADataArr = null;
        if (this.password != null) {
            defaults = EType.getDefaults("default_tkt_enctypes");
        } else {
            EncryptionKey[] encryptionKeyArrKeysFromJavaxKeyTab = Krb5Util.keysFromJavaxKeyTab(this.ktab, this.cname);
            defaults = EType.getDefaults("default_tkt_enctypes", encryptionKeyArrKeysFromJavaxKeyTab);
            for (EncryptionKey encryptionKey2 : encryptionKeyArrKeysFromJavaxKeyTab) {
                encryptionKey2.destroy();
            }
        }
        this.options = this.options == null ? new KDCOptions() : this.options;
        if (referralsState.isEnabled()) {
            if (referralsState.sendCanonicalize()) {
                this.options.set(15, true);
            }
            pADataArr = new PAData[]{new PAData(149, new byte[0])};
        } else {
            this.options.set(15, false);
        }
        return new KrbAsReq(encryptionKey, this.options, this.refCname, this.sname, this.from, this.till, this.rtime, defaults, this.addresses, pADataArr);
    }

    private KrbAsReqBuilder resolve() throws IOException, KrbException {
        if (this.ktab != null) {
            this.rep.decryptUsingKeyTab(this.ktab, this.req, this.cname);
        } else {
            this.rep.decryptUsingPassword(this.password, this.req, this.cname);
        }
        if (this.rep.getPA() != null) {
            if (this.paList == null || this.paList.length == 0) {
                this.paList = this.rep.getPA();
            } else {
                int length = this.rep.getPA().length;
                if (length > 0) {
                    int length2 = this.paList.length;
                    this.paList = (PAData[]) Arrays.copyOf(this.paList, this.paList.length + length);
                    System.arraycopy(this.rep.getPA(), 0, this.paList, length2, length);
                }
            }
        }
        return this;
    }

    private KrbAsReqBuilder send() throws IOException, KrbException {
        boolean z2 = false;
        KdcComm kdcComm = null;
        EncryptionKey encryptionKeyAcquireSecretKey = null;
        ReferralsState referralsState = new ReferralsState(this);
        while (true) {
            if (referralsState.refreshComm()) {
                kdcComm = new KdcComm(this.refCname.getRealmAsString());
            }
            try {
                this.req = build(encryptionKeyAcquireSecretKey, referralsState);
                this.rep = new KrbAsRep(kdcComm.send(this.req.encoding()));
                return this;
            } catch (KrbException e2) {
                if (!z2 && (e2.returnCode() == 24 || e2.returnCode() == 25)) {
                    if (Krb5.DEBUG) {
                        System.out.println("KrbAsReqBuilder: PREAUTH FAILED/REQ, re-send AS-REQ");
                    }
                    z2 = true;
                    KRBError error = e2.getError();
                    int preferredEType = PAData.getPreferredEType(error.getPA(), EType.getDefaults("default_tkt_enctypes")[0]);
                    if (this.password == null) {
                        EncryptionKey[] encryptionKeyArrKeysFromJavaxKeyTab = Krb5Util.keysFromJavaxKeyTab(this.ktab, this.cname);
                        encryptionKeyAcquireSecretKey = EncryptionKey.findKey(preferredEType, encryptionKeyArrKeysFromJavaxKeyTab);
                        if (encryptionKeyAcquireSecretKey != null) {
                            encryptionKeyAcquireSecretKey = (EncryptionKey) encryptionKeyAcquireSecretKey.clone();
                        }
                        for (EncryptionKey encryptionKey : encryptionKeyArrKeysFromJavaxKeyTab) {
                            encryptionKey.destroy();
                        }
                    } else {
                        encryptionKeyAcquireSecretKey = EncryptionKey.acquireSecretKey(this.cname, this.password, preferredEType, PAData.getSaltAndParams(preferredEType, error.getPA()));
                    }
                    this.paList = error.getPA();
                } else if (referralsState.handleError(e2)) {
                    encryptionKeyAcquireSecretKey = null;
                    z2 = false;
                } else {
                    throw e2;
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/security/krb5/KrbAsReqBuilder$ReferralsState.class */
    static final class ReferralsState {
        private static boolean canonicalizeConfig;
        private boolean enabled;
        private boolean sendCanonicalize = canonicalizeConfig;
        private boolean isEnterpriseCname;
        private int count;
        private boolean refreshComm;
        private KrbAsReqBuilder reqBuilder;

        static {
            initStatic();
        }

        static void initStatic() {
            canonicalizeConfig = false;
            try {
                canonicalizeConfig = Config.getInstance().getBooleanObject("libdefaults", "canonicalize") == Boolean.TRUE;
            } catch (KrbException e2) {
                if (Krb5.DEBUG) {
                    System.out.println("Exception in getting canonicalize, using default value " + ((Object) Boolean.valueOf(canonicalizeConfig)) + ": " + e2.getMessage());
                }
            }
        }

        ReferralsState(KrbAsReqBuilder krbAsReqBuilder) throws KrbException {
            this.reqBuilder = krbAsReqBuilder;
            this.isEnterpriseCname = krbAsReqBuilder.refCname.getNameType() == 10;
            updateStatus();
            if (!this.enabled && this.isEnterpriseCname) {
                throw new KrbException("NT-ENTERPRISE principals only allowed when referrals are enabled.");
            }
            this.refreshComm = true;
        }

        private void updateStatus() {
            this.enabled = !Config.DISABLE_REFERRALS && (this.isEnterpriseCname || this.sendCanonicalize);
        }

        boolean handleError(KrbException krbException) throws RealmException {
            Realm clientRealm;
            if (this.enabled) {
                if (krbException.returnCode() == 68 && (clientRealm = krbException.getError().getClientRealm()) != null && !clientRealm.toString().isEmpty() && this.count < Config.MAX_REFERRALS) {
                    this.reqBuilder.refCname = new PrincipalName(this.reqBuilder.refCname.getNameType(), this.reqBuilder.refCname.getNameStrings(), clientRealm);
                    this.refreshComm = true;
                    this.count++;
                    return true;
                }
                if (this.count < Config.MAX_REFERRALS && this.sendCanonicalize) {
                    if (Krb5.DEBUG) {
                        System.out.println("KrbAsReqBuilder: AS-REQ failed. Retrying with CANONICALIZE false.");
                    }
                    this.sendCanonicalize = false;
                    updateStatus();
                    return true;
                }
                return false;
            }
            return false;
        }

        boolean refreshComm() {
            boolean z2 = this.refreshComm;
            this.refreshComm = false;
            return z2;
        }

        boolean isEnabled() {
            return this.enabled;
        }

        boolean sendCanonicalize() {
            return this.sendCanonicalize;
        }
    }

    public KrbAsReqBuilder action() throws IOException, KrbException {
        checkState(State.INIT, "Cannot call action");
        this.state = State.REQ_OK;
        return send().resolve();
    }

    public Credentials getCreds() {
        checkState(State.REQ_OK, "Cannot retrieve creds");
        return this.rep.getCreds();
    }

    public sun.security.krb5.internal.ccache.Credentials getCCreds() {
        checkState(State.REQ_OK, "Cannot retrieve CCreds");
        return this.rep.getCCreds();
    }

    public void destroy() {
        this.state = State.DESTROYED;
        if (this.password != null) {
            Arrays.fill(this.password, (char) 0);
        }
    }

    private void checkState(State state, String str) {
        if (this.state != state) {
            throw new IllegalStateException(str + " at " + ((Object) state) + " state");
        }
    }
}
