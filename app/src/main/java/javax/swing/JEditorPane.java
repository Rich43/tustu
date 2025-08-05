package javax.swing;

import com.sun.glass.ui.Clipboard;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleHyperlink;
import javax.accessibility.AccessibleHypertext;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.Caret;
import javax.swing.text.ChangedCharSetException;
import javax.swing.text.CompositeView;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.GlyphView;
import javax.swing.text.JTextComponent;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.WrappedPlainView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/* loaded from: rt.jar:javax/swing/JEditorPane.class */
public class JEditorPane extends JTextComponent {
    private SwingWorker<URL, Object> pageLoader;
    private EditorKit kit;
    private boolean isUserSetEditorKit;
    private Hashtable<String, Object> pageProperties;
    static final String PostDataProperty = "javax.swing.JEditorPane.postdata";
    private Hashtable<String, EditorKit> typeHandlers;
    private static final String uiClassID = "EditorPaneUI";
    public static final String W3C_LENGTH_UNITS = "JEditorPane.w3cLengthUnits";
    public static final String HONOR_DISPLAY_PROPERTIES = "JEditorPane.honorDisplayProperties";
    private static final Object kitRegistryKey = new StringBuffer("JEditorPane.kitRegistry");
    private static final Object kitTypeRegistryKey = new StringBuffer("JEditorPane.kitTypeRegistry");
    private static final Object kitLoaderRegistryKey = new StringBuffer("JEditorPane.kitLoaderRegistry");
    static final Map<String, String> defaultEditorKitMap = new HashMap(0);

    public JEditorPane() {
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() { // from class: javax.swing.JEditorPane.1
            @Override // javax.swing.LayoutFocusTraversalPolicy, javax.swing.SortingFocusTraversalPolicy, java.awt.FocusTraversalPolicy
            public Component getComponentAfter(Container container, Component component) {
                if (container != JEditorPane.this || (!JEditorPane.this.isEditable() && JEditorPane.this.getComponentCount() > 0)) {
                    return super.getComponentAfter(container, component);
                }
                Container focusCycleRootAncestor = JEditorPane.this.getFocusCycleRootAncestor();
                if (focusCycleRootAncestor != null) {
                    return focusCycleRootAncestor.getFocusTraversalPolicy().getComponentAfter(focusCycleRootAncestor, JEditorPane.this);
                }
                return null;
            }

            @Override // javax.swing.LayoutFocusTraversalPolicy, javax.swing.SortingFocusTraversalPolicy, java.awt.FocusTraversalPolicy
            public Component getComponentBefore(Container container, Component component) {
                if (container != JEditorPane.this || (!JEditorPane.this.isEditable() && JEditorPane.this.getComponentCount() > 0)) {
                    return super.getComponentBefore(container, component);
                }
                Container focusCycleRootAncestor = JEditorPane.this.getFocusCycleRootAncestor();
                if (focusCycleRootAncestor != null) {
                    return focusCycleRootAncestor.getFocusTraversalPolicy().getComponentBefore(focusCycleRootAncestor, JEditorPane.this);
                }
                return null;
            }

            @Override // javax.swing.SortingFocusTraversalPolicy, java.awt.FocusTraversalPolicy
            public Component getDefaultComponent(Container container) {
                if (container != JEditorPane.this || (!JEditorPane.this.isEditable() && JEditorPane.this.getComponentCount() > 0)) {
                    return super.getDefaultComponent(container);
                }
                return null;
            }

            @Override // javax.swing.LayoutFocusTraversalPolicy, javax.swing.SortingFocusTraversalPolicy
            protected boolean accept(Component component) {
                if (component != JEditorPane.this) {
                    return super.accept(component);
                }
                return false;
            }
        });
        LookAndFeel.installProperty(this, "focusTraversalKeysForward", JComponent.getManagingFocusForwardTraversalKeys());
        LookAndFeel.installProperty(this, "focusTraversalKeysBackward", JComponent.getManagingFocusBackwardTraversalKeys());
    }

    public JEditorPane(URL url) throws IOException {
        this();
        setPage(url);
    }

    public JEditorPane(String str) throws IOException {
        this();
        setPage(str);
    }

    public JEditorPane(String str, String str2) {
        this();
        setContentType(str);
        setText(str2);
    }

    public synchronized void addHyperlinkListener(HyperlinkListener hyperlinkListener) {
        this.listenerList.add(HyperlinkListener.class, hyperlinkListener);
    }

    public synchronized void removeHyperlinkListener(HyperlinkListener hyperlinkListener) {
        this.listenerList.remove(HyperlinkListener.class, hyperlinkListener);
    }

    public synchronized HyperlinkListener[] getHyperlinkListeners() {
        return (HyperlinkListener[]) this.listenerList.getListeners(HyperlinkListener.class);
    }

    public void fireHyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == HyperlinkListener.class) {
                ((HyperlinkListener) listenerList[length + 1]).hyperlinkUpdate(hyperlinkEvent);
            }
        }
    }

    public void setPage(URL url) throws IOException {
        if (url == null) {
            throw new IOException("invalid url");
        }
        URL page = getPage();
        if (!url.equals(page) && url.getRef() == null) {
            scrollRectToVisible(new Rectangle(0, 0, 1, 1));
        }
        boolean z2 = false;
        Object postData = getPostData();
        if (page == null || !page.sameFile(url) || postData != null) {
            if (getAsynchronousLoadPriority(getDocument()) < 0) {
                InputStream stream = getStream(url);
                if (this.kit != null) {
                    Document documentInitializeModel = initializeModel(this.kit, url);
                    if (getAsynchronousLoadPriority(documentInitializeModel) >= 0) {
                        setDocument(documentInitializeModel);
                        synchronized (this) {
                            this.pageLoader = new PageLoader(documentInitializeModel, stream, page, url);
                            this.pageLoader.execute();
                        }
                        return;
                    }
                    read(stream, documentInitializeModel);
                    setDocument(documentInitializeModel);
                    z2 = true;
                }
            } else {
                if (this.pageLoader != null) {
                    this.pageLoader.cancel(true);
                }
                this.pageLoader = new PageLoader(null, null, page, url);
                this.pageLoader.execute();
                return;
            }
        }
        final String ref = url.getRef();
        if (ref != null) {
            if (!z2) {
                scrollToReference(ref);
            } else {
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.JEditorPane.2
                    @Override // java.lang.Runnable
                    public void run() {
                        JEditorPane.this.scrollToReference(ref);
                    }
                });
            }
            getDocument().putProperty(Document.StreamDescriptionProperty, url);
        }
        firePropertyChange("page", page, url);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Document initializeModel(EditorKit editorKit, URL url) {
        Document documentCreateDefaultDocument = editorKit.createDefaultDocument();
        if (this.pageProperties != null) {
            Enumeration<String> enumerationKeys = this.pageProperties.keys();
            while (enumerationKeys.hasMoreElements()) {
                String strNextElement = enumerationKeys.nextElement2();
                documentCreateDefaultDocument.putProperty(strNextElement, this.pageProperties.get(strNextElement));
            }
            this.pageProperties.clear();
        }
        if (documentCreateDefaultDocument.getProperty(Document.StreamDescriptionProperty) == null) {
            documentCreateDefaultDocument.putProperty(Document.StreamDescriptionProperty, url);
        }
        return documentCreateDefaultDocument;
    }

    private int getAsynchronousLoadPriority(Document document) {
        if (document instanceof AbstractDocument) {
            return ((AbstractDocument) document).getAsynchronousLoadPriority();
        }
        return -1;
    }

    public void read(InputStream inputStream, Object obj) throws IOException {
        if ((obj instanceof HTMLDocument) && (this.kit instanceof HTMLEditorKit)) {
            HTMLDocument hTMLDocument = (HTMLDocument) obj;
            setDocument(hTMLDocument);
            read(inputStream, (Document) hTMLDocument);
        } else {
            String str = (String) getClientProperty("charset");
            super.read(str != null ? new InputStreamReader(inputStream, str) : new InputStreamReader(inputStream), obj);
        }
    }

    void read(InputStream inputStream, Document document) throws IOException {
        if (!Boolean.TRUE.equals(document.getProperty("IgnoreCharsetDirective"))) {
            inputStream = new BufferedInputStream(inputStream, 10240);
            inputStream.mark(10240);
        }
        try {
            String str = (String) getClientProperty("charset");
            this.kit.read(str != null ? new InputStreamReader(inputStream, str) : new InputStreamReader(inputStream), document, 0);
        } catch (BadLocationException e2) {
            throw new IOException(e2.getMessage());
        } catch (ChangedCharSetException e3) {
            String charSetSpec = e3.getCharSetSpec();
            if (e3.keyEqualsCharSet()) {
                putClientProperty("charset", charSetSpec);
            } else {
                setCharsetFromContentTypeParameters(charSetSpec);
            }
            try {
                inputStream.reset();
            } catch (IOException e4) {
                inputStream.close();
                URL url = (URL) document.getProperty(Document.StreamDescriptionProperty);
                if (url != null) {
                    inputStream = url.openConnection().getInputStream();
                } else {
                    throw e3;
                }
            }
            try {
                document.remove(0, document.getLength());
            } catch (BadLocationException e5) {
            }
            document.putProperty("IgnoreCharsetDirective", true);
            read(inputStream, document);
        }
    }

    /* loaded from: rt.jar:javax/swing/JEditorPane$PageLoader.class */
    class PageLoader extends SwingWorker<URL, Object> {
        InputStream in;
        URL old;
        URL page;
        Document doc;

        PageLoader(Document document, InputStream inputStream, URL url, URL url2) {
            this.in = inputStream;
            this.old = url;
            this.page = url2;
            this.doc = document;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javax.swing.SwingWorker
        public URL doInBackground() {
            try {
                try {
                    if (this.in == null) {
                        this.in = JEditorPane.this.getStream(this.page);
                        if (JEditorPane.this.kit == null) {
                            UIManager.getLookAndFeel().provideErrorFeedback(JEditorPane.this);
                            URL url = this.old;
                            if (0 != 0) {
                                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.JEditorPane.PageLoader.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        JEditorPane.this.firePropertyChange("page", PageLoader.this.old, PageLoader.this.page);
                                    }
                                });
                            }
                            return 0 != 0 ? this.page : this.old;
                        }
                    }
                    if (this.doc == null) {
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() { // from class: javax.swing.JEditorPane.PageLoader.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    PageLoader.this.doc = JEditorPane.this.initializeModel(JEditorPane.this.kit, PageLoader.this.page);
                                    JEditorPane.this.setDocument(PageLoader.this.doc);
                                }
                            });
                        } catch (InterruptedException e2) {
                            UIManager.getLookAndFeel().provideErrorFeedback(JEditorPane.this);
                            URL url2 = this.old;
                            if (0 != 0) {
                                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.JEditorPane.PageLoader.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        JEditorPane.this.firePropertyChange("page", PageLoader.this.old, PageLoader.this.page);
                                    }
                                });
                            }
                            return 0 != 0 ? this.page : this.old;
                        } catch (InvocationTargetException e3) {
                            UIManager.getLookAndFeel().provideErrorFeedback(JEditorPane.this);
                            URL url3 = this.old;
                            if (0 != 0) {
                                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.JEditorPane.PageLoader.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        JEditorPane.this.firePropertyChange("page", PageLoader.this.old, PageLoader.this.page);
                                    }
                                });
                            }
                            return 0 != 0 ? this.page : this.old;
                        }
                    }
                    JEditorPane.this.read(this.in, this.doc);
                    if (((URL) this.doc.getProperty(Document.StreamDescriptionProperty)).getRef() != null) {
                        SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.JEditorPane.PageLoader.2
                            @Override // java.lang.Runnable
                            public void run() {
                                JEditorPane.this.scrollToReference(((URL) JEditorPane.this.getDocument().getProperty(Document.StreamDescriptionProperty)).getRef());
                            }
                        });
                    }
                    if (1 != 0) {
                        SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.JEditorPane.PageLoader.3
                            @Override // java.lang.Runnable
                            public void run() {
                                JEditorPane.this.firePropertyChange("page", PageLoader.this.old, PageLoader.this.page);
                            }
                        });
                    }
                    return 1 != 0 ? this.page : this.old;
                } catch (IOException e4) {
                    UIManager.getLookAndFeel().provideErrorFeedback(JEditorPane.this);
                    if (0 != 0) {
                        SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.JEditorPane.PageLoader.3
                            @Override // java.lang.Runnable
                            public void run() {
                                JEditorPane.this.firePropertyChange("page", PageLoader.this.old, PageLoader.this.page);
                            }
                        });
                    }
                    return 0 != 0 ? this.page : this.old;
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.JEditorPane.PageLoader.3
                        @Override // java.lang.Runnable
                        public void run() {
                            JEditorPane.this.firePropertyChange("page", PageLoader.this.old, PageLoader.this.page);
                        }
                    });
                }
                return 0 != 0 ? this.page : this.old;
            }
        }
    }

    protected InputStream getStream(URL url) throws IOException {
        URL url2;
        final URLConnection uRLConnectionOpenConnection = url.openConnection();
        if (uRLConnectionOpenConnection instanceof HttpURLConnection) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnectionOpenConnection;
            httpURLConnection.setInstanceFollowRedirects(false);
            Object postData = getPostData();
            if (postData != null) {
                handlePostData(httpURLConnection, postData);
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode >= 300 && responseCode <= 399) {
                String headerField = uRLConnectionOpenConnection.getHeaderField("Location");
                if (headerField.startsWith("http", 0)) {
                    url2 = new URL(headerField);
                } else {
                    url2 = new URL(url, headerField);
                }
                return getStream(url2);
            }
        }
        if (SwingUtilities.isEventDispatchThread()) {
            handleConnectionProperties(uRLConnectionOpenConnection);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() { // from class: javax.swing.JEditorPane.3
                    @Override // java.lang.Runnable
                    public void run() {
                        JEditorPane.this.handleConnectionProperties(uRLConnectionOpenConnection);
                    }
                });
            } catch (InterruptedException e2) {
                throw new RuntimeException(e2);
            } catch (InvocationTargetException e3) {
                throw new RuntimeException(e3);
            }
        }
        return uRLConnectionOpenConnection.getInputStream();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectionProperties(URLConnection uRLConnection) {
        if (this.pageProperties == null) {
            this.pageProperties = new Hashtable<>();
        }
        String contentType = uRLConnection.getContentType();
        if (contentType != null) {
            setContentType(contentType);
            this.pageProperties.put("content-type", contentType);
        }
        this.pageProperties.put(Document.StreamDescriptionProperty, uRLConnection.getURL());
        String contentEncoding = uRLConnection.getContentEncoding();
        if (contentEncoding != null) {
            this.pageProperties.put("content-encoding", contentEncoding);
        }
    }

    private Object getPostData() {
        return getDocument().getProperty(PostDataProperty);
    }

    private void handlePostData(HttpURLConnection httpURLConnection, Object obj) throws IOException {
        httpURLConnection.setDoOutput(true);
        DataOutputStream dataOutputStream = null;
        try {
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes((String) obj);
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
        } catch (Throwable th) {
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            throw th;
        }
    }

    public void scrollToReference(String str) {
        Document document = getDocument();
        if (document instanceof HTMLDocument) {
            HTMLDocument.Iterator iterator = ((HTMLDocument) document).getIterator(HTML.Tag.f12846A);
            while (iterator.isValid()) {
                String str2 = (String) iterator.getAttributes().getAttribute(HTML.Attribute.NAME);
                if (str2 != null && str2.equals(str)) {
                    try {
                        int startOffset = iterator.getStartOffset();
                        Rectangle rectangleModelToView = modelToView(startOffset);
                        if (rectangleModelToView != null) {
                            rectangleModelToView.height = getVisibleRect().height;
                            scrollRectToVisible(rectangleModelToView);
                            setCaretPosition(startOffset);
                        }
                    } catch (BadLocationException e2) {
                        UIManager.getLookAndFeel().provideErrorFeedback(this);
                    }
                }
                iterator.next();
            }
        }
    }

    public URL getPage() {
        return (URL) getDocument().getProperty(Document.StreamDescriptionProperty);
    }

    public void setPage(String str) throws IOException {
        if (str == null) {
            throw new IOException("invalid url");
        }
        setPage(new URL(str));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    protected EditorKit createDefaultEditorKit() {
        return new PlainEditorKit();
    }

    public EditorKit getEditorKit() {
        if (this.kit == null) {
            this.kit = createDefaultEditorKit();
            this.isUserSetEditorKit = false;
        }
        return this.kit;
    }

    public final String getContentType() {
        if (this.kit != null) {
            return this.kit.getContentType();
        }
        return null;
    }

    public final void setContentType(String str) {
        EditorKit editorKitForContentType;
        int iIndexOf = str.indexOf(";");
        if (iIndexOf > -1) {
            String strSubstring = str.substring(iIndexOf);
            str = str.substring(0, iIndexOf).trim();
            if (str.toLowerCase().startsWith("text/")) {
                setCharsetFromContentTypeParameters(strSubstring);
            }
        }
        if ((this.kit == null || !str.equals(this.kit.getContentType()) || !this.isUserSetEditorKit) && (editorKitForContentType = getEditorKitForContentType(str)) != null && editorKitForContentType != this.kit) {
            setEditorKit(editorKitForContentType);
            this.isUserSetEditorKit = false;
        }
    }

    private void setCharsetFromContentTypeParameters(String str) {
        String strFindValue;
        try {
            int iIndexOf = str.indexOf(59);
            if (iIndexOf > -1 && iIndexOf < str.length() - 1) {
                str = str.substring(iIndexOf + 1);
            }
            if (str.length() > 0 && (strFindValue = new HeaderParser(str).findValue("charset")) != null) {
                putClientProperty("charset", strFindValue);
            }
        } catch (IndexOutOfBoundsException e2) {
        } catch (NullPointerException e3) {
        } catch (Exception e4) {
            System.err.println("JEditorPane.getCharsetFromContentTypeParameters failed on: " + str);
            e4.printStackTrace();
        }
    }

    public void setEditorKit(EditorKit editorKit) {
        EditorKit editorKit2 = this.kit;
        this.isUserSetEditorKit = true;
        if (editorKit2 != null) {
            editorKit2.deinstall(this);
        }
        this.kit = editorKit;
        if (this.kit != null) {
            this.kit.install(this);
            setDocument(this.kit.createDefaultDocument());
        }
        firePropertyChange("editorKit", editorKit2, editorKit);
    }

    public EditorKit getEditorKitForContentType(String str) {
        if (this.typeHandlers == null) {
            this.typeHandlers = new Hashtable<>(3);
        }
        EditorKit editorKitCreateDefaultEditorKit = this.typeHandlers.get(str);
        if (editorKitCreateDefaultEditorKit == null) {
            editorKitCreateDefaultEditorKit = createEditorKitForContentType(str);
            if (editorKitCreateDefaultEditorKit != null) {
                setEditorKitForContentType(str, editorKitCreateDefaultEditorKit);
            }
        }
        if (editorKitCreateDefaultEditorKit == null) {
            editorKitCreateDefaultEditorKit = createDefaultEditorKit();
        }
        return editorKitCreateDefaultEditorKit;
    }

    public void setEditorKitForContentType(String str, EditorKit editorKit) {
        if (this.typeHandlers == null) {
            this.typeHandlers = new Hashtable<>(3);
        }
        this.typeHandlers.put(str, editorKit);
    }

    @Override // javax.swing.text.JTextComponent
    public void replaceSelection(String str) {
        if (!isEditable()) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
            return;
        }
        EditorKit editorKit = getEditorKit();
        if (editorKit instanceof StyledEditorKit) {
            try {
                Document document = getDocument();
                Caret caret = getCaret();
                boolean zSaveComposedText = saveComposedText(caret.getDot());
                int iMin = Math.min(caret.getDot(), caret.getMark());
                int iMax = Math.max(caret.getDot(), caret.getMark());
                if (document instanceof AbstractDocument) {
                    ((AbstractDocument) document).replace(iMin, iMax - iMin, str, ((StyledEditorKit) editorKit).getInputAttributes());
                } else {
                    if (iMin != iMax) {
                        document.remove(iMin, iMax - iMin);
                    }
                    if (str != null && str.length() > 0) {
                        document.insertString(iMin, str, ((StyledEditorKit) editorKit).getInputAttributes());
                    }
                }
                if (zSaveComposedText) {
                    restoreComposedText();
                }
                return;
            } catch (BadLocationException e2) {
                UIManager.getLookAndFeel().provideErrorFeedback(this);
                return;
            }
        }
        super.replaceSelection(str);
    }

    public static EditorKit createEditorKitForContentType(String str) {
        Class<?> cls;
        Hashtable<String, EditorKit> kitRegisty = getKitRegisty();
        EditorKit editorKit = kitRegisty.get(str);
        if (editorKit == null) {
            String str2 = getKitTypeRegistry().get(str);
            ClassLoader classLoader = getKitLoaderRegistry().get(str);
            try {
                if (classLoader != null) {
                    cls = classLoader.loadClass(str2);
                } else {
                    cls = Class.forName(str2, true, Thread.currentThread().getContextClassLoader());
                }
                editorKit = (EditorKit) cls.newInstance();
                kitRegisty.put(str, editorKit);
            } catch (Throwable th) {
                editorKit = null;
            }
        }
        if (editorKit != null) {
            return (EditorKit) editorKit.clone();
        }
        return null;
    }

    public static void registerEditorKitForContentType(String str, String str2) {
        registerEditorKitForContentType(str, str2, Thread.currentThread().getContextClassLoader());
    }

    public static void registerEditorKitForContentType(String str, String str2, ClassLoader classLoader) {
        getKitTypeRegistry().put(str, str2);
        if (classLoader != null) {
            getKitLoaderRegistry().put(str, classLoader);
        } else {
            getKitLoaderRegistry().remove(str);
        }
        getKitRegisty().remove(str);
    }

    public static String getEditorKitClassNameForContentType(String str) {
        return getKitTypeRegistry().get(str);
    }

    private static Hashtable<String, String> getKitTypeRegistry() {
        loadDefaultKitsIfNecessary();
        return (Hashtable) SwingUtilities.appContextGet(kitTypeRegistryKey);
    }

    private static Hashtable<String, ClassLoader> getKitLoaderRegistry() {
        loadDefaultKitsIfNecessary();
        return (Hashtable) SwingUtilities.appContextGet(kitLoaderRegistryKey);
    }

    private static Hashtable<String, EditorKit> getKitRegisty() {
        Hashtable<String, EditorKit> hashtable = (Hashtable) SwingUtilities.appContextGet(kitRegistryKey);
        if (hashtable == null) {
            hashtable = new Hashtable<>(3);
            SwingUtilities.appContextPut(kitRegistryKey, hashtable);
        }
        return hashtable;
    }

    private static void loadDefaultKitsIfNecessary() {
        if (SwingUtilities.appContextGet(kitTypeRegistryKey) == null) {
            synchronized (defaultEditorKitMap) {
                if (defaultEditorKitMap.size() == 0) {
                    defaultEditorKitMap.put(Clipboard.TEXT_TYPE, "javax.swing.JEditorPane$PlainEditorKit");
                    defaultEditorKitMap.put(Clipboard.HTML_TYPE, "javax.swing.text.html.HTMLEditorKit");
                    defaultEditorKitMap.put(Clipboard.RTF_TYPE, "javax.swing.text.rtf.RTFEditorKit");
                    defaultEditorKitMap.put("application/rtf", "javax.swing.text.rtf.RTFEditorKit");
                }
            }
            SwingUtilities.appContextPut(kitTypeRegistryKey, new Hashtable());
            SwingUtilities.appContextPut(kitLoaderRegistryKey, new Hashtable());
            for (String str : defaultEditorKitMap.keySet()) {
                registerEditorKitForContentType(str, defaultEditorKitMap.get(str));
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        if (unwrappedParent instanceof JViewport) {
            JViewport jViewport = (JViewport) unwrappedParent;
            TextUI ui = getUI();
            int i2 = preferredSize.width;
            int i3 = preferredSize.height;
            if (!getScrollableTracksViewportWidth()) {
                int width = jViewport.getWidth();
                Dimension minimumSize = ui.getMinimumSize(this);
                if (width != 0 && width < minimumSize.width) {
                    i2 = minimumSize.width;
                }
            }
            if (!getScrollableTracksViewportHeight()) {
                int height = jViewport.getHeight();
                Dimension minimumSize2 = ui.getMinimumSize(this);
                if (height != 0 && height < minimumSize2.height) {
                    i3 = minimumSize2.height;
                }
            }
            if (i2 != preferredSize.width || i3 != preferredSize.height) {
                preferredSize = new Dimension(i2, i3);
            }
        }
        return preferredSize;
    }

    @Override // javax.swing.text.JTextComponent
    public void setText(String str) {
        try {
            Document document = getDocument();
            document.remove(0, document.getLength());
            if (str == null || str.equals("")) {
                return;
            }
            getEditorKit().read(new StringReader(str), document, 0);
        } catch (IOException e2) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        } catch (BadLocationException e3) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        }
    }

    @Override // javax.swing.text.JTextComponent
    public String getText() {
        String string;
        try {
            StringWriter stringWriter = new StringWriter();
            write(stringWriter);
            string = stringWriter.toString();
        } catch (IOException e2) {
            string = null;
        }
        return string;
    }

    @Override // javax.swing.text.JTextComponent, javax.swing.Scrollable
    public boolean getScrollableTracksViewportWidth() {
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        if (unwrappedParent instanceof JViewport) {
            JViewport jViewport = (JViewport) unwrappedParent;
            TextUI ui = getUI();
            int width = jViewport.getWidth();
            Dimension minimumSize = ui.getMinimumSize(this);
            Dimension maximumSize = ui.getMaximumSize(this);
            if (width >= minimumSize.width && width <= maximumSize.width) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // javax.swing.text.JTextComponent, javax.swing.Scrollable
    public boolean getScrollableTracksViewportHeight() {
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        if (unwrappedParent instanceof JViewport) {
            JViewport jViewport = (JViewport) unwrappedParent;
            TextUI ui = getUI();
            int height = jViewport.getHeight();
            if (height >= ui.getMinimumSize(this).height && height <= ui.getMaximumSize(this).height) {
                return true;
            }
            return false;
        }
        return false;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.text.JTextComponent, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",kit=" + (this.kit != null ? this.kit.toString() : "") + ",typeHandlers=" + (this.typeHandlers != null ? this.typeHandlers.toString() : "");
    }

    @Override // javax.swing.text.JTextComponent, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (getEditorKit() instanceof HTMLEditorKit) {
            if (this.accessibleContext == null || this.accessibleContext.getClass() != AccessibleJEditorPaneHTML.class) {
                this.accessibleContext = new AccessibleJEditorPaneHTML();
            }
        } else if (this.accessibleContext == null || this.accessibleContext.getClass() != AccessibleJEditorPane.class) {
            this.accessibleContext = new AccessibleJEditorPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JEditorPane$AccessibleJEditorPane.class */
    protected class AccessibleJEditorPane extends JTextComponent.AccessibleJTextComponent {
        protected AccessibleJEditorPane() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            String contentType = this.accessibleDescription;
            if (contentType == null) {
                contentType = (String) JEditorPane.this.getClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY);
            }
            if (contentType == null) {
                contentType = JEditorPane.this.getContentType();
            }
            return contentType;
        }

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            accessibleStateSet.add(AccessibleState.MULTI_LINE);
            return accessibleStateSet;
        }
    }

    /* loaded from: rt.jar:javax/swing/JEditorPane$AccessibleJEditorPaneHTML.class */
    protected class AccessibleJEditorPaneHTML extends AccessibleJEditorPane {
        private AccessibleContext accessibleContext;

        @Override // javax.swing.text.JTextComponent.AccessibleJTextComponent, javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            return JEditorPane.this.new JEditorPaneAccessibleHypertextSupport();
        }

        protected AccessibleJEditorPaneHTML() {
            super();
            this.accessibleContext = ((HTMLEditorKit) JEditorPane.this.getEditorKit()).getAccessibleContext();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            if (this.accessibleContext != null) {
                return this.accessibleContext.getAccessibleChildrenCount();
            }
            return 0;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (this.accessibleContext != null) {
                return this.accessibleContext.getAccessibleChild(i2);
            }
            return null;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            if (this.accessibleContext != null && point != null) {
                try {
                    AccessibleComponent accessibleComponent = this.accessibleContext.getAccessibleComponent();
                    if (accessibleComponent != null) {
                        return accessibleComponent.getAccessibleAt(point);
                    }
                    return null;
                } catch (IllegalComponentStateException e2) {
                    return null;
                }
            }
            return null;
        }
    }

    /* loaded from: rt.jar:javax/swing/JEditorPane$JEditorPaneAccessibleHypertextSupport.class */
    protected class JEditorPaneAccessibleHypertextSupport extends AccessibleJEditorPane implements AccessibleHypertext {
        LinkVector hyperlinks;
        boolean linksValid;

        /* loaded from: rt.jar:javax/swing/JEditorPane$JEditorPaneAccessibleHypertextSupport$HTMLLink.class */
        public class HTMLLink extends AccessibleHyperlink {
            Element element;

            public HTMLLink(Element element) {
                this.element = element;
            }

            @Override // javax.accessibility.AccessibleHyperlink
            public boolean isValid() {
                return JEditorPaneAccessibleHypertextSupport.this.linksValid;
            }

            @Override // javax.accessibility.AccessibleHyperlink, javax.accessibility.AccessibleAction
            public int getAccessibleActionCount() {
                return 1;
            }

            @Override // javax.accessibility.AccessibleHyperlink, javax.accessibility.AccessibleAction
            public boolean doAccessibleAction(int i2) {
                URL url;
                if (i2 == 0 && isValid() && (url = (URL) getAccessibleActionObject(i2)) != null) {
                    JEditorPane.this.fireHyperlinkUpdate(new HyperlinkEvent(JEditorPane.this, HyperlinkEvent.EventType.ACTIVATED, url));
                    return true;
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleHyperlink, javax.accessibility.AccessibleAction
            public String getAccessibleActionDescription(int i2) {
                Document document;
                if (i2 == 0 && isValid() && (document = JEditorPane.this.getDocument()) != null) {
                    try {
                        return document.getText(getStartIndex(), getEndIndex() - getStartIndex());
                    } catch (BadLocationException e2) {
                        return null;
                    }
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleHyperlink
            public Object getAccessibleActionObject(int i2) {
                URL url;
                if (i2 == 0 && isValid()) {
                    AttributeSet attributeSet = (AttributeSet) this.element.getAttributes().getAttribute(HTML.Tag.f12846A);
                    String str = attributeSet != null ? (String) attributeSet.getAttribute(HTML.Attribute.HREF) : null;
                    if (str != null) {
                        try {
                            url = new URL(JEditorPane.this.getPage(), str);
                        } catch (MalformedURLException e2) {
                            url = null;
                        }
                        return url;
                    }
                    return null;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleHyperlink
            public Object getAccessibleActionAnchor(int i2) {
                return getAccessibleActionDescription(i2);
            }

            @Override // javax.accessibility.AccessibleHyperlink
            public int getStartIndex() {
                return this.element.getStartOffset();
            }

            @Override // javax.accessibility.AccessibleHyperlink
            public int getEndIndex() {
                return this.element.getEndOffset();
            }
        }

        /* loaded from: rt.jar:javax/swing/JEditorPane$JEditorPaneAccessibleHypertextSupport$LinkVector.class */
        private class LinkVector extends Vector<HTMLLink> {
            private LinkVector() {
            }

            public int baseElementIndex(Element element) {
                for (int i2 = 0; i2 < this.elementCount; i2++) {
                    if (elementAt(i2).element == element) {
                        return i2;
                    }
                }
                return -1;
            }
        }

        private void buildLinkTable() {
            this.hyperlinks.removeAllElements();
            Document document = JEditorPane.this.getDocument();
            if (document != null) {
                ElementIterator elementIterator = new ElementIterator(document);
                while (true) {
                    Element next = elementIterator.next();
                    if (next == null) {
                        break;
                    }
                    if (next.isLeaf()) {
                        AttributeSet attributeSet = (AttributeSet) next.getAttributes().getAttribute(HTML.Tag.f12846A);
                        if ((attributeSet != null ? (String) attributeSet.getAttribute(HTML.Attribute.HREF) : null) != null) {
                            this.hyperlinks.addElement(new HTMLLink(next));
                        }
                    }
                }
            }
            this.linksValid = true;
        }

        public JEditorPaneAccessibleHypertextSupport() {
            super();
            this.linksValid = false;
            this.hyperlinks = new LinkVector();
            Document document = JEditorPane.this.getDocument();
            if (document != null) {
                document.addDocumentListener(new DocumentListener() { // from class: javax.swing.JEditorPane.JEditorPaneAccessibleHypertextSupport.1
                    @Override // javax.swing.event.DocumentListener
                    public void changedUpdate(DocumentEvent documentEvent) {
                        JEditorPaneAccessibleHypertextSupport.this.linksValid = false;
                    }

                    @Override // javax.swing.event.DocumentListener
                    public void insertUpdate(DocumentEvent documentEvent) {
                        JEditorPaneAccessibleHypertextSupport.this.linksValid = false;
                    }

                    @Override // javax.swing.event.DocumentListener
                    public void removeUpdate(DocumentEvent documentEvent) {
                        JEditorPaneAccessibleHypertextSupport.this.linksValid = false;
                    }
                });
            }
        }

        @Override // javax.accessibility.AccessibleHypertext
        public int getLinkCount() {
            if (!this.linksValid) {
                buildLinkTable();
            }
            return this.hyperlinks.size();
        }

        @Override // javax.accessibility.AccessibleHypertext
        public int getLinkIndex(int i2) {
            if (!this.linksValid) {
                buildLinkTable();
            }
            Element element = null;
            Document document = JEditorPane.this.getDocument();
            if (document != null) {
                Element defaultRootElement = document.getDefaultRootElement();
                while (true) {
                    element = defaultRootElement;
                    if (element.isLeaf()) {
                        break;
                    }
                    defaultRootElement = element.getElement(element.getElementIndex(i2));
                }
            }
            return this.hyperlinks.baseElementIndex(element);
        }

        @Override // javax.accessibility.AccessibleHypertext
        public AccessibleHyperlink getLink(int i2) {
            if (!this.linksValid) {
                buildLinkTable();
            }
            if (i2 >= 0 && i2 < this.hyperlinks.size()) {
                return this.hyperlinks.elementAt(i2);
            }
            return null;
        }

        public String getLinkText(int i2) {
            Document document;
            if (!this.linksValid) {
                buildLinkTable();
            }
            Element element = (Element) this.hyperlinks.elementAt(i2);
            if (element != null && (document = JEditorPane.this.getDocument()) != null) {
                try {
                    return document.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
                } catch (BadLocationException e2) {
                    return null;
                }
            }
            return null;
        }
    }

    /* loaded from: rt.jar:javax/swing/JEditorPane$PlainEditorKit.class */
    static class PlainEditorKit extends DefaultEditorKit implements ViewFactory {
        PlainEditorKit() {
        }

        @Override // javax.swing.text.DefaultEditorKit, javax.swing.text.EditorKit
        public ViewFactory getViewFactory() {
            return this;
        }

        @Override // javax.swing.text.ViewFactory
        public View create(Element element) {
            Object property = element.getDocument().getProperty("i18n");
            if (property != null && property.equals(Boolean.TRUE)) {
                return createI18N(element);
            }
            return new WrappedPlainView(element);
        }

        View createI18N(Element element) {
            String name = element.getName();
            if (name != null) {
                if (name.equals(AbstractDocument.ContentElementName)) {
                    return new PlainParagraph(element);
                }
                if (name.equals(AbstractDocument.ParagraphElementName)) {
                    return new BoxView(element, 1);
                }
                return null;
            }
            return null;
        }

        /* loaded from: rt.jar:javax/swing/JEditorPane$PlainEditorKit$PlainParagraph.class */
        static class PlainParagraph extends ParagraphView {
            PlainParagraph(Element element) {
                super(element);
                this.layoutPool = new LogicalView(element);
                this.layoutPool.setParent(this);
            }

            @Override // javax.swing.text.ParagraphView
            protected void setPropertiesFromAttributes() {
                Container container = getContainer();
                if (container != null && !container.getComponentOrientation().isLeftToRight()) {
                    setJustification(2);
                } else {
                    setJustification(0);
                }
            }

            @Override // javax.swing.text.ParagraphView, javax.swing.text.FlowView
            public int getFlowSpan(int i2) {
                Container container = getContainer();
                if ((container instanceof JTextArea) && !((JTextArea) container).getLineWrap()) {
                    return Integer.MAX_VALUE;
                }
                return super.getFlowSpan(i2);
            }

            @Override // javax.swing.text.ParagraphView, javax.swing.text.FlowView, javax.swing.text.BoxView
            protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
                SizeRequirements sizeRequirementsCalculateMinorAxisRequirements = super.calculateMinorAxisRequirements(i2, sizeRequirements);
                Container container = getContainer();
                if ((container instanceof JTextArea) && !((JTextArea) container).getLineWrap()) {
                    sizeRequirementsCalculateMinorAxisRequirements.minimum = sizeRequirementsCalculateMinorAxisRequirements.preferred;
                }
                return sizeRequirementsCalculateMinorAxisRequirements;
            }

            /* loaded from: rt.jar:javax/swing/JEditorPane$PlainEditorKit$PlainParagraph$LogicalView.class */
            static class LogicalView extends CompositeView {
                LogicalView(Element element) {
                    super(element);
                }

                @Override // javax.swing.text.CompositeView
                protected int getViewIndexAtPosition(int i2) {
                    Element element = getElement();
                    if (element.getElementCount() > 0) {
                        return element.getElementIndex(i2);
                    }
                    return 0;
                }

                @Override // javax.swing.text.View
                protected boolean updateChildren(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, ViewFactory viewFactory) {
                    return false;
                }

                @Override // javax.swing.text.CompositeView
                protected void loadChildren(ViewFactory viewFactory) {
                    Element element = getElement();
                    if (element.getElementCount() > 0) {
                        super.loadChildren(viewFactory);
                    } else {
                        append(new GlyphView(element));
                    }
                }

                @Override // javax.swing.text.View
                public float getPreferredSpan(int i2) {
                    if (getViewCount() != 1) {
                        throw new Error("One child view is assumed.");
                    }
                    return getView(0).getPreferredSpan(i2);
                }

                @Override // javax.swing.text.View
                protected void forwardUpdateToView(View view, DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
                    view.setParent(this);
                    super.forwardUpdateToView(view, documentEvent, shape, viewFactory);
                }

                @Override // javax.swing.text.View
                public void paint(Graphics graphics, Shape shape) {
                }

                @Override // javax.swing.text.CompositeView
                protected boolean isBefore(int i2, int i3, Rectangle rectangle) {
                    return false;
                }

                @Override // javax.swing.text.CompositeView
                protected boolean isAfter(int i2, int i3, Rectangle rectangle) {
                    return false;
                }

                @Override // javax.swing.text.CompositeView
                protected View getViewAtPoint(int i2, int i3, Rectangle rectangle) {
                    return null;
                }

                @Override // javax.swing.text.CompositeView
                protected void childAllocation(int i2, Rectangle rectangle) {
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JEditorPane$HeaderParser.class */
    static class HeaderParser {
        String raw;
        String[][] tab = new String[10][2];

        public HeaderParser(String str) {
            this.raw = str;
            parse();
        }

        private void parse() {
            if (this.raw != null) {
                this.raw = this.raw.trim();
                char[] charArray = this.raw.toCharArray();
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                boolean z2 = true;
                boolean z3 = false;
                int length = charArray.length;
                while (i3 < length) {
                    char c2 = charArray[i3];
                    if (c2 == '=') {
                        this.tab[i4][0] = new String(charArray, i2, i3 - i2).toLowerCase();
                        z2 = false;
                        i3++;
                        i2 = i3;
                    } else if (c2 == '\"') {
                        if (z3) {
                            int i5 = i4;
                            i4++;
                            this.tab[i5][1] = new String(charArray, i2, i3 - i2);
                            z3 = false;
                            while (true) {
                                i3++;
                                if (i3 >= length || (charArray[i3] != ' ' && charArray[i3] != ',')) {
                                    break;
                                }
                            }
                            z2 = true;
                            i2 = i3;
                        } else {
                            z3 = true;
                            i3++;
                            i2 = i3;
                        }
                    } else if (c2 == ' ' || c2 == ',') {
                        if (z3) {
                            i3++;
                        } else {
                            if (z2) {
                                int i6 = i4;
                                i4++;
                                this.tab[i6][0] = new String(charArray, i2, i3 - i2).toLowerCase();
                            } else {
                                int i7 = i4;
                                i4++;
                                this.tab[i7][1] = new String(charArray, i2, i3 - i2);
                            }
                            while (i3 < length && (charArray[i3] == ' ' || charArray[i3] == ',')) {
                                i3++;
                            }
                            z2 = true;
                            i2 = i3;
                        }
                    } else {
                        i3++;
                    }
                }
                int i8 = i3 - 1;
                if (i8 > i2) {
                    if (!z2) {
                        if (charArray[i8] == '\"') {
                            int i9 = i4;
                            int i10 = i4 + 1;
                            this.tab[i9][1] = new String(charArray, i2, i8 - i2);
                            return;
                        } else {
                            int i11 = i4;
                            int i12 = i4 + 1;
                            this.tab[i11][1] = new String(charArray, i2, (i8 - i2) + 1);
                            return;
                        }
                    }
                    this.tab[i4][0] = new String(charArray, i2, (i8 - i2) + 1).toLowerCase();
                    return;
                }
                if (i8 == i2) {
                    if (!z2) {
                        if (charArray[i8] == '\"') {
                            int i13 = i4;
                            int i14 = i4 + 1;
                            this.tab[i13][1] = String.valueOf(charArray[i8 - 1]);
                            return;
                        } else {
                            int i15 = i4;
                            int i16 = i4 + 1;
                            this.tab[i15][1] = String.valueOf(charArray[i8]);
                            return;
                        }
                    }
                    this.tab[i4][0] = String.valueOf(charArray[i8]).toLowerCase();
                }
            }
        }

        public String findKey(int i2) {
            if (i2 < 0 || i2 > 10) {
                return null;
            }
            return this.tab[i2][0];
        }

        public String findValue(int i2) {
            if (i2 < 0 || i2 > 10) {
                return null;
            }
            return this.tab[i2][1];
        }

        public String findValue(String str) {
            return findValue(str, null);
        }

        public String findValue(String str, String str2) {
            if (str == null) {
                return str2;
            }
            String lowerCase = str.toLowerCase();
            for (int i2 = 0; i2 < 10; i2++) {
                if (this.tab[i2][0] == null) {
                    return str2;
                }
                if (lowerCase.equals(this.tab[i2][0])) {
                    return this.tab[i2][1];
                }
            }
            return str2;
        }

        public int findInt(String str, int i2) {
            try {
                return Integer.parseInt(findValue(str, String.valueOf(i2)));
            } catch (Throwable th) {
                return i2;
            }
        }
    }
}
