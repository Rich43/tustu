package com.sun.jndi.ldap;

import java.util.Hashtable;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.ReferralException;
import javax.naming.ldap.Control;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapReferralException.class */
public final class LdapReferralException extends javax.naming.ldap.LdapReferralException {
    private static final long serialVersionUID = 627059076356906399L;
    private int handleReferrals;
    private Hashtable<?, ?> envprops;
    private String nextName;
    private Control[] reqCtls;
    private Vector<?> referrals;
    private int referralIndex;
    private int referralCount;
    private boolean foundEntry;
    private boolean skipThisReferral;
    private int hopCount;
    private NamingException errorEx;
    private String newRdn;
    private boolean debug;
    LdapReferralException nextReferralEx;

    LdapReferralException(Name name, Object obj, Name name2, String str, Hashtable<?, ?> hashtable, String str2, int i2, Control[] controlArr) {
        super(str);
        this.referrals = null;
        this.referralIndex = 0;
        this.referralCount = 0;
        this.foundEntry = false;
        this.skipThisReferral = false;
        this.hopCount = 1;
        this.errorEx = null;
        this.newRdn = null;
        this.debug = false;
        this.nextReferralEx = null;
        if (this.debug) {
            System.out.println("LdapReferralException constructor");
        }
        setResolvedName(name);
        setResolvedObj(obj);
        setRemainingName(name2);
        this.envprops = hashtable;
        this.nextName = str2;
        this.handleReferrals = i2;
        this.reqCtls = (i2 == 1 || i2 == 4) ? controlArr : null;
    }

    @Override // javax.naming.ldap.LdapReferralException, javax.naming.ReferralException
    public Context getReferralContext() throws NamingException {
        return getReferralContext(this.envprops, null);
    }

    @Override // javax.naming.ldap.LdapReferralException, javax.naming.ReferralException
    public Context getReferralContext(Hashtable<?, ?> hashtable) throws NamingException {
        return getReferralContext(hashtable, null);
    }

    @Override // javax.naming.ldap.LdapReferralException
    public Context getReferralContext(Hashtable<?, ?> hashtable, Control[] controlArr) throws NamingException {
        if (this.debug) {
            System.out.println("LdapReferralException.getReferralContext");
        }
        LdapReferralContext ldapReferralContext = new LdapReferralContext(this, hashtable, controlArr, this.reqCtls, this.nextName, this.skipThisReferral, this.handleReferrals);
        ldapReferralContext.setHopCount(this.hopCount + 1);
        if (this.skipThisReferral) {
            this.skipThisReferral = false;
        }
        return ldapReferralContext;
    }

    @Override // javax.naming.ReferralException
    public Object getReferralInfo() {
        if (this.debug) {
            System.out.println("LdapReferralException.getReferralInfo");
            System.out.println("  referralIndex=" + this.referralIndex);
        }
        if (hasMoreReferrals()) {
            return this.referrals.elementAt(this.referralIndex);
        }
        return null;
    }

    @Override // javax.naming.ReferralException
    public void retryReferral() {
        if (this.debug) {
            System.out.println("LdapReferralException.retryReferral");
        }
        if (this.referralIndex > 0) {
            this.referralIndex--;
        }
    }

    @Override // javax.naming.ReferralException
    public boolean skipReferral() {
        if (this.debug) {
            System.out.println("LdapReferralException.skipReferral");
        }
        this.skipThisReferral = true;
        try {
            getNextReferral();
        } catch (ReferralException e2) {
        }
        return hasMoreReferrals() || hasMoreReferralExceptions();
    }

    void setReferralInfo(Vector<?> vector, boolean z2) {
        if (this.debug) {
            System.out.println("LdapReferralException.setReferralInfo");
        }
        this.referrals = vector;
        this.referralCount = vector == null ? 0 : vector.size();
        if (this.debug) {
            if (vector != null) {
                for (int i2 = 0; i2 < this.referralCount; i2++) {
                    System.out.println("  [" + i2 + "] " + vector.elementAt(i2));
                }
                return;
            }
            System.out.println("setReferralInfo : referrals == null");
        }
    }

    String getNextReferral() throws ReferralException {
        if (this.debug) {
            System.out.println("LdapReferralException.getNextReferral");
        }
        if (hasMoreReferrals()) {
            Vector<?> vector = this.referrals;
            int i2 = this.referralIndex;
            this.referralIndex = i2 + 1;
            return (String) vector.elementAt(i2);
        }
        if (hasMoreReferralExceptions()) {
            throw this.nextReferralEx;
        }
        return null;
    }

    LdapReferralException appendUnprocessedReferrals(LdapReferralException ldapReferralException) {
        if (this.debug) {
            System.out.println("LdapReferralException.appendUnprocessedReferrals");
            dump();
            if (ldapReferralException != null) {
                ldapReferralException.dump();
            }
        }
        LdapReferralException ldapReferralException2 = this;
        if (!ldapReferralException2.hasMoreReferrals()) {
            ldapReferralException2 = this.nextReferralEx;
            if (this.errorEx != null && ldapReferralException2 != null) {
                ldapReferralException2.setNamingException(this.errorEx);
            }
        }
        if (this == ldapReferralException) {
            return ldapReferralException2;
        }
        if (ldapReferralException != null && !ldapReferralException.hasMoreReferrals()) {
            ldapReferralException = ldapReferralException.nextReferralEx;
        }
        if (ldapReferralException == null) {
            return ldapReferralException2;
        }
        LdapReferralException ldapReferralException3 = ldapReferralException2;
        while (true) {
            LdapReferralException ldapReferralException4 = ldapReferralException3;
            if (ldapReferralException4.nextReferralEx != null) {
                ldapReferralException3 = ldapReferralException4.nextReferralEx;
            } else {
                ldapReferralException4.nextReferralEx = ldapReferralException;
                return ldapReferralException2;
            }
        }
    }

    boolean hasMoreReferrals() {
        if (this.debug) {
            System.out.println("LdapReferralException.hasMoreReferrals");
        }
        return !this.foundEntry && this.referralIndex < this.referralCount;
    }

    boolean hasMoreReferralExceptions() {
        if (this.debug) {
            System.out.println("LdapReferralException.hasMoreReferralExceptions");
        }
        return this.nextReferralEx != null;
    }

    void setHopCount(int i2) {
        if (this.debug) {
            System.out.println("LdapReferralException.setHopCount");
        }
        this.hopCount = i2;
    }

    void setNameResolved(boolean z2) {
        if (this.debug) {
            System.out.println("LdapReferralException.setNameResolved");
        }
        this.foundEntry = z2;
    }

    void setNamingException(NamingException namingException) {
        if (this.debug) {
            System.out.println("LdapReferralException.setNamingException");
        }
        if (this.errorEx == null) {
            namingException.setRootCause(this);
            this.errorEx = namingException;
        }
    }

    String getNewRdn() {
        if (this.debug) {
            System.out.println("LdapReferralException.getNewRdn");
        }
        return this.newRdn;
    }

    void setNewRdn(String str) {
        if (this.debug) {
            System.out.println("LdapReferralException.setNewRdn");
        }
        this.newRdn = str;
    }

    NamingException getNamingException() {
        if (this.debug) {
            System.out.println("LdapReferralException.getNamingException");
        }
        return this.errorEx;
    }

    void dump() {
        System.out.println();
        System.out.println("LdapReferralException.dump");
        LdapReferralException ldapReferralException = this;
        while (true) {
            LdapReferralException ldapReferralException2 = ldapReferralException;
            if (ldapReferralException2 != null) {
                ldapReferralException2.dumpState();
                ldapReferralException = ldapReferralException2.nextReferralEx;
            } else {
                return;
            }
        }
    }

    private void dumpState() {
        System.out.println("LdapReferralException.dumpState");
        System.out.println("  hashCode=" + hashCode());
        System.out.println("  foundEntry=" + this.foundEntry);
        System.out.println("  skipThisReferral=" + this.skipThisReferral);
        System.out.println("  referralIndex=" + this.referralIndex);
        if (this.referrals != null) {
            System.out.println("  referrals:");
            for (int i2 = 0; i2 < this.referralCount; i2++) {
                System.out.println("    [" + i2 + "] " + this.referrals.elementAt(i2));
            }
        } else {
            System.out.println("  referrals=null");
        }
        System.out.println("  errorEx=" + ((Object) this.errorEx));
        if (this.nextReferralEx == null) {
            System.out.println("  nextRefEx=null");
        } else {
            System.out.println("  nextRefEx=" + this.nextReferralEx.hashCode());
        }
        System.out.println();
    }
}
