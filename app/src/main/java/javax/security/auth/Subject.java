package javax.security.auth;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.DomainCombiner;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import sun.security.util.ResourcesMgr;

/* loaded from: rt.jar:javax/security/auth/Subject.class */
public final class Subject implements Serializable {
    private static final long serialVersionUID = -8308522755600156056L;
    Set<Principal> principals;
    transient Set<Object> pubCredentials;
    transient Set<Object> privCredentials;
    private volatile boolean readOnly;
    private static final int PRINCIPAL_SET = 1;
    private static final int PUB_CREDENTIAL_SET = 2;
    private static final int PRIV_CREDENTIAL_SET = 3;
    private static final ProtectionDomain[] NULL_PD_ARRAY = new ProtectionDomain[0];

    public Subject() {
        this.readOnly = false;
        this.principals = Collections.synchronizedSet(new SecureSet(this, 1));
        this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2));
        this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3));
    }

    public Subject(boolean z2, Set<? extends Principal> set, Set<?> set2, Set<?> set3) {
        this.readOnly = false;
        if (set == null || set2 == null || set3 == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.input.s."));
        }
        this.principals = Collections.synchronizedSet(new SecureSet(this, 1, set));
        this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2, set2));
        this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3, set3));
        this.readOnly = z2;
    }

    public void setReadOnly() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.SET_READ_ONLY_PERMISSION);
        }
        this.readOnly = true;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public static Subject getSubject(final AccessControlContext accessControlContext) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.GET_SUBJECT_PERMISSION);
        }
        if (accessControlContext == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.AccessControlContext.provided"));
        }
        return (Subject) AccessController.doPrivileged(new PrivilegedAction<Subject>() { // from class: javax.security.auth.Subject.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Subject run() {
                DomainCombiner domainCombiner = accessControlContext.getDomainCombiner();
                if (!(domainCombiner instanceof SubjectDomainCombiner)) {
                    return null;
                }
                return ((SubjectDomainCombiner) domainCombiner).getSubject();
            }
        });
    }

    public static <T> T doAs(Subject subject, PrivilegedAction<T> privilegedAction) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.DO_AS_PERMISSION);
        }
        if (privilegedAction == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
        }
        return (T) AccessController.doPrivileged(privilegedAction, createContext(subject, AccessController.getContext()));
    }

    public static <T> T doAs(Subject subject, PrivilegedExceptionAction<T> privilegedExceptionAction) throws PrivilegedActionException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.DO_AS_PERMISSION);
        }
        if (privilegedExceptionAction == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
        }
        return (T) AccessController.doPrivileged(privilegedExceptionAction, createContext(subject, AccessController.getContext()));
    }

    public static <T> T doAsPrivileged(Subject subject, PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.DO_AS_PRIVILEGED_PERMISSION);
        }
        if (privilegedAction == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
        }
        return (T) AccessController.doPrivileged(privilegedAction, createContext(subject, accessControlContext == null ? new AccessControlContext(NULL_PD_ARRAY) : accessControlContext));
    }

    public static <T> T doAsPrivileged(Subject subject, PrivilegedExceptionAction<T> privilegedExceptionAction, AccessControlContext accessControlContext) throws PrivilegedActionException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.DO_AS_PRIVILEGED_PERMISSION);
        }
        if (privilegedExceptionAction == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
        }
        return (T) AccessController.doPrivileged(privilegedExceptionAction, createContext(subject, accessControlContext == null ? new AccessControlContext(NULL_PD_ARRAY) : accessControlContext));
    }

    private static AccessControlContext createContext(Subject subject, final AccessControlContext accessControlContext) {
        return (AccessControlContext) AccessController.doPrivileged(new PrivilegedAction<AccessControlContext>() { // from class: javax.security.auth.Subject.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public AccessControlContext run() {
                if (Subject.this == null) {
                    return new AccessControlContext(accessControlContext, (DomainCombiner) null);
                }
                return new AccessControlContext(accessControlContext, new SubjectDomainCombiner(Subject.this));
            }
        });
    }

    public Set<Principal> getPrincipals() {
        return this.principals;
    }

    public <T extends Principal> Set<T> getPrincipals(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
        }
        return new ClassSet(1, cls);
    }

    public Set<Object> getPublicCredentials() {
        return this.pubCredentials;
    }

    public Set<Object> getPrivateCredentials() {
        return this.privCredentials;
    }

    public <T> Set<T> getPublicCredentials(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
        }
        return new ClassSet(2, cls);
    }

    public <T> Set<T> getPrivateCredentials(Class<T> cls) {
        if (cls == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
        }
        return new ClassSet(3, cls);
    }

    public boolean equals(Object obj) {
        HashSet hashSet;
        HashSet hashSet2;
        HashSet hashSet3;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Subject) {
            Subject subject = (Subject) obj;
            synchronized (subject.principals) {
                hashSet = new HashSet(subject.principals);
            }
            if (!this.principals.equals(hashSet)) {
                return false;
            }
            synchronized (subject.pubCredentials) {
                hashSet2 = new HashSet(subject.pubCredentials);
            }
            if (!this.pubCredentials.equals(hashSet2)) {
                return false;
            }
            synchronized (subject.privCredentials) {
                hashSet3 = new HashSet(subject.privCredentials);
            }
            if (!this.privCredentials.equals(hashSet3)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        return toString(true);
    }

    String toString(boolean z2) {
        String string = ResourcesMgr.getString("Subject.");
        String str = "";
        synchronized (this.principals) {
            Iterator<Principal> it = this.principals.iterator();
            while (it.hasNext()) {
                str = str + ResourcesMgr.getString(".Principal.") + it.next().toString() + ResourcesMgr.getString("NEWLINE");
            }
        }
        synchronized (this.pubCredentials) {
            Iterator<Object> it2 = this.pubCredentials.iterator();
            while (it2.hasNext()) {
                str = str + ResourcesMgr.getString(".Public.Credential.") + it2.next().toString() + ResourcesMgr.getString("NEWLINE");
            }
        }
        if (z2) {
            synchronized (this.privCredentials) {
                Iterator<Object> it3 = this.privCredentials.iterator();
                while (it3.hasNext()) {
                    try {
                        str = str + ResourcesMgr.getString(".Private.Credential.") + it3.next().toString() + ResourcesMgr.getString("NEWLINE");
                    } catch (SecurityException e2) {
                        str = str + ResourcesMgr.getString(".Private.Credential.inaccessible.");
                    }
                }
            }
        }
        return string + str;
    }

    public int hashCode() {
        int credHashCode = 0;
        synchronized (this.principals) {
            Iterator<Principal> it = this.principals.iterator();
            while (it.hasNext()) {
                credHashCode ^= it.next().hashCode();
            }
        }
        synchronized (this.pubCredentials) {
            Iterator<Object> it2 = this.pubCredentials.iterator();
            while (it2.hasNext()) {
                credHashCode ^= getCredHashCode(it2.next());
            }
        }
        return credHashCode;
    }

    private int getCredHashCode(Object obj) {
        try {
            return obj.hashCode();
        } catch (IllegalStateException e2) {
            return obj.getClass().toString().hashCode();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        synchronized (this.principals) {
            objectOutputStream.defaultWriteObject();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.readOnly = fields.get("readOnly", false);
        Set set = (Set) fields.get("principals", (Object) null);
        if (set == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.input.s."));
        }
        try {
            this.principals = Collections.synchronizedSet(new SecureSet(this, 1, set));
        } catch (NullPointerException e2) {
            this.principals = Collections.synchronizedSet(new SecureSet(this, 1));
        }
        this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2));
        this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3));
    }

    /* loaded from: rt.jar:javax/security/auth/Subject$SecureSet.class */
    private static class SecureSet<E> extends AbstractSet<E> implements Serializable {
        private static final long serialVersionUID = 7911754171111800359L;
        private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("this$0", Subject.class), new ObjectStreamField(Constants.ATTRNAME_ELEMENTS, LinkedList.class), new ObjectStreamField("which", Integer.TYPE)};
        Subject subject;
        LinkedList<E> elements;
        private int which;

        SecureSet(Subject subject, int i2) {
            this.subject = subject;
            this.which = i2;
            this.elements = new LinkedList<>();
        }

        SecureSet(Subject subject, int i2, Set<? extends E> set) {
            this.subject = subject;
            this.which = i2;
            this.elements = new LinkedList<>(set);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.elements.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            final LinkedList<E> linkedList = this.elements;
            return new Iterator<E>() { // from class: javax.security.auth.Subject.SecureSet.1

                /* renamed from: i, reason: collision with root package name */
                ListIterator<E> f12798i;

                {
                    this.f12798i = linkedList.listIterator(0);
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.f12798i.hasNext();
                }

                @Override // java.util.Iterator
                public E next() {
                    if (SecureSet.this.which != 3) {
                        return this.f12798i.next();
                    }
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkPermission(new PrivateCredentialPermission(linkedList.get(this.f12798i.nextIndex()).getClass().getName(), SecureSet.this.subject.getPrincipals()));
                        } catch (SecurityException e2) {
                            this.f12798i.next();
                            throw e2;
                        }
                    }
                    return this.f12798i.next();
                }

                @Override // java.util.Iterator
                public void remove() {
                    if (SecureSet.this.subject.isReadOnly()) {
                        throw new IllegalStateException(ResourcesMgr.getString("Subject.is.read.only"));
                    }
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        switch (SecureSet.this.which) {
                            case 1:
                                securityManager.checkPermission(AuthPermissionHolder.MODIFY_PRINCIPALS_PERMISSION);
                                break;
                            case 2:
                                securityManager.checkPermission(AuthPermissionHolder.MODIFY_PUBLIC_CREDENTIALS_PERMISSION);
                                break;
                            default:
                                securityManager.checkPermission(AuthPermissionHolder.MODIFY_PRIVATE_CREDENTIALS_PERMISSION);
                                break;
                        }
                    }
                    this.f12798i.remove();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(E e2) {
            if (this.subject.isReadOnly()) {
                throw new IllegalStateException(ResourcesMgr.getString("Subject.is.read.only"));
            }
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                switch (this.which) {
                    case 1:
                        securityManager.checkPermission(AuthPermissionHolder.MODIFY_PRINCIPALS_PERMISSION);
                        break;
                    case 2:
                        securityManager.checkPermission(AuthPermissionHolder.MODIFY_PUBLIC_CREDENTIALS_PERMISSION);
                        break;
                    default:
                        securityManager.checkPermission(AuthPermissionHolder.MODIFY_PRIVATE_CREDENTIALS_PERMISSION);
                        break;
                }
            }
            switch (this.which) {
                case 1:
                    if (!(e2 instanceof Principal)) {
                        throw new SecurityException(ResourcesMgr.getString("attempting.to.add.an.object.which.is.not.an.instance.of.java.security.Principal.to.a.Subject.s.Principal.Set"));
                    }
                    break;
            }
            if (!this.elements.contains(e2)) {
                return this.elements.add(e2);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Object objDoPrivileged;
            final Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (this.which != 3) {
                    objDoPrivileged = it.next();
                } else {
                    objDoPrivileged = AccessController.doPrivileged(new PrivilegedAction<E>() { // from class: javax.security.auth.Subject.SecureSet.2
                        @Override // java.security.PrivilegedAction
                        public E run() {
                            return (E) it.next();
                        }
                    });
                }
                if (objDoPrivileged == null) {
                    if (obj == null) {
                        it.remove();
                        return true;
                    }
                } else if (objDoPrivileged.equals(obj)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            Object objDoPrivileged;
            final Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (this.which != 3) {
                    objDoPrivileged = it.next();
                } else {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkPermission(new PrivateCredentialPermission(obj.getClass().getName(), this.subject.getPrincipals()));
                    }
                    objDoPrivileged = AccessController.doPrivileged(new PrivilegedAction<E>() { // from class: javax.security.auth.Subject.SecureSet.3
                        @Override // java.security.PrivilegedAction
                        public E run() {
                            return (E) it.next();
                        }
                    });
                }
                if (objDoPrivileged == null) {
                    if (obj == null) {
                        return true;
                    }
                } else if (objDoPrivileged.equals(obj)) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            Object objDoPrivileged;
            Objects.requireNonNull(collection);
            boolean z2 = false;
            final Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (this.which != 3) {
                    objDoPrivileged = it.next();
                } else {
                    objDoPrivileged = AccessController.doPrivileged(new PrivilegedAction<E>() { // from class: javax.security.auth.Subject.SecureSet.4
                        @Override // java.security.PrivilegedAction
                        public E run() {
                            return (E) it.next();
                        }
                    });
                }
                Iterator<?> it2 = collection.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        Object next = it2.next();
                        if (objDoPrivileged == null) {
                            if (next == null) {
                                it.remove();
                                z2 = true;
                                break;
                            }
                        } else if (objDoPrivileged.equals(next)) {
                            it.remove();
                            z2 = true;
                            break;
                        }
                    }
                }
            }
            return z2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            Object objDoPrivileged;
            Objects.requireNonNull(collection);
            boolean z2 = false;
            final Iterator<E> it = iterator();
            while (it.hasNext()) {
                boolean z3 = false;
                if (this.which != 3) {
                    objDoPrivileged = it.next();
                } else {
                    objDoPrivileged = AccessController.doPrivileged(new PrivilegedAction<E>() { // from class: javax.security.auth.Subject.SecureSet.5
                        @Override // java.security.PrivilegedAction
                        public E run() {
                            return (E) it.next();
                        }
                    });
                }
                Iterator<?> it2 = collection.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Object next = it2.next();
                    if (objDoPrivileged == null) {
                        if (next == null) {
                            z3 = true;
                            break;
                        }
                    } else if (objDoPrivileged.equals(next)) {
                        z3 = true;
                        break;
                    }
                }
                if (!z3) {
                    it.remove();
                    z2 = true;
                }
            }
            return z2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            E next;
            final Iterator<E> it = iterator();
            while (it.hasNext()) {
                if (this.which != 3) {
                    next = it.next();
                } else {
                    next = (E) AccessController.doPrivileged(new PrivilegedAction<E>() { // from class: javax.security.auth.Subject.SecureSet.6
                        @Override // java.security.PrivilegedAction
                        public E run() {
                            return (E) it.next();
                        }
                    });
                }
                it.remove();
            }
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            if (this.which == 3) {
                Iterator<E> it = iterator();
                while (it.hasNext()) {
                    it.next();
                }
            }
            ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
            putFieldPutFields.put("this$0", this.subject);
            putFieldPutFields.put(Constants.ATTRNAME_ELEMENTS, this.elements);
            putFieldPutFields.put("which", this.which);
            objectOutputStream.writeFields();
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            ObjectInputStream.GetField fields = objectInputStream.readFields();
            this.subject = (Subject) fields.get("this$0", (Object) null);
            this.which = fields.get("which", 0);
            LinkedList<E> linkedList = (LinkedList) fields.get(Constants.ATTRNAME_ELEMENTS, (Object) null);
            if (linkedList.getClass() != LinkedList.class) {
                this.elements = new LinkedList<>(linkedList);
            } else {
                this.elements = linkedList;
            }
        }
    }

    /* loaded from: rt.jar:javax/security/auth/Subject$ClassSet.class */
    private class ClassSet<T> extends AbstractSet<T> {
        private int which;

        /* renamed from: c, reason: collision with root package name */
        private Class<T> f12797c;
        private Set<T> set = new HashSet();

        ClassSet(int i2, Class<T> cls) {
            this.which = i2;
            this.f12797c = cls;
            switch (i2) {
                case 1:
                    synchronized (Subject.this.principals) {
                        populateSet();
                    }
                    return;
                case 2:
                    synchronized (Subject.this.pubCredentials) {
                        populateSet();
                    }
                    return;
                default:
                    synchronized (Subject.this.privCredentials) {
                        populateSet();
                    }
                    return;
            }
        }

        private void populateSet() {
            Iterator<Object> it;
            Object next;
            switch (this.which) {
                case 1:
                    it = Subject.this.principals.iterator();
                    break;
                case 2:
                    it = Subject.this.pubCredentials.iterator();
                    break;
                default:
                    it = Subject.this.privCredentials.iterator();
                    break;
            }
            while (it.hasNext()) {
                if (this.which == 3) {
                    final Iterator<Object> it2 = it;
                    next = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javax.security.auth.Subject.ClassSet.1
                        @Override // java.security.PrivilegedAction
                        public Object run() {
                            return it2.next();
                        }
                    });
                } else {
                    next = it.next();
                }
                if (this.f12797c.isAssignableFrom(next.getClass())) {
                    if (this.which != 3) {
                        this.set.add(next);
                    } else {
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            securityManager.checkPermission(new PrivateCredentialPermission(next.getClass().getName(), Subject.this.getPrincipals()));
                        }
                        this.set.add(next);
                    }
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.set.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<T> iterator() {
            return this.set.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(T t2) {
            if (!t2.getClass().isAssignableFrom(this.f12797c)) {
                throw new SecurityException(new MessageFormat(ResourcesMgr.getString("attempting.to.add.an.object.which.is.not.an.instance.of.class")).format(new Object[]{this.f12797c.toString()}));
            }
            return this.set.add(t2);
        }
    }

    /* loaded from: rt.jar:javax/security/auth/Subject$AuthPermissionHolder.class */
    static class AuthPermissionHolder {
        static final AuthPermission DO_AS_PERMISSION = new AuthPermission("doAs");
        static final AuthPermission DO_AS_PRIVILEGED_PERMISSION = new AuthPermission("doAsPrivileged");
        static final AuthPermission SET_READ_ONLY_PERMISSION = new AuthPermission("setReadOnly");
        static final AuthPermission GET_SUBJECT_PERMISSION = new AuthPermission("getSubject");
        static final AuthPermission MODIFY_PRINCIPALS_PERMISSION = new AuthPermission("modifyPrincipals");
        static final AuthPermission MODIFY_PUBLIC_CREDENTIALS_PERMISSION = new AuthPermission("modifyPublicCredentials");
        static final AuthPermission MODIFY_PRIVATE_CREDENTIALS_PERMISSION = new AuthPermission("modifyPrivateCredentials");

        AuthPermissionHolder() {
        }
    }
}
