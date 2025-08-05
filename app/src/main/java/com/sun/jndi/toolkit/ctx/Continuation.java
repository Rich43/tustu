package com.sun.jndi.toolkit.ctx;

import java.util.Hashtable;
import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.spi.ResolveResult;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/Continuation.class */
public class Continuation extends ResolveResult {
    protected Name starter;
    protected Object followingLink;
    protected Hashtable<?, ?> environment;
    protected boolean continuing;
    protected Context resolvedContext;
    protected Name relativeResolvedName;
    private static final long serialVersionUID = 8162530656132624308L;

    public Continuation() {
        this.followingLink = null;
        this.environment = null;
        this.continuing = false;
        this.resolvedContext = null;
        this.relativeResolvedName = null;
    }

    public Continuation(Name name, Hashtable<?, ?> hashtable) {
        this.followingLink = null;
        this.environment = null;
        this.continuing = false;
        this.resolvedContext = null;
        this.relativeResolvedName = null;
        this.starter = name;
        this.environment = (Hashtable) (hashtable == null ? null : hashtable.clone());
    }

    public boolean isContinue() {
        return this.continuing;
    }

    public void setSuccess() {
        this.continuing = false;
    }

    public NamingException fillInException(NamingException namingException) {
        namingException.setRemainingName(this.remainingName);
        namingException.setResolvedObj(this.resolvedObj);
        if (this.starter == null || this.starter.isEmpty()) {
            namingException.setResolvedName(null);
        } else if (this.remainingName == null) {
            namingException.setResolvedName(this.starter);
        } else {
            namingException.setResolvedName(this.starter.getPrefix(this.starter.size() - this.remainingName.size()));
        }
        if (namingException instanceof CannotProceedException) {
            CannotProceedException cannotProceedException = (CannotProceedException) namingException;
            cannotProceedException.setEnvironment(this.environment == null ? new Hashtable<>(11) : (Hashtable) this.environment.clone());
            cannotProceedException.setAltNameCtx(this.resolvedContext);
            cannotProceedException.setAltName(this.relativeResolvedName);
        }
        return namingException;
    }

    public void setErrorNNS(Object obj, Name name) {
        Name name2 = (Name) name.clone();
        try {
            name2.add("");
        } catch (InvalidNameException e2) {
        }
        setErrorAux(obj, name2);
    }

    public void setErrorNNS(Object obj, String str) {
        CompositeName compositeName = new CompositeName();
        if (str != null) {
            try {
                if (!str.equals("")) {
                    compositeName.add(str);
                }
            } catch (InvalidNameException e2) {
            }
        }
        compositeName.add("");
        setErrorAux(obj, compositeName);
    }

    public void setError(Object obj, Name name) {
        if (name != null) {
            this.remainingName = (Name) name.clone();
        } else {
            this.remainingName = null;
        }
        setErrorAux(obj, this.remainingName);
    }

    public void setError(Object obj, String str) {
        CompositeName compositeName = new CompositeName();
        if (str != null && !str.equals("")) {
            try {
                compositeName.add(str);
            } catch (InvalidNameException e2) {
            }
        }
        setErrorAux(obj, compositeName);
    }

    private void setErrorAux(Object obj, Name name) {
        this.remainingName = name;
        this.resolvedObj = obj;
        this.continuing = false;
    }

    private void setContinueAux(Object obj, Name name, Context context, Name name2) {
        if (obj instanceof LinkRef) {
            setContinueLink(obj, name, context, name2);
            return;
        }
        this.remainingName = name2;
        this.resolvedObj = obj;
        this.relativeResolvedName = name;
        this.resolvedContext = context;
        this.continuing = true;
    }

    public void setContinueNNS(Object obj, Name name, Context context) {
        new CompositeName();
        setContinue(obj, name, context, PartialCompositeContext._NNS_NAME);
    }

    public void setContinueNNS(Object obj, String str, Context context) {
        CompositeName compositeName = new CompositeName();
        try {
            compositeName.add(str);
        } catch (NamingException e2) {
        }
        setContinue(obj, compositeName, context, PartialCompositeContext._NNS_NAME);
    }

    public void setContinue(Object obj, Name name, Context context) {
        setContinueAux(obj, name, context, (Name) PartialCompositeContext._EMPTY_NAME.clone());
    }

    public void setContinue(Object obj, Name name, Context context, Name name2) {
        if (name2 != null) {
            this.remainingName = (Name) name2.clone();
        } else {
            this.remainingName = new CompositeName();
        }
        setContinueAux(obj, name, context, this.remainingName);
    }

    public void setContinue(Object obj, String str, Context context, String str2) {
        CompositeName compositeName = new CompositeName();
        if (!str.equals("")) {
            try {
                compositeName.add(str);
            } catch (NamingException e2) {
            }
        }
        CompositeName compositeName2 = new CompositeName();
        if (!str2.equals("")) {
            try {
                compositeName2.add(str2);
            } catch (NamingException e3) {
            }
        }
        setContinueAux(obj, compositeName, context, compositeName2);
    }

    @Deprecated
    public void setContinue(Object obj, Object obj2) {
        setContinue(obj, null, (Context) obj2);
    }

    private void setContinueLink(Object obj, Name name, Context context, Name name2) {
        this.followingLink = obj;
        this.remainingName = name2;
        this.resolvedObj = context;
        this.relativeResolvedName = PartialCompositeContext._EMPTY_NAME;
        this.resolvedContext = context;
        this.continuing = true;
    }

    public String toString() {
        if (this.remainingName != null) {
            return this.starter.toString() + "; remainingName: '" + ((Object) this.remainingName) + PdfOps.SINGLE_QUOTE_TOKEN;
        }
        return this.starter.toString();
    }

    public String toString(boolean z2) {
        if (!z2 || this.resolvedObj == null) {
            return toString();
        }
        return toString() + "; resolvedObj: " + this.resolvedObj + "; relativeResolvedName: " + ((Object) this.relativeResolvedName) + "; resolvedContext: " + ((Object) this.resolvedContext);
    }
}
