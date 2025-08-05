package java.beans;

/* loaded from: rt.jar:java/beans/PersistenceDelegate.class */
public abstract class PersistenceDelegate {
    protected abstract Expression instantiate(Object obj, Encoder encoder);

    public void writeObject(Object obj, Encoder encoder) {
        Object obj2 = encoder.get(obj);
        if (!mutatesTo(obj, obj2)) {
            encoder.remove(obj);
            encoder.writeExpression(instantiate(obj, encoder));
        } else {
            initialize(obj.getClass(), obj, obj2, encoder);
        }
    }

    protected boolean mutatesTo(Object obj, Object obj2) {
        return (obj2 == null || obj == null || obj.getClass() != obj2.getClass()) ? false : true;
    }

    protected void initialize(Class<?> cls, Object obj, Object obj2, Encoder encoder) {
        Class<? super Object> superclass = cls.getSuperclass();
        encoder.getPersistenceDelegate(superclass).initialize(superclass, obj, obj2, encoder);
    }
}
