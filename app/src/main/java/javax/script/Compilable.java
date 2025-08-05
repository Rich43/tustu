package javax.script;

import java.io.Reader;

/* loaded from: rt.jar:javax/script/Compilable.class */
public interface Compilable {
    CompiledScript compile(String str) throws ScriptException;

    CompiledScript compile(Reader reader) throws ScriptException;
}
