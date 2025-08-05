package sun.security.jgss.spnego;

import java.io.IOException;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSUtil;
import sun.security.util.BitArray;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/jgss/spnego/NegTokenInit.class */
public class NegTokenInit extends SpNegoToken {
    private byte[] mechTypes;
    private Oid[] mechTypeList;
    private BitArray reqFlags;
    private byte[] mechToken;
    private byte[] mechListMIC;

    NegTokenInit(byte[] bArr, BitArray bitArray, byte[] bArr2, byte[] bArr3) {
        super(0);
        this.mechTypes = null;
        this.mechTypeList = null;
        this.reqFlags = null;
        this.mechToken = null;
        this.mechListMIC = null;
        this.mechTypes = bArr;
        this.reqFlags = bitArray;
        this.mechToken = bArr2;
        this.mechListMIC = bArr3;
    }

    public NegTokenInit(byte[] bArr) throws IOException, GSSException {
        super(0);
        this.mechTypes = null;
        this.mechTypeList = null;
        this.reqFlags = null;
        this.mechToken = null;
        this.mechListMIC = null;
        parseToken(bArr);
    }

    @Override // sun.security.jgss.spnego.SpNegoToken
    final byte[] encode() throws GSSException {
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            if (this.mechTypes != null) {
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.mechTypes);
            }
            if (this.reqFlags != null) {
                DerOutputStream derOutputStream2 = new DerOutputStream();
                derOutputStream2.putUnalignedBitString(this.reqFlags);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream2);
            }
            if (this.mechToken != null) {
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.putOctetString(this.mechToken);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream3);
            }
            if (this.mechListMIC != null) {
                if (DEBUG) {
                    System.out.println("SpNegoToken NegTokenInit: sending MechListMIC");
                }
                DerOutputStream derOutputStream4 = new DerOutputStream();
                derOutputStream4.putOctetString(this.mechListMIC);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream4);
            }
            DerOutputStream derOutputStream5 = new DerOutputStream();
            derOutputStream5.write((byte) 48, derOutputStream);
            return derOutputStream5.toByteArray();
        } catch (IOException e2) {
            throw new GSSException(10, -1, "Invalid SPNEGO NegTokenInit token : " + e2.getMessage());
        }
    }

    private void parseToken(byte[] bArr) throws IOException, GSSException {
        try {
            DerValue derValue = new DerValue(bArr);
            if (!derValue.isContextSpecific((byte) 0)) {
                throw new IOException("SPNEGO NegoTokenInit : did not have right token type");
            }
            DerValue derValue2 = derValue.data.getDerValue();
            if (derValue2.tag != 48) {
                throw new IOException("SPNEGO NegoTokenInit : did not have the Sequence tag");
            }
            int iCheckNextField = -1;
            while (derValue2.data.available() > 0) {
                DerValue derValue3 = derValue2.data.getDerValue();
                if (derValue3.isContextSpecific((byte) 0)) {
                    iCheckNextField = checkNextField(iCheckNextField, 0);
                    DerInputStream derInputStream = derValue3.data;
                    this.mechTypes = derInputStream.toByteArray();
                    DerValue[] sequence = derInputStream.getSequence(0);
                    this.mechTypeList = new Oid[sequence.length];
                    for (int i2 = 0; i2 < sequence.length; i2++) {
                        ObjectIdentifier oid = sequence[i2].getOID();
                        if (DEBUG) {
                            System.out.println("SpNegoToken NegTokenInit: reading Mechanism Oid = " + ((Object) oid));
                        }
                        this.mechTypeList[i2] = new Oid(oid.toString());
                    }
                } else if (derValue3.isContextSpecific((byte) 1)) {
                    iCheckNextField = checkNextField(iCheckNextField, 1);
                } else if (derValue3.isContextSpecific((byte) 2)) {
                    iCheckNextField = checkNextField(iCheckNextField, 2);
                    if (DEBUG) {
                        System.out.println("SpNegoToken NegTokenInit: reading Mech Token");
                    }
                    this.mechToken = derValue3.data.getOctetString();
                } else if (derValue3.isContextSpecific((byte) 3)) {
                    iCheckNextField = checkNextField(iCheckNextField, 3);
                    if (!GSSUtil.useMSInterop()) {
                        this.mechListMIC = derValue3.data.getOctetString();
                        if (DEBUG) {
                            System.out.println("SpNegoToken NegTokenInit: MechListMIC Token = " + getHexBytes(this.mechListMIC));
                        }
                    }
                }
            }
        } catch (IOException e2) {
            throw new GSSException(10, -1, "Invalid SPNEGO NegTokenInit token : " + e2.getMessage());
        }
    }

    byte[] getMechTypes() {
        return this.mechTypes;
    }

    public Oid[] getMechTypeList() {
        return this.mechTypeList;
    }

    BitArray getReqFlags() {
        return this.reqFlags;
    }

    public byte[] getMechToken() {
        return this.mechToken;
    }

    byte[] getMechListMIC() {
        return this.mechListMIC;
    }
}
