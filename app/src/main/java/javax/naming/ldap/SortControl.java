package javax.naming.ldap;

import com.sun.jndi.ldap.BerEncoder;
import java.io.IOException;

/* loaded from: rt.jar:javax/naming/ldap/SortControl.class */
public final class SortControl extends BasicControl {
    public static final String OID = "1.2.840.113556.1.4.473";
    private static final long serialVersionUID = -1965961680233330744L;

    public SortControl(String str, boolean z2) throws IOException {
        super(OID, z2, null);
        this.value = setEncodedValue(new SortKey[]{new SortKey(str)});
    }

    public SortControl(String[] strArr, boolean z2) throws IOException {
        super(OID, z2, null);
        SortKey[] sortKeyArr = new SortKey[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            sortKeyArr[i2] = new SortKey(strArr[i2]);
        }
        this.value = setEncodedValue(sortKeyArr);
    }

    public SortControl(SortKey[] sortKeyArr, boolean z2) throws IOException {
        super(OID, z2, null);
        this.value = setEncodedValue(sortKeyArr);
    }

    private byte[] setEncodedValue(SortKey[] sortKeyArr) throws IOException {
        BerEncoder berEncoder = new BerEncoder((30 * sortKeyArr.length) + 10);
        berEncoder.beginSeq(48);
        for (int i2 = 0; i2 < sortKeyArr.length; i2++) {
            berEncoder.beginSeq(48);
            berEncoder.encodeString(sortKeyArr[i2].getAttributeID(), true);
            String matchingRuleID = sortKeyArr[i2].getMatchingRuleID();
            if (matchingRuleID != null) {
                berEncoder.encodeString(matchingRuleID, 128, true);
            }
            if (!sortKeyArr[i2].isAscending()) {
                berEncoder.encodeBoolean(true, 129);
            }
            berEncoder.endSeq();
        }
        berEncoder.endSeq();
        return berEncoder.getTrimmedBuf();
    }
}
