package sun.reflect;

/* loaded from: rt.jar:sun/reflect/SignatureIterator.class */
public class SignatureIterator {
    private final String sig;
    private int idx;

    public SignatureIterator(String str) {
        this.sig = str;
        reset();
    }

    public void reset() {
        this.idx = 1;
    }

    public boolean atEnd() {
        return this.sig.charAt(this.idx) == ')';
    }

    public String next() {
        if (atEnd()) {
            return null;
        }
        char cCharAt = this.sig.charAt(this.idx);
        if (cCharAt != '[' && cCharAt != 'L') {
            this.idx++;
            return new String(new char[]{cCharAt});
        }
        int i2 = this.idx;
        if (cCharAt == '[') {
            while (true) {
                char cCharAt2 = this.sig.charAt(i2);
                cCharAt = cCharAt2;
                if (cCharAt2 != '[') {
                    break;
                }
                i2++;
            }
        }
        if (cCharAt == 'L') {
            while (this.sig.charAt(i2) != ';') {
                i2++;
            }
        }
        int i3 = this.idx;
        this.idx = i2 + 1;
        return this.sig.substring(i3, this.idx);
    }

    public String returnType() {
        if (!atEnd()) {
            throw new InternalError("Illegal use of SignatureIterator");
        }
        return this.sig.substring(this.idx + 1, this.sig.length());
    }
}
