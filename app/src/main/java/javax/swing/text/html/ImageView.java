package javax.swing.text.html;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.GlyphView;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/ImageView.class */
public class ImageView extends View {
    private static boolean sIsInc = false;
    private static int sIncRate = 100;
    private static final String PENDING_IMAGE = "html.pendingImage";
    private static final String MISSING_IMAGE = "html.missingImage";
    private static final String IMAGE_CACHE_PROPERTY = "imageCache";
    private static final int DEFAULT_WIDTH = 38;
    private static final int DEFAULT_HEIGHT = 38;
    private static final int DEFAULT_BORDER = 2;
    private static final int LOADING_FLAG = 1;
    private static final int LINK_FLAG = 2;
    private static final int WIDTH_FLAG = 4;
    private static final int HEIGHT_FLAG = 8;
    private static final int RELOAD_FLAG = 16;
    private static final int RELOAD_IMAGE_FLAG = 32;
    private static final int SYNC_LOAD_FLAG = 64;
    private AttributeSet attr;
    private Image image;
    private Image disabledImage;
    private int width;
    private int height;
    private int state;
    private Container container;
    private Rectangle fBounds;
    private Color borderColor;
    private short borderSize;
    private short leftInset;
    private short rightInset;
    private short topInset;
    private short bottomInset;
    private ImageObserver imageObserver;
    private View altView;
    private float vAlign;

    public ImageView(Element element) {
        super(element);
        this.fBounds = new Rectangle();
        this.imageObserver = new ImageHandler();
        this.state = 48;
    }

    public String getAltText() {
        return (String) getElement().getAttributes().getAttribute(HTML.Attribute.ALT);
    }

    public URL getImageURL() {
        String str = (String) getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
        if (str == null) {
            return null;
        }
        try {
            return new URL(((HTMLDocument) getDocument()).getBase(), str);
        } catch (MalformedURLException e2) {
            return null;
        }
    }

    public Icon getNoImageIcon() {
        return (Icon) UIManager.getLookAndFeelDefaults().get(MISSING_IMAGE);
    }

    public Icon getLoadingImageIcon() {
        return (Icon) UIManager.getLookAndFeelDefaults().get(PENDING_IMAGE);
    }

    public Image getImage() {
        sync();
        return this.image;
    }

    private Image getImage(boolean z2) {
        Image image = getImage();
        if (!z2) {
            if (this.disabledImage == null) {
                this.disabledImage = GrayFilter.createDisabledImage(image);
            }
            image = this.disabledImage;
        }
        return image;
    }

    public void setLoadsSynchronously(boolean z2) {
        synchronized (this) {
            if (z2) {
                this.state |= 64;
            } else {
                this.state = (this.state | 64) ^ 64;
            }
        }
    }

    public boolean getLoadsSynchronously() {
        return (this.state & 64) != 0;
    }

    protected StyleSheet getStyleSheet() {
        return ((HTMLDocument) getDocument()).getStyleSheet();
    }

    @Override // javax.swing.text.View
    public AttributeSet getAttributes() {
        sync();
        return this.attr;
    }

    @Override // javax.swing.text.View
    public String getToolTipText(float f2, float f3, Shape shape) {
        return getAltText();
    }

    protected void setPropertiesFromAttributes() {
        this.attr = getStyleSheet().getViewAttributes(this);
        this.borderSize = (short) getIntAttr(HTML.Attribute.BORDER, isLink() ? 2 : 0);
        short intAttr = (short) (getIntAttr(HTML.Attribute.HSPACE, 0) + this.borderSize);
        this.rightInset = intAttr;
        this.leftInset = intAttr;
        short intAttr2 = (short) (getIntAttr(HTML.Attribute.VSPACE, 0) + this.borderSize);
        this.bottomInset = intAttr2;
        this.topInset = intAttr2;
        this.borderColor = ((StyledDocument) getDocument()).getForeground(getAttributes());
        AttributeSet attributes = getElement().getAttributes();
        Object attribute = attributes.getAttribute(HTML.Attribute.ALIGN);
        this.vAlign = 1.0f;
        if (attribute != null) {
            String string = attribute.toString();
            if (JSplitPane.TOP.equals(string)) {
                this.vAlign = 0.0f;
            } else if ("middle".equals(string)) {
                this.vAlign = 0.5f;
            }
        }
        AttributeSet attributeSet = (AttributeSet) attributes.getAttribute(HTML.Tag.f12846A);
        if (attributeSet != null && attributeSet.isDefined(HTML.Attribute.HREF)) {
            synchronized (this) {
                this.state |= 2;
            }
        } else {
            synchronized (this) {
                this.state = (this.state | 2) ^ 2;
            }
        }
    }

    @Override // javax.swing.text.View
    public void setParent(View view) {
        View parent = getParent();
        super.setParent(view);
        this.container = view != null ? getContainer() : null;
        if (parent != view) {
            synchronized (this) {
                this.state |= 16;
            }
        }
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.changedUpdate(documentEvent, shape, viewFactory);
        synchronized (this) {
            this.state |= 48;
        }
        preferenceChanged(null, true, true);
    }

    @Override // javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        sync();
        Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
        Rectangle clipBounds = graphics.getClipBounds();
        this.fBounds.setBounds(bounds);
        paintHighlights(graphics, shape);
        paintBorder(graphics, bounds);
        if (clipBounds != null) {
            graphics.clipRect(bounds.f12372x + this.leftInset, bounds.f12373y + this.topInset, (bounds.width - this.leftInset) - this.rightInset, (bounds.height - this.topInset) - this.bottomInset);
        }
        Container container = getContainer();
        Image image = getImage(container == null || container.isEnabled());
        if (image != null) {
            if (!hasPixels(image)) {
                Icon loadingImageIcon = getLoadingImageIcon();
                if (loadingImageIcon != null) {
                    loadingImageIcon.paintIcon(container, graphics, bounds.f12372x + this.leftInset, bounds.f12373y + this.topInset);
                }
            } else {
                graphics.drawImage(image, bounds.f12372x + this.leftInset, bounds.f12373y + this.topInset, this.width, this.height, this.imageObserver);
            }
        } else {
            Icon noImageIcon = getNoImageIcon();
            if (noImageIcon != null) {
                noImageIcon.paintIcon(container, graphics, bounds.f12372x + this.leftInset, bounds.f12373y + this.topInset);
            }
            View altView = getAltView();
            if (altView != null && ((this.state & 4) == 0 || this.width > 38)) {
                altView.paint(graphics, new Rectangle(bounds.f12372x + this.leftInset + 38, bounds.f12373y + this.topInset, ((bounds.width - this.leftInset) - this.rightInset) - 38, (bounds.height - this.topInset) - this.bottomInset));
            }
        }
        if (clipBounds != null) {
            graphics.setClip(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
        }
    }

    private void paintHighlights(Graphics graphics, Shape shape) {
        if (this.container instanceof JTextComponent) {
            JTextComponent jTextComponent = (JTextComponent) this.container;
            Highlighter highlighter = jTextComponent.getHighlighter();
            if (highlighter instanceof LayeredHighlighter) {
                ((LayeredHighlighter) highlighter).paintLayeredHighlights(graphics, getStartOffset(), getEndOffset(), shape, jTextComponent, this);
            }
        }
    }

    private void paintBorder(Graphics graphics, Rectangle rectangle) {
        Color color = this.borderColor;
        if ((this.borderSize > 0 || this.image == null) && color != null) {
            int i2 = this.leftInset - this.borderSize;
            int i3 = this.topInset - this.borderSize;
            graphics.setColor(color);
            short s2 = this.image == null ? (short) 1 : this.borderSize;
            for (int i4 = 0; i4 < s2; i4++) {
                graphics.drawRect(rectangle.f12372x + i2 + i4, rectangle.f12373y + i3 + i4, ((((rectangle.width - i4) - i4) - i2) - i2) - 1, ((((rectangle.height - i4) - i4) - i3) - i3) - 1);
            }
        }
    }

    @Override // javax.swing.text.View
    public float getPreferredSpan(int i2) {
        sync();
        if (i2 == 0 && (this.state & 4) == 4) {
            getPreferredSpanFromAltView(i2);
            return this.width + this.leftInset + this.rightInset;
        }
        if (i2 == 1 && (this.state & 8) == 8) {
            getPreferredSpanFromAltView(i2);
            return this.height + this.topInset + this.bottomInset;
        }
        if (getImage() != null) {
            switch (i2) {
                case 0:
                    return this.width + this.leftInset + this.rightInset;
                case 1:
                    return this.height + this.topInset + this.bottomInset;
                default:
                    throw new IllegalArgumentException("Invalid axis: " + i2);
            }
        }
        View altView = getAltView();
        float preferredSpan = 0.0f;
        if (altView != null) {
            preferredSpan = altView.getPreferredSpan(i2);
        }
        switch (i2) {
            case 0:
                return preferredSpan + this.width + this.leftInset + this.rightInset;
            case 1:
                return preferredSpan + this.height + this.topInset + this.bottomInset;
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.View
    public float getAlignment(int i2) {
        switch (i2) {
            case 1:
                return this.vAlign;
            default:
                return super.getAlignment(i2);
        }
    }

    @Override // javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        int startOffset = getStartOffset();
        int endOffset = getEndOffset();
        if (i2 >= startOffset && i2 <= endOffset) {
            Rectangle bounds = shape.getBounds();
            if (i2 == endOffset) {
                bounds.f12372x += bounds.width;
            }
            bounds.width = 0;
            return bounds;
        }
        return null;
    }

    @Override // javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        Rectangle rectangle = (Rectangle) shape;
        if (f2 < rectangle.f12372x + rectangle.width) {
            biasArr[0] = Position.Bias.Forward;
            return getStartOffset();
        }
        biasArr[0] = Position.Bias.Backward;
        return getEndOffset();
    }

    @Override // javax.swing.text.View
    public void setSize(float f2, float f3) {
        View altView;
        sync();
        if (getImage() == null && (altView = getAltView()) != null) {
            altView.setSize(Math.max(0.0f, f2 - ((38 + this.leftInset) + this.rightInset)), Math.max(0.0f, f3 - (this.topInset + this.bottomInset)));
        }
    }

    private boolean isLink() {
        return (this.state & 2) == 2;
    }

    private boolean hasPixels(Image image) {
        return image != null && image.getHeight(this.imageObserver) > 0 && image.getWidth(this.imageObserver) > 0;
    }

    private float getPreferredSpanFromAltView(int i2) {
        View altView;
        if (getImage() == null && (altView = getAltView()) != null) {
            return altView.getPreferredSpan(i2);
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void repaint(long j2) {
        if (this.container != null && this.fBounds != null) {
            this.container.repaint(j2, this.fBounds.f12372x, this.fBounds.f12373y, this.fBounds.width, this.fBounds.height);
        }
    }

    private int getIntAttr(HTML.Attribute attribute, int i2) {
        int iMax;
        AttributeSet attributes = getElement().getAttributes();
        if (attributes.isDefined(attribute)) {
            String str = (String) attributes.getAttribute(attribute);
            if (str == null) {
                iMax = i2;
            } else {
                try {
                    iMax = Math.max(0, Integer.parseInt(str));
                } catch (NumberFormatException e2) {
                    iMax = i2;
                }
            }
            return iMax;
        }
        return i2;
    }

    private void sync() {
        if ((this.state & 32) != 0) {
            refreshImage();
        }
        if ((this.state & 16) != 0) {
            synchronized (this) {
                this.state = (this.state | 16) ^ 16;
            }
            setPropertiesFromAttributes();
        }
    }

    private void refreshImage() {
        synchronized (this) {
            this.state = ((((this.state | 1) | 32) | 4) | 8) ^ 44;
            this.image = null;
            this.height = 0;
            this.width = 0;
        }
        try {
            loadImage();
            updateImageSize();
            synchronized (this) {
                this.state = (this.state | 1) ^ 1;
            }
        } catch (Throwable th) {
            synchronized (this) {
                this.state = (this.state | 1) ^ 1;
                throw th;
            }
        }
    }

    private void loadImage() {
        URL imageURL = getImageURL();
        Image imageCreateImage = null;
        if (imageURL != null) {
            Dictionary dictionary = (Dictionary) getDocument().getProperty(IMAGE_CACHE_PROPERTY);
            if (dictionary != null) {
                imageCreateImage = (Image) dictionary.get(imageURL);
            } else {
                imageCreateImage = Toolkit.getDefaultToolkit().createImage(imageURL);
                if (imageCreateImage != null && getLoadsSynchronously()) {
                    new ImageIcon().setImage(imageCreateImage);
                }
            }
        }
        this.image = imageCreateImage;
    }

    private void updateImageSize() {
        Image image;
        int i2 = 0;
        Image image2 = getImage();
        if (image2 != null) {
            getElement().getAttributes();
            int intAttr = getIntAttr(HTML.Attribute.WIDTH, -1);
            if (intAttr > 0) {
                i2 = 0 | 4;
            }
            int intAttr2 = getIntAttr(HTML.Attribute.HEIGHT, -1);
            if (intAttr2 > 0) {
                i2 |= 8;
            }
            synchronized (this) {
                image = this.image;
            }
            if (intAttr <= 0) {
                intAttr = image.getWidth(this.imageObserver);
                if (intAttr <= 0) {
                    intAttr = 38;
                }
            }
            if (intAttr2 <= 0) {
                intAttr2 = image.getHeight(this.imageObserver);
                if (intAttr2 <= 0) {
                    intAttr2 = 38;
                }
            }
            if (getLoadsSynchronously()) {
                Dimension dimensionAdjustWidthHeight = adjustWidthHeight(intAttr, intAttr2);
                intAttr = dimensionAdjustWidthHeight.width;
                intAttr2 = dimensionAdjustWidthHeight.height;
                i2 |= 12;
            }
            if ((i2 & 12) != 0) {
                Toolkit.getDefaultToolkit().prepareImage(image2, intAttr, intAttr2, this.imageObserver);
            } else {
                Toolkit.getDefaultToolkit().prepareImage(image2, -1, -1, this.imageObserver);
            }
            boolean z2 = false;
            synchronized (this) {
                if (this.image != null) {
                    if ((i2 & 4) == 4 || this.width == 0) {
                        this.width = intAttr;
                    }
                    if ((i2 & 8) == 8 || this.height == 0) {
                        this.height = intAttr2;
                    }
                } else {
                    z2 = true;
                    if ((i2 & 4) == 4) {
                        this.width = intAttr;
                    }
                    if ((i2 & 8) == 8) {
                        this.height = intAttr2;
                    }
                }
                this.state |= i2;
                this.state = (this.state | 1) ^ 1;
            }
            if (z2) {
                updateAltTextView();
                return;
            }
            return;
        }
        this.height = 38;
        this.width = 38;
        updateAltTextView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAltTextView() {
        String altText = getAltText();
        if (altText != null) {
            ImageLabelView imageLabelView = new ImageLabelView(getElement(), altText);
            synchronized (this) {
                this.altView = imageLabelView;
            }
        }
    }

    private View getAltView() {
        View view;
        synchronized (this) {
            view = this.altView;
        }
        if (view != null && view.getParent() == null) {
            view.setParent(getParent());
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void safePreferenceChanged() {
        if (SwingUtilities.isEventDispatchThread()) {
            Document document = getDocument();
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readLock();
            }
            preferenceChanged(null, true, true);
            if (document instanceof AbstractDocument) {
                ((AbstractDocument) document).readUnlock();
                return;
            }
            return;
        }
        SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.html.ImageView.1
            @Override // java.lang.Runnable
            public void run() {
                ImageView.this.safePreferenceChanged();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Dimension adjustWidthHeight(int i2, int i3) {
        Dimension dimension = new Dimension();
        int intAttr = getIntAttr(HTML.Attribute.WIDTH, -1);
        int intAttr2 = getIntAttr(HTML.Attribute.HEIGHT, -1);
        if (intAttr == -1 || intAttr2 == -1) {
            if ((intAttr != -1) ^ (intAttr2 != -1)) {
                if (intAttr <= 0) {
                    i2 = (int) ((intAttr2 / i3) * i2);
                    i3 = intAttr2;
                }
                if (intAttr2 <= 0) {
                    i3 = (int) ((intAttr / i2) * i3);
                    i2 = intAttr;
                }
            }
        } else {
            i2 = intAttr;
            i3 = intAttr2;
        }
        dimension.width = i2;
        dimension.height = i3;
        return dimension;
    }

    /* loaded from: rt.jar:javax/swing/text/html/ImageView$ImageHandler.class */
    private class ImageHandler implements ImageObserver {
        private ImageHandler() {
        }

        @Override // java.awt.image.ImageObserver
        public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
            if ((image != ImageView.this.image && image != ImageView.this.disabledImage) || ImageView.this.image == null || ImageView.this.getParent() == null) {
                return false;
            }
            if ((i2 & 192) != 0) {
                ImageView.this.repaint(0L);
                synchronized (ImageView.this) {
                    if (ImageView.this.image == image) {
                        ImageView.this.image = null;
                        if ((ImageView.this.state & 4) != 4) {
                            ImageView.this.width = 38;
                        }
                        if ((ImageView.this.state & 8) != 8) {
                            ImageView.this.height = 38;
                        }
                    } else {
                        ImageView.this.disabledImage = null;
                    }
                    if ((ImageView.this.state & 1) != 1) {
                        ImageView.this.updateAltTextView();
                        ImageView.this.safePreferenceChanged();
                        return false;
                    }
                    return false;
                }
            }
            if (ImageView.this.image == image) {
                short s2 = 0;
                if ((i2 & 2) != 0 && !ImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.HEIGHT)) {
                    s2 = (short) (0 | 1);
                }
                if ((i2 & 1) != 0 && !ImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.WIDTH)) {
                    s2 = (short) (s2 | 2);
                }
                if ((i2 & 2) != 0 && (i2 & 1) != 0) {
                    Dimension dimensionAdjustWidthHeight = ImageView.this.adjustWidthHeight(i5, i6);
                    i5 = dimensionAdjustWidthHeight.width;
                    i6 = dimensionAdjustWidthHeight.height;
                    s2 = (short) (s2 | 3);
                }
                synchronized (ImageView.this) {
                    if ((s2 & 1) == 1 && (ImageView.this.state & 8) == 0) {
                        ImageView.this.height = i6;
                    }
                    if ((s2 & 2) == 2 && (ImageView.this.state & 4) == 0) {
                        ImageView.this.width = i5;
                    }
                    if ((ImageView.this.state & 1) == 1) {
                        return true;
                    }
                    if (s2 != 0) {
                        ImageView.this.safePreferenceChanged();
                        return true;
                    }
                }
            }
            if ((i2 & 48) != 0) {
                ImageView.this.repaint(0L);
            } else if ((i2 & 8) != 0 && ImageView.sIsInc) {
                ImageView.this.repaint(ImageView.sIncRate);
            }
            return (i2 & 32) == 0;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/ImageView$ImageLabelView.class */
    private class ImageLabelView extends InlineView {
        private Segment segment;
        private Color fg;

        ImageLabelView(Element element, String str) {
            super(element);
            reset(str);
        }

        public void reset(String str) {
            this.segment = new Segment(str.toCharArray(), 0, str.length());
        }

        @Override // javax.swing.text.GlyphView, javax.swing.text.View
        public void paint(Graphics graphics, Shape shape) {
            GlyphView.GlyphPainter glyphPainter = getGlyphPainter();
            if (glyphPainter != null) {
                graphics.setColor(getForeground());
                glyphPainter.paint(this, graphics, shape, getStartOffset(), getEndOffset());
            }
        }

        @Override // javax.swing.text.GlyphView
        public Segment getText(int i2, int i3) {
            if (i2 < 0 || i3 > this.segment.array.length) {
                throw new RuntimeException("ImageLabelView: Stale view");
            }
            this.segment.offset = i2;
            this.segment.count = i3 - i2;
            return this.segment;
        }

        @Override // javax.swing.text.GlyphView, javax.swing.text.View
        public int getStartOffset() {
            return 0;
        }

        @Override // javax.swing.text.GlyphView, javax.swing.text.View
        public int getEndOffset() {
            return this.segment.array.length;
        }

        @Override // javax.swing.text.html.InlineView, javax.swing.text.GlyphView, javax.swing.text.View
        public View breakView(int i2, int i3, float f2, float f3) {
            return this;
        }

        @Override // javax.swing.text.LabelView, javax.swing.text.GlyphView
        public Color getForeground() {
            View parent;
            if (this.fg == null && (parent = getParent()) != null) {
                Document document = getDocument();
                AttributeSet attributes = parent.getAttributes();
                if (attributes != null && (document instanceof StyledDocument)) {
                    this.fg = ((StyledDocument) document).getForeground(attributes);
                }
            }
            return this.fg;
        }
    }
}
