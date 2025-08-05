package javax.naming;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/naming/LinkException.class */
public class LinkException extends NamingException {
    protected Name linkResolvedName;
    protected Object linkResolvedObj;
    protected Name linkRemainingName;
    protected String linkExplanation;
    private static final long serialVersionUID = -7967662604076777712L;

    public LinkException(String str) {
        super(str);
        this.linkResolvedName = null;
        this.linkResolvedObj = null;
        this.linkRemainingName = null;
        this.linkExplanation = null;
    }

    public LinkException() {
        this.linkResolvedName = null;
        this.linkResolvedObj = null;
        this.linkRemainingName = null;
        this.linkExplanation = null;
    }

    public Name getLinkResolvedName() {
        return this.linkResolvedName;
    }

    public Name getLinkRemainingName() {
        return this.linkRemainingName;
    }

    public Object getLinkResolvedObj() {
        return this.linkResolvedObj;
    }

    public String getLinkExplanation() {
        return this.linkExplanation;
    }

    public void setLinkExplanation(String str) {
        this.linkExplanation = str;
    }

    public void setLinkResolvedName(Name name) {
        if (name != null) {
            this.linkResolvedName = (Name) name.clone();
        } else {
            this.linkResolvedName = null;
        }
    }

    public void setLinkRemainingName(Name name) {
        if (name != null) {
            this.linkRemainingName = (Name) name.clone();
        } else {
            this.linkRemainingName = null;
        }
    }

    public void setLinkResolvedObj(Object obj) {
        this.linkResolvedObj = obj;
    }

    @Override // javax.naming.NamingException, java.lang.Throwable
    public String toString() {
        return super.toString() + "; Link Remaining Name: '" + ((Object) this.linkRemainingName) + PdfOps.SINGLE_QUOTE_TOKEN;
    }

    @Override // javax.naming.NamingException
    public String toString(boolean z2) {
        if (!z2 || this.linkResolvedObj == null) {
            return toString();
        }
        return toString() + "; Link Resolved Object: " + this.linkResolvedObj;
    }
}
