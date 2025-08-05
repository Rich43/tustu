package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.transform.Result;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/DomLoader.class */
public class DomLoader<ResultT extends Result> extends Loader {
    private final DomHandler<?, ResultT> dom;

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/DomLoader$State.class */
    private final class State {
        private TransformerHandler handler;
        private final ResultT result;
        int depth = 1;

        public State(UnmarshallingContext unmarshallingContext) throws SAXException, IllegalArgumentException {
            this.handler = null;
            this.handler = JAXBContextImpl.createTransformerHandler(unmarshallingContext.getJAXBContext().disableSecurityProcessing);
            this.result = (ResultT) DomLoader.this.dom.createUnmarshaller(unmarshallingContext);
            this.handler.setResult(this.result);
            try {
                this.handler.setDocumentLocator(unmarshallingContext.getLocator());
                this.handler.startDocument();
                declarePrefixes(unmarshallingContext, unmarshallingContext.getAllDeclaredPrefixes());
            } catch (SAXException e2) {
                unmarshallingContext.handleError(e2);
                throw e2;
            }
        }

        public Object getElement() {
            return DomLoader.this.dom.getElement(this.result);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void declarePrefixes(UnmarshallingContext context, String[] prefixes) throws SAXException {
            for (int i2 = prefixes.length - 1; i2 >= 0; i2--) {
                String nsUri = context.getNamespaceURI(prefixes[i2]);
                if (nsUri == null) {
                    throw new IllegalStateException("prefix '" + prefixes[i2] + "' isn't bound");
                }
                this.handler.startPrefixMapping(prefixes[i2], nsUri);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void undeclarePrefixes(String[] prefixes) throws SAXException {
            for (int i2 = prefixes.length - 1; i2 >= 0; i2--) {
                this.handler.endPrefixMapping(prefixes[i2]);
            }
        }
    }

    public DomLoader(DomHandler<?, ResultT> dom) {
        super(true);
        this.dom = dom;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        UnmarshallingContext context = state.getContext();
        if (state.getTarget() == null) {
            state.setTarget(new State(context));
        }
        DomLoader<ResultT>.State s2 = (State) state.getTarget();
        try {
            s2.declarePrefixes(context, context.getNewlyDeclaredPrefixes());
            ((State) s2).handler.startElement(ea.uri, ea.local, ea.getQname(), ea.atts);
        } catch (SAXException e2) {
            context.handleError(e2);
            throw e2;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        state.setLoader(this);
        DomLoader<ResultT>.State s2 = (State) state.getPrev().getTarget();
        s2.depth++;
        state.setTarget(s2);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
        if (text.length() == 0) {
            return;
        }
        try {
            DomLoader<ResultT>.State s2 = (State) state.getTarget();
            ((State) s2).handler.characters(text.toString().toCharArray(), 0, text.length());
        } catch (SAXException e2) {
            state.getContext().handleError(e2);
            throw e2;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        DomLoader<ResultT>.State s2 = (State) state.getTarget();
        UnmarshallingContext context = state.getContext();
        try {
            ((State) s2).handler.endElement(ea.uri, ea.local, ea.getQname());
            s2.undeclarePrefixes(context.getNewlyDeclaredPrefixes());
            int i2 = s2.depth - 1;
            s2.depth = i2;
            if (i2 == 0) {
                try {
                    s2.undeclarePrefixes(context.getAllDeclaredPrefixes());
                    ((State) s2).handler.endDocument();
                    state.setTarget(s2.getElement());
                } catch (SAXException e2) {
                    context.handleError(e2);
                    throw e2;
                }
            }
        } catch (SAXException e3) {
            context.handleError(e3);
            throw e3;
        }
    }
}
