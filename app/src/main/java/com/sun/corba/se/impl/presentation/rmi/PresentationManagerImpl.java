package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.sun.corba.se.impl.orbutil.graph.GraphImpl;
import com.sun.corba.se.impl.orbutil.graph.Node;
import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory;
import com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller;
import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.rmi.CORBA.Tie;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/PresentationManagerImpl.class */
public final class PresentationManagerImpl implements PresentationManager {
    private Map classToClassData = new HashMap();
    private Map methodToDMM = new HashMap();
    private PresentationManager.StubFactoryFactory staticStubFactoryFactory;
    private PresentationManager.StubFactoryFactory dynamicStubFactoryFactory;
    private ORBUtilSystemException wrapper;
    private boolean useDynamicStubs;

    public PresentationManagerImpl(boolean z2) {
        this.wrapper = null;
        this.useDynamicStubs = z2;
        this.wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PRESENTATION);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager
    public synchronized DynamicMethodMarshaller getDynamicMethodMarshaller(Method method) {
        if (method == null) {
            return null;
        }
        DynamicMethodMarshaller dynamicMethodMarshallerImpl = (DynamicMethodMarshaller) this.methodToDMM.get(method);
        if (dynamicMethodMarshallerImpl == null) {
            dynamicMethodMarshallerImpl = new DynamicMethodMarshallerImpl(method);
            this.methodToDMM.put(method, dynamicMethodMarshallerImpl);
        }
        return dynamicMethodMarshallerImpl;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager
    public synchronized PresentationManager.ClassData getClassData(Class cls) {
        PresentationManager.ClassData classDataImpl = (PresentationManager.ClassData) this.classToClassData.get(cls);
        if (classDataImpl == null) {
            classDataImpl = new ClassDataImpl(cls);
            this.classToClassData.put(cls, classDataImpl);
        }
        return classDataImpl;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/PresentationManagerImpl$ClassDataImpl.class */
    private class ClassDataImpl implements PresentationManager.ClassData {
        private Class cls;
        private IDLNameTranslator nameTranslator;
        private String[] typeIds;
        private PresentationManager.StubFactory sfactory;
        private InvocationHandlerFactory ihfactory;
        private Map dictionary;

        public ClassDataImpl(Class cls) {
            this.cls = cls;
            GraphImpl graphImpl = new GraphImpl();
            NodeImpl nodeImpl = new NodeImpl(cls);
            Set rootSet = PresentationManagerImpl.this.getRootSet(cls, nodeImpl, graphImpl);
            this.nameTranslator = IDLNameTranslatorImpl.get(PresentationManagerImpl.this.getInterfaces(rootSet));
            this.typeIds = PresentationManagerImpl.this.makeTypeIds(nodeImpl, graphImpl, rootSet);
            this.ihfactory = new InvocationHandlerFactoryImpl(PresentationManagerImpl.this, this);
            this.dictionary = new HashMap();
        }

        @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData
        public Class getMyClass() {
            return this.cls;
        }

        @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData
        public IDLNameTranslator getIDLNameTranslator() {
            return this.nameTranslator;
        }

        @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData
        public String[] getTypeIds() {
            return this.typeIds;
        }

        @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData
        public InvocationHandlerFactory getInvocationHandlerFactory() {
            return this.ihfactory;
        }

        @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData
        public Map getDictionary() {
            return this.dictionary;
        }
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager
    public PresentationManager.StubFactoryFactory getStubFactoryFactory(boolean z2) {
        if (z2) {
            return this.dynamicStubFactoryFactory;
        }
        return this.staticStubFactoryFactory;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager
    public void setStubFactoryFactory(boolean z2, PresentationManager.StubFactoryFactory stubFactoryFactory) {
        if (z2) {
            this.dynamicStubFactoryFactory = stubFactoryFactory;
        } else {
            this.staticStubFactoryFactory = stubFactoryFactory;
        }
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager
    public Tie getTie() {
        return this.dynamicStubFactoryFactory.getTie(null);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager
    public boolean useDynamicStubs() {
        return this.useDynamicStubs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Set getRootSet(Class cls, NodeImpl nodeImpl, Graph graph) {
        Set roots;
        if (cls.isInterface()) {
            graph.add(nodeImpl);
            roots = graph.getRoots();
        } else {
            HashSet hashSet = new HashSet();
            for (Class superclass = cls; superclass != null && !superclass.equals(Object.class); superclass = superclass.getSuperclass()) {
                NodeImpl nodeImpl2 = new NodeImpl(superclass);
                graph.add(nodeImpl2);
                hashSet.add(nodeImpl2);
            }
            graph.getRoots();
            graph.removeAll(hashSet);
            roots = graph.getRoots();
        }
        return roots;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class[] getInterfaces(Set set) {
        Class[] clsArr = new Class[set.size()];
        Iterator it = set.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            clsArr[i3] = ((NodeImpl) it.next()).getInterface();
        }
        return clsArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] makeTypeIds(NodeImpl nodeImpl, Graph graph, Set set) {
        HashSet hashSet = new HashSet(graph);
        hashSet.removeAll(set);
        ArrayList arrayList = new ArrayList();
        if (set.size() > 1) {
            arrayList.add(nodeImpl.getTypeId());
        }
        addNodes(arrayList, set);
        addNodes(arrayList, hashSet);
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private void addNodes(List list, Set set) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            list.add(((NodeImpl) it.next()).getTypeId());
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/PresentationManagerImpl$NodeImpl.class */
    private static class NodeImpl implements Node {
        private Class interf;

        public Class getInterface() {
            return this.interf;
        }

        public NodeImpl(Class cls) {
            this.interf = cls;
        }

        public String getTypeId() {
            return "RMI:" + this.interf.getName() + RepositoryId.kPrimitiveSequenceValueHash;
        }

        @Override // com.sun.corba.se.impl.orbutil.graph.Node
        public Set getChildren() {
            HashSet hashSet = new HashSet();
            for (Class<?> cls : this.interf.getInterfaces()) {
                if (Remote.class.isAssignableFrom(cls) && !Remote.class.equals(cls)) {
                    hashSet.add(new NodeImpl(cls));
                }
            }
            return hashSet;
        }

        public String toString() {
            return "NodeImpl[" + ((Object) this.interf) + "]";
        }

        public int hashCode() {
            return this.interf.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof NodeImpl)) {
                return false;
            }
            return ((NodeImpl) obj).interf.equals(this.interf);
        }
    }
}
