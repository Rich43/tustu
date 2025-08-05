package javafx.fxml;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.javafx.fxml.LoadListener;
import com.sun.javafx.fxml.ParseTraceElement;
import com.sun.javafx.fxml.PropertyNotFoundException;
import com.sun.javafx.fxml.expression.Expression;
import com.sun.javafx.fxml.expression.ExpressionValue;
import com.sun.javafx.fxml.expression.KeyPath;
import com.sun.javafx.util.Logging;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ConstructorUtil;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader.class */
public class FXMLLoader {
    private URL location;
    private ResourceBundle resources;
    private ObservableMap<String, Object> namespace;
    private Object root;
    private Object controller;
    private BuilderFactory builderFactory;
    private Callback<Class<?>, Object> controllerFactory;
    private Charset charset;
    private final LinkedList<FXMLLoader> loaders;
    private ClassLoader classLoader;
    private boolean staticLoad;
    private LoadListener loadListener;
    private FXMLLoader parentLoader;
    private XMLStreamReader xmlStreamReader;
    private Element current;
    private ScriptEngine scriptEngine;
    private List<String> packages;
    private Map<String, Class<?>> classes;
    private ScriptEngineManager scriptEngineManager;
    private static final Boolean ALLOW_JAVASCRIPT;
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    public static final String LANGUAGE_PROCESSING_INSTRUCTION = "language";
    public static final String IMPORT_PROCESSING_INSTRUCTION = "import";
    public static final String FX_NAMESPACE_PREFIX = "fx";
    public static final String FX_CONTROLLER_ATTRIBUTE = "controller";
    public static final String FX_ID_ATTRIBUTE = "id";
    public static final String FX_VALUE_ATTRIBUTE = "value";
    public static final String FX_CONSTANT_ATTRIBUTE = "constant";
    public static final String FX_FACTORY_ATTRIBUTE = "factory";
    public static final String INCLUDE_TAG = "include";
    public static final String INCLUDE_SOURCE_ATTRIBUTE = "source";
    public static final String INCLUDE_RESOURCES_ATTRIBUTE = "resources";
    public static final String INCLUDE_CHARSET_ATTRIBUTE = "charset";
    public static final String SCRIPT_TAG = "script";
    public static final String SCRIPT_SOURCE_ATTRIBUTE = "source";
    public static final String SCRIPT_CHARSET_ATTRIBUTE = "charset";
    public static final String DEFINE_TAG = "define";
    public static final String REFERENCE_TAG = "reference";
    public static final String REFERENCE_SOURCE_ATTRIBUTE = "source";
    public static final String ROOT_TAG = "root";
    public static final String ROOT_TYPE_ATTRIBUTE = "type";
    public static final String COPY_TAG = "copy";
    public static final String COPY_SOURCE_ATTRIBUTE = "source";
    public static final String EVENT_HANDLER_PREFIX = "on";
    public static final String EVENT_KEY = "event";
    public static final String CHANGE_EVENT_HANDLER_SUFFIX = "Change";
    private static final String COLLECTION_HANDLER_NAME = "onChange";
    public static final String NULL_KEYWORD = "null";
    public static final String ESCAPE_PREFIX = "\\";
    public static final String RELATIVE_PATH_PREFIX = "@";
    public static final String RESOURCE_KEY_PREFIX = "%";
    public static final String EXPRESSION_PREFIX = "$";
    public static final String BINDING_EXPRESSION_PREFIX = "${";
    public static final String BINDING_EXPRESSION_SUFFIX = "}";
    public static final String BI_DIRECTIONAL_BINDING_PREFIX = "#{";
    public static final String BI_DIRECTIONAL_BINDING_SUFFIX = "}";
    public static final String ARRAY_COMPONENT_DELIMITER = ",";
    public static final String LOCATION_KEY = "location";
    public static final String RESOURCES_KEY = "resources";
    public static final String CONTROLLER_METHOD_PREFIX = "#";
    public static final String CONTROLLER_KEYWORD = "controller";
    public static final String CONTROLLER_SUFFIX = "Controller";
    public static final String INITIALIZE_METHOD_NAME = "initialize";
    public static final String FX_NAMESPACE_VERSION = "1";
    private Class<?> callerClass;
    private final ControllerAccessor controllerAccessor;
    private static final RuntimePermission GET_CLASSLOADER_PERMISSION = new RuntimePermission("getClassLoader");
    private static ClassLoader defaultClassLoader = null;
    private static final Pattern extraneousWhitespacePattern = Pattern.compile("\\s+");
    private static BuilderFactory DEFAULT_BUILDER_FACTORY = new JavaFXBuilderFactory();
    public static final String JAVAFX_VERSION = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javafx.fxml.FXMLLoader.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public String run2() {
            return System.getProperty("javafx.version");
        }
    });

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$SupportedType.class */
    private enum SupportedType {
        PARAMETERLESS { // from class: javafx.fxml.FXMLLoader.SupportedType.1
            @Override // javafx.fxml.FXMLLoader.SupportedType
            protected boolean methodIsOfType(Method m2) {
                return m2.getParameterTypes().length == 0;
            }
        },
        EVENT { // from class: javafx.fxml.FXMLLoader.SupportedType.2
            @Override // javafx.fxml.FXMLLoader.SupportedType
            protected boolean methodIsOfType(Method m2) {
                return m2.getParameterTypes().length == 1 && Event.class.isAssignableFrom(m2.getParameterTypes()[0]);
            }
        },
        LIST_CHANGE_LISTENER { // from class: javafx.fxml.FXMLLoader.SupportedType.3
            @Override // javafx.fxml.FXMLLoader.SupportedType
            protected boolean methodIsOfType(Method m2) {
                return m2.getParameterTypes().length == 1 && m2.getParameterTypes()[0].equals(ListChangeListener.Change.class);
            }
        },
        MAP_CHANGE_LISTENER { // from class: javafx.fxml.FXMLLoader.SupportedType.4
            @Override // javafx.fxml.FXMLLoader.SupportedType
            protected boolean methodIsOfType(Method m2) {
                return m2.getParameterTypes().length == 1 && m2.getParameterTypes()[0].equals(MapChangeListener.Change.class);
            }
        },
        SET_CHANGE_LISTENER { // from class: javafx.fxml.FXMLLoader.SupportedType.5
            @Override // javafx.fxml.FXMLLoader.SupportedType
            protected boolean methodIsOfType(Method m2) {
                return m2.getParameterTypes().length == 1 && m2.getParameterTypes()[0].equals(SetChangeListener.Change.class);
            }
        },
        PROPERTY_CHANGE_LISTENER { // from class: javafx.fxml.FXMLLoader.SupportedType.6
            @Override // javafx.fxml.FXMLLoader.SupportedType
            protected boolean methodIsOfType(Method m2) {
                return m2.getParameterTypes().length == 3 && ObservableValue.class.isAssignableFrom(m2.getParameterTypes()[0]) && m2.getParameterTypes()[1].equals(m2.getParameterTypes()[2]);
            }
        };

        protected abstract boolean methodIsOfType(Method method);
    }

    static {
        boolean tmp2 = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("javafx.allowjs"));
        })).booleanValue();
        ALLOW_JAVASCRIPT = Boolean.valueOf(tmp2);
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$Element.class */
    private abstract class Element {
        public final Element parent;
        public Object value = null;
        private BeanAdapter valueAdapter = null;
        public final LinkedList<Attribute> eventHandlerAttributes = new LinkedList<>();
        public final LinkedList<Attribute> instancePropertyAttributes = new LinkedList<>();
        public final LinkedList<Attribute> staticPropertyAttributes = new LinkedList<>();
        public final LinkedList<PropertyElement> staticPropertyElements = new LinkedList<>();

        public Element() {
            this.parent = FXMLLoader.this.current;
        }

        public boolean isCollection() {
            boolean collection;
            if (this.value instanceof List) {
                collection = true;
            } else {
                Class<?> type = this.value.getClass();
                DefaultProperty defaultProperty = (DefaultProperty) type.getAnnotation(DefaultProperty.class);
                if (defaultProperty != null) {
                    collection = getProperties().get(defaultProperty.value()) instanceof List;
                } else {
                    collection = false;
                }
            }
            return collection;
        }

        public void add(Object element) throws NumberFormatException, SecurityException, LoadException {
            List<Object> list;
            if (this.value instanceof List) {
                list = (List) this.value;
            } else {
                Class<?> type = this.value.getClass();
                DefaultProperty defaultProperty = (DefaultProperty) type.getAnnotation(DefaultProperty.class);
                String defaultPropertyName = defaultProperty.value();
                list = (List) getProperties().get(defaultPropertyName);
                if (!Map.class.isAssignableFrom(type)) {
                    Type listType = getValueAdapter().getGenericType(defaultPropertyName);
                    element = BeanAdapter.coerce(element, BeanAdapter.getListItemType(listType));
                }
            }
            list.add(element);
        }

        public void set(Object value) throws LoadException {
            if (this.value == null) {
                throw FXMLLoader.this.constructLoadException("Cannot set value on this element.");
            }
            Class<?> type = this.value.getClass();
            DefaultProperty defaultProperty = (DefaultProperty) type.getAnnotation(DefaultProperty.class);
            if (defaultProperty == null) {
                throw FXMLLoader.this.constructLoadException("Element does not define a default property.");
            }
            getProperties().put(defaultProperty.value(), value);
        }

        public void updateValue(Object value) {
            this.value = value;
            this.valueAdapter = null;
        }

        public boolean isTyped() {
            return !(this.value instanceof Map);
        }

        public BeanAdapter getValueAdapter() {
            if (this.valueAdapter == null) {
                this.valueAdapter = new BeanAdapter(this.value);
            }
            return this.valueAdapter;
        }

        public Map<String, Object> getProperties() {
            return isTyped() ? getValueAdapter() : (Map) this.value;
        }

        public void processStartElement() throws IOException {
            int n2 = FXMLLoader.this.xmlStreamReader.getAttributeCount();
            for (int i2 = 0; i2 < n2; i2++) {
                String prefix = FXMLLoader.this.xmlStreamReader.getAttributePrefix(i2);
                String localName = FXMLLoader.this.xmlStreamReader.getAttributeLocalName(i2);
                String value = FXMLLoader.this.xmlStreamReader.getAttributeValue(i2);
                if (FXMLLoader.this.loadListener != null && prefix != null && prefix.equals(FXMLLoader.FX_NAMESPACE_PREFIX)) {
                    FXMLLoader.this.loadListener.readInternalAttribute(prefix + CallSiteDescriptor.TOKEN_DELIMITER + localName, value);
                }
                processAttribute(prefix, localName, value);
            }
        }

        public void processEndElement() throws IOException {
        }

        public void processCharacters() throws IOException {
            throw FXMLLoader.this.constructLoadException("Unexpected characters in input stream.");
        }

        public void processInstancePropertyAttributes() throws NumberFormatException, SecurityException, IOException {
            if (this.instancePropertyAttributes.size() > 0) {
                Iterator<Attribute> it = this.instancePropertyAttributes.iterator();
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    processPropertyAttribute(attribute);
                }
            }
        }

        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (prefix != null) {
                throw FXMLLoader.this.constructLoadException(prefix + CallSiteDescriptor.TOKEN_DELIMITER + localName + " is not a valid attribute.");
            }
            if (localName.startsWith(FXMLLoader.EVENT_HANDLER_PREFIX)) {
                if (FXMLLoader.this.loadListener != null) {
                    FXMLLoader.this.loadListener.readEventHandlerAttribute(localName, value);
                }
                this.eventHandlerAttributes.add(new Attribute(localName, null, value));
                return;
            }
            int i2 = localName.lastIndexOf(46);
            if (i2 == -1) {
                if (FXMLLoader.this.loadListener != null) {
                    FXMLLoader.this.loadListener.readPropertyAttribute(localName, null, value);
                }
                this.instancePropertyAttributes.add(new Attribute(localName, null, value));
                return;
            }
            String name = localName.substring(i2 + 1);
            Class<?> sourceType = FXMLLoader.this.getType(localName.substring(0, i2));
            if (sourceType != null) {
                if (FXMLLoader.this.loadListener != null) {
                    FXMLLoader.this.loadListener.readPropertyAttribute(name, sourceType, value);
                }
                this.staticPropertyAttributes.add(new Attribute(name, sourceType, value));
            } else {
                if (FXMLLoader.this.staticLoad) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readUnknownStaticPropertyAttribute(localName, value);
                        return;
                    }
                    return;
                }
                throw FXMLLoader.this.constructLoadException(localName + " is not a valid attribute.");
            }
        }

        public void processPropertyAttribute(Attribute attribute) throws NumberFormatException, SecurityException, IOException {
            String value = attribute.value;
            if (!isBindingExpression(value)) {
                if (isBidirectionalBindingExpression(value)) {
                    throw FXMLLoader.this.constructLoadException(new UnsupportedOperationException("This feature is not currently enabled."));
                }
                processValue(attribute.sourceType, attribute.name, value);
                return;
            }
            if (attribute.sourceType != null) {
                throw FXMLLoader.this.constructLoadException("Cannot bind to static property.");
            }
            if (!isTyped()) {
                throw FXMLLoader.this.constructLoadException("Cannot bind to untyped object.");
            }
            if (this.value instanceof Builder) {
                throw FXMLLoader.this.constructLoadException("Cannot bind to builder property.");
            }
            if (!FXMLLoader.this.impl_isStaticLoad()) {
                Expression expression = Expression.valueOf(value.substring(FXMLLoader.BINDING_EXPRESSION_PREFIX.length(), value.length() - 1));
                BeanAdapter targetAdapter = new BeanAdapter(this.value);
                ObservableValue<Object> propertyModel = targetAdapter.getPropertyModel(attribute.name);
                Class<?> type = targetAdapter.getType(attribute.name);
                if (propertyModel instanceof Property) {
                    ((Property) propertyModel).bind(new ExpressionValue(FXMLLoader.this.namespace, expression, type));
                }
            }
        }

        private boolean isBindingExpression(String aValue) {
            return aValue.startsWith(FXMLLoader.BINDING_EXPRESSION_PREFIX) && aValue.endsWith("}");
        }

        private boolean isBidirectionalBindingExpression(String aValue) {
            return aValue.startsWith(FXMLLoader.BI_DIRECTIONAL_BINDING_PREFIX);
        }

        private boolean processValue(Class sourceType, String propertyName, String aValue) throws NumberFormatException, SecurityException, LoadException {
            boolean processed = false;
            if (sourceType == null && isTyped()) {
                BeanAdapter valueAdapter = getValueAdapter();
                Class<?> type = valueAdapter.getType(propertyName);
                if (type == null) {
                    throw new PropertyNotFoundException("Property \"" + propertyName + "\" does not exist or is read-only.");
                }
                if (List.class.isAssignableFrom(type) && valueAdapter.isReadOnly(propertyName)) {
                    populateListFromString(valueAdapter, propertyName, aValue);
                    processed = true;
                } else if (type.isArray()) {
                    applyProperty(propertyName, sourceType, populateArrayFromString(type, aValue));
                    processed = true;
                }
            }
            if (!processed) {
                applyProperty(propertyName, sourceType, resolvePrefixedValue(aValue));
                processed = true;
            }
            return processed;
        }

        private Object resolvePrefixedValue(String aValue) throws LoadException {
            if (aValue.startsWith(FXMLLoader.ESCAPE_PREFIX)) {
                String aValue2 = aValue.substring(FXMLLoader.ESCAPE_PREFIX.length());
                if (aValue2.length() == 0 || (!aValue2.startsWith(FXMLLoader.ESCAPE_PREFIX) && !aValue2.startsWith("@") && !aValue2.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX) && !aValue2.startsWith(FXMLLoader.EXPRESSION_PREFIX) && !aValue2.startsWith(FXMLLoader.BI_DIRECTIONAL_BINDING_PREFIX))) {
                    throw FXMLLoader.this.constructLoadException("Invalid escape sequence.");
                }
                return aValue2;
            }
            if (aValue.startsWith("@")) {
                aValue = aValue.substring("@".length());
                if (aValue.length() == 0) {
                    throw FXMLLoader.this.constructLoadException("Missing relative path.");
                }
                if (aValue.startsWith("@")) {
                    warnDeprecatedEscapeSequence("@");
                    return aValue;
                }
                if (aValue.charAt(0) == '/') {
                    URL res = FXMLLoader.this.getClassLoader().getResource(aValue.substring(1));
                    if (res == null) {
                        throw FXMLLoader.this.constructLoadException("Invalid resource: " + aValue + " not found on the classpath");
                    }
                    return res.toString();
                }
                try {
                    return new URL(FXMLLoader.this.location, aValue).toString();
                } catch (MalformedURLException e2) {
                    System.err.println(((Object) FXMLLoader.this.location) + "/" + aValue);
                }
            } else {
                if (aValue.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                    String aValue3 = aValue.substring(FXMLLoader.RESOURCE_KEY_PREFIX.length());
                    if (aValue3.length() == 0) {
                        throw FXMLLoader.this.constructLoadException("Missing resource key.");
                    }
                    if (!aValue3.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
                        if (FXMLLoader.this.resources == null) {
                            throw FXMLLoader.this.constructLoadException("No resources specified.");
                        }
                        if (!FXMLLoader.this.resources.containsKey(aValue3)) {
                            throw FXMLLoader.this.constructLoadException("Resource \"" + aValue3 + "\" not found.");
                        }
                        return FXMLLoader.this.resources.getString(aValue3);
                    }
                    warnDeprecatedEscapeSequence(FXMLLoader.RESOURCE_KEY_PREFIX);
                    return aValue3;
                }
                if (aValue.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                    String aValue4 = aValue.substring(FXMLLoader.EXPRESSION_PREFIX.length());
                    if (aValue4.length() == 0) {
                        throw FXMLLoader.this.constructLoadException("Missing expression.");
                    }
                    if (aValue4.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                        warnDeprecatedEscapeSequence(FXMLLoader.EXPRESSION_PREFIX);
                        return aValue4;
                    }
                    if (!aValue4.equals(FXMLLoader.NULL_KEYWORD)) {
                        return Expression.get(FXMLLoader.this.namespace, KeyPath.parse(aValue4));
                    }
                    return null;
                }
            }
            return aValue;
        }

        private Object populateArrayFromString(Class<?> type, String stringValue) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, LoadException, NegativeArraySizeException {
            Object propertyValue;
            Class<?> componentType = type.getComponentType();
            if (stringValue.length() > 0) {
                String[] values = stringValue.split(",");
                propertyValue = Array.newInstance(componentType, values.length);
                for (int i2 = 0; i2 < values.length; i2++) {
                    Array.set(propertyValue, i2, BeanAdapter.coerce(resolvePrefixedValue(values[i2].trim()), type.getComponentType()));
                }
            } else {
                propertyValue = Array.newInstance(componentType, 0);
            }
            return propertyValue;
        }

        private void populateListFromString(BeanAdapter valueAdapter, String listPropertyName, String stringValue) throws LoadException {
            List<Object> list = (List) valueAdapter.get((Object) listPropertyName);
            Type listType = valueAdapter.getGenericType(listPropertyName);
            Type itemType = (Class) BeanAdapter.getGenericListItemType(listType);
            if (itemType instanceof ParameterizedType) {
                itemType = ((ParameterizedType) itemType).getRawType();
            }
            if (stringValue.length() > 0) {
                String[] values = stringValue.split(",");
                for (String aValue : values) {
                    list.add(BeanAdapter.coerce(resolvePrefixedValue(aValue.trim()), (Class) itemType));
                }
            }
        }

        public void warnDeprecatedEscapeSequence(String prefix) {
            System.err.println(prefix + prefix + " is a deprecated escape sequence. Please use \\" + prefix + " instead.");
        }

        public void applyProperty(String name, Class<?> sourceType, Object value) throws NumberFormatException, SecurityException {
            if (sourceType == null) {
                getProperties().put(name, value);
            } else {
                BeanAdapter.put(this.value, sourceType, name, value);
            }
        }

        private Object getExpressionObject(String handlerValue) throws LoadException {
            if (handlerValue.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                String handlerValue2 = handlerValue.substring(FXMLLoader.EXPRESSION_PREFIX.length());
                if (handlerValue2.length() == 0) {
                    throw FXMLLoader.this.constructLoadException("Missing expression reference.");
                }
                Object expression = Expression.get(FXMLLoader.this.namespace, KeyPath.parse(handlerValue2));
                if (expression == null) {
                    throw FXMLLoader.this.constructLoadException("Unable to resolve expression : $" + handlerValue2);
                }
                return expression;
            }
            return null;
        }

        private <T> T getExpressionObjectOfType(String str, Class<T> cls) throws LoadException {
            T t2 = (T) getExpressionObject(str);
            if (t2 != null) {
                if (!cls.isInstance(t2)) {
                    throw FXMLLoader.this.constructLoadException("Error resolving \"" + str + "\" expression.Does not point to a " + cls.getName());
                }
                return t2;
            }
            return null;
        }

        private MethodHandler getControllerMethodHandle(String handlerName, SupportedType... types) throws LoadException {
            if (handlerName.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                String handlerName2 = handlerName.substring(FXMLLoader.CONTROLLER_METHOD_PREFIX.length());
                if (!handlerName2.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                    if (handlerName2.length() == 0) {
                        throw FXMLLoader.this.constructLoadException("Missing controller method.");
                    }
                    if (FXMLLoader.this.controller == null) {
                        throw FXMLLoader.this.constructLoadException("No controller specified.");
                    }
                    for (SupportedType t2 : types) {
                        Method method = FXMLLoader.this.controllerAccessor.getControllerMethods().get(t2).get(handlerName2);
                        if (method != null) {
                            return new MethodHandler(FXMLLoader.this.controller, method, t2);
                        }
                    }
                    Method method2 = FXMLLoader.this.controllerAccessor.getControllerMethods().get(SupportedType.PARAMETERLESS).get(handlerName2);
                    if (method2 != null) {
                        return new MethodHandler(FXMLLoader.this.controller, method2, SupportedType.PARAMETERLESS);
                    }
                    return null;
                }
                return null;
            }
            return null;
        }

        public void processEventHandlerAttributes() throws LoadException {
            if (this.eventHandlerAttributes.size() > 0 && !FXMLLoader.this.staticLoad) {
                Iterator<Attribute> it = this.eventHandlerAttributes.iterator();
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    String handlerName = attribute.value;
                    if ((this.value instanceof ObservableList) && attribute.name.equals(FXMLLoader.COLLECTION_HANDLER_NAME)) {
                        processObservableListHandler(handlerName);
                    } else if ((this.value instanceof ObservableMap) && attribute.name.equals(FXMLLoader.COLLECTION_HANDLER_NAME)) {
                        processObservableMapHandler(handlerName);
                    } else if ((this.value instanceof ObservableSet) && attribute.name.equals(FXMLLoader.COLLECTION_HANDLER_NAME)) {
                        processObservableSetHandler(handlerName);
                    } else if (attribute.name.endsWith(FXMLLoader.CHANGE_EVENT_HANDLER_SUFFIX)) {
                        processPropertyHandler(attribute.name, handlerName);
                    } else {
                        EventHandler<? extends Event> eventHandler = null;
                        MethodHandler handler = getControllerMethodHandle(handlerName, SupportedType.EVENT);
                        if (handler != null) {
                            eventHandler = new ControllerMethodEventHandler<>(handler);
                        }
                        if (eventHandler == null) {
                            eventHandler = (EventHandler) getExpressionObjectOfType(handlerName, EventHandler.class);
                        }
                        if (eventHandler == null) {
                            if (handlerName.length() == 0 || FXMLLoader.this.scriptEngine == null) {
                                throw FXMLLoader.this.constructLoadException("Error resolving " + attribute.name + "='" + attribute.value + "', either the event handler is not in the Namespace or there is an error in the script.");
                            }
                            eventHandler = new ScriptEventHandler(handlerName, FXMLLoader.this.scriptEngine);
                        }
                        getValueAdapter().put(attribute.name, (Object) eventHandler);
                    }
                }
            }
        }

        private void processObservableListHandler(String handlerValue) throws LoadException {
            ObservableList list = (ObservableList) this.value;
            if (handlerValue.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                MethodHandler handler = getControllerMethodHandle(handlerValue, SupportedType.LIST_CHANGE_LISTENER);
                if (handler == null) {
                    throw FXMLLoader.this.constructLoadException("Controller method \"" + handlerValue + "\" not found.");
                }
                list.addListener(new ObservableListChangeAdapter(handler));
                return;
            }
            if (handlerValue.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                Object listener = getExpressionObject(handlerValue);
                if (listener instanceof ListChangeListener) {
                    list.addListener((ListChangeListener) listener);
                } else {
                    if (!(listener instanceof InvalidationListener)) {
                        throw FXMLLoader.this.constructLoadException("Error resolving \"" + handlerValue + "\" expression.Must be either ListChangeListener or InvalidationListener");
                    }
                    list.addListener((InvalidationListener) listener);
                }
            }
        }

        private void processObservableMapHandler(String handlerValue) throws LoadException {
            ObservableMap map = (ObservableMap) this.value;
            if (handlerValue.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                MethodHandler handler = getControllerMethodHandle(handlerValue, SupportedType.MAP_CHANGE_LISTENER);
                if (handler == null) {
                    throw FXMLLoader.this.constructLoadException("Controller method \"" + handlerValue + "\" not found.");
                }
                map.addListener(new ObservableMapChangeAdapter(handler));
                return;
            }
            if (handlerValue.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                Object listener = getExpressionObject(handlerValue);
                if (listener instanceof MapChangeListener) {
                    map.addListener((MapChangeListener) listener);
                } else {
                    if (!(listener instanceof InvalidationListener)) {
                        throw FXMLLoader.this.constructLoadException("Error resolving \"" + handlerValue + "\" expression.Must be either MapChangeListener or InvalidationListener");
                    }
                    map.addListener((InvalidationListener) listener);
                }
            }
        }

        private void processObservableSetHandler(String handlerValue) throws LoadException {
            ObservableSet set = (ObservableSet) this.value;
            if (handlerValue.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                MethodHandler handler = getControllerMethodHandle(handlerValue, SupportedType.SET_CHANGE_LISTENER);
                if (handler == null) {
                    throw FXMLLoader.this.constructLoadException("Controller method \"" + handlerValue + "\" not found.");
                }
                set.addListener(new ObservableSetChangeAdapter(handler));
                return;
            }
            if (handlerValue.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                Object listener = getExpressionObject(handlerValue);
                if (listener instanceof SetChangeListener) {
                    set.addListener((SetChangeListener) listener);
                } else {
                    if (!(listener instanceof InvalidationListener)) {
                        throw FXMLLoader.this.constructLoadException("Error resolving \"" + handlerValue + "\" expression.Must be either SetChangeListener or InvalidationListener");
                    }
                    set.addListener((InvalidationListener) listener);
                }
            }
        }

        private void processPropertyHandler(String attributeName, String handlerValue) throws LoadException {
            int i2 = FXMLLoader.EVENT_HANDLER_PREFIX.length();
            int j2 = attributeName.length() - FXMLLoader.CHANGE_EVENT_HANDLER_SUFFIX.length();
            if (i2 != j2) {
                String key = Character.toLowerCase(attributeName.charAt(i2)) + attributeName.substring(i2 + 1, j2);
                ObservableValue<Object> propertyModel = getValueAdapter().getPropertyModel(key);
                if (propertyModel == null) {
                    throw FXMLLoader.this.constructLoadException(this.value.getClass().getName() + " does not define a property model for \"" + key + "\".");
                }
                if (handlerValue.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                    final MethodHandler handler = getControllerMethodHandle(handlerValue, SupportedType.PROPERTY_CHANGE_LISTENER, SupportedType.EVENT);
                    if (handler == null) {
                        throw FXMLLoader.this.constructLoadException("Controller method \"" + handlerValue + "\" not found.");
                    }
                    if (handler.type == SupportedType.EVENT) {
                        propertyModel.addListener(new ChangeListener<Object>() { // from class: javafx.fxml.FXMLLoader.Element.1
                            @Override // javafx.beans.value.ChangeListener
                            public void changed(ObservableValue<? extends Object> observableValue, Object oldValue, Object newValue) {
                                handler.invoke(new Event(Element.this.value, null, Event.ANY));
                            }
                        });
                        return;
                    } else {
                        propertyModel.addListener(new PropertyChangeAdapter(handler));
                        return;
                    }
                }
                if (handlerValue.startsWith(FXMLLoader.EXPRESSION_PREFIX)) {
                    Object listener = getExpressionObject(handlerValue);
                    if (listener instanceof ChangeListener) {
                        propertyModel.addListener((ChangeListener<? super Object>) listener);
                    } else {
                        if (!(listener instanceof InvalidationListener)) {
                            throw FXMLLoader.this.constructLoadException("Error resolving \"" + handlerValue + "\" expression.Must be either ChangeListener or InvalidationListener");
                        }
                        propertyModel.addListener((InvalidationListener) listener);
                    }
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ValueElement.class */
    private abstract class ValueElement extends Element {
        public String fx_id;

        public abstract Object constructValue() throws IOException;

        private ValueElement() {
            super();
            this.fx_id = null;
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processStartElement() throws IOException, IllegalArgumentException {
            super.processStartElement();
            updateValue(constructValue());
            if (this.value instanceof Builder) {
                processInstancePropertyAttributes();
            } else {
                processValue();
            }
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processEndElement() throws IOException, SecurityException, IllegalArgumentException {
            super.processEndElement();
            if (this.value instanceof Builder) {
                Builder<Object> builder = (Builder) this.value;
                updateValue(builder.build2());
                processValue();
            } else {
                processInstancePropertyAttributes();
            }
            processEventHandlerAttributes();
            if (this.staticPropertyAttributes.size() > 0) {
                Iterator<Attribute> it = this.staticPropertyAttributes.iterator();
                while (it.hasNext()) {
                    Attribute attribute = it.next();
                    processPropertyAttribute(attribute);
                }
            }
            if (this.staticPropertyElements.size() > 0) {
                Iterator<PropertyElement> it2 = this.staticPropertyElements.iterator();
                while (it2.hasNext()) {
                    PropertyElement element = it2.next();
                    BeanAdapter.put(this.value, element.sourceType, element.name, element.value);
                }
            }
            if (this.parent != null) {
                if (this.parent.isCollection()) {
                    this.parent.add(this.value);
                } else {
                    this.parent.set(this.value);
                }
            }
        }

        private Object getListValue(Element parent, String listPropertyName, Object value) throws NumberFormatException, SecurityException {
            Type listType;
            if (parent.isTyped() && (listType = parent.getValueAdapter().getGenericType(listPropertyName)) != null) {
                Type itemType = BeanAdapter.getGenericListItemType(listType);
                if (itemType instanceof ParameterizedType) {
                    itemType = ((ParameterizedType) itemType).getRawType();
                }
                value = BeanAdapter.coerce(value, (Class) itemType);
            }
            return value;
        }

        private void processValue() throws IllegalArgumentException, LoadException {
            if (this.parent == null) {
                FXMLLoader.this.root = this.value;
                String fxNSURI = FXMLLoader.this.xmlStreamReader.getNamespaceContext().getNamespaceURI(FXMLLoader.FX_NAMESPACE_PREFIX);
                if (fxNSURI != null) {
                    String fxVersion = fxNSURI.substring(fxNSURI.lastIndexOf("/") + 1);
                    if (FXMLLoader.compareJFXVersions("1", fxVersion) < 0) {
                        throw FXMLLoader.this.constructLoadException("Loading FXML document of version " + fxVersion + " by JavaFX runtime supporting version 1");
                    }
                }
                String defaultNSURI = FXMLLoader.this.xmlStreamReader.getNamespaceContext().getNamespaceURI("");
                if (defaultNSURI != null) {
                    String nsVersion = defaultNSURI.substring(defaultNSURI.lastIndexOf("/") + 1);
                    if (FXMLLoader.compareJFXVersions(FXMLLoader.JAVAFX_VERSION, nsVersion) < 0) {
                        Logging.getJavaFXLogger().warning("Loading FXML document with JavaFX API of version " + nsVersion + " by JavaFX runtime of version " + FXMLLoader.JAVAFX_VERSION);
                    }
                }
            }
            if (this.fx_id != null) {
                FXMLLoader.this.namespace.put(this.fx_id, this.value);
                IDProperty idProperty = (IDProperty) this.value.getClass().getAnnotation(IDProperty.class);
                if (idProperty != null) {
                    Map<String, Object> properties = getProperties();
                    if (properties.get(idProperty.value()) == null) {
                        properties.put(idProperty.value(), this.fx_id);
                    }
                }
                FXMLLoader.this.injectFields(this.fx_id, this.value);
            }
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processCharacters() throws LoadException {
            Class<?> type = this.value.getClass();
            DefaultProperty defaultProperty = (DefaultProperty) type.getAnnotation(DefaultProperty.class);
            if (defaultProperty != null) {
                String text = FXMLLoader.extraneousWhitespacePattern.matcher(FXMLLoader.this.xmlStreamReader.getText()).replaceAll(" ");
                String defaultPropertyName = defaultProperty.value();
                BeanAdapter valueAdapter = getValueAdapter();
                if (valueAdapter.isReadOnly(defaultPropertyName) && List.class.isAssignableFrom(valueAdapter.getType(defaultPropertyName))) {
                    List<Object> list = (List) valueAdapter.get((Object) defaultPropertyName);
                    list.add(getListValue(this, defaultPropertyName, text));
                    return;
                } else {
                    valueAdapter.put(defaultPropertyName, (Object) text.trim());
                    return;
                }
            }
            throw FXMLLoader.this.constructLoadException(type.getName() + " does not have a default property.");
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (prefix != null && prefix.equals(FXMLLoader.FX_NAMESPACE_PREFIX)) {
                if (localName.equals("id")) {
                    if (value.equals(FXMLLoader.NULL_KEYWORD)) {
                        throw FXMLLoader.this.constructLoadException("Invalid identifier.");
                    }
                    int n2 = value.length();
                    for (int i2 = 0; i2 < n2; i2++) {
                        if (!Character.isJavaIdentifierPart(value.charAt(i2))) {
                            throw FXMLLoader.this.constructLoadException("Invalid identifier.");
                        }
                    }
                    this.fx_id = value;
                    return;
                }
                if (localName.equals("controller")) {
                    if (FXMLLoader.this.current.parent != null) {
                        throw FXMLLoader.this.constructLoadException("fx:controller can only be applied to root element.");
                    }
                    if (FXMLLoader.this.controller != null) {
                        throw FXMLLoader.this.constructLoadException("Controller value already specified.");
                    }
                    if (!FXMLLoader.this.staticLoad) {
                        try {
                            Class<?> type = FXMLLoader.this.getClassLoader().loadClass(value);
                            try {
                                if (FXMLLoader.this.controllerFactory != null) {
                                    FXMLLoader.this.setController(FXMLLoader.this.controllerFactory.call(type));
                                } else {
                                    FXMLLoader.this.setController(ReflectUtil.newInstance(type));
                                }
                                return;
                            } catch (IllegalAccessException exception) {
                                throw FXMLLoader.this.constructLoadException(exception);
                            } catch (InstantiationException exception2) {
                                throw FXMLLoader.this.constructLoadException(exception2);
                            }
                        } catch (ClassNotFoundException exception3) {
                            throw FXMLLoader.this.constructLoadException(exception3);
                        }
                    }
                    return;
                }
                throw FXMLLoader.this.constructLoadException("Invalid attribute.");
            }
            super.processAttribute(prefix, localName, value);
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$InstanceDeclarationElement.class */
    private class InstanceDeclarationElement extends ValueElement {
        public Class<?> type;
        public String constant;
        public String factory;

        public InstanceDeclarationElement(Class<?> type) throws LoadException {
            super();
            this.constant = null;
            this.factory = null;
            this.type = type;
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement, javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (prefix != null && prefix.equals(FXMLLoader.FX_NAMESPACE_PREFIX)) {
                if (localName.equals("value")) {
                    this.value = value;
                    return;
                }
                if (localName.equals(FXMLLoader.FX_CONSTANT_ATTRIBUTE)) {
                    this.constant = value;
                    return;
                } else if (localName.equals(FXMLLoader.FX_FACTORY_ATTRIBUTE)) {
                    this.factory = value;
                    return;
                } else {
                    super.processAttribute(prefix, localName, value);
                    return;
                }
            }
            super.processAttribute(prefix, localName, value);
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement
        public Object constructValue() throws SecurityException, IOException, IllegalArgumentException {
            Object value;
            if (this.value != null) {
                value = BeanAdapter.coerce(this.value, this.type);
            } else if (this.constant != null) {
                value = BeanAdapter.getConstantValue(this.type, this.constant);
            } else if (this.factory == null) {
                value = FXMLLoader.this.builderFactory == null ? null : FXMLLoader.this.builderFactory.getBuilder(this.type);
                if (value == null) {
                    value = FXMLLoader.DEFAULT_BUILDER_FACTORY.getBuilder(this.type);
                }
                if (value == null) {
                    try {
                        value = ReflectUtil.newInstance(this.type);
                    } catch (IllegalAccessException exception) {
                        throw FXMLLoader.this.constructLoadException(exception);
                    } catch (InstantiationException exception2) {
                        throw FXMLLoader.this.constructLoadException(exception2);
                    }
                }
            } else {
                try {
                    Method factoryMethod = MethodUtil.getMethod(this.type, this.factory, new Class[0]);
                    try {
                        value = MethodUtil.invoke(factoryMethod, null, new Object[0]);
                    } catch (IllegalAccessException exception3) {
                        throw FXMLLoader.this.constructLoadException(exception3);
                    } catch (InvocationTargetException exception4) {
                        throw FXMLLoader.this.constructLoadException(exception4);
                    }
                } catch (NoSuchMethodException exception5) {
                    throw FXMLLoader.this.constructLoadException(exception5);
                }
            }
            return value;
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$UnknownTypeElement.class */
    private class UnknownTypeElement extends ValueElement {
        private UnknownTypeElement() {
            super();
        }

        @DefaultProperty("items")
        /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$UnknownTypeElement$UnknownValueMap.class */
        public class UnknownValueMap extends AbstractMap<String, Object> {
            private ArrayList<?> items = new ArrayList<>();
            private HashMap<String, Object> values = new HashMap<>();

            public UnknownValueMap() {
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Object get(Object key) {
                if (key == null) {
                    throw new NullPointerException();
                }
                return key.equals(((DefaultProperty) getClass().getAnnotation(DefaultProperty.class)).value()) ? this.items : this.values.get(key);
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Object put(String key, Object value) {
                if (key == null) {
                    throw new NullPointerException();
                }
                if (key.equals(((DefaultProperty) getClass().getAnnotation(DefaultProperty.class)).value())) {
                    throw new IllegalArgumentException();
                }
                return this.values.put(key, value);
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Set<Map.Entry<String, Object>> entrySet() {
                return Collections.emptySet();
            }
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement, javafx.fxml.FXMLLoader.Element
        public void processEndElement() throws IOException {
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement
        public Object constructValue() throws LoadException {
            return new UnknownValueMap();
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$IncludeElement.class */
    private class IncludeElement extends ValueElement {
        public String source;
        public ResourceBundle resources;
        public Charset charset;

        private IncludeElement() {
            super();
            this.source = null;
            this.resources = FXMLLoader.this.resources;
            this.charset = FXMLLoader.this.charset;
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement, javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (prefix == null) {
                if (localName.equals("source")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(localName, value);
                    }
                    this.source = value;
                    return;
                } else if (localName.equals("resources")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(localName, value);
                    }
                    this.resources = ResourceBundle.getBundle(value, Locale.getDefault(), FXMLLoader.this.resources.getClass().getClassLoader());
                    return;
                } else {
                    if (localName.equals("charset")) {
                        if (FXMLLoader.this.loadListener != null) {
                            FXMLLoader.this.loadListener.readInternalAttribute(localName, value);
                        }
                        this.charset = Charset.forName(value);
                        return;
                    }
                    super.processAttribute(prefix, localName, value);
                    return;
                }
            }
            super.processAttribute(prefix, localName, value);
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement
        public Object constructValue() throws IOException, IllegalArgumentException {
            URL location;
            if (this.source == null) {
                throw FXMLLoader.this.constructLoadException("source is required.");
            }
            ClassLoader cl = FXMLLoader.this.getClassLoader();
            if (this.source.charAt(0) != '/') {
                if (FXMLLoader.this.location == null) {
                    throw FXMLLoader.this.constructLoadException("Base location is undefined.");
                }
                location = new URL(FXMLLoader.this.location, this.source);
            } else {
                location = cl.getResource(this.source.substring(1));
                if (location == null) {
                    throw FXMLLoader.this.constructLoadException("Cannot resolve path: " + this.source);
                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader(location, this.resources, FXMLLoader.this.builderFactory, FXMLLoader.this.controllerFactory, this.charset, FXMLLoader.this.loaders);
            fxmlLoader.parentLoader = FXMLLoader.this;
            if (FXMLLoader.this.isCyclic(FXMLLoader.this, fxmlLoader)) {
                throw new IOException(String.format("Including \"%s\" in \"%s\" created cyclic reference.", fxmlLoader.location.toExternalForm(), FXMLLoader.this.location.toExternalForm()));
            }
            fxmlLoader.setClassLoader(cl);
            fxmlLoader.impl_setStaticLoad(FXMLLoader.this.staticLoad);
            Object value = fxmlLoader.loadImpl(FXMLLoader.this.callerClass);
            if (this.fx_id != null) {
                String id = this.fx_id + FXMLLoader.CONTROLLER_SUFFIX;
                Object controller = fxmlLoader.getController();
                FXMLLoader.this.namespace.put(id, controller);
                FXMLLoader.this.injectFields(id, controller);
            }
            return value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void injectFields(String fieldName, Object value) throws IllegalArgumentException, LoadException {
        List<Field> fields;
        if (this.controller != null && fieldName != null && (fields = this.controllerAccessor.getControllerFields().get(fieldName)) != null) {
            try {
                for (Field f2 : fields) {
                    f2.set(this.controller, value);
                }
            } catch (IllegalAccessException exception) {
                throw constructLoadException(exception);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ReferenceElement.class */
    private class ReferenceElement extends ValueElement {
        public String source;

        private ReferenceElement() {
            super();
            this.source = null;
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement, javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (prefix == null) {
                if (localName.equals("source")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(localName, value);
                    }
                    this.source = value;
                    return;
                }
                super.processAttribute(prefix, localName, value);
                return;
            }
            super.processAttribute(prefix, localName, value);
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement
        public Object constructValue() throws LoadException {
            if (this.source == null) {
                throw FXMLLoader.this.constructLoadException("source is required.");
            }
            KeyPath path = KeyPath.parse(this.source);
            if (!Expression.isDefined(FXMLLoader.this.namespace, path)) {
                throw FXMLLoader.this.constructLoadException("Value \"" + this.source + "\" does not exist.");
            }
            return Expression.get(FXMLLoader.this.namespace, path);
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$CopyElement.class */
    private class CopyElement extends ValueElement {
        public String source;

        private CopyElement() {
            super();
            this.source = null;
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement, javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (prefix == null) {
                if (localName.equals("source")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(localName, value);
                    }
                    this.source = value;
                    return;
                }
                super.processAttribute(prefix, localName, value);
                return;
            }
            super.processAttribute(prefix, localName, value);
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement
        public Object constructValue() throws IllegalArgumentException, LoadException {
            if (this.source == null) {
                throw FXMLLoader.this.constructLoadException("source is required.");
            }
            KeyPath path = KeyPath.parse(this.source);
            if (!Expression.isDefined(FXMLLoader.this.namespace, path)) {
                throw FXMLLoader.this.constructLoadException("Value \"" + this.source + "\" does not exist.");
            }
            Object sourceValue = Expression.get(FXMLLoader.this.namespace, path);
            Class<?> sourceValueType = sourceValue.getClass();
            Constructor<?> constructor = null;
            try {
                constructor = ConstructorUtil.getConstructor(sourceValueType, new Class[]{sourceValueType});
            } catch (NoSuchMethodException e2) {
            }
            if (constructor == null) {
                throw FXMLLoader.this.constructLoadException("Can't copy value " + sourceValue + ".");
            }
            try {
                ReflectUtil.checkPackageAccess(sourceValueType);
                Object value = constructor.newInstance(sourceValue);
                return value;
            } catch (IllegalAccessException exception) {
                throw FXMLLoader.this.constructLoadException(exception);
            } catch (InstantiationException exception2) {
                throw FXMLLoader.this.constructLoadException(exception2);
            } catch (InvocationTargetException exception3) {
                throw FXMLLoader.this.constructLoadException(exception3);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$RootElement.class */
    private class RootElement extends ValueElement {
        public String type;

        private RootElement() {
            super();
            this.type = null;
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement, javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (prefix == null) {
                if (localName.equals("type")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(localName, value);
                    }
                    this.type = value;
                    return;
                }
                super.processAttribute(prefix, localName, value);
                return;
            }
            super.processAttribute(prefix, localName, value);
        }

        @Override // javafx.fxml.FXMLLoader.ValueElement
        public Object constructValue() throws LoadException {
            Object value;
            if (this.type == null) {
                throw FXMLLoader.this.constructLoadException("type is required.");
            }
            Class<?> type = FXMLLoader.this.getType(this.type);
            if (type == null) {
                throw FXMLLoader.this.constructLoadException(this.type + " is not a valid type.");
            }
            if (FXMLLoader.this.root == null) {
                if (FXMLLoader.this.staticLoad) {
                    value = FXMLLoader.this.builderFactory == null ? null : FXMLLoader.this.builderFactory.getBuilder(type);
                    if (value == null) {
                        value = FXMLLoader.DEFAULT_BUILDER_FACTORY.getBuilder(type);
                    }
                    if (value == null) {
                        try {
                            value = ReflectUtil.newInstance(type);
                        } catch (IllegalAccessException exception) {
                            throw FXMLLoader.this.constructLoadException(exception);
                        } catch (InstantiationException exception2) {
                            throw FXMLLoader.this.constructLoadException(exception2);
                        }
                    }
                    FXMLLoader.this.root = value;
                } else {
                    throw FXMLLoader.this.constructLoadException("Root hasn't been set. Use method setRoot() before load.");
                }
            } else {
                if (!type.isAssignableFrom(FXMLLoader.this.root.getClass())) {
                    throw FXMLLoader.this.constructLoadException("Root is not an instance of " + type.getName() + ".");
                }
                value = FXMLLoader.this.root;
            }
            return value;
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$PropertyElement.class */
    private class PropertyElement extends Element {
        public final String name;
        public final Class<?> sourceType;
        public final boolean readOnly;

        public PropertyElement(String name, Class<?> sourceType) throws LoadException {
            super();
            if (this.parent == null) {
                throw FXMLLoader.this.constructLoadException("Invalid root element.");
            }
            if (this.parent.value == null) {
                throw FXMLLoader.this.constructLoadException("Parent element does not support property elements.");
            }
            this.name = name;
            this.sourceType = sourceType;
            if (sourceType == null) {
                if (name.startsWith(FXMLLoader.EVENT_HANDLER_PREFIX)) {
                    throw FXMLLoader.this.constructLoadException(PdfOps.DOUBLE_QUOTE__TOKEN + name + "\" is not a valid element name.");
                }
                Map<String, Object> parentProperties = this.parent.getProperties();
                if (this.parent.isTyped()) {
                    this.readOnly = this.parent.getValueAdapter().isReadOnly(name);
                } else {
                    this.readOnly = parentProperties.containsKey(name);
                }
                if (this.readOnly) {
                    Object value = parentProperties.get(name);
                    if (value == null) {
                        throw FXMLLoader.this.constructLoadException("Invalid property.");
                    }
                    updateValue(value);
                    return;
                }
                return;
            }
            this.readOnly = false;
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public boolean isCollection() {
            if (this.readOnly) {
                return super.isCollection();
            }
            return false;
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void add(Object element) throws NumberFormatException, SecurityException, LoadException {
            if (this.parent.isTyped()) {
                Type listType = this.parent.getValueAdapter().getGenericType(this.name);
                element = BeanAdapter.coerce(element, BeanAdapter.getListItemType(listType));
            }
            super.add(element);
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void set(Object value) throws NumberFormatException, SecurityException, LoadException {
            updateValue(value);
            if (this.sourceType == null) {
                this.parent.getProperties().put(this.name, value);
            } else if (this.parent.value instanceof Builder) {
                this.parent.staticPropertyElements.add(this);
            } else {
                BeanAdapter.put(this.parent.value, this.sourceType, this.name, value);
            }
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (!this.readOnly) {
                throw FXMLLoader.this.constructLoadException("Attributes are not supported for writable property elements.");
            }
            super.processAttribute(prefix, localName, value);
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processEndElement() throws IOException {
            super.processEndElement();
            if (this.readOnly) {
                processInstancePropertyAttributes();
                processEventHandlerAttributes();
            }
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processCharacters() throws NumberFormatException, SecurityException, IOException {
            String text = FXMLLoader.extraneousWhitespacePattern.matcher(FXMLLoader.this.xmlStreamReader.getText()).replaceAll(" ").trim();
            if (this.readOnly) {
                if (isCollection()) {
                    add(text);
                    return;
                } else {
                    super.processCharacters();
                    return;
                }
            }
            set(text);
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$UnknownStaticPropertyElement.class */
    private class UnknownStaticPropertyElement extends Element {
        public UnknownStaticPropertyElement() throws LoadException {
            super();
            if (this.parent == null) {
                throw FXMLLoader.this.constructLoadException("Invalid root element.");
            }
            if (this.parent.value == null) {
                throw FXMLLoader.this.constructLoadException("Parent element does not support property elements.");
            }
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public boolean isCollection() {
            return false;
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void set(Object value) {
            updateValue(value);
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processCharacters() throws IOException {
            String text = FXMLLoader.this.xmlStreamReader.getText();
            updateValue(FXMLLoader.extraneousWhitespacePattern.matcher(text).replaceAll(" ").trim());
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ScriptElement.class */
    private class ScriptElement extends Element {
        public String source;
        public Charset charset;

        private ScriptElement() {
            super();
            this.source = null;
            this.charset = FXMLLoader.this.charset;
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public boolean isCollection() {
            return false;
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processStartElement() throws IOException {
            ScriptEngine engine;
            URL location;
            super.processStartElement();
            if (this.source != null && !FXMLLoader.this.staticLoad) {
                int i2 = this.source.lastIndexOf(".");
                if (i2 == -1) {
                    throw FXMLLoader.this.constructLoadException("Cannot determine type of script \"" + this.source + "\".");
                }
                String extension = this.source.substring(i2 + 1);
                ClassLoader cl = FXMLLoader.this.getClassLoader();
                if (FXMLLoader.this.scriptEngine != null && FXMLLoader.this.scriptEngine.getFactory().getExtensions().contains(extension)) {
                    engine = FXMLLoader.this.scriptEngine;
                } else {
                    ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                    try {
                        Thread.currentThread().setContextClassLoader(cl);
                        ScriptEngineManager scriptEngineManager = FXMLLoader.this.getScriptEngineManager();
                        engine = scriptEngineManager.getEngineByExtension(extension);
                        Thread.currentThread().setContextClassLoader(oldLoader);
                    } catch (Throwable th) {
                        Thread.currentThread().setContextClassLoader(oldLoader);
                        throw th;
                    }
                }
                if (engine == null) {
                    throw FXMLLoader.this.constructLoadException("Unable to locate scripting engine for extension " + extension + ".");
                }
                try {
                    if (this.source.charAt(0) != '/') {
                        if (FXMLLoader.this.location == null) {
                            throw FXMLLoader.this.constructLoadException("Base location is undefined.");
                        }
                        location = new URL(FXMLLoader.this.location, this.source);
                    } else {
                        location = cl.getResource(this.source.substring(1));
                    }
                    InputStreamReader scriptReader = null;
                    try {
                        try {
                            scriptReader = new InputStreamReader(location.openStream(), this.charset);
                            engine.eval(scriptReader);
                            if (scriptReader != null) {
                                scriptReader.close();
                            }
                        } catch (ScriptException exception) {
                            exception.printStackTrace();
                            if (scriptReader != null) {
                                scriptReader.close();
                            }
                        }
                    } catch (Throwable th2) {
                        if (scriptReader != null) {
                            scriptReader.close();
                        }
                        throw th2;
                    }
                } catch (IOException exception2) {
                    throw FXMLLoader.this.constructLoadException(exception2);
                }
            }
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processEndElement() throws IOException {
            super.processEndElement();
            if (this.value != null && !FXMLLoader.this.staticLoad) {
                try {
                    FXMLLoader.this.scriptEngine.eval((String) this.value);
                } catch (ScriptException exception) {
                    System.err.println(exception.getMessage());
                }
            }
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processCharacters() throws LoadException {
            if (this.source != null) {
                throw FXMLLoader.this.constructLoadException("Script source already specified.");
            }
            if (FXMLLoader.this.scriptEngine == null && !FXMLLoader.this.staticLoad) {
                throw FXMLLoader.this.constructLoadException("Page language not specified.");
            }
            updateValue(FXMLLoader.this.xmlStreamReader.getText());
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws IOException {
            if (prefix == null && localName.equals("source")) {
                if (FXMLLoader.this.loadListener != null) {
                    FXMLLoader.this.loadListener.readInternalAttribute(localName, value);
                }
                this.source = value;
            } else {
                if (localName.equals("charset")) {
                    if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readInternalAttribute(localName, value);
                    }
                    this.charset = Charset.forName(value);
                    return;
                }
                throw FXMLLoader.this.constructLoadException(prefix == null ? localName : prefix + CallSiteDescriptor.TOKEN_DELIMITER + localName + " is not a valid attribute.");
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$DefineElement.class */
    private class DefineElement extends Element {
        private DefineElement() {
            super();
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public boolean isCollection() {
            return true;
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void add(Object element) {
        }

        @Override // javafx.fxml.FXMLLoader.Element
        public void processAttribute(String prefix, String localName, String value) throws LoadException {
            throw FXMLLoader.this.constructLoadException("Element does not support attributes.");
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$Attribute.class */
    private static class Attribute {
        public final String name;
        public final Class<?> sourceType;
        public final String value;

        public Attribute(String name, Class<?> sourceType, String value) {
            this.name = name;
            this.sourceType = sourceType;
            this.value = value;
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ControllerMethodEventHandler.class */
    private static class ControllerMethodEventHandler<T extends Event> implements EventHandler<T> {
        private final MethodHandler handler;

        public ControllerMethodEventHandler(MethodHandler handler) {
            this.handler = handler;
        }

        @Override // javafx.event.EventHandler
        public void handle(T event) {
            this.handler.invoke(event);
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ScriptEventHandler.class */
    private static class ScriptEventHandler implements EventHandler<Event> {
        public final String script;
        public final ScriptEngine scriptEngine;

        public ScriptEventHandler(String script, ScriptEngine scriptEngine) {
            this.script = script;
            this.scriptEngine = scriptEngine;
        }

        @Override // javafx.event.EventHandler
        public void handle(Event event) {
            Bindings engineBindings = this.scriptEngine.getBindings(100);
            Bindings localBindings = this.scriptEngine.createBindings();
            localBindings.put(FXMLLoader.EVENT_KEY, (Object) event);
            localBindings.putAll(engineBindings);
            this.scriptEngine.setBindings(localBindings, 100);
            try {
                this.scriptEngine.eval(this.script);
                this.scriptEngine.setBindings(engineBindings, 100);
            } catch (ScriptException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ObservableListChangeAdapter.class */
    private static class ObservableListChangeAdapter implements ListChangeListener {
        private final MethodHandler handler;

        public ObservableListChangeAdapter(MethodHandler handler) {
            this.handler = handler;
        }

        @Override // javafx.collections.ListChangeListener
        public void onChanged(ListChangeListener.Change change) {
            if (this.handler != null) {
                this.handler.invoke(change);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ObservableMapChangeAdapter.class */
    private static class ObservableMapChangeAdapter implements MapChangeListener {
        public final MethodHandler handler;

        public ObservableMapChangeAdapter(MethodHandler handler) {
            this.handler = handler;
        }

        @Override // javafx.collections.MapChangeListener
        public void onChanged(MapChangeListener.Change change) {
            if (this.handler != null) {
                this.handler.invoke(change);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ObservableSetChangeAdapter.class */
    private static class ObservableSetChangeAdapter implements SetChangeListener {
        public final MethodHandler handler;

        public ObservableSetChangeAdapter(MethodHandler handler) {
            this.handler = handler;
        }

        @Override // javafx.collections.SetChangeListener
        public void onChanged(SetChangeListener.Change change) {
            if (this.handler != null) {
                this.handler.invoke(change);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$PropertyChangeAdapter.class */
    private static class PropertyChangeAdapter implements ChangeListener<Object> {
        public final MethodHandler handler;

        public PropertyChangeAdapter(MethodHandler handler) {
            this.handler = handler;
        }

        @Override // javafx.beans.value.ChangeListener
        public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            this.handler.invoke(observable, oldValue, newValue);
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$MethodHandler.class */
    private static class MethodHandler {
        private final Object controller;
        private final Method method;
        private final SupportedType type;

        private MethodHandler(Object controller, Method method, SupportedType type) {
            this.method = method;
            this.controller = controller;
            this.type = type;
        }

        public void invoke(Object... params) {
            try {
                if (this.type != SupportedType.PARAMETERLESS) {
                    MethodUtil.invoke(this.method, this.controller, params);
                } else {
                    MethodUtil.invoke(this.method, this.controller, new Object[0]);
                }
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            } catch (InvocationTargetException exception2) {
                throw new RuntimeException(exception2);
            }
        }
    }

    public FXMLLoader() {
        this((URL) null);
    }

    public FXMLLoader(URL location) {
        this(location, null);
    }

    public FXMLLoader(URL location, ResourceBundle resources) {
        this(location, resources, null);
    }

    public FXMLLoader(URL location, ResourceBundle resources, BuilderFactory builderFactory) {
        this(location, resources, builderFactory, null);
    }

    public FXMLLoader(URL location, ResourceBundle resources, BuilderFactory builderFactory, Callback<Class<?>, Object> controllerFactory) {
        this(location, resources, builderFactory, controllerFactory, Charset.forName("UTF-8"));
    }

    public FXMLLoader(Charset charset) {
        this(null, null, null, null, charset);
    }

    public FXMLLoader(URL location, ResourceBundle resources, BuilderFactory builderFactory, Callback<Class<?>, Object> controllerFactory, Charset charset) {
        this(location, resources, builderFactory, controllerFactory, charset, new LinkedList());
    }

    public FXMLLoader(URL location, ResourceBundle resources, BuilderFactory builderFactory, Callback<Class<?>, Object> controllerFactory, Charset charset, LinkedList<FXMLLoader> loaders) {
        this.namespace = FXCollections.observableHashMap();
        this.root = null;
        this.controller = null;
        this.classLoader = null;
        this.staticLoad = false;
        this.loadListener = null;
        this.xmlStreamReader = null;
        this.current = null;
        this.scriptEngine = null;
        this.packages = new LinkedList();
        this.classes = new HashMap();
        this.scriptEngineManager = null;
        this.controllerAccessor = new ControllerAccessor();
        setLocation(location);
        setResources(resources);
        setBuilderFactory(builderFactory);
        setControllerFactory(controllerFactory);
        setCharset(charset);
        this.loaders = new LinkedList<>(loaders);
    }

    public URL getLocation() {
        return this.location;
    }

    public void setLocation(URL location) {
        this.location = location;
    }

    public ResourceBundle getResources() {
        return this.resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }

    public ObservableMap<String, Object> getNamespace() {
        return this.namespace;
    }

    public <T> T getRoot() {
        return (T) this.root;
    }

    public void setRoot(Object root) {
        this.root = root;
    }

    public boolean equals(Object obj) {
        if (obj instanceof FXMLLoader) {
            FXMLLoader loader = (FXMLLoader) obj;
            if (this.location == null || loader.location == null) {
                return loader.location == this.location;
            }
            return this.location.toExternalForm().equals(loader.location.toExternalForm());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCyclic(FXMLLoader currentLoader, FXMLLoader node) {
        if (currentLoader == null) {
            return false;
        }
        if (currentLoader.equals(node)) {
            return true;
        }
        return isCyclic(currentLoader.parentLoader, node);
    }

    public <T> T getController() {
        return (T) this.controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
        if (controller == null) {
            this.namespace.remove("controller");
        } else {
            this.namespace.put("controller", controller);
        }
        this.controllerAccessor.setController(controller);
    }

    public BuilderFactory getBuilderFactory() {
        return this.builderFactory;
    }

    public void setBuilderFactory(BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    public Callback<Class<?>, Object> getControllerFactory() {
        return this.controllerFactory;
    }

    public void setControllerFactory(Callback<Class<?>, Object> controllerFactory) {
        this.controllerFactory = controllerFactory;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset is null.");
        }
        this.charset = charset;
    }

    @CallerSensitive
    public ClassLoader getClassLoader() {
        if (this.classLoader == null) {
            SecurityManager sm = System.getSecurityManager();
            Class caller = sm != null ? Reflection.getCallerClass() : null;
            return getDefaultClassLoader(caller);
        }
        return this.classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException();
        }
        this.classLoader = classLoader;
        clearImports();
    }

    public boolean impl_isStaticLoad() {
        return this.staticLoad;
    }

    public void impl_setStaticLoad(boolean staticLoad) {
        this.staticLoad = staticLoad;
    }

    public LoadListener impl_getLoadListener() {
        return this.loadListener;
    }

    public void impl_setLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    @CallerSensitive
    public <T> T load() throws IOException {
        return (T) loadImpl(System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    @CallerSensitive
    public <T> T load(InputStream inputStream) throws IOException {
        return (T) loadImpl(inputStream, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> T loadImpl(Class<?> cls) throws IOException {
        if (this.location == null) {
            throw new IllegalStateException("Location is not set.");
        }
        InputStream inputStreamOpenStream = null;
        try {
            inputStreamOpenStream = this.location.openStream();
            T t2 = (T) loadImpl(inputStreamOpenStream, cls);
            if (inputStreamOpenStream != null) {
                inputStreamOpenStream.close();
            }
            return t2;
        } catch (Throwable th) {
            if (inputStreamOpenStream != null) {
                inputStreamOpenStream.close();
            }
            throw th;
        }
    }

    private <T> T loadImpl(InputStream inputStream, Class<?> cls) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is null.");
        }
        this.callerClass = cls;
        this.controllerAccessor.setCallerClass(cls);
        try {
            try {
                try {
                    clearImports();
                    this.namespace.put("location", this.location);
                    this.namespace.put("resources", this.resources);
                    this.scriptEngine = null;
                    try {
                        XMLInputFactory xMLInputFactoryNewInstance = XMLInputFactory.newInstance();
                        xMLInputFactoryNewInstance.setProperty(XMLInputFactory.IS_COALESCING, true);
                        this.xmlStreamReader = new StreamReaderDelegate(xMLInputFactoryNewInstance.createXMLStreamReader(new InputStreamReader(inputStream, this.charset))) { // from class: javafx.fxml.FXMLLoader.2
                            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
                            public String getPrefix() {
                                String prefix = super.getPrefix();
                                if (prefix != null && prefix.length() == 0) {
                                    prefix = null;
                                }
                                return prefix;
                            }

                            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
                            public String getAttributePrefix(int index) {
                                String attributePrefix = super.getAttributePrefix(index);
                                if (attributePrefix != null && attributePrefix.length() == 0) {
                                    attributePrefix = null;
                                }
                                return attributePrefix;
                            }
                        };
                        this.loaders.push(this);
                        while (this.xmlStreamReader.hasNext()) {
                            try {
                                switch (this.xmlStreamReader.next()) {
                                    case 1:
                                        processStartElement();
                                        break;
                                    case 2:
                                        processEndElement();
                                        break;
                                    case 3:
                                        processProcessingInstruction();
                                        break;
                                    case 4:
                                        processCharacters();
                                        break;
                                    case 5:
                                        processComment();
                                        break;
                                }
                            } catch (XMLStreamException e2) {
                                throw constructLoadException(e2);
                            }
                        }
                        if (this.controller != null) {
                            if (this.controller instanceof Initializable) {
                                ((Initializable) this.controller).initialize(this.location, this.resources);
                            } else {
                                this.controllerAccessor.getControllerFields();
                                injectFields("location", this.location);
                                injectFields("resources", this.resources);
                                Method method = this.controllerAccessor.getControllerMethods().get(SupportedType.PARAMETERLESS).get(INITIALIZE_METHOD_NAME);
                                if (method != null) {
                                    try {
                                        MethodUtil.invoke(method, this.controller, new Object[0]);
                                    } catch (IllegalAccessException e3) {
                                    } catch (InvocationTargetException e4) {
                                        throw constructLoadException(e4);
                                    }
                                }
                            }
                        }
                        return (T) this.root;
                    } catch (XMLStreamException e5) {
                        throw constructLoadException(e5);
                    }
                } catch (Exception e6) {
                    throw constructLoadException(e6);
                }
            } catch (LoadException e7) {
                throw e7;
            }
        } finally {
            this.controllerAccessor.setCallerClass(null);
            this.controllerAccessor.reset();
            this.xmlStreamReader = null;
        }
    }

    private void clearImports() {
        this.packages.clear();
        this.classes.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LoadException constructLoadException(String message) {
        return new LoadException(message + constructFXMLTrace());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LoadException constructLoadException(Throwable cause) {
        return new LoadException(constructFXMLTrace(), cause);
    }

    private LoadException constructLoadException(String message, Throwable cause) {
        return new LoadException(message + constructFXMLTrace(), cause);
    }

    private String constructFXMLTrace() {
        StringBuilder messageBuilder = new StringBuilder("\n");
        Iterator<FXMLLoader> it = this.loaders.iterator();
        while (it.hasNext()) {
            FXMLLoader loader = it.next();
            messageBuilder.append(loader.location != null ? loader.location.getPath() : "unknown path");
            if (loader.current != null) {
                messageBuilder.append(CallSiteDescriptor.TOKEN_DELIMITER);
                messageBuilder.append(loader.impl_getLineNumber());
            }
            messageBuilder.append("\n");
        }
        return messageBuilder.toString();
    }

    public int impl_getLineNumber() {
        return this.xmlStreamReader.getLocation().getLineNumber();
    }

    public ParseTraceElement[] impl_getParseTrace() {
        ParseTraceElement[] parseTrace = new ParseTraceElement[this.loaders.size()];
        int i2 = 0;
        Iterator<FXMLLoader> it = this.loaders.iterator();
        while (it.hasNext()) {
            FXMLLoader loader = it.next();
            int i3 = i2;
            i2++;
            parseTrace[i3] = new ParseTraceElement(loader.location, loader.current != null ? loader.impl_getLineNumber() : -1);
        }
        return parseTrace;
    }

    private void processProcessingInstruction() throws LoadException {
        String piTarget = this.xmlStreamReader.getPITarget().trim();
        if (piTarget.equals("language")) {
            processLanguage();
        } else if (piTarget.equals("import")) {
            processImport();
        }
    }

    private boolean isLanguageJavaScript(String str) {
        if (str == null) {
            return false;
        }
        String str2 = str.trim();
        String[] jsLang = {"nashorn", "javascript", "js", "ecmascript"};
        for (String item : jsLang) {
            if (str2.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }

    private void processLanguage() throws LoadException {
        if (this.scriptEngine != null) {
            throw constructLoadException("Page language already set.");
        }
        String language = this.xmlStreamReader.getPIData();
        if (isLanguageJavaScript(language) && !ALLOW_JAVASCRIPT.booleanValue()) {
            throw constructLoadException("JavaScript script engine is disabled.");
        }
        if (this.loadListener != null) {
            this.loadListener.readLanguageProcessingInstruction(language);
        }
        if (!this.staticLoad) {
            ScriptEngineManager scriptEngineManager = getScriptEngineManager();
            this.scriptEngine = scriptEngineManager.getEngineByName(language);
        }
        if (this.scriptEngine != null) {
            String engineName = this.scriptEngine.getFactory().getEngineName();
            if (engineName.toLowerCase(Locale.ROOT).contains("nashorn") && !ALLOW_JAVASCRIPT.booleanValue()) {
                throw constructLoadException("JavaScript script engine is disabled.");
            }
        }
    }

    private void processImport() throws LoadException {
        String target = this.xmlStreamReader.getPIData().trim();
        if (this.loadListener != null) {
            this.loadListener.readImportProcessingInstruction(target);
        }
        if (target.endsWith(".*")) {
            importPackage(target.substring(0, target.length() - 2));
        } else {
            importClass(target);
        }
    }

    private void processComment() throws LoadException {
        if (this.loadListener != null) {
            this.loadListener.readComment(this.xmlStreamReader.getText());
        }
    }

    private void processStartElement() throws IOException {
        createElement();
        this.current.processStartElement();
        if (this.root == null) {
            this.root = this.current.value;
        }
    }

    private void createElement() throws IOException {
        String prefix = this.xmlStreamReader.getPrefix();
        String localName = this.xmlStreamReader.getLocalName();
        if (prefix != null) {
            if (prefix.equals(FX_NAMESPACE_PREFIX)) {
                if (localName.equals("include")) {
                    if (this.loadListener != null) {
                        this.loadListener.beginIncludeElement();
                    }
                    this.current = new IncludeElement();
                    return;
                }
                if (localName.equals(REFERENCE_TAG)) {
                    if (this.loadListener != null) {
                        this.loadListener.beginReferenceElement();
                    }
                    this.current = new ReferenceElement();
                    return;
                }
                if (localName.equals("copy")) {
                    if (this.loadListener != null) {
                        this.loadListener.beginCopyElement();
                    }
                    this.current = new CopyElement();
                    return;
                }
                if (localName.equals("root")) {
                    if (this.loadListener != null) {
                        this.loadListener.beginRootElement();
                    }
                    this.current = new RootElement();
                    return;
                } else if (localName.equals("script")) {
                    if (this.loadListener != null) {
                        this.loadListener.beginScriptElement();
                    }
                    this.current = new ScriptElement();
                    return;
                } else {
                    if (localName.equals(DEFINE_TAG)) {
                        if (this.loadListener != null) {
                            this.loadListener.beginDefineElement();
                        }
                        this.current = new DefineElement();
                        return;
                    }
                    throw constructLoadException(prefix + CallSiteDescriptor.TOKEN_DELIMITER + localName + " is not a valid element.");
                }
            }
            throw constructLoadException("Unexpected namespace prefix: " + prefix + ".");
        }
        int i2 = localName.lastIndexOf(46);
        if (Character.isLowerCase(localName.charAt(i2 + 1))) {
            String name = localName.substring(i2 + 1);
            if (i2 == -1) {
                if (this.loadListener != null) {
                    this.loadListener.beginPropertyElement(name, null);
                }
                this.current = new PropertyElement(name, null);
                return;
            }
            Class<?> sourceType = getType(localName.substring(0, i2));
            if (sourceType != null) {
                if (this.loadListener != null) {
                    this.loadListener.beginPropertyElement(name, sourceType);
                }
                this.current = new PropertyElement(name, sourceType);
                return;
            } else {
                if (this.staticLoad) {
                    if (this.loadListener != null) {
                        this.loadListener.beginUnknownStaticPropertyElement(localName);
                    }
                    this.current = new UnknownStaticPropertyElement();
                    return;
                }
                throw constructLoadException(localName + " is not a valid property.");
            }
        }
        if (this.current == null && this.root != null) {
            throw constructLoadException("Root value already specified.");
        }
        Class<?> type = getType(localName);
        if (type != null) {
            if (this.loadListener != null) {
                this.loadListener.beginInstanceDeclarationElement(type);
            }
            this.current = new InstanceDeclarationElement(type);
        } else {
            if (this.staticLoad) {
                if (this.loadListener != null) {
                    this.loadListener.beginUnknownTypeElement(localName);
                }
                this.current = new UnknownTypeElement();
                return;
            }
            throw constructLoadException(localName + " is not a valid type.");
        }
    }

    private void processEndElement() throws IOException {
        this.current.processEndElement();
        if (this.loadListener != null) {
            this.loadListener.endElement(this.current.value);
        }
        this.current = this.current.parent;
    }

    private void processCharacters() throws IOException {
        if (!this.xmlStreamReader.isWhiteSpace()) {
            this.current.processCharacters();
        }
    }

    private void importPackage(String name) throws LoadException {
        this.packages.add(name);
    }

    private void importClass(String name) throws LoadException {
        try {
            loadType(name, true);
        } catch (ClassNotFoundException exception) {
            throw constructLoadException(exception);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class<?> getType(String name) throws LoadException {
        Class<?> type = null;
        if (Character.isLowerCase(name.charAt(0))) {
            try {
                type = loadType(name, false);
            } catch (ClassNotFoundException e2) {
            }
        } else {
            type = this.classes.get(name);
            if (type == null) {
                for (String packageName : this.packages) {
                    try {
                        type = loadTypeForPackage(packageName, name);
                    } catch (ClassNotFoundException e3) {
                    }
                    if (type != null) {
                        break;
                    }
                }
                if (type != null) {
                    this.classes.put(name, type);
                }
            }
        }
        return type;
    }

    private Class<?> loadType(String name, boolean cache) throws ClassNotFoundException {
        int i2 = name.indexOf(46);
        int n2 = name.length();
        while (i2 != -1 && i2 < n2 && Character.isLowerCase(name.charAt(i2 + 1))) {
            i2 = name.indexOf(46, i2 + 1);
        }
        if (i2 == -1 || i2 == n2) {
            throw new ClassNotFoundException();
        }
        String packageName = name.substring(0, i2);
        String className = name.substring(i2 + 1);
        Class<?> type = loadTypeForPackage(packageName, className);
        if (cache) {
            this.classes.put(className, type);
        }
        return type;
    }

    private Class<?> loadTypeForPackage(String packageName, String className) throws ClassNotFoundException {
        return getClassLoader().loadClass(packageName + "." + className.replace('.', '$'));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SupportedType toSupportedType(Method m2) {
        for (SupportedType t2 : SupportedType.values()) {
            if (t2.methodIsOfType(m2)) {
                return t2;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ScriptEngineManager getScriptEngineManager() {
        if (this.scriptEngineManager == null) {
            this.scriptEngineManager = new ScriptEngineManager();
            this.scriptEngineManager.setBindings(new SimpleBindings(this.namespace));
        }
        return this.scriptEngineManager;
    }

    public static Class<?> loadType(String packageName, String className) throws ClassNotFoundException {
        return loadType(packageName + "." + className.replace('.', '$'));
    }

    public static Class<?> loadType(String className) throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(className);
        return Class.forName(className, true, getDefaultClassLoader());
    }

    private static boolean needsClassLoaderPermissionCheck(ClassLoader from, ClassLoader to) {
        if (from == to || from == null) {
            return false;
        }
        if (to == null) {
            return true;
        }
        ClassLoader acl = to;
        do {
            acl = acl.getParent();
            if (from == acl) {
                return false;
            }
        } while (acl != null);
        return true;
    }

    private static ClassLoader getDefaultClassLoader(Class caller) {
        if (defaultClassLoader == null) {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                ClassLoader callerClassLoader = caller != null ? caller.getClassLoader() : null;
                if (needsClassLoaderPermissionCheck(callerClassLoader, FXMLLoader.class.getClassLoader())) {
                    sm.checkPermission(GET_CLASSLOADER_PERMISSION);
                }
            }
            return Thread.currentThread().getContextClassLoader();
        }
        return defaultClassLoader;
    }

    @CallerSensitive
    public static ClassLoader getDefaultClassLoader() {
        SecurityManager sm = System.getSecurityManager();
        Class caller = sm != null ? Reflection.getCallerClass() : null;
        return getDefaultClassLoader(caller);
    }

    public static void setDefaultClassLoader(ClassLoader defaultClassLoader2) {
        if (defaultClassLoader2 == null) {
            throw new NullPointerException();
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AllPermission());
        }
        defaultClassLoader = defaultClassLoader2;
    }

    @CallerSensitive
    public static <T> T load(URL url) throws IOException {
        return (T) loadImpl(url, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL url, Class<?> cls) throws IOException {
        return (T) loadImpl(url, null, cls);
    }

    @CallerSensitive
    public static <T> T load(URL url, ResourceBundle resourceBundle) throws IOException {
        return (T) loadImpl(url, resourceBundle, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL url, ResourceBundle resourceBundle, Class<?> cls) throws IOException {
        return (T) loadImpl(url, resourceBundle, null, cls);
    }

    @CallerSensitive
    public static <T> T load(URL url, ResourceBundle resourceBundle, BuilderFactory builderFactory) throws IOException {
        return (T) loadImpl(url, resourceBundle, builderFactory, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL url, ResourceBundle resourceBundle, BuilderFactory builderFactory, Class<?> cls) throws IOException {
        return (T) loadImpl(url, resourceBundle, builderFactory, null, cls);
    }

    @CallerSensitive
    public static <T> T load(URL url, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback) throws IOException {
        return (T) loadImpl(url, resourceBundle, builderFactory, callback, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL url, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback, Class<?> cls) throws IOException {
        return (T) loadImpl(url, resourceBundle, builderFactory, callback, Charset.forName("UTF-8"), cls);
    }

    @CallerSensitive
    public static <T> T load(URL url, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback, Charset charset) throws IOException {
        return (T) loadImpl(url, resourceBundle, builderFactory, callback, charset, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
    }

    private static <T> T loadImpl(URL url, ResourceBundle resourceBundle, BuilderFactory builderFactory, Callback<Class<?>, Object> callback, Charset charset, Class<?> cls) throws IOException {
        if (url == null) {
            throw new NullPointerException("Location is required.");
        }
        return (T) new FXMLLoader(url, resourceBundle, builderFactory, callback, charset).loadImpl(cls);
    }

    static int compareJFXVersions(String rtVer, String nsVer) throws NumberFormatException {
        int retVal = 0;
        if (rtVer == null || "".equals(rtVer) || nsVer == null || "".equals(nsVer)) {
            return 0;
        }
        if (rtVer.equals(nsVer)) {
            return 0;
        }
        int dashIndex = rtVer.indexOf(LanguageTag.SEP);
        if (dashIndex > 0) {
            rtVer = rtVer.substring(0, dashIndex);
        }
        int underIndex = rtVer.indexOf("_");
        if (underIndex > 0) {
            rtVer = rtVer.substring(0, underIndex);
        }
        if (!Pattern.matches("^(\\d+)(\\.\\d+)*$", rtVer) || !Pattern.matches("^(\\d+)(\\.\\d+)*$", nsVer)) {
            return 0;
        }
        StringTokenizer nsVerTokenizer = new StringTokenizer(nsVer, ".");
        StringTokenizer rtVerTokenizer = new StringTokenizer(rtVer, ".");
        int nsDigit = 0;
        boolean rtVerEnd = false;
        while (true) {
            if (!nsVerTokenizer.hasMoreTokens() || retVal != 0) {
                break;
            }
            nsDigit = Integer.parseInt(nsVerTokenizer.nextToken());
            if (rtVerTokenizer.hasMoreTokens()) {
                int rtDigit = Integer.parseInt(rtVerTokenizer.nextToken());
                retVal = rtDigit - nsDigit;
            } else {
                rtVerEnd = true;
                break;
            }
        }
        if (rtVerTokenizer.hasMoreTokens() && retVal == 0) {
            int rtDigit2 = Integer.parseInt(rtVerTokenizer.nextToken());
            if (rtDigit2 > 0) {
                retVal = 1;
            }
        }
        if (rtVerEnd) {
            if (nsDigit <= 0) {
                while (true) {
                    if (!nsVerTokenizer.hasMoreTokens()) {
                        break;
                    }
                    int nsDigit2 = Integer.parseInt(nsVerTokenizer.nextToken());
                    if (nsDigit2 > 0) {
                        retVal = -1;
                        break;
                    }
                }
            } else {
                retVal = -1;
            }
        }
        return retVal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkAllPermissions() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AllPermission());
        }
    }

    /* loaded from: jfxrt.jar:javafx/fxml/FXMLLoader$ControllerAccessor.class */
    private static final class ControllerAccessor {
        private static final int PUBLIC = 1;
        private static final int PROTECTED = 2;
        private static final int PACKAGE = 4;
        private static final int PRIVATE = 8;
        private static final int INITIAL_CLASS_ACCESS = 15;
        private static final int INITIAL_MEMBER_ACCESS = 15;
        private static final int METHODS = 0;
        private static final int FIELDS = 1;
        private Object controller;
        private ClassLoader callerClassLoader;
        private Map<String, List<Field>> controllerFields;
        private Map<SupportedType, Map<String, Method>> controllerMethods;

        private ControllerAccessor() {
        }

        void setController(Object controller) {
            if (this.controller != controller) {
                this.controller = controller;
                reset();
            }
        }

        void setCallerClass(Class<?> callerClass) {
            ClassLoader newCallerClassLoader = callerClass != null ? callerClass.getClassLoader() : null;
            if (this.callerClassLoader != newCallerClassLoader) {
                this.callerClassLoader = newCallerClassLoader;
                reset();
            }
        }

        void reset() {
            this.controllerFields = null;
            this.controllerMethods = null;
        }

        Map<String, List<Field>> getControllerFields() {
            if (this.controllerFields == null) {
                this.controllerFields = new HashMap();
                if (this.callerClassLoader == null) {
                    FXMLLoader.checkAllPermissions();
                }
                addAccessibleMembers(this.controller.getClass(), 15, 15, 1);
            }
            return this.controllerFields;
        }

        Map<SupportedType, Map<String, Method>> getControllerMethods() {
            if (this.controllerMethods == null) {
                this.controllerMethods = new EnumMap(SupportedType.class);
                for (SupportedType t2 : SupportedType.values()) {
                    this.controllerMethods.put(t2, new HashMap());
                }
                if (this.callerClassLoader == null) {
                    FXMLLoader.checkAllPermissions();
                }
                addAccessibleMembers(this.controller.getClass(), 15, 15, 0);
            }
            return this.controllerMethods;
        }

        private void addAccessibleMembers(final Class<?> type, int prevAllowedClassAccess, int prevAllowedMemberAccess, final int membersType) {
            if (type == Object.class) {
                return;
            }
            int allowedClassAccess = prevAllowedClassAccess;
            int allowedMemberAccess = prevAllowedMemberAccess;
            if (this.callerClassLoader != null && type.getClassLoader() != this.callerClassLoader) {
                allowedClassAccess &= 1;
                allowedMemberAccess &= 1;
            }
            int classAccess = getAccess(type.getModifiers());
            if ((classAccess & allowedClassAccess) == 0) {
                return;
            }
            ReflectUtil.checkPackageAccess(type);
            addAccessibleMembers(type.getSuperclass(), allowedClassAccess, allowedMemberAccess, membersType);
            final int finalAllowedMemberAccess = allowedMemberAccess;
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: javafx.fxml.FXMLLoader.ControllerAccessor.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() throws SecurityException {
                    if (membersType == 1) {
                        ControllerAccessor.this.addAccessibleFields(type, finalAllowedMemberAccess);
                        return null;
                    }
                    ControllerAccessor.this.addAccessibleMethods(type, finalAllowedMemberAccess);
                    return null;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:20:0x0073  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void addAccessibleFields(java.lang.Class<?> r5, int r6) throws java.lang.SecurityException {
            /*
                r4 = this;
                r0 = r5
                int r0 = r0.getModifiers()
                boolean r0 = java.lang.reflect.Modifier.isPublic(r0)
                r7 = r0
                r0 = r5
                java.lang.reflect.Field[] r0 = r0.getDeclaredFields()
                r8 = r0
                r0 = 0
                r9 = r0
            L11:
                r0 = r9
                r1 = r8
                int r1 = r1.length
                if (r0 >= r1) goto L9e
                r0 = r8
                r1 = r9
                r0 = r0[r1]
                r10 = r0
                r0 = r10
                int r0 = r0.getModifiers()
                r11 = r0
                r0 = r11
                r1 = 24
                r0 = r0 & r1
                if (r0 != 0) goto L98
                r0 = r11
                int r0 = getAccess(r0)
                r1 = r6
                r0 = r0 & r1
                if (r0 != 0) goto L3c
                goto L98
            L3c:
                r0 = r7
                if (r0 == 0) goto L48
                r0 = r11
                boolean r0 = java.lang.reflect.Modifier.isPublic(r0)
                if (r0 != 0) goto L5b
            L48:
                r0 = r10
                java.lang.Class<javafx.fxml.FXML> r1 = javafx.fxml.FXML.class
                java.lang.annotation.Annotation r0 = r0.getAnnotation(r1)
                if (r0 != 0) goto L55
                goto L98
            L55:
                r0 = r10
                r1 = 1
                r0.setAccessible(r1)
            L5b:
                r0 = r4
                java.util.Map<java.lang.String, java.util.List<java.lang.reflect.Field>> r0 = r0.controllerFields
                r1 = r10
                java.lang.String r1 = r1.getName()
                java.lang.Object r0 = r0.get(r1)
                java.util.List r0 = (java.util.List) r0
                r12 = r0
                r0 = r12
                if (r0 != 0) goto L8e
                java.util.ArrayList r0 = new java.util.ArrayList
                r1 = r0
                r2 = 1
                r1.<init>(r2)
                r12 = r0
                r0 = r4
                java.util.Map<java.lang.String, java.util.List<java.lang.reflect.Field>> r0 = r0.controllerFields
                r1 = r10
                java.lang.String r1 = r1.getName()
                r2 = r12
                java.lang.Object r0 = r0.put(r1, r2)
            L8e:
                r0 = r12
                r1 = r10
                boolean r0 = r0.add(r1)
            L98:
                int r9 = r9 + 1
                goto L11
            L9e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javafx.fxml.FXMLLoader.ControllerAccessor.addAccessibleFields(java.lang.Class, int):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:20:0x006e  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0086 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void addAccessibleMethods(java.lang.Class<?> r5, int r6) throws java.lang.SecurityException {
            /*
                r4 = this;
                r0 = r5
                int r0 = r0.getModifiers()
                boolean r0 = java.lang.reflect.Modifier.isPublic(r0)
                r7 = r0
                r0 = r5
                java.lang.reflect.Method[] r0 = r0.getDeclaredMethods()
                r8 = r0
                r0 = 0
                r9 = r0
            L11:
                r0 = r9
                r1 = r8
                int r1 = r1.length
                if (r0 >= r1) goto L8c
                r0 = r8
                r1 = r9
                r0 = r0[r1]
                r10 = r0
                r0 = r10
                int r0 = r0.getModifiers()
                r11 = r0
                r0 = r11
                r1 = 264(0x108, float:3.7E-43)
                r0 = r0 & r1
                if (r0 != 0) goto L86
                r0 = r11
                int r0 = getAccess(r0)
                r1 = r6
                r0 = r0 & r1
                if (r0 != 0) goto L3d
                goto L86
            L3d:
                r0 = r7
                if (r0 == 0) goto L49
                r0 = r11
                boolean r0 = java.lang.reflect.Modifier.isPublic(r0)
                if (r0 != 0) goto L5c
            L49:
                r0 = r10
                java.lang.Class<javafx.fxml.FXML> r1 = javafx.fxml.FXML.class
                java.lang.annotation.Annotation r0 = r0.getAnnotation(r1)
                if (r0 != 0) goto L56
                goto L86
            L56:
                r0 = r10
                r1 = 1
                r0.setAccessible(r1)
            L5c:
                r0 = r10
                java.lang.String r0 = r0.getName()
                r12 = r0
                r0 = r10
                javafx.fxml.FXMLLoader$SupportedType r0 = javafx.fxml.FXMLLoader.access$4100(r0)
                r1 = r0
                r13 = r1
                if (r0 == 0) goto L86
                r0 = r4
                java.util.Map<javafx.fxml.FXMLLoader$SupportedType, java.util.Map<java.lang.String, java.lang.reflect.Method>> r0 = r0.controllerMethods
                r1 = r13
                java.lang.Object r0 = r0.get(r1)
                java.util.Map r0 = (java.util.Map) r0
                r1 = r12
                r2 = r10
                java.lang.Object r0 = r0.put(r1, r2)
            L86:
                int r9 = r9 + 1
                goto L11
            L8c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javafx.fxml.FXMLLoader.ControllerAccessor.addAccessibleMethods(java.lang.Class, int):void");
        }

        private static int getAccess(int fullModifiers) {
            int untransformedAccess = fullModifiers & 7;
            switch (untransformedAccess) {
                case 1:
                    return 1;
                case 2:
                    return 8;
                case 3:
                default:
                    return 4;
                case 4:
                    return 2;
            }
        }
    }
}
