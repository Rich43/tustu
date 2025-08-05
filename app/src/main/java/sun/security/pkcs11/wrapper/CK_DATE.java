package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_DATE.class */
public class CK_DATE implements Cloneable {
    public char[] year;
    public char[] month;
    public char[] day;

    public CK_DATE(char[] cArr, char[] cArr2, char[] cArr3) {
        this.year = cArr;
        this.month = cArr2;
        this.day = cArr3;
    }

    public Object clone() {
        try {
            CK_DATE ck_date = (CK_DATE) super.clone();
            ck_date.year = (char[]) this.year.clone();
            ck_date.month = (char[]) this.month.clone();
            ck_date.day = (char[]) this.day.clone();
            return ck_date;
        } catch (CloneNotSupportedException e2) {
            throw ((RuntimeException) new RuntimeException("Clone error").initCause(e2));
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new String(this.day));
        stringBuffer.append('.');
        stringBuffer.append(new String(this.month));
        stringBuffer.append('.');
        stringBuffer.append(new String(this.year));
        stringBuffer.append(" (DD.MM.YYYY)");
        return stringBuffer.toString();
    }
}
