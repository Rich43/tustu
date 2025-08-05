package sun.security.provider;

import java.io.Serializable;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javax.security.auth.Subject;
import sun.security.provider.PolicyParser;
import sun.security.util.Debug;

/* loaded from: rt.jar:sun/security/provider/SubjectCodeSource.class */
class SubjectCodeSource extends CodeSource implements Serializable {
    private static final long serialVersionUID = 6039418085604715275L;
    private Subject subject;
    private LinkedList<PolicyParser.PrincipalEntry> principals;
    private ClassLoader sysClassLoader;
    private static final ResourceBundle rb = (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: sun.security.provider.SubjectCodeSource.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ResourceBundle run2() {
            return ResourceBundle.getBundle("sun.security.util.AuthResources");
        }
    });
    private static final Class<?>[] PARAMS = {String.class};
    private static final Debug debug = Debug.getInstance("auth", "\t[Auth Access]");

    SubjectCodeSource(Subject subject, LinkedList<PolicyParser.PrincipalEntry> linkedList, URL url, Certificate[] certificateArr) {
        super(url, certificateArr);
        this.subject = subject;
        this.principals = linkedList == null ? new LinkedList<>() : new LinkedList<>(linkedList);
        this.sysClassLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: sun.security.provider.SubjectCodeSource.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }

    LinkedList<PolicyParser.PrincipalEntry> getPrincipals() {
        return this.principals;
    }

    Subject getSubject() {
        return this.subject;
    }

    @Override // java.security.CodeSource
    public boolean implies(CodeSource codeSource) {
        LinkedList<PolicyParser.PrincipalEntry> linkedList = null;
        if (codeSource == null || !(codeSource instanceof SubjectCodeSource) || !super.implies(codeSource)) {
            if (debug != null) {
                debug.println("\tSubjectCodeSource.implies: FAILURE 1");
                return false;
            }
            return false;
        }
        SubjectCodeSource subjectCodeSource = (SubjectCodeSource) codeSource;
        if (this.principals == null) {
            if (debug != null) {
                debug.println("\tSubjectCodeSource.implies: PASS 1");
                return true;
            }
            return true;
        }
        if (subjectCodeSource.getSubject() == null || subjectCodeSource.getSubject().getPrincipals().size() == 0) {
            if (debug != null) {
                debug.println("\tSubjectCodeSource.implies: FAILURE 2");
                return false;
            }
            return false;
        }
        ListIterator<PolicyParser.PrincipalEntry> listIterator = this.principals.listIterator(0);
        while (listIterator.hasNext()) {
            PolicyParser.PrincipalEntry next = listIterator.next();
            try {
                Class<?> cls = Class.forName(next.principalClass, true, this.sysClassLoader);
                if (!Principal.class.isAssignableFrom(cls)) {
                    throw new ClassCastException(next.principalClass + " is not a Principal");
                }
                if (!((Principal) cls.getConstructor(PARAMS).newInstance(next.principalName)).implies(subjectCodeSource.getSubject())) {
                    if (debug != null) {
                        debug.println("\tSubjectCodeSource.implies: FAILURE 3");
                        return false;
                    }
                    return false;
                }
                if (debug != null) {
                    debug.println("\tSubjectCodeSource.implies: PASS 2");
                    return true;
                }
                return true;
            } catch (Exception e2) {
                if (linkedList == null) {
                    if (subjectCodeSource.getSubject() == null) {
                        if (debug != null) {
                            debug.println("\tSubjectCodeSource.implies: FAILURE 4");
                            return false;
                        }
                        return false;
                    }
                    linkedList = new LinkedList<>();
                    for (Principal principal : subjectCodeSource.getSubject().getPrincipals()) {
                        linkedList.add(new PolicyParser.PrincipalEntry(principal.getClass().getName(), principal.getName()));
                    }
                }
                if (!subjectListImpliesPrincipalEntry(linkedList, next)) {
                    if (debug != null) {
                        debug.println("\tSubjectCodeSource.implies: FAILURE 5");
                        return false;
                    }
                    return false;
                }
            }
        }
        if (debug != null) {
            debug.println("\tSubjectCodeSource.implies: PASS 3");
            return true;
        }
        return true;
    }

    private boolean subjectListImpliesPrincipalEntry(LinkedList<PolicyParser.PrincipalEntry> linkedList, PolicyParser.PrincipalEntry principalEntry) {
        ListIterator<PolicyParser.PrincipalEntry> listIterator = linkedList.listIterator(0);
        while (listIterator.hasNext()) {
            PolicyParser.PrincipalEntry next = listIterator.next();
            if (principalEntry.getPrincipalClass().equals(PolicyParser.PrincipalEntry.WILDCARD_CLASS) || principalEntry.getPrincipalClass().equals(next.getPrincipalClass())) {
                if (principalEntry.getPrincipalName().equals(PolicyParser.PrincipalEntry.WILDCARD_NAME) || principalEntry.getPrincipalName().equals(next.getPrincipalName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // java.security.CodeSource
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof SubjectCodeSource)) {
            return false;
        }
        SubjectCodeSource subjectCodeSource = (SubjectCodeSource) obj;
        try {
            if (getSubject() != subjectCodeSource.getSubject()) {
                return false;
            }
            if (this.principals == null && subjectCodeSource.principals != null) {
                return false;
            }
            if (this.principals != null && subjectCodeSource.principals == null) {
                return false;
            }
            if (this.principals != null && subjectCodeSource.principals != null) {
                if (!this.principals.containsAll(subjectCodeSource.principals) || !subjectCodeSource.principals.containsAll(this.principals)) {
                    return false;
                }
                return true;
            }
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    @Override // java.security.CodeSource
    public int hashCode() {
        return super.hashCode();
    }

    @Override // java.security.CodeSource
    public String toString() {
        String string = super.toString();
        if (getSubject() != null) {
            if (debug != null) {
                final Subject subject = getSubject();
                string = string + "\n" + ((String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.provider.SubjectCodeSource.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public String run2() {
                        return subject.toString();
                    }
                }));
            } else {
                string = string + "\n" + getSubject().toString();
            }
        }
        if (this.principals != null) {
            ListIterator<PolicyParser.PrincipalEntry> listIterator = this.principals.listIterator();
            while (listIterator.hasNext()) {
                PolicyParser.PrincipalEntry next = listIterator.next();
                string = string + rb.getString("NEWLINE") + next.getPrincipalClass() + " " + next.getPrincipalName();
            }
        }
        return string;
    }
}
