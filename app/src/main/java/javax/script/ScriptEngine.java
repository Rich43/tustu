package javax.script;

import java.io.Reader;

/* loaded from: rt.jar:javax/script/ScriptEngine.class */
public interface ScriptEngine {
    public static final String ARGV = "javax.script.argv";
    public static final String FILENAME = "javax.script.filename";
    public static final String ENGINE = "javax.script.engine";
    public static final String ENGINE_VERSION = "javax.script.engine_version";
    public static final String NAME = "javax.script.name";
    public static final String LANGUAGE = "javax.script.language";
    public static final String LANGUAGE_VERSION = "javax.script.language_version";

    Object eval(String str, ScriptContext scriptContext) throws ScriptException;

    Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException;

    Object eval(String str) throws ScriptException;

    Object eval(Reader reader) throws ScriptException;

    Object eval(String str, Bindings bindings) throws ScriptException;

    Object eval(Reader reader, Bindings bindings) throws ScriptException;

    void put(String str, Object obj);

    Object get(String str);

    Bindings getBindings(int i2);

    void setBindings(Bindings bindings, int i2);

    Bindings createBindings();

    ScriptContext getContext();

    void setContext(ScriptContext scriptContext);

    ScriptEngineFactory getFactory();
}
