package jdk.nashorn.api.scripting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import jdk.Exported;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Version;

@Exported
/* loaded from: nashorn.jar:jdk/nashorn/api/scripting/NashornScriptEngineFactory.class */
public final class NashornScriptEngineFactory implements ScriptEngineFactory {
    private static final String[] DEFAULT_OPTIONS = {"-doe"};
    private static final List<String> names = immutableList("nashorn", "Nashorn", "js", "JS", "JavaScript", "javascript", "ECMAScript", "ecmascript");
    private static final List<String> mimeTypes = immutableList("application/javascript", "application/ecmascript", "text/javascript", "text/ecmascript");
    private static final List<String> extensions = immutableList("js");

    @Override // javax.script.ScriptEngineFactory
    public String getEngineName() {
        return (String) getParameter(ScriptEngine.ENGINE);
    }

    @Override // javax.script.ScriptEngineFactory
    public String getEngineVersion() {
        return (String) getParameter(ScriptEngine.ENGINE_VERSION);
    }

    @Override // javax.script.ScriptEngineFactory
    public List<String> getExtensions() {
        return Collections.unmodifiableList(extensions);
    }

    @Override // javax.script.ScriptEngineFactory
    public String getLanguageName() {
        return (String) getParameter(ScriptEngine.LANGUAGE);
    }

    @Override // javax.script.ScriptEngineFactory
    public String getLanguageVersion() {
        return (String) getParameter(ScriptEngine.LANGUAGE_VERSION);
    }

    @Override // javax.script.ScriptEngineFactory
    public String getMethodCallSyntax(String obj, String method, String... args) {
        StringBuilder sb = new StringBuilder().append(obj).append('.').append(method).append('(');
        int len = args.length;
        if (len > 0) {
            sb.append(args[0]);
        }
        for (int i2 = 1; i2 < len; i2++) {
            sb.append(',').append(args[i2]);
        }
        sb.append(')');
        return sb.toString();
    }

    @Override // javax.script.ScriptEngineFactory
    public List<String> getMimeTypes() {
        return Collections.unmodifiableList(mimeTypes);
    }

    @Override // javax.script.ScriptEngineFactory
    public List<String> getNames() {
        return Collections.unmodifiableList(names);
    }

    @Override // javax.script.ScriptEngineFactory
    public String getOutputStatement(String toDisplay) {
        return "print(" + toDisplay + ")";
    }

    @Override // javax.script.ScriptEngineFactory
    public Object getParameter(String key) {
        switch (key) {
            case "javax.script.name":
                return "javascript";
            case "javax.script.engine":
                return "Oracle Nashorn";
            case "javax.script.engine_version":
                return Version.version();
            case "javax.script.language":
                return "ECMAScript";
            case "javax.script.language_version":
                return "ECMA - 262 Edition 5.1";
            case "THREADING":
                return null;
            default:
                return null;
        }
    }

    @Override // javax.script.ScriptEngineFactory
    public String getProgram(String... statements) {
        StringBuilder sb = new StringBuilder();
        for (String statement : statements) {
            sb.append(statement).append(';');
        }
        return sb.toString();
    }

    @Override // javax.script.ScriptEngineFactory
    public ScriptEngine getScriptEngine() {
        try {
            return new NashornScriptEngine(this, DEFAULT_OPTIONS, getAppClassLoader(), null);
        } catch (RuntimeException e2) {
            if (Context.DEBUG) {
                e2.printStackTrace();
            }
            throw e2;
        }
    }

    public ScriptEngine getScriptEngine(ClassLoader appLoader) {
        return newEngine(DEFAULT_OPTIONS, appLoader, null);
    }

    public ScriptEngine getScriptEngine(ClassFilter classFilter) {
        return newEngine(DEFAULT_OPTIONS, getAppClassLoader(), (ClassFilter) Objects.requireNonNull(classFilter));
    }

    public ScriptEngine getScriptEngine(String... args) {
        return newEngine((String[]) Objects.requireNonNull(args), getAppClassLoader(), null);
    }

    public ScriptEngine getScriptEngine(String[] args, ClassLoader appLoader) {
        return newEngine((String[]) Objects.requireNonNull(args), appLoader, null);
    }

    public ScriptEngine getScriptEngine(String[] args, ClassLoader appLoader, ClassFilter classFilter) {
        return newEngine((String[]) Objects.requireNonNull(args), appLoader, (ClassFilter) Objects.requireNonNull(classFilter));
    }

    private ScriptEngine newEngine(String[] args, ClassLoader appLoader, ClassFilter classFilter) {
        checkConfigPermission();
        try {
            return new NashornScriptEngine(this, args, appLoader, classFilter);
        } catch (RuntimeException e2) {
            if (Context.DEBUG) {
                e2.printStackTrace();
            }
            throw e2;
        }
    }

    private static void checkConfigPermission() {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new RuntimePermission(Context.NASHORN_SET_CONFIG));
        }
    }

    private static List<String> immutableList(String... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }

    private static ClassLoader getAppClassLoader() {
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        return ccl == null ? NashornScriptEngineFactory.class.getClassLoader() : ccl;
    }
}
