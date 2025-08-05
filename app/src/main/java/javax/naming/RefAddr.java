package javax.naming;

import java.io.Serializable;

/* loaded from: rt.jar:javax/naming/RefAddr.class */
public abstract class RefAddr implements Serializable {
    protected String addrType;
    private static final long serialVersionUID = -1468165120479154358L;

    public abstract Object getContent();

    protected RefAddr(String str) {
        this.addrType = str;
    }

    public String getType() {
        return this.addrType;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof RefAddr)) {
            RefAddr refAddr = (RefAddr) obj;
            if (this.addrType.compareTo(refAddr.addrType) == 0) {
                Object content = getContent();
                Object content2 = refAddr.getContent();
                if (content == content2) {
                    return true;
                }
                if (content != null) {
                    return content.equals(content2);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        if (getContent() == null) {
            return this.addrType.hashCode();
        }
        return this.addrType.hashCode() + getContent().hashCode();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Type: " + this.addrType + "\n");
        stringBuffer.append("Content: " + getContent() + "\n");
        return stringBuffer.toString();
    }
}
