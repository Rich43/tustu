package java.beans;

/* loaded from: rt.jar:java/beans/Expression.class */
public class Expression extends Statement {
    private static Object unbound = new Object();
    private Object value;

    @ConstructorProperties({"target", "methodName", "arguments"})
    public Expression(Object obj, String str, Object[] objArr) {
        super(obj, str, objArr);
        this.value = unbound;
    }

    public Expression(Object obj, Object obj2, String str, Object[] objArr) {
        this(obj2, str, objArr);
        setValue(obj);
    }

    @Override // java.beans.Statement
    public void execute() throws Exception {
        setValue(invoke());
    }

    public Object getValue() throws Exception {
        if (this.value == unbound) {
            setValue(invoke());
        }
        return this.value;
    }

    public void setValue(Object obj) {
        this.value = obj;
    }

    @Override // java.beans.Statement
    String instanceName(Object obj) {
        return obj == unbound ? "<unbound>" : super.instanceName(obj);
    }

    @Override // java.beans.Statement
    public String toString() {
        return instanceName(this.value) + "=" + super.toString();
    }
}
