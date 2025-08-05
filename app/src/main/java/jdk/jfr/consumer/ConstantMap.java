package jdk.jfr.consumer;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: jfr.jar:jdk/jfr/consumer/ConstantMap.class */
final class ConstantMap {
    private final ObjectFactory<?> factory;
    private final LongMap<Object> objects = new LongMap<>();
    private LongMap<Boolean> isResolving;
    private boolean allResolved;
    private String name;

    /* loaded from: jfr.jar:jdk/jfr/consumer/ConstantMap$Reference.class */
    private static final class Reference {
        private final long key;
        private final ConstantMap pool;

        Reference(ConstantMap constantMap, long j2) {
            this.pool = constantMap;
            this.key = j2;
        }

        Object resolve() {
            return this.pool.get(this.key);
        }
    }

    ConstantMap(ObjectFactory<?> objectFactory, String str) {
        this.name = str;
        this.factory = objectFactory;
    }

    Object get(long j2) {
        if (this.allResolved) {
            return this.objects.get(j2);
        }
        if (this.isResolving == null) {
            return new Reference(this, j2);
        }
        Boolean bool = this.isResolving.get(j2);
        if (Boolean.FALSE.equals(bool)) {
            return this.objects.get(j2);
        }
        if (Boolean.TRUE.equals(bool)) {
            return null;
        }
        this.isResolving.put(j2, Boolean.TRUE);
        Object objResolve = resolve(this.objects.get(j2));
        this.isResolving.put(j2, Boolean.FALSE);
        if (this.factory != null) {
            Object objCreateObject = this.factory.createObject(j2, objResolve);
            this.objects.put(j2, objCreateObject);
            return objCreateObject;
        }
        this.objects.put(j2, objResolve);
        return objResolve;
    }

    private static Object resolve(Object obj) {
        if (obj instanceof Reference) {
            return resolve(((Reference) obj).resolve());
        }
        if (obj != null && obj.getClass().isArray()) {
            Object[] objArr = (Object[]) obj;
            for (int i2 = 0; i2 < objArr.length; i2++) {
                objArr[i2] = resolve(objArr[i2]);
            }
            return objArr;
        }
        return obj;
    }

    public void resolve() {
        ArrayList arrayList = new ArrayList();
        Iterator<Long> itKeys = this.objects.keys();
        arrayList.getClass();
        itKeys.forEachRemaining((v1) -> {
            r1.add(v1);
        });
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            get(((Long) it.next()).longValue());
        }
    }

    public void put(long j2, Object obj) {
        this.objects.put(j2, obj);
    }

    public void setIsResolving() {
        this.isResolving = new LongMap<>();
    }

    public void setResolved() {
        this.allResolved = true;
        this.isResolving = null;
    }

    public String getName() {
        return this.name;
    }
}
