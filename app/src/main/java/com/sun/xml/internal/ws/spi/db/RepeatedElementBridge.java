package com.sun.xml.internal.ws.spi.db;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/RepeatedElementBridge.class */
public class RepeatedElementBridge<T> implements XMLBridge<T> {
    XMLBridge<T> delegate;
    CollectionHandler collectionHandler;
    static final CollectionHandler ListHandler = new BaseCollectionHandler(List.class) { // from class: com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.1
        @Override // com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.BaseCollectionHandler, com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.CollectionHandler
        public Object convert(List list) {
            return list;
        }
    };
    static final CollectionHandler HashSetHandler = new BaseCollectionHandler(HashSet.class) { // from class: com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.2
        @Override // com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.BaseCollectionHandler, com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.CollectionHandler
        public Object convert(List list) {
            return new HashSet(list);
        }
    };

    /* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/RepeatedElementBridge$CollectionHandler.class */
    public interface CollectionHandler {
        int getSize(Object obj);

        Iterator iterator(Object obj);

        Object convert(List list);
    }

    public RepeatedElementBridge(TypeInfo typeInfo, XMLBridge xb) {
        this.delegate = xb;
        this.collectionHandler = create(typeInfo);
    }

    public CollectionHandler collectionHandler() {
        return this.collectionHandler;
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public BindingContext context() {
        return this.delegate.context();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, XMLStreamWriter output, AttachmentMarshaller am2) throws JAXBException {
        this.delegate.marshal((XMLBridge<T>) object, output, am2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, OutputStream output, NamespaceContext nsContext, AttachmentMarshaller am2) throws JAXBException {
        this.delegate.marshal(object, output, nsContext, am2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, Node output) throws JAXBException {
        this.delegate.marshal((XMLBridge<T>) object, output);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, ContentHandler contentHandler, AttachmentMarshaller am2) throws JAXBException {
        this.delegate.marshal((XMLBridge<T>) object, contentHandler, am2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public void marshal(T object, Result result) throws JAXBException {
        this.delegate.marshal((XMLBridge<T>) object, result);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public T unmarshal(XMLStreamReader in, AttachmentUnmarshaller au2) throws JAXBException {
        return this.delegate.unmarshal(in, au2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public T unmarshal(Source in, AttachmentUnmarshaller au2) throws JAXBException {
        return this.delegate.unmarshal(in, au2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public T unmarshal(InputStream in) throws JAXBException {
        return this.delegate.unmarshal(in);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public T unmarshal(Node n2, AttachmentUnmarshaller au2) throws JAXBException {
        return this.delegate.unmarshal(n2, au2);
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public TypeInfo getTypeInfo() {
        return this.delegate.getTypeInfo();
    }

    @Override // com.sun.xml.internal.ws.spi.db.XMLBridge
    public boolean supportOutputStream() {
        return this.delegate.supportOutputStream();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/RepeatedElementBridge$BaseCollectionHandler.class */
    static class BaseCollectionHandler implements CollectionHandler {
        Class type;

        BaseCollectionHandler(Class c2) {
            this.type = c2;
        }

        @Override // com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.CollectionHandler
        public int getSize(Object c2) {
            return ((Collection) c2).size();
        }

        @Override // com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.CollectionHandler
        public Object convert(List list) {
            try {
                Object o2 = this.type.newInstance();
                ((Collection) o2).addAll(list);
                return o2;
            } catch (Exception e2) {
                e2.printStackTrace();
                return list;
            }
        }

        @Override // com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.CollectionHandler
        public Iterator iterator(Object c2) {
            return ((Collection) c2).iterator();
        }
    }

    public static CollectionHandler create(TypeInfo ti) {
        Class javaClass = (Class) ti.type;
        if (javaClass.isArray()) {
            return new ArrayHandler((Class) ti.getItemType().type);
        }
        if (List.class.equals(javaClass) || Collection.class.equals(javaClass)) {
            return ListHandler;
        }
        if (Set.class.equals(javaClass) || HashSet.class.equals(javaClass)) {
            return HashSetHandler;
        }
        return new BaseCollectionHandler(javaClass);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/RepeatedElementBridge$ArrayHandler.class */
    static class ArrayHandler implements CollectionHandler {
        Class componentClass;

        public ArrayHandler(Class component) {
            this.componentClass = component;
        }

        @Override // com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.CollectionHandler
        public int getSize(Object c2) {
            return Array.getLength(c2);
        }

        @Override // com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.CollectionHandler
        public Object convert(List list) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
            Object array = Array.newInstance((Class<?>) this.componentClass, list.size());
            for (int i2 = 0; i2 < list.size(); i2++) {
                Array.set(array, i2, list.get(i2));
            }
            return array;
        }

        @Override // com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.CollectionHandler
        public Iterator iterator(final Object c2) {
            return new Iterator() { // from class: com.sun.xml.internal.ws.spi.db.RepeatedElementBridge.ArrayHandler.1
                int index = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return (c2 == null || Array.getLength(c2) == 0 || this.index == Array.getLength(c2)) ? false : true;
                }

                @Override // java.util.Iterator
                public Object next() throws IllegalArgumentException, NoSuchElementException {
                    try {
                        Object obj = c2;
                        int i2 = this.index;
                        this.index = i2 + 1;
                        Object retVal = Array.get(obj, i2);
                        return retVal;
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        throw new NoSuchElementException();
                    }
                }

                @Override // java.util.Iterator
                public void remove() {
                }
            };
        }
    }
}
