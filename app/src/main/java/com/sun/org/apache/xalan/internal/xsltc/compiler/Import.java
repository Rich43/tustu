package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import java.util.Iterator;
import javax.xml.XMLConstants;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Import.class */
final class Import extends TopLevelElement {
    private Stylesheet _imported = null;

    Import() {
    }

    public Stylesheet getImportedStylesheet() {
        return this._imported;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        XSLTC xsltc = parser.getXSLTC();
        Stylesheet context = parser.getCurrentStylesheet();
        try {
            try {
                String docToLoad = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_HREF);
                if (context.checkForLoop(docToLoad)) {
                    ErrorMsg msg = new ErrorMsg(ErrorMsg.CIRCULAR_INCLUDE_ERR, (Object) docToLoad, (SyntaxTreeNode) this);
                    parser.reportError(2, msg);
                    parser.setCurrentStylesheet(context);
                    return;
                }
                InputSource input = null;
                XMLReader reader = null;
                String currLoadedDoc = context.getSystemId();
                SourceLoader loader = context.getSourceLoader();
                if (loader != null) {
                    input = loader.loadSource(docToLoad, currLoadedDoc, xsltc);
                    if (input != null) {
                        docToLoad = input.getSystemId();
                        reader = xsltc.getXMLReader();
                    } else if (parser.errorsFound()) {
                        parser.setCurrentStylesheet(context);
                        return;
                    }
                }
                if (input == null) {
                    docToLoad = SystemIDResolver.getAbsoluteURI(docToLoad, currLoadedDoc);
                    String accessError = SecuritySupport.checkAccess(docToLoad, (String) xsltc.getProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET), "all");
                    if (accessError != null) {
                        ErrorMsg msg2 = new ErrorMsg(ErrorMsg.ACCESSING_XSLT_TARGET_ERR, SecuritySupport.sanitizePath(docToLoad), accessError, this);
                        parser.reportError(2, msg2);
                        parser.setCurrentStylesheet(context);
                        return;
                    }
                    input = new InputSource(docToLoad);
                }
                if (input == null) {
                    ErrorMsg msg3 = new ErrorMsg(ErrorMsg.FILE_NOT_FOUND_ERR, (Object) docToLoad, (SyntaxTreeNode) this);
                    parser.reportError(2, msg3);
                    parser.setCurrentStylesheet(context);
                    return;
                }
                SyntaxTreeNode root = reader != null ? parser.parse(reader, input) : parser.parse(input);
                if (root == null) {
                    parser.setCurrentStylesheet(context);
                    return;
                }
                this._imported = parser.makeStylesheet(root);
                if (this._imported == null) {
                    parser.setCurrentStylesheet(context);
                    return;
                }
                this._imported.setSourceLoader(loader);
                this._imported.setSystemId(docToLoad);
                this._imported.setParentStylesheet(context);
                this._imported.setImportingStylesheet(context);
                this._imported.setTemplateInlining(context.getTemplateInlining());
                int currPrecedence = parser.getCurrentImportPrecedence();
                int nextPrecedence = parser.getNextImportPrecedence();
                this._imported.setImportPrecedence(currPrecedence);
                context.setImportPrecedence(nextPrecedence);
                parser.setCurrentStylesheet(this._imported);
                this._imported.parseContents(parser);
                Iterator<SyntaxTreeNode> elements = this._imported.elements();
                Stylesheet topStylesheet = parser.getTopLevelStylesheet();
                while (elements.hasNext()) {
                    SyntaxTreeNode element = elements.next();
                    if (element instanceof TopLevelElement) {
                        if (element instanceof Variable) {
                            topStylesheet.addVariable((Variable) element);
                        } else if (element instanceof Param) {
                            topStylesheet.addParam((Param) element);
                        } else {
                            topStylesheet.addElement((TopLevelElement) element);
                        }
                    }
                }
                parser.setCurrentStylesheet(context);
            } catch (Exception e2) {
                e2.printStackTrace();
                parser.setCurrentStylesheet(context);
            }
        } catch (Throwable th) {
            parser.setCurrentStylesheet(context);
            throw th;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }
}
