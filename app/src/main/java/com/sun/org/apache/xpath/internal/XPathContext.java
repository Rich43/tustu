package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMFilter;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeIterator;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2RTFDTM;
import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import com.sun.org.apache.xml.internal.utils.IntStack;
import com.sun.org.apache.xml.internal.utils.NodeVector;
import com.sun.org.apache.xml.internal.utils.ObjectStack;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xml.internal.utils.QName;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.axes.OneStepIteratorForward;
import com.sun.org.apache.xpath.internal.axes.SubContextList;
import com.sun.org.apache.xpath.internal.objects.DTMXRTreeFrag;
import com.sun.org.apache.xpath.internal.objects.XMLStringFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/XPathContext.class */
public class XPathContext extends DTMManager {
    IntStack m_last_pushed_rtfdtm;
    private Vector m_rtfdtm_stack;
    private int m_which_rtfdtm;
    private SAX2RTFDTM m_global_rtfdtm;
    private HashMap m_DTMXRTreeFrags;
    private boolean m_isSecureProcessing;
    private boolean m_overrideDefaultParser;
    protected DTMManager m_dtmManager;
    ObjectStack m_saxLocations;
    private Object m_owner;
    private Method m_ownerGetErrorListener;
    private VariableStack m_variableStacks;
    private SourceTreeManager m_sourceTreeManager;
    private ErrorListener m_errorListener;
    private ErrorListener m_defaultErrorListener;
    private URIResolver m_uriResolver;
    public XMLReader m_primaryReader;
    private Stack m_contextNodeLists;
    public static final int RECURSIONLIMIT = 4096;
    private IntStack m_currentNodes;
    private NodeVector m_iteratorRoots;
    private NodeVector m_predicateRoots;
    private IntStack m_currentExpressionNodes;
    private IntStack m_predicatePos;
    private ObjectStack m_prefixResolvers;
    private Stack m_axesIteratorStack;
    XPathExpressionContext expressionContext;

    public DTMManager getDTMManager() {
        return this.m_dtmManager;
    }

    public void setSecureProcessing(boolean flag) {
        this.m_isSecureProcessing = flag;
    }

    public boolean isSecureProcessing() {
        return this.m_isSecureProcessing;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTM getDTM(Source source, boolean unique, DTMWSFilter wsfilter, boolean incremental, boolean doIndexing) {
        return this.m_dtmManager.getDTM(source, unique, wsfilter, incremental, doIndexing);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTM getDTM(int nodeHandle) {
        return this.m_dtmManager.getDTM(nodeHandle);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public int getDTMHandleFromNode(Node node) {
        return this.m_dtmManager.getDTMHandleFromNode(node);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public int getDTMIdentity(DTM dtm) {
        return this.m_dtmManager.getDTMIdentity(dtm);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTM createDocumentFragment() {
        return this.m_dtmManager.createDocumentFragment();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public boolean release(DTM dtm, boolean shouldHardDelete) {
        if (this.m_rtfdtm_stack != null && this.m_rtfdtm_stack.contains(dtm)) {
            return false;
        }
        return this.m_dtmManager.release(dtm, shouldHardDelete);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTMIterator createDTMIterator(Object xpathCompiler, int pos) {
        return this.m_dtmManager.createDTMIterator(xpathCompiler, pos);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTMIterator createDTMIterator(String xpathString, PrefixResolver presolver) {
        return this.m_dtmManager.createDTMIterator(xpathString, presolver);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTMIterator createDTMIterator(int whatToShow, DTMFilter filter, boolean entityReferenceExpansion) {
        return this.m_dtmManager.createDTMIterator(whatToShow, filter, entityReferenceExpansion);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTMIterator createDTMIterator(int node) {
        DTMIterator iter = new OneStepIteratorForward(13);
        iter.setRoot(node, this);
        return iter;
    }

    public XPathContext() {
        this(false);
    }

    public XPathContext(boolean overrideDefaultParser) {
        this.m_last_pushed_rtfdtm = new IntStack();
        this.m_rtfdtm_stack = null;
        this.m_which_rtfdtm = -1;
        this.m_global_rtfdtm = null;
        this.m_DTMXRTreeFrags = null;
        this.m_isSecureProcessing = false;
        this.m_dtmManager = null;
        this.m_saxLocations = new ObjectStack(4096);
        this.m_variableStacks = new VariableStack();
        this.m_sourceTreeManager = new SourceTreeManager();
        this.m_contextNodeLists = new Stack();
        this.m_currentNodes = new IntStack(4096);
        this.m_iteratorRoots = new NodeVector();
        this.m_predicateRoots = new NodeVector();
        this.m_currentExpressionNodes = new IntStack(4096);
        this.m_predicatePos = new IntStack();
        this.m_prefixResolvers = new ObjectStack(4096);
        this.m_axesIteratorStack = new Stack();
        this.expressionContext = new XPathExpressionContext();
        init(overrideDefaultParser);
    }

    public XPathContext(Object owner) {
        this.m_last_pushed_rtfdtm = new IntStack();
        this.m_rtfdtm_stack = null;
        this.m_which_rtfdtm = -1;
        this.m_global_rtfdtm = null;
        this.m_DTMXRTreeFrags = null;
        this.m_isSecureProcessing = false;
        this.m_dtmManager = null;
        this.m_saxLocations = new ObjectStack(4096);
        this.m_variableStacks = new VariableStack();
        this.m_sourceTreeManager = new SourceTreeManager();
        this.m_contextNodeLists = new Stack();
        this.m_currentNodes = new IntStack(4096);
        this.m_iteratorRoots = new NodeVector();
        this.m_predicateRoots = new NodeVector();
        this.m_currentExpressionNodes = new IntStack(4096);
        this.m_predicatePos = new IntStack();
        this.m_prefixResolvers = new ObjectStack(4096);
        this.m_axesIteratorStack = new Stack();
        this.expressionContext = new XPathExpressionContext();
        this.m_owner = owner;
        try {
            this.m_ownerGetErrorListener = this.m_owner.getClass().getMethod("getErrorListener", new Class[0]);
        } catch (NoSuchMethodException e2) {
        }
        init(false);
    }

    private void init(boolean overrideDefaultParser) {
        this.m_prefixResolvers.push(null);
        this.m_currentNodes.push(-1);
        this.m_currentExpressionNodes.push(-1);
        this.m_saxLocations.push(null);
        this.m_overrideDefaultParser = overrideDefaultParser;
        this.m_dtmManager = DTMManager.newInstance(XMLStringFactoryImpl.getFactory());
    }

    public void reset() {
        releaseDTMXRTreeFrags();
        if (this.m_rtfdtm_stack != null) {
            Enumeration e2 = this.m_rtfdtm_stack.elements();
            while (e2.hasMoreElements()) {
                this.m_dtmManager.release((DTM) e2.nextElement(), true);
            }
        }
        this.m_rtfdtm_stack = null;
        this.m_which_rtfdtm = -1;
        if (this.m_global_rtfdtm != null) {
            this.m_dtmManager.release(this.m_global_rtfdtm, true);
        }
        this.m_global_rtfdtm = null;
        this.m_dtmManager = DTMManager.newInstance(XMLStringFactoryImpl.getFactory());
        this.m_saxLocations.removeAllElements();
        this.m_axesIteratorStack.removeAllElements();
        this.m_contextNodeLists.removeAllElements();
        this.m_currentExpressionNodes.removeAllElements();
        this.m_currentNodes.removeAllElements();
        this.m_iteratorRoots.RemoveAllNoClear();
        this.m_predicatePos.removeAllElements();
        this.m_predicateRoots.RemoveAllNoClear();
        this.m_prefixResolvers.removeAllElements();
        this.m_prefixResolvers.push(null);
        this.m_currentNodes.push(-1);
        this.m_currentExpressionNodes.push(-1);
        this.m_saxLocations.push(null);
    }

    public void setSAXLocator(SourceLocator location) {
        this.m_saxLocations.setTop(location);
    }

    public void pushSAXLocator(SourceLocator location) {
        this.m_saxLocations.push(location);
    }

    public void pushSAXLocatorNull() {
        this.m_saxLocations.push(null);
    }

    public void popSAXLocator() {
        this.m_saxLocations.pop();
    }

    public SourceLocator getSAXLocator() {
        return (SourceLocator) this.m_saxLocations.peek();
    }

    public Object getOwnerObject() {
        return this.m_owner;
    }

    public final VariableStack getVarStack() {
        return this.m_variableStacks;
    }

    public final void setVarStack(VariableStack varStack) {
        this.m_variableStacks = varStack;
    }

    public final SourceTreeManager getSourceTreeManager() {
        return this.m_sourceTreeManager;
    }

    public void setSourceTreeManager(SourceTreeManager mgr) {
        this.m_sourceTreeManager = mgr;
    }

    public final ErrorListener getErrorListener() {
        if (null != this.m_errorListener) {
            return this.m_errorListener;
        }
        ErrorListener retval = null;
        try {
            if (null != this.m_ownerGetErrorListener) {
                retval = (ErrorListener) this.m_ownerGetErrorListener.invoke(this.m_owner, new Object[0]);
            }
        } catch (Exception e2) {
        }
        if (null == retval) {
            if (null == this.m_defaultErrorListener) {
                this.m_defaultErrorListener = new DefaultErrorHandler();
            }
            retval = this.m_defaultErrorListener;
        }
        return retval;
    }

    public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
        if (listener == null) {
            throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_NULL_ERROR_HANDLER", null));
        }
        this.m_errorListener = listener;
    }

    public final URIResolver getURIResolver() {
        return this.m_uriResolver;
    }

    public void setURIResolver(URIResolver resolver) {
        this.m_uriResolver = resolver;
    }

    public final XMLReader getPrimaryReader() {
        return this.m_primaryReader;
    }

    public void setPrimaryReader(XMLReader reader) {
        this.m_primaryReader = reader;
    }

    public Stack getContextNodeListsStack() {
        return this.m_contextNodeLists;
    }

    public void setContextNodeListsStack(Stack s2) {
        this.m_contextNodeLists = s2;
    }

    public final DTMIterator getContextNodeList() {
        if (this.m_contextNodeLists.size() > 0) {
            return (DTMIterator) this.m_contextNodeLists.peek();
        }
        return null;
    }

    public final void pushContextNodeList(DTMIterator nl) {
        this.m_contextNodeLists.push(nl);
    }

    public final void popContextNodeList() {
        if (this.m_contextNodeLists.isEmpty()) {
            System.err.println("Warning: popContextNodeList when stack is empty!");
        } else {
            this.m_contextNodeLists.pop();
        }
    }

    public IntStack getCurrentNodeStack() {
        return this.m_currentNodes;
    }

    public void setCurrentNodeStack(IntStack nv) {
        this.m_currentNodes = nv;
    }

    public final int getCurrentNode() {
        return this.m_currentNodes.peek();
    }

    public final void pushCurrentNodeAndExpression(int cn, int en) {
        this.m_currentNodes.push(cn);
        this.m_currentExpressionNodes.push(cn);
    }

    public final void popCurrentNodeAndExpression() {
        this.m_currentNodes.quickPop(1);
        this.m_currentExpressionNodes.quickPop(1);
    }

    public final void pushExpressionState(int cn, int en, PrefixResolver nc) {
        this.m_currentNodes.push(cn);
        this.m_currentExpressionNodes.push(cn);
        this.m_prefixResolvers.push(nc);
    }

    public final void popExpressionState() {
        this.m_currentNodes.quickPop(1);
        this.m_currentExpressionNodes.quickPop(1);
        this.m_prefixResolvers.pop();
    }

    public final void pushCurrentNode(int n2) {
        this.m_currentNodes.push(n2);
    }

    public final void popCurrentNode() {
        this.m_currentNodes.quickPop(1);
    }

    public final void pushPredicateRoot(int n2) {
        this.m_predicateRoots.push(n2);
    }

    public final void popPredicateRoot() {
        this.m_predicateRoots.popQuick();
    }

    public final int getPredicateRoot() {
        return this.m_predicateRoots.peepOrNull();
    }

    public final void pushIteratorRoot(int n2) {
        this.m_iteratorRoots.push(n2);
    }

    public final void popIteratorRoot() {
        this.m_iteratorRoots.popQuick();
    }

    public final int getIteratorRoot() {
        return this.m_iteratorRoots.peepOrNull();
    }

    public IntStack getCurrentExpressionNodeStack() {
        return this.m_currentExpressionNodes;
    }

    public void setCurrentExpressionNodeStack(IntStack nv) {
        this.m_currentExpressionNodes = nv;
    }

    public final int getPredicatePos() {
        return this.m_predicatePos.peek();
    }

    public final void pushPredicatePos(int n2) {
        this.m_predicatePos.push(n2);
    }

    public final void popPredicatePos() {
        this.m_predicatePos.pop();
    }

    public final int getCurrentExpressionNode() {
        return this.m_currentExpressionNodes.peek();
    }

    public final void pushCurrentExpressionNode(int n2) {
        this.m_currentExpressionNodes.push(n2);
    }

    public final void popCurrentExpressionNode() {
        this.m_currentExpressionNodes.quickPop(1);
    }

    public final PrefixResolver getNamespaceContext() {
        return (PrefixResolver) this.m_prefixResolvers.peek();
    }

    public final void setNamespaceContext(PrefixResolver pr) {
        this.m_prefixResolvers.setTop(pr);
    }

    public final void pushNamespaceContext(PrefixResolver pr) {
        this.m_prefixResolvers.push(pr);
    }

    public final void pushNamespaceContextNull() {
        this.m_prefixResolvers.push(null);
    }

    public final void popNamespaceContext() {
        this.m_prefixResolvers.pop();
    }

    public Stack getAxesIteratorStackStacks() {
        return this.m_axesIteratorStack;
    }

    public void setAxesIteratorStackStacks(Stack s2) {
        this.m_axesIteratorStack = s2;
    }

    public final void pushSubContextList(SubContextList iter) {
        this.m_axesIteratorStack.push(iter);
    }

    public final void popSubContextList() {
        this.m_axesIteratorStack.pop();
    }

    public SubContextList getSubContextList() {
        if (this.m_axesIteratorStack.isEmpty()) {
            return null;
        }
        return (SubContextList) this.m_axesIteratorStack.peek();
    }

    public SubContextList getCurrentNodeList() {
        if (this.m_axesIteratorStack.isEmpty()) {
            return null;
        }
        return (SubContextList) this.m_axesIteratorStack.elementAt(0);
    }

    public final int getContextNode() {
        return getCurrentNode();
    }

    public final DTMIterator getContextNodes() {
        try {
            DTMIterator cnl = getContextNodeList();
            if (null != cnl) {
                return cnl.cloneWithReset();
            }
            return null;
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public ExpressionContext getExpressionContext() {
        return this.expressionContext;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/XPathContext$XPathExpressionContext.class */
    public class XPathExpressionContext implements ExpressionContext {
        public XPathExpressionContext() {
        }

        @Override // com.sun.org.apache.xalan.internal.extensions.ExpressionContext
        public XPathContext getXPathContext() {
            return XPathContext.this;
        }

        public DTMManager getDTMManager() {
            return XPathContext.this.m_dtmManager;
        }

        @Override // com.sun.org.apache.xalan.internal.extensions.ExpressionContext
        public Node getContextNode() {
            int context = XPathContext.this.getCurrentNode();
            return XPathContext.this.getDTM(context).getNode(context);
        }

        @Override // com.sun.org.apache.xalan.internal.extensions.ExpressionContext
        public NodeIterator getContextNodes() {
            return new DTMNodeIterator(XPathContext.this.getContextNodeList());
        }

        @Override // com.sun.org.apache.xalan.internal.extensions.ExpressionContext
        public ErrorListener getErrorListener() {
            return XPathContext.this.getErrorListener();
        }

        public boolean overrideDefaultParser() {
            return XPathContext.this.m_overrideDefaultParser;
        }

        public void setOverrideDefaultParser(boolean flag) {
            XPathContext.this.m_overrideDefaultParser = flag;
        }

        @Override // com.sun.org.apache.xalan.internal.extensions.ExpressionContext
        public double toNumber(Node n2) {
            int nodeHandle = XPathContext.this.getDTMHandleFromNode(n2);
            DTM dtm = XPathContext.this.getDTM(nodeHandle);
            XString xobj = (XString) dtm.getStringValue(nodeHandle);
            return xobj.num();
        }

        @Override // com.sun.org.apache.xalan.internal.extensions.ExpressionContext
        public String toString(Node n2) {
            int nodeHandle = XPathContext.this.getDTMHandleFromNode(n2);
            DTM dtm = XPathContext.this.getDTM(nodeHandle);
            XMLString strVal = dtm.getStringValue(nodeHandle);
            return strVal.toString();
        }

        @Override // com.sun.org.apache.xalan.internal.extensions.ExpressionContext
        public final XObject getVariableOrParam(QName qname) throws TransformerException {
            return XPathContext.this.m_variableStacks.getVariableOrParam(XPathContext.this, qname);
        }
    }

    public DTM getGlobalRTFDTM() {
        if (this.m_global_rtfdtm == null || this.m_global_rtfdtm.isTreeIncomplete()) {
            this.m_global_rtfdtm = (SAX2RTFDTM) this.m_dtmManager.getDTM(null, true, null, false, false);
        }
        return this.m_global_rtfdtm;
    }

    public DTM getRTFDTM() {
        SAX2RTFDTM rtfdtm;
        if (this.m_rtfdtm_stack == null) {
            this.m_rtfdtm_stack = new Vector();
            rtfdtm = (SAX2RTFDTM) this.m_dtmManager.getDTM(null, true, null, false, false);
            this.m_rtfdtm_stack.addElement(rtfdtm);
            this.m_which_rtfdtm++;
        } else if (this.m_which_rtfdtm < 0) {
            Vector vector = this.m_rtfdtm_stack;
            int i2 = this.m_which_rtfdtm + 1;
            this.m_which_rtfdtm = i2;
            rtfdtm = (SAX2RTFDTM) vector.elementAt(i2);
        } else {
            rtfdtm = (SAX2RTFDTM) this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm);
            if (rtfdtm.isTreeIncomplete()) {
                int i3 = this.m_which_rtfdtm + 1;
                this.m_which_rtfdtm = i3;
                if (i3 < this.m_rtfdtm_stack.size()) {
                    rtfdtm = (SAX2RTFDTM) this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm);
                } else {
                    rtfdtm = (SAX2RTFDTM) this.m_dtmManager.getDTM(null, true, null, false, false);
                    this.m_rtfdtm_stack.addElement(rtfdtm);
                }
            }
        }
        return rtfdtm;
    }

    public void pushRTFContext() {
        this.m_last_pushed_rtfdtm.push(this.m_which_rtfdtm);
        if (null != this.m_rtfdtm_stack) {
            ((SAX2RTFDTM) getRTFDTM()).pushRewindMark();
        }
    }

    public void popRTFContext() {
        int previous = this.m_last_pushed_rtfdtm.pop();
        if (null == this.m_rtfdtm_stack) {
            return;
        }
        if (this.m_which_rtfdtm == previous) {
            if (previous >= 0) {
                ((SAX2RTFDTM) this.m_rtfdtm_stack.elementAt(previous)).popRewindMark();
            }
        } else {
            while (this.m_which_rtfdtm != previous) {
                ((SAX2RTFDTM) this.m_rtfdtm_stack.elementAt(this.m_which_rtfdtm)).popRewindMark();
                this.m_which_rtfdtm--;
            }
        }
    }

    public DTMXRTreeFrag getDTMXRTreeFrag(int dtmIdentity) {
        if (this.m_DTMXRTreeFrags == null) {
            this.m_DTMXRTreeFrags = new HashMap();
        }
        if (this.m_DTMXRTreeFrags.containsKey(new Integer(dtmIdentity))) {
            return (DTMXRTreeFrag) this.m_DTMXRTreeFrags.get(new Integer(dtmIdentity));
        }
        DTMXRTreeFrag frag = new DTMXRTreeFrag(dtmIdentity, this);
        this.m_DTMXRTreeFrags.put(new Integer(dtmIdentity), frag);
        return frag;
    }

    private final void releaseDTMXRTreeFrags() {
        if (this.m_DTMXRTreeFrags == null) {
            return;
        }
        Iterator iter = this.m_DTMXRTreeFrags.values().iterator();
        while (iter.hasNext()) {
            DTMXRTreeFrag frag = (DTMXRTreeFrag) iter.next();
            frag.destruct();
            iter.remove();
        }
        this.m_DTMXRTreeFrags = null;
    }
}
