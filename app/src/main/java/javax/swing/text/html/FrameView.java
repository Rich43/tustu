package javax.swing.text.html;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import sun.swing.text.html.FrameEditorPaneTag;

/* loaded from: rt.jar:javax/swing/text/html/FrameView.class */
class FrameView extends ComponentView implements HyperlinkListener {
    JEditorPane htmlPane;
    JScrollPane scroller;
    boolean editable;
    float width;
    float height;
    URL src;
    private boolean createdComponent;

    public FrameView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.ComponentView
    protected Component createComponent() {
        Element element = getElement();
        String str = (String) element.getAttributes().getAttribute(HTML.Attribute.SRC);
        if (str != null && !str.equals("")) {
            try {
                this.src = new URL(((HTMLDocument) element.getDocument()).getBase(), str);
                this.htmlPane = new FrameEditorPane();
                this.htmlPane.addHyperlinkListener(this);
                JEditorPane hostPane = getHostPane();
                boolean zIsAutoFormSubmission = true;
                if (hostPane != null) {
                    this.htmlPane.setEditable(hostPane.isEditable());
                    String str2 = (String) hostPane.getClientProperty("charset");
                    if (str2 != null) {
                        this.htmlPane.putClientProperty("charset", str2);
                    }
                    HTMLEditorKit hTMLEditorKit = (HTMLEditorKit) hostPane.getEditorKit();
                    if (hTMLEditorKit != null) {
                        zIsAutoFormSubmission = hTMLEditorKit.isAutoFormSubmission();
                    }
                }
                this.htmlPane.setPage(this.src);
                HTMLEditorKit hTMLEditorKit2 = (HTMLEditorKit) this.htmlPane.getEditorKit();
                if (hTMLEditorKit2 != null) {
                    hTMLEditorKit2.setAutoFormSubmission(zIsAutoFormSubmission);
                }
                Document document = this.htmlPane.getDocument();
                if (document instanceof HTMLDocument) {
                    ((HTMLDocument) document).setFrameDocumentState(true);
                }
                setMargin();
                createScrollPane();
                setBorder();
            } catch (MalformedURLException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        this.createdComponent = true;
        return this.scroller;
    }

    JEditorPane getHostPane() {
        Container container;
        Container container2 = getContainer();
        while (true) {
            container = container2;
            if (container == null || (container instanceof JEditorPane)) {
                break;
            }
            container2 = container.getParent();
        }
        return (JEditorPane) container;
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public void setParent(View view) {
        if (view != null) {
            this.editable = ((JTextComponent) view.getContainer()).isEditable();
        }
        super.setParent(view);
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Container container = getContainer();
        if (container != null && this.htmlPane != null && this.htmlPane.isEditable() != ((JTextComponent) container).isEditable()) {
            this.editable = ((JTextComponent) container).isEditable();
            this.htmlPane.setEditable(this.editable);
        }
        super.paint(graphics, shape);
    }

    private void setMargin() {
        Insets insets;
        int i2;
        int i3;
        Insets margin = this.htmlPane.getMargin();
        boolean z2 = false;
        AttributeSet attributes = getElement().getAttributes();
        String str = (String) attributes.getAttribute(HTML.Attribute.MARGINWIDTH);
        if (margin != null) {
            insets = new Insets(margin.top, margin.left, margin.right, margin.bottom);
        } else {
            insets = new Insets(0, 0, 0, 0);
        }
        if (str != null && (i3 = Integer.parseInt(str)) > 0) {
            insets.left = i3;
            insets.right = i3;
            z2 = true;
        }
        String str2 = (String) attributes.getAttribute(HTML.Attribute.MARGINHEIGHT);
        if (str2 != null && (i2 = Integer.parseInt(str2)) > 0) {
            insets.top = i2;
            insets.bottom = i2;
            z2 = true;
        }
        if (z2) {
            this.htmlPane.setMargin(insets);
        }
    }

    private void setBorder() {
        String str = (String) getElement().getAttributes().getAttribute(HTML.Attribute.FRAMEBORDER);
        if (str != null) {
            if (str.equals("no") || str.equals("0")) {
                this.scroller.setBorder(null);
            }
        }
    }

    private void createScrollPane() {
        String str = (String) getElement().getAttributes().getAttribute(HTML.Attribute.SCROLLING);
        if (str == null) {
            str = "auto";
        }
        if (!str.equals("no")) {
            if (str.equals("yes")) {
                this.scroller = new JScrollPane(22, 32);
            } else {
                this.scroller = new JScrollPane();
            }
        } else {
            this.scroller = new JScrollPane(21, 31);
        }
        JViewport viewport = this.scroller.getViewport();
        viewport.add(this.htmlPane);
        viewport.setBackingStoreEnabled(true);
        this.scroller.setMinimumSize(new Dimension(5, 5));
        this.scroller.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    JEditorPane getOutermostJEditorPane() {
        FrameSetView frameSetView = null;
        for (View parent = getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof FrameSetView) {
                frameSetView = (FrameSetView) parent;
            }
        }
        if (frameSetView != null) {
            return (JEditorPane) frameSetView.getContainer();
        }
        return null;
    }

    private boolean inNestedFrameSet() {
        return ((FrameSetView) getParent()).getParent() instanceof FrameSetView;
    }

    @Override // javax.swing.event.HyperlinkListener
    public void hyperlinkUpdate(HyperlinkEvent hyperlinkEvent) {
        JEditorPane outermostJEditorPane = getOutermostJEditorPane();
        if (outermostJEditorPane == null) {
            return;
        }
        if (!(hyperlinkEvent instanceof HTMLFrameHyperlinkEvent)) {
            outermostJEditorPane.fireHyperlinkUpdate(hyperlinkEvent);
            return;
        }
        HTMLFrameHyperlinkEvent hTMLFrameHyperlinkEvent = (HTMLFrameHyperlinkEvent) hyperlinkEvent;
        if (hTMLFrameHyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            String target = hTMLFrameHyperlinkEvent.getTarget();
            if (target.equals("_parent") && !inNestedFrameSet()) {
                target = "_top";
            }
            if (hyperlinkEvent instanceof FormSubmitEvent) {
                HTMLEditorKit hTMLEditorKit = (HTMLEditorKit) outermostJEditorPane.getEditorKit();
                if (hTMLEditorKit != null && hTMLEditorKit.isAutoFormSubmission()) {
                    if (target.equals("_top")) {
                        try {
                            movePostData(outermostJEditorPane, target);
                            outermostJEditorPane.setPage(hTMLFrameHyperlinkEvent.getURL());
                            return;
                        } catch (IOException e2) {
                            return;
                        }
                    }
                    ((HTMLDocument) outermostJEditorPane.getDocument()).processHTMLFrameHyperlinkEvent(hTMLFrameHyperlinkEvent);
                    return;
                }
                outermostJEditorPane.fireHyperlinkUpdate(hyperlinkEvent);
                return;
            }
            if (target.equals("_top")) {
                try {
                    outermostJEditorPane.setPage(hTMLFrameHyperlinkEvent.getURL());
                } catch (IOException e3) {
                }
            }
            if (!outermostJEditorPane.isEditable()) {
                outermostJEditorPane.fireHyperlinkUpdate(new HTMLFrameHyperlinkEvent(outermostJEditorPane, hTMLFrameHyperlinkEvent.getEventType(), hTMLFrameHyperlinkEvent.getURL(), hTMLFrameHyperlinkEvent.getDescription(), getElement(), hTMLFrameHyperlinkEvent.getInputEvent(), target));
            }
        }
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        Element element = getElement();
        AttributeSet attributes = element.getAttributes();
        URL url = this.src;
        String str = (String) attributes.getAttribute(HTML.Attribute.SRC);
        URL base = ((HTMLDocument) element.getDocument()).getBase();
        try {
            if (!this.createdComponent) {
                return;
            }
            Object objMovePostData = movePostData(this.htmlPane, null);
            this.src = new URL(base, str);
            if (url.equals(this.src) && this.src.getRef() == null && objMovePostData == null) {
                return;
            }
            this.htmlPane.setPage(this.src);
            Document document = this.htmlPane.getDocument();
            if (document instanceof HTMLDocument) {
                ((HTMLDocument) document).setFrameDocumentState(true);
            }
        } catch (MalformedURLException e2) {
        } catch (IOException e3) {
        }
    }

    private Object movePostData(JEditorPane jEditorPane, String str) {
        Object property = null;
        JEditorPane outermostJEditorPane = getOutermostJEditorPane();
        if (outermostJEditorPane != null) {
            if (str == null) {
                str = (String) getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
            }
            if (str != null) {
                String str2 = "javax.swing.JEditorPane.postdata." + str;
                Document document = outermostJEditorPane.getDocument();
                property = document.getProperty(str2);
                if (property != null) {
                    jEditorPane.getDocument().putProperty("javax.swing.JEditorPane.postdata", property);
                    document.putProperty(str2, null);
                }
            }
        }
        return property;
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public float getMinimumSpan(int i2) {
        return 5.0f;
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public float getMaximumSpan(int i2) {
        return 2.1474836E9f;
    }

    /* loaded from: rt.jar:javax/swing/text/html/FrameView$FrameEditorPane.class */
    class FrameEditorPane extends JEditorPane implements FrameEditorPaneTag {
        FrameEditorPane() {
        }

        @Override // javax.swing.JEditorPane
        public EditorKit getEditorKitForContentType(String str) {
            EditorKit editorKitForContentType = super.getEditorKitForContentType(str);
            JEditorPane outermostJEditorPane = FrameView.this.getOutermostJEditorPane();
            if (outermostJEditorPane != null) {
                EditorKit editorKitForContentType2 = outermostJEditorPane.getEditorKitForContentType(str);
                if (!editorKitForContentType.getClass().equals(editorKitForContentType2.getClass())) {
                    editorKitForContentType = (EditorKit) editorKitForContentType2.clone();
                    setEditorKitForContentType(str, editorKitForContentType);
                }
            }
            return editorKitForContentType;
        }

        FrameView getFrameView() {
            return FrameView.this;
        }
    }
}
