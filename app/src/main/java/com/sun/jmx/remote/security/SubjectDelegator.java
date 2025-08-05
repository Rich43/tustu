package com.sun.jmx.remote.security;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javax.management.remote.SubjectDelegationPermission;
import javax.security.auth.Subject;

/* loaded from: rt.jar:com/sun/jmx/remote/security/SubjectDelegator.class */
public class SubjectDelegator {
    public AccessControlContext delegatedContext(AccessControlContext accessControlContext, Subject subject, boolean z2) throws SecurityException {
        if (System.getSecurityManager() != null && accessControlContext == null) {
            throw new SecurityException("Illegal AccessControlContext: null");
        }
        Collection<Principal> subjectPrincipals = getSubjectPrincipals(subject);
        final ArrayList arrayList = new ArrayList(subjectPrincipals.size());
        for (Principal principal : subjectPrincipals) {
            arrayList.add(new SubjectDelegationPermission(principal.getClass().getName() + "." + principal.getName()));
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.jmx.remote.security.SubjectDelegator.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws AccessControlException {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    AccessController.checkPermission((Permission) it.next());
                }
                return null;
            }
        }, accessControlContext);
        return getDelegatedAcc(subject, z2);
    }

    private AccessControlContext getDelegatedAcc(Subject subject, boolean z2) {
        if (z2) {
            return JMXSubjectDomainCombiner.getDomainCombinerContext(subject);
        }
        return JMXSubjectDomainCombiner.getContext(subject);
    }

    public static synchronized boolean checkRemoveCallerContext(Subject subject) {
        try {
            for (Principal principal : getSubjectPrincipals(subject)) {
                AccessController.checkPermission(new SubjectDelegationPermission(principal.getClass().getName() + "." + principal.getName()));
            }
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    private static Collection<Principal> getSubjectPrincipals(Subject subject) {
        if (subject.isReadOnly()) {
            return subject.getPrincipals();
        }
        return Collections.unmodifiableList(Arrays.asList(subject.getPrincipals().toArray(new Principal[0])));
    }
}
