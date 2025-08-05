package java.security.cert;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import sun.misc.HexDumpEncoder;
import sun.security.util.DerValue;

/* loaded from: rt.jar:java/security/cert/PolicyQualifierInfo.class */
public class PolicyQualifierInfo {
    private byte[] mEncoded;
    private String mId;
    private byte[] mData;
    private String pqiString;

    public PolicyQualifierInfo(byte[] bArr) throws IOException {
        this.mEncoded = (byte[]) bArr.clone();
        DerValue derValue = new DerValue(this.mEncoded);
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoding for PolicyQualifierInfo");
        }
        this.mId = derValue.data.getDerValue().getOID().toString();
        byte[] byteArray = derValue.data.toByteArray();
        if (byteArray == null) {
            this.mData = null;
        } else {
            this.mData = new byte[byteArray.length];
            System.arraycopy(byteArray, 0, this.mData, 0, byteArray.length);
        }
    }

    public final String getPolicyQualifierId() {
        return this.mId;
    }

    public final byte[] getEncoded() {
        return (byte[]) this.mEncoded.clone();
    }

    public final byte[] getPolicyQualifier() {
        if (this.mData == null) {
            return null;
        }
        return (byte[]) this.mData.clone();
    }

    public String toString() {
        if (this.pqiString != null) {
            return this.pqiString;
        }
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("PolicyQualifierInfo: [\n");
        stringBuffer.append("  qualifierID: " + this.mId + "\n");
        stringBuffer.append("  qualifier: " + (this.mData == null ? FXMLLoader.NULL_KEYWORD : hexDumpEncoder.encodeBuffer(this.mData)) + "\n");
        stringBuffer.append("]");
        this.pqiString = stringBuffer.toString();
        return this.pqiString;
    }
}
