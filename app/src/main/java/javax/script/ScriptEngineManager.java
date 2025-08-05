package javax.script;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/* loaded from: rt.jar:javax/script/ScriptEngineManager.class */
public class ScriptEngineManager {
    private static final boolean DEBUG = false;
    private HashSet<ScriptEngineFactory> engineSpis;
    private HashMap<String, ScriptEngineFactory> nameAssociations;
    private HashMap<String, ScriptEngineFactory> extensionAssociations;
    private HashMap<String, ScriptEngineFactory> mimeTypeAssociations;
    private Bindings globalScope;

    public ScriptEngineManager() {
        init(Thread.currentThread().getContextClassLoader());
    }

    public ScriptEngineManager(ClassLoader classLoader) {
        init(classLoader);
    }

    private void init(ClassLoader classLoader) {
        this.globalScope = new SimpleBindings();
        this.engineSpis = new HashSet<>();
        this.nameAssociations = new HashMap<>();
        this.extensionAssociations = new HashMap<>();
        this.mimeTypeAssociations = new HashMap<>();
        initEngines(classLoader);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ServiceLoader<ScriptEngineFactory> getServiceLoader(ClassLoader classLoader) {
        if (classLoader != null) {
            return ServiceLoader.load(ScriptEngineFactory.class, classLoader);
        }
        return ServiceLoader.loadInstalled(ScriptEngineFactory.class);
    }

    private void initEngines(final ClassLoader classLoader) {
        try {
            Iterator it = ((ServiceLoader) AccessController.doPrivileged(new PrivilegedAction<ServiceLoader<ScriptEngineFactory>>() { // from class: javax.script.ScriptEngineManager.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ServiceLoader<ScriptEngineFactory> run2() {
                    return ScriptEngineManager.this.getServiceLoader(classLoader);
                }
            })).iterator();
            while (it.hasNext()) {
                try {
                    try {
                        this.engineSpis.add((ScriptEngineFactory) it.next());
                    } catch (ServiceConfigurationError e2) {
                        System.err.println("ScriptEngineManager providers.next(): " + e2.getMessage());
                    }
                } catch (ServiceConfigurationError e3) {
                    System.err.println("ScriptEngineManager providers.hasNext(): " + e3.getMessage());
                    return;
                }
            }
        } catch (ServiceConfigurationError e4) {
            System.err.println("Can't find ScriptEngineFactory providers: " + e4.getMessage());
        }
    }

    public void setBindings(Bindings bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("Global scope cannot be null.");
        }
        this.globalScope = bindings;
    }

    public Bindings getBindings() {
        return this.globalScope;
    }

    public void put(String str, Object obj) {
        this.globalScope.put(str, obj);
    }

    public Object get(String str) {
        return this.globalScope.get(str);
    }

    public ScriptEngine getEngineByName(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        ScriptEngineFactory scriptEngineFactory = this.nameAssociations.get(str);
        if (null != scriptEngineFactory) {
            try {
                ScriptEngine scriptEngine = scriptEngineFactory.getScriptEngine();
                scriptEngine.setBindings(getBindings(), 200);
                return scriptEngine;
            } catch (Exception e2) {
            }
        }
        Iterator<ScriptEngineFactory> it = this.engineSpis.iterator();
        while (it.hasNext()) {
            ScriptEngineFactory next = it.next();
            List<String> names = null;
            try {
                names = next.getNames();
            } catch (Exception e3) {
            }
            if (names != null) {
                Iterator<String> it2 = names.iterator();
                while (it2.hasNext()) {
                    if (str.equals(it2.next())) {
                        try {
                            ScriptEngine scriptEngine2 = next.getScriptEngine();
                            scriptEngine2.setBindings(getBindings(), 200);
                            return scriptEngine2;
                        } catch (Exception e4) {
                        }
                    }
                }
            }
        }
        return null;
    }

    public ScriptEngine getEngineByExtension(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        ScriptEngineFactory scriptEngineFactory = this.extensionAssociations.get(str);
        if (null != scriptEngineFactory) {
            try {
                ScriptEngine scriptEngine = scriptEngineFactory.getScriptEngine();
                scriptEngine.setBindings(getBindings(), 200);
                return scriptEngine;
            } catch (Exception e2) {
            }
        }
        Iterator<ScriptEngineFactory> it = this.engineSpis.iterator();
        while (it.hasNext()) {
            ScriptEngineFactory next = it.next();
            List<String> extensions = null;
            try {
                extensions = next.getExtensions();
            } catch (Exception e3) {
            }
            if (extensions != null) {
                Iterator<String> it2 = extensions.iterator();
                while (it2.hasNext()) {
                    if (str.equals(it2.next())) {
                        try {
                            ScriptEngine scriptEngine2 = next.getScriptEngine();
                            scriptEngine2.setBindings(getBindings(), 200);
                            return scriptEngine2;
                        } catch (Exception e4) {
                        }
                    }
                }
            }
        }
        return null;
    }

    public ScriptEngine getEngineByMimeType(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        ScriptEngineFactory scriptEngineFactory = this.mimeTypeAssociations.get(str);
        if (null != scriptEngineFactory) {
            try {
                ScriptEngine scriptEngine = scriptEngineFactory.getScriptEngine();
                scriptEngine.setBindings(getBindings(), 200);
                return scriptEngine;
            } catch (Exception e2) {
            }
        }
        Iterator<ScriptEngineFactory> it = this.engineSpis.iterator();
        while (it.hasNext()) {
            ScriptEngineFactory next = it.next();
            List<String> mimeTypes = null;
            try {
                mimeTypes = next.getMimeTypes();
            } catch (Exception e3) {
            }
            if (mimeTypes != null) {
                Iterator<String> it2 = mimeTypes.iterator();
                while (it2.hasNext()) {
                    if (str.equals(it2.next())) {
                        try {
                            ScriptEngine scriptEngine2 = next.getScriptEngine();
                            scriptEngine2.setBindings(getBindings(), 200);
                            return scriptEngine2;
                        } catch (Exception e4) {
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<ScriptEngineFactory> getEngineFactories() {
        ArrayList arrayList = new ArrayList(this.engineSpis.size());
        Iterator<ScriptEngineFactory> it = this.engineSpis.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void registerEngineName(String str, ScriptEngineFactory scriptEngineFactory) {
        if (str == null || scriptEngineFactory == null) {
            throw new NullPointerException();
        }
        this.nameAssociations.put(str, scriptEngineFactory);
    }

    public void registerEngineMimeType(String str, ScriptEngineFactory scriptEngineFactory) {
        if (str == null || scriptEngineFactory == null) {
            throw new NullPointerException();
        }
        this.mimeTypeAssociations.put(str, scriptEngineFactory);
    }

    public void registerEngineExtension(String str, ScriptEngineFactory scriptEngineFactory) {
        if (str == null || scriptEngineFactory == null) {
            throw new NullPointerException();
        }
        this.extensionAssociations.put(str, scriptEngineFactory);
    }
}
