package javax.management.relation;

import com.sun.jmx.mbeanserver.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:javax/management/relation/RoleUnresolvedList.class */
public class RoleUnresolvedList extends ArrayList<Object> {
    private transient boolean typeSafe;
    private transient boolean tainted;
    private static final long serialVersionUID = 4054902803091433324L;

    public RoleUnresolvedList() {
    }

    public RoleUnresolvedList(int i2) {
        super(i2);
    }

    public RoleUnresolvedList(List<RoleUnresolved> list) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException("Null parameter");
        }
        checkTypeSafe((Collection<?>) list);
        super.addAll(list);
    }

    public List<RoleUnresolved> asList() {
        if (!this.typeSafe) {
            if (this.tainted) {
                checkTypeSafe((Collection<?>) this);
            }
            this.typeSafe = true;
        }
        return (List) Util.cast(this);
    }

    public void add(RoleUnresolved roleUnresolved) throws IllegalArgumentException {
        if (roleUnresolved == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        super.add((RoleUnresolvedList) roleUnresolved);
    }

    public void add(int i2, RoleUnresolved roleUnresolved) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (roleUnresolved == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        super.add(i2, (int) roleUnresolved);
    }

    public void set(int i2, RoleUnresolved roleUnresolved) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (roleUnresolved == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        super.set(i2, (int) roleUnresolved);
    }

    public boolean addAll(RoleUnresolvedList roleUnresolvedList) throws IndexOutOfBoundsException {
        if (roleUnresolvedList == null) {
            return true;
        }
        return super.addAll((Collection) roleUnresolvedList);
    }

    public boolean addAll(int i2, RoleUnresolvedList roleUnresolvedList) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (roleUnresolvedList == null) {
            throw new IllegalArgumentException("Invalid parameter");
        }
        return super.addAll(i2, (Collection) roleUnresolvedList);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Object obj) {
        if (!this.tainted) {
            this.tainted = isTainted(obj);
        }
        if (this.typeSafe) {
            checkTypeSafe(obj);
        }
        return super.add((RoleUnresolvedList) obj);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public void add(int i2, Object obj) {
        if (!this.tainted) {
            this.tainted = isTainted(obj);
        }
        if (this.typeSafe) {
            checkTypeSafe(obj);
        }
        super.add(i2, (int) obj);
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<?> collection) {
        if (!this.tainted) {
            this.tainted = isTainted(collection);
        }
        if (this.typeSafe) {
            checkTypeSafe(collection);
        }
        return super.addAll(collection);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public boolean addAll(int i2, Collection<?> collection) {
        if (!this.tainted) {
            this.tainted = isTainted(collection);
        }
        if (this.typeSafe) {
            checkTypeSafe(collection);
        }
        return super.addAll(i2, collection);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public Object set(int i2, Object obj) {
        if (!this.tainted) {
            this.tainted = isTainted(obj);
        }
        if (this.typeSafe) {
            checkTypeSafe(obj);
        }
        return super.set(i2, (int) obj);
    }

    private static void checkTypeSafe(Object obj) {
        try {
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    private static void checkTypeSafe(Collection<?> collection) {
        try {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
            }
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    private static boolean isTainted(Object obj) {
        try {
            checkTypeSafe(obj);
            return false;
        } catch (IllegalArgumentException e2) {
            return true;
        }
    }

    private static boolean isTainted(Collection<?> collection) {
        try {
            checkTypeSafe(collection);
            return false;
        } catch (IllegalArgumentException e2) {
            return true;
        }
    }
}
