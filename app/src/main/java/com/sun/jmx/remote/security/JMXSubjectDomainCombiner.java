package com.sun.jmx.remote.security;

import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import javax.security.auth.Subject;
import javax.security.auth.SubjectDomainCombiner;

/* loaded from: rt.jar:com/sun/jmx/remote/security/JMXSubjectDomainCombiner.class */
public class JMXSubjectDomainCombiner extends SubjectDomainCombiner {
    private static final CodeSource nullCodeSource = new CodeSource((URL) null, (Certificate[]) null);
    private static final ProtectionDomain pdNoPerms = new ProtectionDomain(nullCodeSource, new Permissions(), null, null);

    public JMXSubjectDomainCombiner(Subject subject) {
        super(subject);
    }

    @Override // javax.security.auth.SubjectDomainCombiner, java.security.DomainCombiner
    public ProtectionDomain[] combine(ProtectionDomain[] protectionDomainArr, ProtectionDomain[] protectionDomainArr2) {
        ProtectionDomain[] protectionDomainArr3;
        if (protectionDomainArr == null || protectionDomainArr.length == 0) {
            protectionDomainArr3 = new ProtectionDomain[]{pdNoPerms};
        } else {
            protectionDomainArr3 = new ProtectionDomain[protectionDomainArr.length + 1];
            for (int i2 = 0; i2 < protectionDomainArr.length; i2++) {
                protectionDomainArr3[i2] = protectionDomainArr[i2];
            }
            protectionDomainArr3[protectionDomainArr.length] = pdNoPerms;
        }
        return super.combine(protectionDomainArr3, protectionDomainArr2);
    }

    public static AccessControlContext getContext(Subject subject) {
        return new AccessControlContext(AccessController.getContext(), new JMXSubjectDomainCombiner(subject));
    }

    public static AccessControlContext getDomainCombinerContext(Subject subject) {
        return new AccessControlContext(new AccessControlContext(new ProtectionDomain[0]), new JMXSubjectDomainCombiner(subject));
    }
}
