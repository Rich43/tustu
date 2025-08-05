package jdk.nashorn.internal.runtime.options;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.PropertyPermission;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JOptionPane;
import jdk.nashorn.internal.runtime.QuotedStringTokenizer;
import sun.util.locale.LanguageTag;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/options/Options.class */
public final class Options {
    private static final AccessControlContext READ_PROPERTY_ACC_CTXT;
    private final String resource;
    private final PrintWriter err;
    private final List<String> files;
    private final List<String> arguments;
    private final TreeMap<String, Option<?>> options;
    private static final String NASHORN_ARGS_PREPEND_PROPERTY = "nashorn.args.prepend";
    private static final String NASHORN_ARGS_PROPERTY = "nashorn.args";
    private static final String MESSAGES_RESOURCE = "jdk.nashorn.internal.runtime.resources.Options";
    private static ResourceBundle bundle;
    private static HashMap<Object, Object> usage;
    private static Collection<OptionTemplate> validOptions;
    private static OptionTemplate helpOptionTemplate;
    private static OptionTemplate definePropTemplate;
    private static String definePropPrefix;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Options.class.desiredAssertionStatus();
        READ_PROPERTY_ACC_CTXT = createPropertyReadAccCtxt();
        bundle = ResourceBundle.getBundle(MESSAGES_RESOURCE, Locale.getDefault());
        validOptions = new TreeSet();
        usage = new HashMap<>();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement2();
            StringTokenizer st = new StringTokenizer(key, ".");
            String resource = null;
            String type = null;
            if (st.countTokens() > 0) {
                resource = st.nextToken();
            }
            if (st.countTokens() > 0) {
                type = st.nextToken();
            }
            if ("option".equals(type)) {
                String helpKey = null;
                String xhelpKey = null;
                String definePropKey = null;
                try {
                    helpKey = bundle.getString(resource + ".options.help.key");
                    xhelpKey = bundle.getString(resource + ".options.xhelp.key");
                    definePropKey = bundle.getString(resource + ".options.D.key");
                } catch (MissingResourceException e2) {
                }
                boolean isHelp = key.equals(helpKey);
                boolean isXHelp = key.equals(xhelpKey);
                OptionTemplate t2 = new OptionTemplate(resource, key, bundle.getString(key), isHelp, isXHelp);
                validOptions.add(t2);
                if (isHelp) {
                    helpOptionTemplate = t2;
                }
                if (key.equals(definePropKey)) {
                    definePropPrefix = t2.getName();
                    definePropTemplate = t2;
                }
            } else if (resource != null && JOptionPane.OPTIONS_PROPERTY.equals(type)) {
                usage.put(resource, bundle.getObject(key));
            }
        }
    }

    private static AccessControlContext createPropertyReadAccCtxt() {
        Permissions perms = new Permissions();
        perms.add(new PropertyPermission("nashorn.*", "read"));
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }

    public Options(String resource) {
        this(resource, new PrintWriter((OutputStream) System.err, true));
    }

    public Options(String resource, PrintWriter err) {
        this.resource = resource;
        this.err = err;
        this.files = new ArrayList();
        this.arguments = new ArrayList();
        this.options = new TreeMap<>();
        for (OptionTemplate t2 : validOptions) {
            if (t2.getDefaultValue() != null) {
                String v2 = getStringProperty(t2.getKey(), null);
                if (v2 != null) {
                    set(t2.getKey(), createOption(t2, v2));
                } else if (t2.getDefaultValue() != null) {
                    set(t2.getKey(), createOption(t2, t2.getDefaultValue()));
                }
            }
        }
    }

    public String getResource() {
        return this.resource;
    }

    public String toString() {
        return this.options.toString();
    }

    private static void checkPropertyName(String name) {
        if (!((String) Objects.requireNonNull(name)).startsWith("nashorn.")) {
            throw new IllegalArgumentException(name);
        }
    }

    public static boolean getBooleanProperty(final String name, final Boolean defValue) {
        checkPropertyName(name);
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: jdk.nashorn.internal.runtime.options.Options.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                try {
                    String property = System.getProperty(name);
                    if (property != null || defValue == null) {
                        return Boolean.valueOf((property == null || "false".equalsIgnoreCase(property)) ? false : true);
                    }
                    return defValue;
                } catch (SecurityException e2) {
                    return false;
                }
            }
        }, READ_PROPERTY_ACC_CTXT)).booleanValue();
    }

    public static boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, null);
    }

    public static String getStringProperty(final String name, final String defValue) {
        checkPropertyName(name);
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: jdk.nashorn.internal.runtime.options.Options.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                try {
                    return System.getProperty(name, defValue);
                } catch (SecurityException e2) {
                    return defValue;
                }
            }
        }, READ_PROPERTY_ACC_CTXT);
    }

    public static int getIntProperty(final String name, final int defValue) {
        checkPropertyName(name);
        return ((Integer) AccessController.doPrivileged(new PrivilegedAction<Integer>() { // from class: jdk.nashorn.internal.runtime.options.Options.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Integer run2() {
                try {
                    return Integer.getInteger(name, defValue);
                } catch (SecurityException e2) {
                    return Integer.valueOf(defValue);
                }
            }
        }, READ_PROPERTY_ACC_CTXT)).intValue();
    }

    public Option<?> get(String key) {
        return this.options.get(key(key));
    }

    public boolean getBoolean(String key) {
        Option<?> option = get(key);
        if (option != null) {
            return ((Boolean) option.getValue()).booleanValue();
        }
        return false;
    }

    public int getInteger(String key) {
        Option<?> option = get(key);
        if (option != null) {
            return ((Integer) option.getValue()).intValue();
        }
        return 0;
    }

    public String getString(String key) {
        String value;
        Option<?> option = get(key);
        if (option != null && (value = (String) option.getValue()) != null) {
            return value.intern();
        }
        return null;
    }

    public void set(String key, Option<?> option) {
        this.options.put(key(key), option);
    }

    public void set(String key, boolean option) {
        set(key, new Option<>(Boolean.valueOf(option)));
    }

    public void set(String key, String option) {
        set(key, new Option<>(option));
    }

    public List<String> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    public List<String> getFiles() {
        return Collections.unmodifiableList(this.files);
    }

    public static Collection<OptionTemplate> getValidOptions() {
        return Collections.unmodifiableCollection(validOptions);
    }

    private String key(String shortKey) {
        String key;
        String strSubstring = shortKey;
        while (true) {
            key = strSubstring;
            if (!key.startsWith(LanguageTag.SEP)) {
                break;
            }
            strSubstring = key.substring(1, key.length());
        }
        String key2 = key.replace(LanguageTag.SEP, ".");
        String keyPrefix = this.resource + ".option.";
        if (key2.startsWith(keyPrefix)) {
            return key2;
        }
        return keyPrefix + key2;
    }

    static String getMsg(String msgId, String... args) {
        try {
            String msg = bundle.getString(msgId);
            if (args.length == 0) {
                return msg;
            }
            return new MessageFormat(msg).format(args);
        } catch (MissingResourceException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public void displayHelp(IllegalArgumentException e2) {
        if (e2 instanceof IllegalOptionException) {
            OptionTemplate template = ((IllegalOptionException) e2).getTemplate();
            if (template.isXHelp()) {
                displayHelp(true);
                return;
            } else {
                this.err.println(((IllegalOptionException) e2).getTemplate());
                return;
            }
        }
        if (e2 != null && e2.getMessage() != null) {
            this.err.println(getMsg("option.error.invalid.option", e2.getMessage(), helpOptionTemplate.getShortName(), helpOptionTemplate.getName()));
            this.err.println();
        } else {
            displayHelp(false);
        }
    }

    public void displayHelp(boolean extended) {
        for (OptionTemplate t2 : validOptions) {
            if (extended || !t2.isUndocumented()) {
                if (t2.getResource().equals(this.resource)) {
                    this.err.println(t2);
                    this.err.println();
                }
            }
        }
    }

    public void process(String[] args) {
        LinkedList<String> argList = new LinkedList<>();
        addSystemProperties(NASHORN_ARGS_PREPEND_PROPERTY, argList);
        processArgList(argList);
        if (!$assertionsDisabled && !argList.isEmpty()) {
            throw new AssertionError();
        }
        Collections.addAll(argList, args);
        processArgList(argList);
        if (!$assertionsDisabled && !argList.isEmpty()) {
            throw new AssertionError();
        }
        addSystemProperties(NASHORN_ARGS_PROPERTY, argList);
        processArgList(argList);
        if (!$assertionsDisabled && !argList.isEmpty()) {
            throw new AssertionError();
        }
    }

    private void processArgList(LinkedList<String> argList) {
        while (!argList.isEmpty()) {
            String arg = argList.remove(0);
            if (!arg.isEmpty()) {
                if ("--".equals(arg)) {
                    this.arguments.addAll(argList);
                    argList.clear();
                } else if (!arg.startsWith(LanguageTag.SEP) || arg.length() == 1) {
                    this.files.add(arg);
                } else if (arg.startsWith(definePropPrefix)) {
                    String value = arg.substring(definePropPrefix.length());
                    int eq = value.indexOf(61);
                    if (eq != -1) {
                        System.setProperty(value.substring(0, eq), value.substring(eq + 1));
                    } else if (!value.isEmpty()) {
                        System.setProperty(value, "");
                    } else {
                        throw new IllegalOptionException(definePropTemplate);
                    }
                } else {
                    ParsedArg parg = new ParsedArg(arg);
                    if (parg.template.isValueNextArg()) {
                        if (argList.isEmpty()) {
                            throw new IllegalOptionException(parg.template);
                        }
                        parg.value = argList.remove(0);
                    }
                    if (parg.template.isHelp()) {
                        if (!argList.isEmpty()) {
                            try {
                                OptionTemplate t2 = new ParsedArg(argList.get(0)).template;
                                throw new IllegalOptionException(t2);
                            } catch (IllegalArgumentException e2) {
                                throw e2;
                            }
                        }
                        throw new IllegalArgumentException();
                    }
                    if (parg.template.isXHelp()) {
                        throw new IllegalOptionException(parg.template);
                    }
                    set(parg.template.getKey(), createOption(parg.template, parg.value));
                    if (parg.template.getDependency() != null) {
                        argList.addFirst(parg.template.getDependency());
                    }
                }
            }
        }
    }

    private static void addSystemProperties(String sysPropName, List<String> argList) {
        String sysArgs = getStringProperty(sysPropName, null);
        if (sysArgs != null) {
            StringTokenizer st = new StringTokenizer(sysArgs);
            while (st.hasMoreTokens()) {
                argList.add(st.nextToken());
            }
        }
    }

    public OptionTemplate getOptionTemplateByKey(String shortKey) {
        String fullKey = key(shortKey);
        for (OptionTemplate t2 : validOptions) {
            if (t2.getKey().equals(fullKey)) {
                return t2;
            }
        }
        throw new IllegalArgumentException(shortKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static OptionTemplate getOptionTemplateByName(String name) {
        for (OptionTemplate t2 : validOptions) {
            if (t2.nameMatches(name)) {
                return t2;
            }
        }
        return null;
    }

    private static Option<?> createOption(OptionTemplate t2, String value) {
        switch (t2.getType()) {
            case "string":
                return new Option<>(value);
            case "timezone":
                return new Option<>(TimeZone.getTimeZone(value));
            case "locale":
                return new Option<>(Locale.forLanguageTag(value));
            case "keyvalues":
                return new KeyValueOption(value);
            case "log":
                return new LoggingOption(value);
            case "boolean":
                return new Option<>(Boolean.valueOf(value != null && Boolean.parseBoolean(value)));
            case "integer":
                try {
                    return new Option<>(Integer.valueOf(value == null ? 0 : Integer.parseInt(value)));
                } catch (NumberFormatException e2) {
                    throw new IllegalOptionException(t2);
                }
            case "properties":
                initProps(new KeyValueOption(value));
                return null;
            default:
                throw new IllegalArgumentException(value);
        }
    }

    private static void initProps(KeyValueOption kv) {
        for (Map.Entry<String, String> entry : kv.getValues().entrySet()) {
            System.setProperty(entry.getKey(), entry.getValue());
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/options/Options$IllegalOptionException.class */
    private static class IllegalOptionException extends IllegalArgumentException {
        private final OptionTemplate template;

        IllegalOptionException(OptionTemplate t2) {
            this.template = t2;
        }

        OptionTemplate getTemplate() {
            return this.template;
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/options/Options$ParsedArg.class */
    private static class ParsedArg {
        OptionTemplate template;
        String value;

        ParsedArg(String argument) {
            QuotedStringTokenizer st = new QuotedStringTokenizer(argument, "=");
            if (!st.hasMoreTokens()) {
                throw new IllegalArgumentException();
            }
            String token = st.nextToken();
            this.template = Options.getOptionTemplateByName(token);
            if (this.template == null) {
                throw new IllegalArgumentException(argument);
            }
            this.value = "";
            if (st.hasMoreTokens()) {
                while (st.hasMoreTokens()) {
                    this.value += st.nextToken();
                    if (st.hasMoreTokens()) {
                        this.value += ':';
                    }
                }
                return;
            }
            if ("boolean".equals(this.template.getType())) {
                this.value = "true";
            }
        }
    }
}
