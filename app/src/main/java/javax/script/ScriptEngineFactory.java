package javax.script;

import java.util.List;

/* loaded from: rt.jar:javax/script/ScriptEngineFactory.class */
public interface ScriptEngineFactory {
    String getEngineName();

    String getEngineVersion();

    List<String> getExtensions();

    List<String> getMimeTypes();

    List<String> getNames();

    String getLanguageName();

    String getLanguageVersion();

    Object getParameter(String str);

    String getMethodCallSyntax(String str, String str2, String... strArr);

    String getOutputStatement(String str);

    String getProgram(String... strArr);

    ScriptEngine getScriptEngine();
}
