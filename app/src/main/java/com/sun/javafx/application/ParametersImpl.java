package com.sun.javafx.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Application;

/* loaded from: jfxrt.jar:com/sun/javafx/application/ParametersImpl.class */
public class ParametersImpl extends Application.Parameters {
    private List<String> rawArgs = new ArrayList();
    private Map<String, String> namedParams = new HashMap();
    private List<String> unnamedParams = new ArrayList();
    private List<String> readonlyRawArgs = null;
    private Map<String, String> readonlyNamedParams = null;
    private List<String> readonlyUnnamedParams = null;
    private static Map<Application, Application.Parameters> params = new HashMap();

    public ParametersImpl() {
    }

    public ParametersImpl(List<String> args) {
        if (args != null) {
            init(args);
        }
    }

    public ParametersImpl(String[] args) {
        if (args != null) {
            init(Arrays.asList(args));
        }
    }

    public ParametersImpl(Map params2, String[] arguments) {
        init(params2, arguments);
    }

    private void init(List<String> args) {
        for (String arg : args) {
            if (arg != null) {
                this.rawArgs.add(arg);
            }
        }
        computeNamedParams();
        computeUnnamedParams();
    }

    private void init(Map params2, String[] arguments) {
        for (Object e2 : params2.entrySet()) {
            Object key = ((Map.Entry) e2).getKey();
            if (validKey(key)) {
                Object value = params2.get(key);
                if (value instanceof String) {
                    this.namedParams.put((String) key, (String) value);
                }
            }
        }
        computeRawArgs();
        if (arguments != null) {
            for (String arg : arguments) {
                this.unnamedParams.add(arg);
                this.rawArgs.add(arg);
            }
        }
    }

    private boolean validFirstChar(char c2) {
        return Character.isLetter(c2) || c2 == '_';
    }

    private boolean validKey(Object key) {
        if (key instanceof String) {
            String keyStr = (String) key;
            if (keyStr.length() > 0 && keyStr.indexOf(61) < 0) {
                return validFirstChar(keyStr.charAt(0));
            }
            return false;
        }
        return false;
    }

    private boolean isNamedParam(String arg) {
        return arg.startsWith("--") && arg.indexOf(61) > 2 && validFirstChar(arg.charAt(2));
    }

    private void computeUnnamedParams() {
        for (String arg : this.rawArgs) {
            if (!isNamedParam(arg)) {
                this.unnamedParams.add(arg);
            }
        }
    }

    private void computeNamedParams() {
        for (String arg : this.rawArgs) {
            if (isNamedParam(arg)) {
                int eqIdx = arg.indexOf(61);
                String key = arg.substring(2, eqIdx);
                String value = arg.substring(eqIdx + 1);
                this.namedParams.put(key, value);
            }
        }
    }

    private void computeRawArgs() {
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll(this.namedParams.keySet());
        Collections.sort(keys);
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            this.rawArgs.add("--" + key + "=" + this.namedParams.get(key));
        }
    }

    @Override // javafx.application.Application.Parameters
    public List<String> getRaw() {
        if (this.readonlyRawArgs == null) {
            this.readonlyRawArgs = Collections.unmodifiableList(this.rawArgs);
        }
        return this.readonlyRawArgs;
    }

    @Override // javafx.application.Application.Parameters
    public Map<String, String> getNamed() {
        if (this.readonlyNamedParams == null) {
            this.readonlyNamedParams = Collections.unmodifiableMap(this.namedParams);
        }
        return this.readonlyNamedParams;
    }

    @Override // javafx.application.Application.Parameters
    public List<String> getUnnamed() {
        if (this.readonlyUnnamedParams == null) {
            this.readonlyUnnamedParams = Collections.unmodifiableList(this.unnamedParams);
        }
        return this.readonlyUnnamedParams;
    }

    public static Application.Parameters getParameters(Application app) {
        Application.Parameters p2 = params.get(app);
        return p2;
    }

    public static void registerParameters(Application app, Application.Parameters p2) {
        params.put(app, p2);
    }
}
