package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.WhiteSpaceProcessor;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/ListTransducedAccessorImpl.class */
public final class ListTransducedAccessorImpl<BeanT, ListT, ItemT, PackT> extends DefaultTransducedAccessor<BeanT> {
    private final Transducer<ItemT> xducer;
    private final Lister<BeanT, ListT, ItemT, PackT> lister;
    private final Accessor<BeanT, ListT> acc;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException, SAXException {
        return print((ListTransducedAccessorImpl<BeanT, ListT, ItemT, PackT>) obj);
    }

    public ListTransducedAccessorImpl(Transducer<ItemT> xducer, Accessor<BeanT, ListT> acc, Lister<BeanT, ListT, ItemT, PackT> lister) {
        this.xducer = xducer;
        this.lister = lister;
        this.acc = acc;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public boolean useNamespace() {
        return this.xducer.useNamespace();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public void declareNamespace(BeanT bean, XMLSerializer w2) throws AccessorException, SAXException {
        ListT list = this.acc.get(bean);
        if (list != null) {
            ListIterator<ItemT> itr = this.lister.iterator(list, w2);
            while (itr.hasNext()) {
                try {
                    ItemT item = itr.next();
                    if (item != null) {
                        this.xducer.declareNamespace(item, w2);
                    }
                } catch (JAXBException e2) {
                    w2.reportError(null, e2);
                }
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public String print(BeanT o2) throws AccessorException, SAXException {
        ListT list = this.acc.get(o2);
        if (list == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        XMLSerializer w2 = XMLSerializer.getInstance();
        ListIterator<ItemT> itr = this.lister.iterator(list, w2);
        while (itr.hasNext()) {
            try {
                ItemT item = itr.next();
                if (item != null) {
                    if (buf.length() > 0) {
                        buf.append(' ');
                    }
                    buf.append(this.xducer.print(item));
                }
            } catch (JAXBException e2) {
                w2.reportError(null, e2);
            }
        }
        return buf.toString();
    }

    private void processValue(BeanT beant, CharSequence charSequence) throws AccessorException, SAXException {
        PackT packtStartPacking = this.lister.startPacking(beant, this.acc);
        int i2 = 0;
        int length = charSequence.length();
        while (true) {
            int i3 = i2;
            while (i3 < length && !WhiteSpaceProcessor.isWhiteSpace(charSequence.charAt(i3))) {
                i3++;
            }
            CharSequence charSequenceSubSequence = charSequence.subSequence(i2, i3);
            if (!charSequenceSubSequence.equals("")) {
                this.lister.addToPack(packtStartPacking, this.xducer.parse(charSequenceSubSequence));
            }
            if (i3 == length) {
                break;
            }
            while (i3 < length && WhiteSpaceProcessor.isWhiteSpace(charSequence.charAt(i3))) {
                i3++;
            }
            if (i3 == length) {
                break;
            } else {
                i2 = i3;
            }
        }
        this.lister.endPacking(packtStartPacking, beant, this.acc);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public void parse(BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
        processValue(bean, lexical);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public boolean hasValue(BeanT bean) throws AccessorException {
        return this.acc.get(bean) != null;
    }
}
