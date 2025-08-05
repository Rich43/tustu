package sun.security.jgss.spnego;

import java.io.IOException;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSUtil;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/jgss/spnego/NegTokenTarg.class */
public class NegTokenTarg extends SpNegoToken {
    private int negResult;
    private Oid supportedMech;
    private byte[] responseToken;
    private byte[] mechListMIC;

    NegTokenTarg(int i2, Oid oid, byte[] bArr, byte[] bArr2) {
        super(1);
        this.negResult = 0;
        this.supportedMech = null;
        this.responseToken = null;
        this.mechListMIC = null;
        this.negResult = i2;
        this.supportedMech = oid;
        this.responseToken = bArr;
        this.mechListMIC = bArr2;
    }

    public NegTokenTarg(byte[] bArr) throws IOException, GSSException {
        super(1);
        this.negResult = 0;
        this.supportedMech = null;
        this.responseToken = null;
        this.mechListMIC = null;
        parseToken(bArr);
    }

    @Override // sun.security.jgss.spnego.SpNegoToken
    final byte[] encode() throws GSSException {
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.putEnumerated(this.negResult);
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
            if (this.supportedMech != null) {
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.write(this.supportedMech.getDER());
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream3);
            }
            if (this.responseToken != null) {
                DerOutputStream derOutputStream4 = new DerOutputStream();
                derOutputStream4.putOctetString(this.responseToken);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream4);
            }
            if (this.mechListMIC != null) {
                if (DEBUG) {
                    System.out.println("SpNegoToken NegTokenTarg: sending MechListMIC");
                }
                DerOutputStream derOutputStream5 = new DerOutputStream();
                derOutputStream5.putOctetString(this.mechListMIC);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream5);
            } else if (GSSUtil.useMSInterop() && this.responseToken != null) {
                if (DEBUG) {
                    System.out.println("SpNegoToken NegTokenTarg: sending additional token for MS Interop");
                }
                DerOutputStream derOutputStream6 = new DerOutputStream();
                derOutputStream6.putOctetString(this.responseToken);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream6);
            }
            DerOutputStream derOutputStream7 = new DerOutputStream();
            derOutputStream7.write((byte) 48, derOutputStream);
            return derOutputStream7.toByteArray();
        } catch (IOException e2) {
            throw new GSSException(10, -1, "Invalid SPNEGO NegTokenTarg token : " + e2.getMessage());
        }
    }

    private void parseToken(byte[] bArr) throws IOException, GSSException {
        try {
            DerValue derValue = new DerValue(bArr);
            if (!derValue.isContextSpecific((byte) 1)) {
                throw new IOException("SPNEGO NegoTokenTarg : did not have the right token type");
            }
            DerValue derValue2 = derValue.data.getDerValue();
            if (derValue2.tag != 48) {
                throw new IOException("SPNEGO NegoTokenTarg : did not have the Sequence tag");
            }
            int iCheckNextField = -1;
            while (derValue2.data.available() > 0) {
                DerValue derValue3 = derValue2.data.getDerValue();
                if (derValue3.isContextSpecific((byte) 0)) {
                    iCheckNextField = checkNextField(iCheckNextField, 0);
                    this.negResult = derValue3.data.getEnumerated();
                    if (DEBUG) {
                        System.out.println("SpNegoToken NegTokenTarg: negotiated result = " + getNegoResultString(this.negResult));
                    }
                } else if (derValue3.isContextSpecific((byte) 1)) {
                    iCheckNextField = checkNextField(iCheckNextField, 1);
                    this.supportedMech = new Oid(derValue3.data.getOID().toString());
                    if (DEBUG) {
                        System.out.println("SpNegoToken NegTokenTarg: supported mechanism = " + ((Object) this.supportedMech));
                    }
                } else if (derValue3.isContextSpecific((byte) 2)) {
                    iCheckNextField = checkNextField(iCheckNextField, 2);
                    this.responseToken = derValue3.data.getOctetString();
                } else if (derValue3.isContextSpecific((byte) 3)) {
                    iCheckNextField = checkNextField(iCheckNextField, 3);
                    if (!GSSUtil.useMSInterop()) {
                        this.mechListMIC = derValue3.data.getOctetString();
                        if (DEBUG) {
                            System.out.println("SpNegoToken NegTokenTarg: MechListMIC Token = " + getHexBytes(this.mechListMIC));
                        }
                    }
                }
            }
        } catch (IOException e2) {
            throw new GSSException(10, -1, "Invalid SPNEGO NegTokenTarg token : " + e2.getMessage());
        }
    }

    int getNegotiatedResult() {
        return this.negResult;
    }

    public Oid getSupportedMech() {
        return this.supportedMech;
    }

    byte[] getResponseToken() {
        return this.responseToken;
    }

    byte[] getMechListMIC() {
        return this.mechListMIC;
    }
}
