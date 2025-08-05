package jdk.nashorn.api.scripting;

import java.io.IOException;
import java.io.Reader;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import jdk.Exported;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;
import jdk.nashorn.internal.runtime.options.Options;

@Exported
/* loaded from: nashorn.jar:jdk/nashorn/api/scripting/NashornScriptEngine.class */
public final class NashornScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {
    public static final String NASHORN_GLOBAL = "nashorn.global";
    private static final AccessControlContext CREATE_CONTEXT_ACC_CTXT;
    private static final AccessControlContext CREATE_GLOBAL_ACC_CTXT;
    private final ScriptEngineFactory factory;
    private final Context nashornContext;
    private final boolean _global_per_engine;
    private final Global global;
    private static final String MESSAGES_RESOURCE = "jdk.nashorn.api.scripting.resources.Messages";
    private static final ResourceBundle MESSAGES_BUNDLE;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NashornScriptEngine.class.desiredAssertionStatus();
        CREATE_CONTEXT_ACC_CTXT = createPermAccCtxt(Context.NASHORN_CREATE_CONTEXT);
        CREATE_GLOBAL_ACC_CTXT = createPermAccCtxt(Context.NASHORN_CREATE_GLOBAL);
        MESSAGES_BUNDLE = ResourceBundle.getBundle(MESSAGES_RESOURCE, Locale.getDefault());
    }

    private static AccessControlContext createPermAccCtxt(String permName) {
        Permissions perms = new Permissions();
        perms.add(new RuntimePermission(permName));
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }

    private static String getMessage(String msgId, String... args) {
        try {
            return new MessageFormat(MESSAGES_BUNDLE.getString(msgId)).format(args);
        } catch (MissingResourceException e2) {
            throw new RuntimeException("no message resource found for message id: " + msgId);
        }
    }

    NashornScriptEngine(NashornScriptEngineFactory factory, String[] args, final ClassLoader appLoader, final ClassFilter classFilter) {
        if (!$assertionsDisabled && args == null) {
            throw new AssertionError((Object) "null argument array");
        }
        this.factory = factory;
        final Options options = new Options("nashorn");
        options.process(args);
        final ErrorManager errMgr = new Context.ThrowErrorManager();
        this.nashornContext = (Context) AccessController.doPrivileged(new PrivilegedAction<Context>() { // from class: jdk.nashorn.api.scripting.NashornScriptEngine.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Context run2() {
                try {
                    return new Context(options, errMgr, appLoader, classFilter);
                } catch (RuntimeException e2) {
                    if (Context.DEBUG) {
                        e2.printStackTrace();
                    }
                    throw e2;
                }
            }
        }, CREATE_CONTEXT_ACC_CTXT);
        this._global_per_engine = this.nashornContext.getEnv()._global_per_engine;
        this.global = createNashornGlobal();
        this.context.setBindings(new ScriptObjectMirror(this.global, this.global), 100);
    }

    @Override // javax.script.ScriptEngine
    public Object eval(Reader reader, ScriptContext ctxt) throws ScriptException {
        return evalImpl(makeSource(reader, ctxt), ctxt);
    }

    @Override // javax.script.ScriptEngine
    public Object eval(String script, ScriptContext ctxt) throws ScriptException {
        return evalImpl(makeSource(script, ctxt), ctxt);
    }

    @Override // javax.script.ScriptEngine
    public ScriptEngineFactory getFactory() {
        return this.factory;
    }

    @Override // javax.script.ScriptEngine
    public Bindings createBindings() {
        if (this._global_per_engine) {
            return new SimpleBindings();
        }
        return createGlobalMirror();
    }

    @Override // javax.script.Compilable
    public CompiledScript compile(Reader reader) throws ScriptException {
        return asCompiledScript(makeSource(reader, this.context));
    }

    @Override // javax.script.Compilable
    public CompiledScript compile(String str) throws ScriptException {
        return asCompiledScript(makeSource(str, this.context));
    }

    @Override // javax.script.Invocable
    public Object invokeFunction(String name, Object... args) throws NoSuchMethodException, ScriptException {
        return invokeImpl(null, name, args);
    }

    @Override // javax.script.Invocable
    public Object invokeMethod(Object thiz, String name, Object... args) throws NoSuchMethodException, ScriptException {
        if (thiz == null) {
            throw new IllegalArgumentException(getMessage("thiz.cannot.be.null", new String[0]));
        }
        return invokeImpl(thiz, name, args);
    }

    @Override // javax.script.Invocable
    public <T> T getInterface(Class<T> cls) {
        return (T) getInterfaceInner(null, cls);
    }

    @Override // javax.script.Invocable
    public <T> T getInterface(Object obj, Class<T> cls) {
        if (obj == null) {
            throw new IllegalArgumentException(getMessage("thiz.cannot.be.null", new String[0]));
        }
        return (T) getInterfaceInner(obj, cls);
    }

    private static Source makeSource(Reader reader, ScriptContext ctxt) throws ScriptException {
        try {
            return Source.sourceFor(getScriptName(ctxt), reader);
        } catch (IOException e2) {
            throw new ScriptException(e2);
        }
    }

    private static Source makeSource(String src, ScriptContext ctxt) {
        return Source.sourceFor(getScriptName(ctxt), src);
    }

    private static String getScriptName(ScriptContext ctxt) {
        Object val = ctxt.getAttribute(ScriptEngine.FILENAME);
        return val != null ? val.toString() : "<eval>";
    }

    private <T> T getInterfaceInner(Object thiz, Class<T> clazz) {
        if (!$assertionsDisabled && (thiz instanceof ScriptObject)) {
            throw new AssertionError((Object) "raw ScriptObject not expected here");
        }
        if (clazz == null || !clazz.isInterface()) {
            throw new IllegalArgumentException(getMessage("interface.class.expected", new String[0]));
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            if (!Modifier.isPublic(clazz.getModifiers())) {
                throw new SecurityException(getMessage("implementing.non.public.interface", clazz.getName()));
            }
            Context.checkPackageAccess((Class<?>) clazz);
        }
        ScriptObject realSelf = null;
        Global realGlobal = null;
        if (thiz == null) {
            Global nashornGlobalFrom = getNashornGlobalFrom(this.context);
            realGlobal = nashornGlobalFrom;
            realSelf = nashornGlobalFrom;
        } else if (thiz instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror) thiz;
            realSelf = mirror.getScriptObject();
            realGlobal = mirror.getHomeGlobal();
            if (!isOfContext(realGlobal, this.nashornContext)) {
                throw new IllegalArgumentException(getMessage("script.object.from.another.engine", new String[0]));
            }
        }
        if (realSelf == null) {
            throw new IllegalArgumentException(getMessage("interface.on.non.script.object", new String[0]));
        }
        try {
            try {
                Global oldGlobal = Context.getGlobal();
                boolean globalChanged = oldGlobal != realGlobal;
                if (globalChanged) {
                    try {
                        Context.setGlobal(realGlobal);
                    } finally {
                        if (globalChanged) {
                            Context.setGlobal(oldGlobal);
                        }
                    }
                }
                if (!isInterfaceImplemented(clazz, realSelf)) {
                    return null;
                }
                T tCast = clazz.cast((Object) JavaAdapterFactory.getConstructor(realSelf.getClass(), clazz, MethodHandles.publicLookup()).invoke(realSelf));
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
                return tCast;
            } catch (Error | RuntimeException e2) {
                throw e2;
            }
        } catch (Throwable t2) {
            throw new RuntimeException(t2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Global getNashornGlobalFrom(ScriptContext ctxt) {
        Global glob;
        Global glob2;
        if (this._global_per_engine) {
            return this.global;
        }
        Bindings bindings = ctxt.getBindings(100);
        if ((bindings instanceof ScriptObjectMirror) && (glob2 = globalFromMirror((ScriptObjectMirror) bindings)) != null) {
            return glob2;
        }
        Object scope = bindings.get(NASHORN_GLOBAL);
        if ((scope instanceof ScriptObjectMirror) && (glob = globalFromMirror((ScriptObjectMirror) scope)) != null) {
            return glob;
        }
        ScriptObjectMirror mirror = createGlobalMirror();
        bindings.put(NASHORN_GLOBAL, (Object) mirror);
        mirror.getHomeGlobal().setInitScriptContext(ctxt);
        return mirror.getHomeGlobal();
    }

    private Global globalFromMirror(ScriptObjectMirror mirror) {
        ScriptObject sobj = mirror.getScriptObject();
        if ((sobj instanceof Global) && isOfContext((Global) sobj, this.nashornContext)) {
            return (Global) sobj;
        }
        return null;
    }

    private ScriptObjectMirror createGlobalMirror() {
        Global newGlobal = createNashornGlobal();
        return new ScriptObjectMirror(newGlobal, newGlobal);
    }

    private Global createNashornGlobal() {
        Global newGlobal = (Global) AccessController.doPrivileged(new PrivilegedAction<Global>() { // from class: jdk.nashorn.api.scripting.NashornScriptEngine.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Global run2() {
                try {
                    return NashornScriptEngine.this.nashornContext.newGlobal();
                } catch (RuntimeException e2) {
                    if (Context.DEBUG) {
                        e2.printStackTrace();
                    }
                    throw e2;
                }
            }
        }, CREATE_GLOBAL_ACC_CTXT);
        this.nashornContext.initGlobal(newGlobal, this);
        return newGlobal;
    }

    private Object invokeImpl(Object selfObject, String name, Object... args) throws NoSuchMethodException, ScriptException {
        Objects.requireNonNull(name);
        if (!$assertionsDisabled && (selfObject instanceof ScriptObject)) {
            throw new AssertionError((Object) "raw ScriptObject not expected here");
        }
        Global invokeGlobal = null;
        ScriptObjectMirror selfMirror = null;
        if (selfObject instanceof ScriptObjectMirror) {
            selfMirror = (ScriptObjectMirror) selfObject;
            if (!isOfContext(selfMirror.getHomeGlobal(), this.nashornContext)) {
                throw new IllegalArgumentException(getMessage("script.object.from.another.engine", new String[0]));
            }
            invokeGlobal = selfMirror.getHomeGlobal();
        } else if (selfObject == null) {
            Global ctxtGlobal = getNashornGlobalFrom(this.context);
            invokeGlobal = ctxtGlobal;
            selfMirror = (ScriptObjectMirror) ScriptObjectMirror.wrap(ctxtGlobal, ctxtGlobal);
        }
        if (selfMirror != null) {
            try {
                return ScriptObjectMirror.translateUndefined(selfMirror.callMember(name, args));
            } catch (Exception e2) {
                Throwable cause = e2.getCause();
                if (cause instanceof NoSuchMethodException) {
                    throw ((NoSuchMethodException) cause);
                }
                throwAsScriptException(e2, invokeGlobal);
                throw new AssertionError((Object) "should not reach here");
            }
        }
        throw new IllegalArgumentException(getMessage("interface.on.non.script.object", new String[0]));
    }

    private Object evalImpl(Source src, ScriptContext ctxt) throws ScriptException {
        return evalImpl(compileImpl(src, ctxt), ctxt);
    }

    private Object evalImpl(ScriptFunction script, ScriptContext ctxt) throws ScriptException {
        return evalImpl(script, ctxt, getNashornGlobalFrom(ctxt));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object evalImpl(Context.MultiGlobalCompiledScript mgcs, ScriptContext ctxt, Global ctxtGlobal) throws ScriptException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != ctxtGlobal;
        try {
            if (globalChanged) {
                try {
                    Context.setGlobal(ctxtGlobal);
                } catch (Exception e2) {
                    throwAsScriptException(e2, ctxtGlobal);
                    throw new AssertionError((Object) "should not reach here");
                }
            }
            ScriptFunction script = mgcs.getFunction(ctxtGlobal);
            ScriptContext oldCtxt = ctxtGlobal.getScriptContext();
            ctxtGlobal.setScriptContext(ctxt);
            try {
                Object objTranslateUndefined = ScriptObjectMirror.translateUndefined(ScriptObjectMirror.wrap(ScriptRuntime.apply(script, ctxtGlobal, new Object[0]), ctxtGlobal));
                ctxtGlobal.setScriptContext(oldCtxt);
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
                return objTranslateUndefined;
            } catch (Throwable th) {
                ctxtGlobal.setScriptContext(oldCtxt);
                throw th;
            }
        } catch (Throwable th2) {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
            throw th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object evalImpl(ScriptFunction script, ScriptContext ctxt, Global ctxtGlobal) throws ScriptException {
        if (script == null) {
            return null;
        }
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != ctxtGlobal;
        try {
            if (globalChanged) {
                try {
                    Context.setGlobal(ctxtGlobal);
                } catch (Exception e2) {
                    throwAsScriptException(e2, ctxtGlobal);
                    throw new AssertionError((Object) "should not reach here");
                }
            }
            ScriptContext oldCtxt = ctxtGlobal.getScriptContext();
            ctxtGlobal.setScriptContext(ctxt);
            try {
                Object objTranslateUndefined = ScriptObjectMirror.translateUndefined(ScriptObjectMirror.wrap(ScriptRuntime.apply(script, ctxtGlobal, new Object[0]), ctxtGlobal));
                ctxtGlobal.setScriptContext(oldCtxt);
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
                return objTranslateUndefined;
            } catch (Throwable th) {
                ctxtGlobal.setScriptContext(oldCtxt);
                throw th;
            }
        } catch (Throwable th2) {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
            throw th2;
        }
    }

    private static void throwAsScriptException(Exception e2, Global global) throws ScriptException {
        if (e2 instanceof ScriptException) {
            throw ((ScriptException) e2);
        }
        if (e2 instanceof NashornException) {
            NashornException ne = (NashornException) e2;
            ScriptException se = new ScriptException(ne.getMessage(), ne.getFileName(), ne.getLineNumber(), ne.getColumnNumber());
            ne.initEcmaError(global);
            se.initCause(e2);
            throw se;
        }
        if (e2 instanceof RuntimeException) {
            throw ((RuntimeException) e2);
        }
        throw new ScriptException(e2);
    }

    private CompiledScript asCompiledScript(Source source) throws ScriptException {
        Global oldGlobal = Context.getGlobal();
        Global newGlobal = getNashornGlobalFrom(this.context);
        boolean globalChanged = oldGlobal != newGlobal;
        try {
            if (globalChanged) {
                try {
                    Context.setGlobal(newGlobal);
                } catch (Exception e2) {
                    throwAsScriptException(e2, newGlobal);
                    throw new AssertionError((Object) "should not reach here");
                }
            }
            final Context.MultiGlobalCompiledScript mgcs = this.nashornContext.compileScript(source);
            final ScriptFunction func = mgcs.getFunction(newGlobal);
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
            return new CompiledScript() { // from class: jdk.nashorn.api.scripting.NashornScriptEngine.3
                @Override // javax.script.CompiledScript
                public Object eval(ScriptContext ctxt) throws ScriptException {
                    Global globalObject = NashornScriptEngine.this.getNashornGlobalFrom(ctxt);
                    return func.getScope() == globalObject ? NashornScriptEngine.this.evalImpl(func, ctxt, globalObject) : NashornScriptEngine.this.evalImpl(mgcs, ctxt, globalObject);
                }

                @Override // javax.script.CompiledScript
                public ScriptEngine getEngine() {
                    return NashornScriptEngine.this;
                }
            };
        } catch (Throwable th) {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
            throw th;
        }
    }

    private ScriptFunction compileImpl(Source source, ScriptContext ctxt) throws ScriptException {
        return compileImpl(source, getNashornGlobalFrom(ctxt));
    }

    private ScriptFunction compileImpl(Source source, Global newGlobal) throws ScriptException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != newGlobal;
        if (globalChanged) {
            try {
                try {
                    Context.setGlobal(newGlobal);
                } catch (Exception e2) {
                    throwAsScriptException(e2, newGlobal);
                    throw new AssertionError((Object) "should not reach here");
                }
            } catch (Throwable th) {
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
                throw th;
            }
        }
        ScriptFunction scriptFunctionCompileScript = this.nashornContext.compileScript(source, newGlobal);
        if (globalChanged) {
            Context.setGlobal(oldGlobal);
        }
        return scriptFunctionCompileScript;
    }

    private static boolean isInterfaceImplemented(Class<?> iface, ScriptObject sobj) throws SecurityException {
        for (Method method : iface.getMethods()) {
            if (method.getDeclaringClass() != Object.class && Modifier.isAbstract(method.getModifiers())) {
                Object obj = sobj.get(method.getName());
                if (!(obj instanceof ScriptFunction)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isOfContext(Global global, Context context) {
        return global.isOfContext(context);
    }
}
