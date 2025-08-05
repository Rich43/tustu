package javax.script;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: rt.jar:javax/script/SimpleScriptContext.class */
public class SimpleScriptContext implements ScriptContext {
    private static List<Integer> scopes;
    protected Bindings engineScope = new SimpleBindings();
    protected Bindings globalScope = null;
    protected Reader reader = new InputStreamReader(System.in);
    protected Writer writer = new PrintWriter((OutputStream) System.out, true);
    protected Writer errorWriter = new PrintWriter((OutputStream) System.err, true);

    @Override // javax.script.ScriptContext
    public void setBindings(Bindings bindings, int i2) {
        switch (i2) {
            case 100:
                if (bindings == null) {
                    throw new NullPointerException("Engine scope cannot be null.");
                }
                this.engineScope = bindings;
                return;
            case 200:
                this.globalScope = bindings;
                return;
            default:
                throw new IllegalArgumentException("Invalid scope value.");
        }
    }

    @Override // javax.script.ScriptContext
    public Object getAttribute(String str) {
        checkName(str);
        if (this.engineScope.containsKey(str)) {
            return getAttribute(str, 100);
        }
        if (this.globalScope != null && this.globalScope.containsKey(str)) {
            return getAttribute(str, 200);
        }
        return null;
    }

    @Override // javax.script.ScriptContext
    public Object getAttribute(String str, int i2) {
        checkName(str);
        switch (i2) {
            case 100:
                return this.engineScope.get(str);
            case 200:
                if (this.globalScope != null) {
                    return this.globalScope.get(str);
                }
                return null;
            default:
                throw new IllegalArgumentException("Illegal scope value.");
        }
    }

    @Override // javax.script.ScriptContext
    public Object removeAttribute(String str, int i2) {
        checkName(str);
        switch (i2) {
            case 100:
                if (getBindings(100) != null) {
                    return getBindings(100).remove(str);
                }
                return null;
            case 200:
                if (getBindings(200) != null) {
                    return getBindings(200).remove(str);
                }
                return null;
            default:
                throw new IllegalArgumentException("Illegal scope value.");
        }
    }

    @Override // javax.script.ScriptContext
    public void setAttribute(String str, Object obj, int i2) {
        checkName(str);
        switch (i2) {
            case 100:
                this.engineScope.put(str, obj);
                return;
            case 200:
                if (this.globalScope != null) {
                    this.globalScope.put(str, obj);
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException("Illegal scope value.");
        }
    }

    @Override // javax.script.ScriptContext
    public Writer getWriter() {
        return this.writer;
    }

    @Override // javax.script.ScriptContext
    public Reader getReader() {
        return this.reader;
    }

    @Override // javax.script.ScriptContext
    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override // javax.script.ScriptContext
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override // javax.script.ScriptContext
    public Writer getErrorWriter() {
        return this.errorWriter;
    }

    @Override // javax.script.ScriptContext
    public void setErrorWriter(Writer writer) {
        this.errorWriter = writer;
    }

    @Override // javax.script.ScriptContext
    public int getAttributesScope(String str) {
        checkName(str);
        if (this.engineScope.containsKey(str)) {
            return 100;
        }
        if (this.globalScope != null && this.globalScope.containsKey(str)) {
            return 200;
        }
        return -1;
    }

    @Override // javax.script.ScriptContext
    public Bindings getBindings(int i2) {
        if (i2 == 100) {
            return this.engineScope;
        }
        if (i2 == 200) {
            return this.globalScope;
        }
        throw new IllegalArgumentException("Illegal scope value.");
    }

    @Override // javax.script.ScriptContext
    public List<Integer> getScopes() {
        return scopes;
    }

    private void checkName(String str) {
        Objects.requireNonNull(str);
        if (str.isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }
    }

    static {
        scopes = new ArrayList(2);
        scopes.add(100);
        scopes.add(200);
        scopes = Collections.unmodifiableList(scopes);
    }
}
