package javax.naming.spi;

import java.io.Serializable;
import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.Name;

/* loaded from: rt.jar:javax/naming/spi/ResolveResult.class */
public class ResolveResult implements Serializable {
    protected Object resolvedObj;
    protected Name remainingName;
    private static final long serialVersionUID = -4552108072002407559L;

    protected ResolveResult() {
        this.resolvedObj = null;
        this.remainingName = null;
    }

    public ResolveResult(Object obj, String str) {
        this.resolvedObj = obj;
        try {
            this.remainingName = new CompositeName(str);
        } catch (InvalidNameException e2) {
        }
    }

    public ResolveResult(Object obj, Name name) {
        this.resolvedObj = obj;
        setRemainingName(name);
    }

    public Name getRemainingName() {
        return this.remainingName;
    }

    public Object getResolvedObj() {
        return this.resolvedObj;
    }

    public void setRemainingName(Name name) {
        if (name != null) {
            this.remainingName = (Name) name.clone();
        } else {
            this.remainingName = null;
        }
    }

    public void appendRemainingName(Name name) {
        if (name != null) {
            if (this.remainingName != null) {
                try {
                    this.remainingName.addAll(name);
                } catch (InvalidNameException e2) {
                }
            } else {
                this.remainingName = (Name) name.clone();
            }
        }
    }

    public void appendRemainingComponent(String str) {
        if (str != null) {
            CompositeName compositeName = new CompositeName();
            try {
                compositeName.add(str);
            } catch (InvalidNameException e2) {
            }
            appendRemainingName(compositeName);
        }
    }

    public void setResolvedObj(Object obj) {
        this.resolvedObj = obj;
    }
}
