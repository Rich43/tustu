package javax.script;

import java.io.Reader;

/* loaded from: rt.jar:javax/script/AbstractScriptEngine.class */
public abstract class AbstractScriptEngine implements ScriptEngine {
    protected ScriptContext context;

    public AbstractScriptEngine() {
        this.context = new SimpleScriptContext();
    }

    public AbstractScriptEngine(Bindings bindings) {
        this();
        if (bindings == null) {
            throw new NullPointerException("n is null");
        }
        this.context.setBindings(bindings, 100);
    }

    @Override // javax.script.ScriptEngine
    public void setContext(ScriptContext scriptContext) {
        if (scriptContext == null) {
            throw new NullPointerException("null context");
        }
        this.context = scriptContext;
    }

    @Override // javax.script.ScriptEngine
    public ScriptContext getContext() {
        return this.context;
    }

    @Override // javax.script.ScriptEngine
    public Bindings getBindings(int i2) {
        if (i2 == 200) {
            return this.context.getBindings(200);
        }
        if (i2 == 100) {
            return this.context.getBindings(100);
        }
        throw new IllegalArgumentException("Invalid scope value.");
    }

    @Override // javax.script.ScriptEngine
    public void setBindings(Bindings bindings, int i2) {
        if (i2 == 200) {
            this.context.setBindings(bindings, 200);
        } else {
            if (i2 == 100) {
                this.context.setBindings(bindings, 100);
                return;
            }
            throw new IllegalArgumentException("Invalid scope value.");
        }
    }

    @Override // javax.script.ScriptEngine
    public void put(String str, Object obj) {
        Bindings bindings = getBindings(100);
        if (bindings != null) {
            bindings.put(str, obj);
        }
    }

    @Override // javax.script.ScriptEngine
    public Object get(String str) {
        Bindings bindings = getBindings(100);
        if (bindings != null) {
            return bindings.get(str);
        }
        return null;
    }

    @Override // javax.script.ScriptEngine
    public Object eval(Reader reader, Bindings bindings) throws ScriptException {
        return eval(reader, getScriptContext(bindings));
    }

    @Override // javax.script.ScriptEngine
    public Object eval(String str, Bindings bindings) throws ScriptException {
        return eval(str, getScriptContext(bindings));
    }

    @Override // javax.script.ScriptEngine
    public Object eval(Reader reader) throws ScriptException {
        return eval(reader, this.context);
    }

    @Override // javax.script.ScriptEngine
    public Object eval(String str) throws ScriptException {
        return eval(str, this.context);
    }

    protected ScriptContext getScriptContext(Bindings bindings) {
        SimpleScriptContext simpleScriptContext = new SimpleScriptContext();
        Bindings bindings2 = getBindings(200);
        if (bindings2 != null) {
            simpleScriptContext.setBindings(bindings2, 200);
        }
        if (bindings != null) {
            simpleScriptContext.setBindings(bindings, 100);
            simpleScriptContext.setReader(this.context.getReader());
            simpleScriptContext.setWriter(this.context.getWriter());
            simpleScriptContext.setErrorWriter(this.context.getErrorWriter());
            return simpleScriptContext;
        }
        throw new NullPointerException("Engine scope Bindings may not be null.");
    }
}
