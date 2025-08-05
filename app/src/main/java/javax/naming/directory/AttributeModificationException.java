package javax.naming.directory;

import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/directory/AttributeModificationException.class */
public class AttributeModificationException extends NamingException {
    private ModificationItem[] unexecs;
    private static final long serialVersionUID = 8060676069678710186L;

    public AttributeModificationException(String str) {
        super(str);
        this.unexecs = null;
    }

    public AttributeModificationException() {
        this.unexecs = null;
    }

    public void setUnexecutedModifications(ModificationItem[] modificationItemArr) {
        this.unexecs = modificationItemArr;
    }

    public ModificationItem[] getUnexecutedModifications() {
        return this.unexecs;
    }

    @Override // javax.naming.NamingException, java.lang.Throwable
    public String toString() {
        String string = super.toString();
        if (this.unexecs != null) {
            string = string + "First unexecuted modification: " + this.unexecs[0].toString();
        }
        return string;
    }
}
