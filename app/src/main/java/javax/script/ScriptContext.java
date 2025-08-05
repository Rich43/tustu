package javax.script;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

/* loaded from: rt.jar:javax/script/ScriptContext.class */
public interface ScriptContext {
    public static final int ENGINE_SCOPE = 100;
    public static final int GLOBAL_SCOPE = 200;

    void setBindings(Bindings bindings, int i2);

    Bindings getBindings(int i2);

    void setAttribute(String str, Object obj, int i2);

    Object getAttribute(String str, int i2);

    Object removeAttribute(String str, int i2);

    Object getAttribute(String str);

    int getAttributesScope(String str);

    Writer getWriter();

    Writer getErrorWriter();

    void setWriter(Writer writer);

    void setErrorWriter(Writer writer);

    Reader getReader();

    void setReader(Reader reader);

    List<Integer> getScopes();
}
