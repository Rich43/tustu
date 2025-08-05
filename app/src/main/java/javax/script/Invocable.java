package javax.script;

/* loaded from: rt.jar:javax/script/Invocable.class */
public interface Invocable {
    Object invokeMethod(Object obj, String str, Object... objArr) throws NoSuchMethodException, ScriptException;

    Object invokeFunction(String str, Object... objArr) throws NoSuchMethodException, ScriptException;

    <T> T getInterface(Class<T> cls);

    <T> T getInterface(Object obj, Class<T> cls);
}
