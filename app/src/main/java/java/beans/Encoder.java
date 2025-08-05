package java.beans;

import com.sun.beans.finder.PersistenceDelegateFinder;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/* loaded from: rt.jar:java/beans/Encoder.class */
public class Encoder {
    private ExceptionListener exceptionListener;
    private Map<Object, Object> attributes;
    private final PersistenceDelegateFinder finder = new PersistenceDelegateFinder();
    private Map<Object, Expression> bindings = new IdentityHashMap();
    boolean executeStatements = true;

    protected void writeObject(Object obj) {
        if (obj == this) {
            return;
        }
        getPersistenceDelegate(obj == null ? null : obj.getClass()).writeObject(obj, this);
    }

    public void setExceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    public ExceptionListener getExceptionListener() {
        return this.exceptionListener != null ? this.exceptionListener : Statement.defaultExceptionListener;
    }

    Object getValue(Expression expression) {
        if (expression == null) {
            return null;
        }
        try {
            return expression.getValue();
        } catch (Exception e2) {
            getExceptionListener().exceptionThrown(e2);
            throw new RuntimeException("failed to evaluate: " + expression.toString());
        }
    }

    public PersistenceDelegate getPersistenceDelegate(Class<?> cls) throws SecurityException {
        PersistenceDelegate persistenceDelegateFind = this.finder.find(cls);
        if (persistenceDelegateFind == null) {
            persistenceDelegateFind = MetaData.getPersistenceDelegate(cls);
            if (persistenceDelegateFind != null) {
                this.finder.register(cls, persistenceDelegateFind);
            }
        }
        return persistenceDelegateFind;
    }

    public void setPersistenceDelegate(Class<?> cls, PersistenceDelegate persistenceDelegate) {
        this.finder.register(cls, persistenceDelegate);
    }

    public Object remove(Object obj) {
        return getValue(this.bindings.remove(obj));
    }

    public Object get(Object obj) {
        if (obj == null || obj == this || obj.getClass() == String.class) {
            return obj;
        }
        return getValue(this.bindings.get(obj));
    }

    private Object writeObject1(Object obj) {
        Object obj2 = get(obj);
        if (obj2 == null) {
            writeObject(obj);
            obj2 = get(obj);
        }
        return obj2;
    }

    private Statement cloneStatement(Statement statement) {
        Statement expression;
        Object objWriteObject1 = writeObject1(statement.getTarget());
        Object[] arguments = statement.getArguments();
        Object[] objArr = new Object[arguments.length];
        for (int i2 = 0; i2 < arguments.length; i2++) {
            objArr[i2] = writeObject1(arguments[i2]);
        }
        if (Statement.class.equals(statement.getClass())) {
            expression = new Statement(objWriteObject1, statement.getMethodName(), objArr);
        } else {
            expression = new Expression(objWriteObject1, statement.getMethodName(), objArr);
        }
        Statement statement2 = expression;
        statement2.loader = statement.loader;
        return statement2;
    }

    public void writeStatement(Statement statement) {
        Statement statementCloneStatement = cloneStatement(statement);
        if (statement.getTarget() != this && this.executeStatements) {
            try {
                statementCloneStatement.execute();
            } catch (Exception e2) {
                getExceptionListener().exceptionThrown(new Exception("Encoder: discarding statement " + ((Object) statementCloneStatement), e2));
            }
        }
    }

    public void writeExpression(Expression expression) {
        Object value = getValue(expression);
        if (get(value) != null) {
            return;
        }
        this.bindings.put(value, (Expression) cloneStatement(expression));
        writeObject(value);
    }

    void clear() {
        this.bindings.clear();
    }

    void setAttribute(Object obj, Object obj2) {
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }
        this.attributes.put(obj, obj2);
    }

    Object getAttribute(Object obj) {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get(obj);
    }
}
