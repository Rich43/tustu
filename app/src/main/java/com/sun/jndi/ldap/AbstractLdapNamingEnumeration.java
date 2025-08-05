package com.sun.jndi.ldap;

import com.sun.jndi.toolkit.ctx.Continuation;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.naming.LimitExceededException;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.Control;

/* loaded from: rt.jar:com/sun/jndi/ldap/AbstractLdapNamingEnumeration.class */
abstract class AbstractLdapNamingEnumeration<T extends NameClassPair> implements NamingEnumeration<T>, ReferralEnumeration<T> {
    protected Name listArg;
    private LdapResult res;
    private LdapClient enumClnt;
    private Continuation cont;
    private Vector<LdapEntry> entries;
    private int limit;
    protected LdapCtx homeCtx;
    private LdapReferralException refEx;
    private boolean cleaned = false;
    private int posn = 0;
    private NamingException errEx = null;
    private boolean more = true;
    private boolean hasMoreCalled = false;

    protected abstract T createItem(String str, Attributes attributes, Vector<Control> vector) throws NamingException;

    protected abstract AbstractLdapNamingEnumeration<? extends NameClassPair> getReferredResults(LdapReferralContext ldapReferralContext) throws NamingException;

    AbstractLdapNamingEnumeration(LdapCtx ldapCtx, LdapResult ldapResult, Name name, Continuation continuation) throws NamingException {
        this.entries = null;
        this.limit = 0;
        this.refEx = null;
        if (ldapResult.status != 0 && ldapResult.status != 4 && ldapResult.status != 3 && ldapResult.status != 11 && ldapResult.status != 10 && ldapResult.status != 9) {
            throw continuation.fillInException(new NamingException(LdapClient.getErrorMessage(ldapResult.status, ldapResult.errorMessage)));
        }
        this.res = ldapResult;
        this.entries = ldapResult.entries;
        this.limit = this.entries == null ? 0 : this.entries.size();
        this.listArg = name;
        this.cont = continuation;
        if (ldapResult.refEx != null) {
            this.refEx = ldapResult.refEx;
        }
        this.homeCtx = ldapCtx;
        ldapCtx.incEnumCount();
        this.enumClnt = ldapCtx.clnt;
    }

    @Override // java.util.Enumeration
    /* renamed from: nextElement */
    public final T nextElement2() {
        try {
            return (T) next();
        } catch (NamingException e2) {
            cleanup();
            return null;
        }
    }

    @Override // java.util.Enumeration
    public final boolean hasMoreElements() {
        try {
            return hasMore();
        } catch (NamingException e2) {
            cleanup();
            return false;
        }
    }

    private void getNextBatch() throws NamingException {
        this.res = this.homeCtx.getSearchReply(this.enumClnt, this.res);
        if (this.res == null) {
            this.posn = 0;
            this.limit = 0;
            return;
        }
        this.entries = this.res.entries;
        this.limit = this.entries == null ? 0 : this.entries.size();
        this.posn = 0;
        if (this.res.status != 0 || (this.res.status == 0 && this.res.referrals != null)) {
            try {
                this.homeCtx.processReturnCode(this.res, this.listArg);
            } catch (LimitExceededException | PartialResultException e2) {
                setNamingException(e2);
            }
        }
        if (this.res.refEx != null) {
            if (this.refEx == null) {
                this.refEx = this.res.refEx;
            } else {
                this.refEx = this.refEx.appendUnprocessedReferrals(this.res.refEx);
            }
            this.res.refEx = null;
        }
        if (this.res.resControls != null) {
            this.homeCtx.respCtls = this.res.resControls;
        }
    }

    @Override // javax.naming.NamingEnumeration
    public final boolean hasMore() throws NamingException {
        if (this.hasMoreCalled) {
            return this.more;
        }
        this.hasMoreCalled = true;
        if (!this.more) {
            return false;
        }
        boolean zHasMoreImpl = hasMoreImpl();
        this.more = zHasMoreImpl;
        return zHasMoreImpl;
    }

    @Override // javax.naming.NamingEnumeration
    public final T next() throws NamingException {
        if (!this.hasMoreCalled) {
            hasMore();
        }
        this.hasMoreCalled = false;
        return (T) nextImpl();
    }

    private boolean hasMoreImpl() throws NamingException {
        if (this.posn == this.limit) {
            getNextBatch();
        }
        if (this.posn < this.limit) {
            return true;
        }
        try {
            return hasMoreReferrals();
        } catch (LdapReferralException | LimitExceededException | PartialResultException e2) {
            cleanup();
            throw e2;
        } catch (NamingException e3) {
            cleanup();
            PartialResultException partialResultException = new PartialResultException();
            partialResultException.setRootCause(e3);
            throw partialResultException;
        }
    }

    private T nextImpl() throws NamingException {
        try {
            return (T) nextAux();
        } catch (NamingException e2) {
            cleanup();
            throw this.cont.fillInException(e2);
        }
    }

    private T nextAux() throws NamingException {
        if (this.posn == this.limit) {
            getNextBatch();
        }
        if (this.posn >= this.limit) {
            cleanup();
            throw new NoSuchElementException("invalid enumeration handle");
        }
        Vector<LdapEntry> vector = this.entries;
        int i2 = this.posn;
        this.posn = i2 + 1;
        LdapEntry ldapEntryElementAt = vector.elementAt(i2);
        return (T) createItem(ldapEntryElementAt.DN, ldapEntryElementAt.attributes, ldapEntryElementAt.respCtls);
    }

    protected final String getAtom(String str) {
        try {
            LdapName ldapName = new LdapName(str);
            return ldapName.get(ldapName.size() - 1);
        } catch (NamingException e2) {
            return str;
        }
    }

    @Override // com.sun.jndi.ldap.ReferralEnumeration
    public void appendUnprocessedReferrals(LdapReferralException ldapReferralException) {
        if (this.refEx != null) {
            this.refEx = this.refEx.appendUnprocessedReferrals(ldapReferralException);
        } else {
            this.refEx = ldapReferralException.appendUnprocessedReferrals(this.refEx);
        }
    }

    final void setNamingException(NamingException namingException) {
        this.errEx = namingException;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(6:15|32|16|(1:18)|19|20) */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x005d, code lost:
    
        r6 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0062, code lost:
    
        if (r4.errEx == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0065, code lost:
    
        r4.errEx = r6.getNamingException();
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006d, code lost:
    
        r4.refEx = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0079, code lost:
    
        r7 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007a, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007f, code lost:
    
        throw r7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected final boolean hasMoreReferrals() throws javax.naming.NamingException {
        /*
            r4 = this;
            r0 = r4
            com.sun.jndi.ldap.LdapReferralException r0 = r0.refEx
            if (r0 == 0) goto L85
            r0 = r4
            com.sun.jndi.ldap.LdapReferralException r0 = r0.refEx
            boolean r0 = r0.hasMoreReferrals()
            if (r0 != 0) goto L1b
            r0 = r4
            com.sun.jndi.ldap.LdapReferralException r0 = r0.refEx
            boolean r0 = r0.hasMoreReferralExceptions()
            if (r0 == 0) goto L85
        L1b:
            r0 = r4
            com.sun.jndi.ldap.LdapCtx r0 = r0.homeCtx
            int r0 = r0.handleReferrals
            r1 = 2
            if (r0 != r1) goto L34
            r0 = r4
            com.sun.jndi.ldap.LdapReferralException r0 = r0.refEx
            java.lang.Throwable r0 = r0.fillInStackTrace()
            javax.naming.NamingException r0 = (javax.naming.NamingException) r0
            javax.naming.NamingException r0 = (javax.naming.NamingException) r0
            throw r0
        L34:
            r0 = r4
            com.sun.jndi.ldap.LdapReferralException r0 = r0.refEx
            r1 = r4
            com.sun.jndi.ldap.LdapCtx r1 = r1.homeCtx
            java.util.Hashtable<java.lang.String, java.lang.Object> r1 = r1.envprops
            r2 = r4
            com.sun.jndi.ldap.LdapCtx r2 = r2.homeCtx
            javax.naming.ldap.Control[] r2 = r2.reqCtls
            javax.naming.Context r0 = r0.getReferralContext(r1, r2)
            com.sun.jndi.ldap.LdapReferralContext r0 = (com.sun.jndi.ldap.LdapReferralContext) r0
            r5 = r0
            r0 = r4
            r1 = r4
            r2 = r5
            com.sun.jndi.ldap.AbstractLdapNamingEnumeration r1 = r1.getReferredResults(r2)     // Catch: com.sun.jndi.ldap.LdapReferralException -> L5d java.lang.Throwable -> L79
            r0.update(r1)     // Catch: com.sun.jndi.ldap.LdapReferralException -> L5d java.lang.Throwable -> L79
            r0 = r5
            r0.close()
            goto L80
        L5d:
            r6 = move-exception
            r0 = r4
            javax.naming.NamingException r0 = r0.errEx     // Catch: java.lang.Throwable -> L79
            if (r0 != 0) goto L6d
            r0 = r4
            r1 = r6
            javax.naming.NamingException r1 = r1.getNamingException()     // Catch: java.lang.Throwable -> L79
            r0.errEx = r1     // Catch: java.lang.Throwable -> L79
        L6d:
            r0 = r4
            r1 = r6
            r0.refEx = r1     // Catch: java.lang.Throwable -> L79
            r0 = r5
            r0.close()
            goto L34
        L79:
            r7 = move-exception
            r0 = r5
            r0.close()
            r0 = r7
            throw r0
        L80:
            r0 = r4
            boolean r0 = r0.hasMoreImpl()
            return r0
        L85:
            r0 = r4
            r0.cleanup()
            r0 = r4
            javax.naming.NamingException r0 = r0.errEx
            if (r0 == 0) goto L95
            r0 = r4
            javax.naming.NamingException r0 = r0.errEx
            throw r0
        L95:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jndi.ldap.AbstractLdapNamingEnumeration.hasMoreReferrals():boolean");
    }

    protected void update(AbstractLdapNamingEnumeration<? extends NameClassPair> abstractLdapNamingEnumeration) {
        this.homeCtx.decEnumCount();
        this.homeCtx = abstractLdapNamingEnumeration.homeCtx;
        this.enumClnt = abstractLdapNamingEnumeration.enumClnt;
        abstractLdapNamingEnumeration.homeCtx = null;
        this.posn = abstractLdapNamingEnumeration.posn;
        this.limit = abstractLdapNamingEnumeration.limit;
        this.res = abstractLdapNamingEnumeration.res;
        this.entries = abstractLdapNamingEnumeration.entries;
        this.refEx = abstractLdapNamingEnumeration.refEx;
        this.listArg = abstractLdapNamingEnumeration.listArg;
    }

    protected final void finalize() {
        cleanup();
    }

    protected final void cleanup() {
        if (this.cleaned) {
            return;
        }
        if (this.enumClnt != null) {
            this.enumClnt.clearSearchReply(this.res, this.homeCtx.reqCtls);
        }
        this.enumClnt = null;
        this.cleaned = true;
        if (this.homeCtx != null) {
            this.homeCtx.decEnumCount();
            this.homeCtx = null;
        }
    }

    @Override // javax.naming.NamingEnumeration
    public final void close() {
        cleanup();
    }
}
