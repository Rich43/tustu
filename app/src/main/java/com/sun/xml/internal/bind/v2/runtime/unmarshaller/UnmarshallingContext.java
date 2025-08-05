package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.istack.internal.SAXParseException2;
import com.sun.xml.internal.bind.IDResolver;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.api.ClassResolver;
import com.sun.xml.internal.bind.unmarshaller.InfosetScanner;
import com.sun.xml.internal.bind.v2.ClassFactory;
import com.sun.xml.internal.bind.v2.runtime.AssociationMap;
import com.sun.xml.internal.bind.v2.runtime.Coordinator;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/UnmarshallingContext.class */
public final class UnmarshallingContext extends Coordinator implements NamespaceContext, ValidationEventHandler, ErrorHandler, XmlVisitor, XmlVisitor.TextPredictor {
    private static final Logger logger;
    private final State root;
    private State current;
    private static final LocatorEx DUMMY_INSTANCE;
    private Object result;
    private JaxBeanInfo expectedType;
    private IDResolver idResolver;
    public final UnmarshallerImpl parent;
    private final AssociationMap assoc;
    private boolean isInplaceMode;
    private InfosetScanner scanner;
    private Object currentElement;
    private NamespaceContext environmentNamespaceContext;

    @Nullable
    public ClassResolver classResolver;

    @Nullable
    public ClassLoader classLoader;
    private static volatile int errorsCounter;
    private static final Loader DEFAULT_ROOT_LOADER;
    private static final Loader EXPECTED_TYPE_ROOT_LOADER;
    static final /* synthetic */ boolean $assertionsDisabled;

    @NotNull
    private LocatorEx locator = DUMMY_INSTANCE;
    private boolean isUnmarshalInProgress = true;
    private boolean aborted = false;
    private final Map<Class, Factory> factories = new HashMap();
    private Patcher[] patchers = null;
    private int patchersLen = 0;
    private String[] nsBind = new String[16];
    private int nsLen = 0;
    private Scope[] scopes = new Scope[16];
    private int scopeTop = 0;

    static {
        $assertionsDisabled = !UnmarshallingContext.class.desiredAssertionStatus();
        logger = Logger.getLogger(UnmarshallingContext.class.getName());
        LocatorImpl loc = new LocatorImpl();
        loc.setPublicId(null);
        loc.setSystemId(null);
        loc.setLineNumber(-1);
        loc.setColumnNumber(-1);
        DUMMY_INSTANCE = new LocatorExWrapper(loc);
        errorsCounter = 10;
        DEFAULT_ROOT_LOADER = new DefaultRootLoader();
        EXPECTED_TYPE_ROOT_LOADER = new ExpectedTypeRootLoader();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/UnmarshallingContext$State.class */
    public final class State {
        private Loader loader;
        private Receiver receiver;
        private Intercepter intercepter;
        private Object target;
        private Object backup;
        private int numNsDecl;
        private String elementDefaultValue;
        private State prev;
        private State next;
        private boolean nil;
        private boolean mixed;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !UnmarshallingContext.class.desiredAssertionStatus();
        }

        public UnmarshallingContext getContext() {
            return UnmarshallingContext.this;
        }

        private State(State prev) {
            this.nil = false;
            this.mixed = false;
            this.prev = prev;
            if (prev != null) {
                prev.next = this;
                if (prev.mixed) {
                    this.mixed = true;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void push() {
            if (UnmarshallingContext.logger.isLoggable(Level.FINEST)) {
                UnmarshallingContext.logger.log(Level.FINEST, "State.push");
            }
            if (this.next == null) {
                if (!$assertionsDisabled && UnmarshallingContext.this.current != this) {
                    throw new AssertionError();
                }
                this.next = UnmarshallingContext.this.new State(this);
            }
            this.nil = false;
            State n2 = this.next;
            n2.numNsDecl = UnmarshallingContext.this.nsLen;
            UnmarshallingContext.this.current = n2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void pop() {
            if (UnmarshallingContext.logger.isLoggable(Level.FINEST)) {
                UnmarshallingContext.logger.log(Level.FINEST, "State.pop");
            }
            if (!$assertionsDisabled && this.prev == null) {
                throw new AssertionError();
            }
            this.loader = null;
            this.nil = false;
            this.mixed = false;
            this.receiver = null;
            this.intercepter = null;
            this.elementDefaultValue = null;
            this.target = null;
            UnmarshallingContext.this.current = this.prev;
            this.next = null;
        }

        public boolean isMixed() {
            return this.mixed;
        }

        public Object getTarget() {
            return this.target;
        }

        public void setLoader(Loader loader) {
            if (loader instanceof StructureLoader) {
                this.mixed = !((StructureLoader) loader).getBeanInfo().hasElementOnlyContentModel();
            }
            this.loader = loader;
        }

        public void setReceiver(Receiver receiver) {
            this.receiver = receiver;
        }

        public State getPrev() {
            return this.prev;
        }

        public void setIntercepter(Intercepter intercepter) {
            this.intercepter = intercepter;
        }

        public void setBackup(Object backup) {
            this.backup = backup;
        }

        public void setTarget(Object target) {
            this.target = target;
        }

        public Object getBackup() {
            return this.backup;
        }

        public boolean isNil() {
            return this.nil;
        }

        public void setNil(boolean nil) {
            this.nil = nil;
        }

        public Loader getLoader() {
            return this.loader;
        }

        public String getElementDefaultValue() {
            return this.elementDefaultValue;
        }

        public void setElementDefaultValue(String elementDefaultValue) {
            this.elementDefaultValue = elementDefaultValue;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/UnmarshallingContext$Factory.class */
    private static class Factory {
        private final Object factorInstance;
        private final Method method;

        public Factory(Object factorInstance, Method method) {
            this.factorInstance = factorInstance;
            this.method = method;
        }

        public Object createInstance() throws SAXException {
            try {
                return this.method.invoke(this.factorInstance, new Object[0]);
            } catch (IllegalAccessException e2) {
                UnmarshallingContext.getInstance().handleError(e2, false);
                return null;
            } catch (InvocationTargetException e3) {
                UnmarshallingContext.getInstance().handleError(e3, false);
                return null;
            }
        }
    }

    public UnmarshallingContext(UnmarshallerImpl _parent, AssociationMap assoc) {
        for (int i2 = 0; i2 < this.scopes.length; i2++) {
            this.scopes[i2] = new Scope(this);
        }
        this.parent = _parent;
        this.assoc = assoc;
        State state = new State(null);
        this.current = state;
        this.root = state;
    }

    public void reset(InfosetScanner scanner, boolean isInplaceMode, JaxBeanInfo expectedType, IDResolver idResolver) {
        this.scanner = scanner;
        this.isInplaceMode = isInplaceMode;
        this.expectedType = expectedType;
        this.idResolver = idResolver;
    }

    public JAXBContextImpl getJAXBContext() {
        return this.parent.context;
    }

    public State getCurrentState() {
        return this.current;
    }

    public Loader selectRootLoader(State state, TagName tag) throws SAXException {
        Class<?> clazz;
        try {
            Loader l2 = getJAXBContext().selectRootLoader(state, tag);
            if (l2 != null) {
                return l2;
            }
            if (this.classResolver != null && (clazz = this.classResolver.resolveElementName(tag.uri, tag.local)) != null) {
                JAXBContextImpl enhanced = getJAXBContext().createAugmented(clazz);
                JaxBeanInfo<?> bi2 = enhanced.getBeanInfo((Class) clazz);
                return bi2.getLoader(enhanced, true);
            }
            return null;
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Exception e3) {
            handleError(e3);
            return null;
        }
    }

    public void clearStates() {
        State last;
        State state = this.current;
        while (true) {
            last = state;
            if (last.next == null) {
                break;
            } else {
                state = last.next;
            }
        }
        while (last.prev != null) {
            last.loader = null;
            last.nil = false;
            last.receiver = null;
            last.intercepter = null;
            last.elementDefaultValue = null;
            last.target = null;
            last = last.prev;
            last.next.prev = null;
            last.next = null;
        }
        this.current = last;
    }

    public void setFactories(Object factoryInstances) {
        this.factories.clear();
        if (factoryInstances == null) {
            return;
        }
        if (factoryInstances instanceof Object[]) {
            for (Object factory : (Object[]) factoryInstances) {
                addFactory(factory);
            }
            return;
        }
        addFactory(factoryInstances);
    }

    private void addFactory(Object factory) throws SecurityException {
        for (Method m2 : factory.getClass().getMethods()) {
            if (m2.getName().startsWith("create") && m2.getParameterTypes().length <= 0) {
                Class type = m2.getReturnType();
                this.factories.put(type, new Factory(factory, m2));
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException {
        if (locator != null) {
            this.locator = locator;
        }
        this.environmentNamespaceContext = nsContext;
        this.result = null;
        this.current = this.root;
        this.patchersLen = 0;
        this.aborted = false;
        this.isUnmarshalInProgress = true;
        this.nsLen = 0;
        if (this.expectedType != null) {
            this.root.loader = EXPECTED_TYPE_ROOT_LOADER;
        } else {
            this.root.loader = DEFAULT_ROOT_LOADER;
        }
        this.idResolver.startDocument(this);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startElement(TagName tagName) throws SAXException {
        pushCoordinator();
        try {
            _startElement(tagName);
        } finally {
            popCoordinator();
        }
    }

    private void _startElement(TagName tagName) throws SAXException {
        if (this.assoc != null) {
            this.currentElement = this.scanner.getCurrentElement();
        }
        Loader h2 = this.current.loader;
        this.current.push();
        h2.childElement(this.current, tagName);
        if (!$assertionsDisabled && this.current.loader == null) {
            throw new AssertionError();
        }
        this.current.loader.startElement(this.current, tagName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void text(CharSequence pcdata) throws SAXException {
        pushCoordinator();
        try {
            if (this.current.elementDefaultValue != null && pcdata.length() == 0) {
                pcdata = this.current.elementDefaultValue;
            }
            this.current.loader.text(this.current, pcdata);
        } finally {
            popCoordinator();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public final void endElement(TagName tagName) throws SAXException {
        pushCoordinator();
        try {
            State child = this.current;
            child.loader.leaveElement(child, tagName);
            Object target = child.target;
            Receiver recv = child.receiver;
            Intercepter intercepter = child.intercepter;
            child.pop();
            if (intercepter != null) {
                target = intercepter.intercept(this.current, target);
            }
            if (recv != null) {
                recv.receive(this.current, target);
            }
        } finally {
            popCoordinator();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endDocument() throws SAXException {
        runPatchers();
        this.idResolver.endDocument();
        this.isUnmarshalInProgress = false;
        this.currentElement = null;
        this.locator = DUMMY_INSTANCE;
        this.environmentNamespaceContext = null;
        if (!$assertionsDisabled && this.root != this.current) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor.TextPredictor
    @Deprecated
    public boolean expectText() {
        return this.current.loader.expectText;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    @Deprecated
    public XmlVisitor.TextPredictor getPredictor() {
        return this;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public UnmarshallingContext getContext() {
        return this;
    }

    public Object getResult() throws UnmarshalException {
        if (this.isUnmarshalInProgress) {
            throw new IllegalStateException();
        }
        if (this.aborted) {
            throw new UnmarshalException((String) null);
        }
        return this.result;
    }

    void clearResult() {
        if (this.isUnmarshalInProgress) {
            throw new IllegalStateException();
        }
        this.result = null;
    }

    public Object createInstance(Class<?> clazz) throws SAXException {
        Factory factory;
        if (!this.factories.isEmpty() && (factory = this.factories.get(clazz)) != null) {
            return factory.createInstance();
        }
        return ClassFactory.create(clazz);
    }

    public Object createInstance(JaxBeanInfo beanInfo) throws SAXException {
        Factory factory;
        if (!this.factories.isEmpty() && (factory = this.factories.get(beanInfo.jaxbType)) != null) {
            return factory.createInstance();
        }
        try {
            return beanInfo.createInstance(this);
        } catch (IllegalAccessException e2) {
            Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e2, false);
            return null;
        } catch (InstantiationException e3) {
            Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e3, false);
            return null;
        } catch (InvocationTargetException e4) {
            Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e4, false);
            return null;
        }
    }

    public void handleEvent(ValidationEvent event, boolean canRecover) throws SAXException {
        ValidationEventHandler eventHandler = this.parent.getEventHandler();
        boolean recover = eventHandler.handleEvent(event);
        if (!recover) {
            this.aborted = true;
        }
        if (!canRecover || !recover) {
            throw new SAXParseException2(event.getMessage(), this.locator, new UnmarshalException(event.getMessage(), event.getLinkedException()));
        }
    }

    @Override // javax.xml.bind.ValidationEventHandler
    public boolean handleEvent(ValidationEvent event) {
        try {
            boolean recover = this.parent.getEventHandler().handleEvent(event);
            if (!recover) {
                this.aborted = true;
            }
            return recover;
        } catch (RuntimeException e2) {
            return false;
        }
    }

    public void handleError(Exception e2) throws SAXException {
        handleError(e2, true);
    }

    public void handleError(Exception e2, boolean canRecover) throws SAXException {
        handleEvent(new ValidationEventImpl(1, e2.getMessage(), this.locator.getLocation(), e2), canRecover);
    }

    public void handleError(String msg) {
        handleEvent(new ValidationEventImpl(1, msg, this.locator.getLocation()));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Coordinator
    protected ValidationEventLocator getLocation() {
        return this.locator.getLocation();
    }

    public LocatorEx getLocator() {
        return this.locator;
    }

    public void errorUnresolvedIDREF(Object bean, String idref, LocatorEx loc) throws SAXException {
        handleEvent(new ValidationEventImpl(1, Messages.UNRESOLVED_IDREF.format(idref), loc.getLocation()), true);
    }

    public void addPatcher(Patcher job) {
        if (this.patchers == null) {
            this.patchers = new Patcher[32];
        }
        if (this.patchers.length == this.patchersLen) {
            Patcher[] buf = new Patcher[this.patchersLen * 2];
            System.arraycopy(this.patchers, 0, buf, 0, this.patchersLen);
            this.patchers = buf;
        }
        Patcher[] patcherArr = this.patchers;
        int i2 = this.patchersLen;
        this.patchersLen = i2 + 1;
        patcherArr[i2] = job;
    }

    private void runPatchers() throws SAXException {
        if (this.patchers != null) {
            for (int i2 = 0; i2 < this.patchersLen; i2++) {
                this.patchers[i2].run();
                this.patchers[i2] = null;
            }
        }
    }

    public String addToIdTable(String id) throws SAXException {
        Object o2 = this.current.target;
        if (o2 == null) {
            o2 = this.current.prev.target;
        }
        this.idResolver.bind(id, o2);
        return id;
    }

    public Callable getObjectFromId(String id, Class targetType) throws SAXException {
        return this.idResolver.resolve(id, targetType);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startPrefixMapping(String prefix, String uri) {
        if (this.nsBind.length == this.nsLen) {
            String[] n2 = new String[this.nsLen * 2];
            System.arraycopy(this.nsBind, 0, n2, 0, this.nsLen);
            this.nsBind = n2;
        }
        String[] strArr = this.nsBind;
        int i2 = this.nsLen;
        this.nsLen = i2 + 1;
        strArr[i2] = prefix;
        String[] strArr2 = this.nsBind;
        int i3 = this.nsLen;
        this.nsLen = i3 + 1;
        strArr2[i3] = uri;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endPrefixMapping(String prefix) {
        this.nsLen -= 2;
    }

    private String resolveNamespacePrefix(String prefix) {
        if (prefix.equals("xml")) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        for (int i2 = this.nsLen - 2; i2 >= 0; i2 -= 2) {
            if (prefix.equals(this.nsBind[i2])) {
                return this.nsBind[i2 + 1];
            }
        }
        if (this.environmentNamespaceContext != null) {
            return this.environmentNamespaceContext.getNamespaceURI(prefix.intern());
        }
        if (prefix.equals("")) {
            return "";
        }
        return null;
    }

    public String[] getNewlyDeclaredPrefixes() {
        return getPrefixList(this.current.prev.numNsDecl);
    }

    public String[] getAllDeclaredPrefixes() {
        return getPrefixList(0);
    }

    private String[] getPrefixList(int startIndex) {
        int size = (this.current.numNsDecl - startIndex) / 2;
        String[] r2 = new String[size];
        for (int i2 = 0; i2 < r2.length; i2++) {
            r2[i2] = this.nsBind[startIndex + (i2 * 2)];
        }
        return r2;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator<String> getPrefixes(String uri) {
        return Collections.unmodifiableList(getAllPrefixesInList(uri)).iterator();
    }

    private List<String> getAllPrefixesInList(String uri) {
        List<String> a2 = new ArrayList<>();
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        if (uri.equals("http://www.w3.org/XML/1998/namespace")) {
            a2.add("xml");
            return a2;
        }
        if (uri.equals("http://www.w3.org/2000/xmlns/")) {
            a2.add("xmlns");
            return a2;
        }
        for (int i2 = this.nsLen - 2; i2 >= 0; i2 -= 2) {
            if (uri.equals(this.nsBind[i2 + 1]) && getNamespaceURI(this.nsBind[i2]).equals(this.nsBind[i2 + 1])) {
                a2.add(this.nsBind[i2]);
            }
        }
        return a2;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String uri) {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        if (uri.equals("http://www.w3.org/XML/1998/namespace")) {
            return "xml";
        }
        if (uri.equals("http://www.w3.org/2000/xmlns/")) {
            return "xmlns";
        }
        for (int i2 = this.nsLen - 2; i2 >= 0; i2 -= 2) {
            if (uri.equals(this.nsBind[i2 + 1]) && getNamespaceURI(this.nsBind[i2]).equals(this.nsBind[i2 + 1])) {
                return this.nsBind[i2];
            }
        }
        if (this.environmentNamespaceContext != null) {
            return this.environmentNamespaceContext.getPrefix(uri);
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        if (prefix.equals("xmlns")) {
            return "http://www.w3.org/2000/xmlns/";
        }
        return resolveNamespacePrefix(prefix);
    }

    public void startScope(int frameSize) {
        this.scopeTop += frameSize;
        if (this.scopeTop >= this.scopes.length) {
            Scope[] s2 = new Scope[Math.max(this.scopeTop + 1, this.scopes.length * 2)];
            System.arraycopy(this.scopes, 0, s2, 0, this.scopes.length);
            for (int i2 = this.scopes.length; i2 < s2.length; i2++) {
                s2[i2] = new Scope(this);
            }
            this.scopes = s2;
        }
    }

    public void endScope(int frameSize) throws SAXException {
        while (frameSize > 0) {
            try {
                this.scopes[this.scopeTop].finish();
                frameSize--;
                this.scopeTop--;
            } catch (AccessorException e2) {
                handleError(e2);
                while (frameSize > 0) {
                    Scope[] scopeArr = this.scopes;
                    int i2 = this.scopeTop;
                    this.scopeTop = i2 - 1;
                    scopeArr[i2] = new Scope(this);
                    frameSize--;
                }
                return;
            }
        }
    }

    public Scope getScope(int offset) {
        return this.scopes[this.scopeTop - offset];
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/UnmarshallingContext$DefaultRootLoader.class */
    private static final class DefaultRootLoader extends Loader implements Receiver {
        private DefaultRootLoader() {
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void childElement(State state, TagName ea) throws SAXException {
            Loader loader = state.getContext().selectRootLoader(state, ea);
            if (loader != null) {
                state.loader = loader;
                state.receiver = this;
                return;
            }
            JaxBeanInfo beanInfo = XsiTypeLoader.parseXsiType(state, ea, null);
            if (beanInfo != null) {
                state.loader = beanInfo.getLoader(null, false);
                state.prev.backup = new JAXBElement(ea.createQName(), Object.class, null);
                state.receiver = this;
                return;
            }
            reportUnexpectedChildElement(ea, false);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public Collection<QName> getExpectedChildElements() {
            return UnmarshallingContext.getInstance().getJAXBContext().getValidRootNames();
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver
        public void receive(State state, Object o2) {
            if (state.backup != null) {
                ((JAXBElement) state.backup).setValue(o2);
                o2 = state.backup;
            }
            if (state.nil) {
                ((JAXBElement) o2).setNil(true);
            }
            state.getContext().result = o2;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/UnmarshallingContext$ExpectedTypeRootLoader.class */
    private static final class ExpectedTypeRootLoader extends Loader implements Receiver {
        private ExpectedTypeRootLoader() {
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void childElement(State state, TagName ea) {
            UnmarshallingContext context = state.getContext();
            QName qn = new QName(ea.uri, ea.local);
            state.prev.target = new JAXBElement(qn, context.expectedType.jaxbType, null, null);
            state.receiver = this;
            state.loader = new XsiNilLoader(context.expectedType.getLoader(null, true));
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver
        public void receive(State state, Object o2) {
            JAXBElement e2 = (JAXBElement) state.target;
            e2.setValue(o2);
            state.getContext().recordOuterPeer(e2);
            state.getContext().result = e2;
        }
    }

    public void recordInnerPeer(Object innerPeer) {
        if (this.assoc != null) {
            this.assoc.addInner(this.currentElement, innerPeer);
        }
    }

    public Object getInnerPeer() {
        if (this.assoc != null && this.isInplaceMode) {
            return this.assoc.getInnerPeer(this.currentElement);
        }
        return null;
    }

    public void recordOuterPeer(Object outerPeer) {
        if (this.assoc != null) {
            this.assoc.addOuter(this.currentElement, outerPeer);
        }
    }

    public Object getOuterPeer() {
        if (this.assoc != null && this.isInplaceMode) {
            return this.assoc.getOuterPeer(this.currentElement);
        }
        return null;
    }

    public String getXMIMEContentType() {
        Object t2 = this.current.target;
        if (t2 == null) {
            return null;
        }
        return getJAXBContext().getXMIMEContentType(t2);
    }

    public static UnmarshallingContext getInstance() {
        return (UnmarshallingContext) Coordinator._getInstance();
    }

    public Collection<QName> getCurrentExpectedElements() {
        pushCoordinator();
        try {
            State s2 = getCurrentState();
            Loader l2 = s2.loader;
            return l2 != null ? l2.getExpectedChildElements() : null;
        } finally {
            popCoordinator();
        }
    }

    public Collection<QName> getCurrentExpectedAttributes() {
        pushCoordinator();
        try {
            State s2 = getCurrentState();
            Loader l2 = s2.loader;
            return l2 != null ? l2.getExpectedAttributes() : null;
        } finally {
            popCoordinator();
        }
    }

    public StructureLoader getStructureLoader() {
        if (this.current.loader instanceof StructureLoader) {
            return (StructureLoader) this.current.loader;
        }
        return null;
    }

    public boolean shouldErrorBeReported() throws SAXException {
        if (logger.isLoggable(Level.FINEST)) {
            return true;
        }
        if (errorsCounter >= 0) {
            errorsCounter--;
            if (errorsCounter == 0) {
                handleEvent(new ValidationEventImpl(0, Messages.ERRORS_LIMIT_EXCEEDED.format(new Object[0]), getLocator().getLocation(), null), true);
            }
        }
        return errorsCounter >= 0;
    }
}
