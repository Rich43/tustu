package javax.swing.text;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.plaf.TextUI;
import javax.swing.text.Highlighter;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/DefaultHighlighter.class */
public class DefaultHighlighter extends LayeredHighlighter {
    private JTextComponent component;
    private static final Highlighter.Highlight[] noHighlights = new Highlighter.Highlight[0];
    public static final LayeredHighlighter.LayerPainter DefaultPainter = new DefaultHighlightPainter(null);
    private Vector<HighlightInfo> highlights = new Vector<>();
    private SafeDamager safeDamager = new SafeDamager();
    private boolean drawsLayeredHighlights = true;

    @Override // javax.swing.text.Highlighter
    public void paint(Graphics graphics) {
        int size = this.highlights.size();
        int i2 = 0;
        while (i2 < size) {
            if (!(this.highlights.elementAt(i2) instanceof LayeredHighlightInfo)) {
                Rectangle bounds = this.component.getBounds();
                Insets insets = this.component.getInsets();
                bounds.f12372x = insets.left;
                bounds.f12373y = insets.top;
                bounds.width -= insets.left + insets.right;
                bounds.height -= insets.top + insets.bottom;
                while (i2 < size) {
                    HighlightInfo highlightInfoElementAt = this.highlights.elementAt(i2);
                    if (!(highlightInfoElementAt instanceof LayeredHighlightInfo)) {
                        highlightInfoElementAt.getPainter().paint(graphics, highlightInfoElementAt.getStartOffset(), highlightInfoElementAt.getEndOffset(), bounds, this.component);
                    }
                    i2++;
                }
            }
            i2++;
        }
    }

    @Override // javax.swing.text.Highlighter
    public void install(JTextComponent jTextComponent) {
        this.component = jTextComponent;
        removeAllHighlights();
    }

    @Override // javax.swing.text.Highlighter
    public void deinstall(JTextComponent jTextComponent) {
        this.component = null;
    }

    @Override // javax.swing.text.Highlighter
    public Object addHighlight(int i2, int i3, Highlighter.HighlightPainter highlightPainter) throws BadLocationException {
        if (i2 < 0) {
            throw new BadLocationException("Invalid start offset", i2);
        }
        if (i3 < i2) {
            throw new BadLocationException("Invalid end offset", i3);
        }
        Document document = this.component.getDocument();
        HighlightInfo layeredHighlightInfo = (getDrawsLayeredHighlights() && (highlightPainter instanceof LayeredHighlighter.LayerPainter)) ? new LayeredHighlightInfo() : new HighlightInfo();
        layeredHighlightInfo.painter = highlightPainter;
        layeredHighlightInfo.p0 = document.createPosition(i2);
        layeredHighlightInfo.p1 = document.createPosition(i3);
        this.highlights.addElement(layeredHighlightInfo);
        safeDamageRange(i2, i3);
        return layeredHighlightInfo;
    }

    @Override // javax.swing.text.Highlighter
    public void removeHighlight(Object obj) {
        if (obj instanceof LayeredHighlightInfo) {
            LayeredHighlightInfo layeredHighlightInfo = (LayeredHighlightInfo) obj;
            if (layeredHighlightInfo.width > 0 && layeredHighlightInfo.height > 0) {
                this.component.repaint(layeredHighlightInfo.f12834x, layeredHighlightInfo.f12835y, layeredHighlightInfo.width, layeredHighlightInfo.height);
            }
        } else {
            HighlightInfo highlightInfo = (HighlightInfo) obj;
            safeDamageRange(highlightInfo.p0, highlightInfo.p1);
        }
        this.highlights.removeElement(obj);
    }

    @Override // javax.swing.text.Highlighter
    public void removeAllHighlights() {
        int size;
        TextUI ui = this.component.getUI();
        if (getDrawsLayeredHighlights()) {
            int size2 = this.highlights.size();
            if (size2 != 0) {
                int iMin = 0;
                int iMin2 = 0;
                int iMax = 0;
                int iMax2 = 0;
                int iMin3 = -1;
                int iMax3 = -1;
                for (int i2 = 0; i2 < size2; i2++) {
                    HighlightInfo highlightInfoElementAt = this.highlights.elementAt(i2);
                    if (highlightInfoElementAt instanceof LayeredHighlightInfo) {
                        LayeredHighlightInfo layeredHighlightInfo = (LayeredHighlightInfo) highlightInfoElementAt;
                        iMin = Math.min(iMin, layeredHighlightInfo.f12834x);
                        iMin2 = Math.min(iMin2, layeredHighlightInfo.f12835y);
                        iMax = Math.max(iMax, layeredHighlightInfo.f12834x + layeredHighlightInfo.width);
                        iMax2 = Math.max(iMax2, layeredHighlightInfo.f12835y + layeredHighlightInfo.height);
                    } else if (iMin3 == -1) {
                        iMin3 = highlightInfoElementAt.p0.getOffset();
                        iMax3 = highlightInfoElementAt.p1.getOffset();
                    } else {
                        iMin3 = Math.min(iMin3, highlightInfoElementAt.p0.getOffset());
                        iMax3 = Math.max(iMax3, highlightInfoElementAt.p1.getOffset());
                    }
                }
                if (iMin != iMax && iMin2 != iMax2) {
                    this.component.repaint(iMin, iMin2, iMax - iMin, iMax2 - iMin2);
                }
                if (iMin3 != -1) {
                    try {
                        safeDamageRange(iMin3, iMax3);
                    } catch (BadLocationException e2) {
                    }
                }
                this.highlights.removeAllElements();
                return;
            }
            return;
        }
        if (ui != null && (size = this.highlights.size()) != 0) {
            int iMin4 = Integer.MAX_VALUE;
            int iMax4 = 0;
            for (int i3 = 0; i3 < size; i3++) {
                HighlightInfo highlightInfoElementAt2 = this.highlights.elementAt(i3);
                iMin4 = Math.min(iMin4, highlightInfoElementAt2.p0.getOffset());
                iMax4 = Math.max(iMax4, highlightInfoElementAt2.p1.getOffset());
            }
            try {
                safeDamageRange(iMin4, iMax4);
            } catch (BadLocationException e3) {
            }
            this.highlights.removeAllElements();
        }
    }

    @Override // javax.swing.text.Highlighter
    public void changeHighlight(Object obj, int i2, int i3) throws BadLocationException {
        if (i2 < 0) {
            throw new BadLocationException("Invalid beginning of the range", i2);
        }
        if (i3 < i2) {
            throw new BadLocationException("Invalid end of the range", i3);
        }
        Document document = this.component.getDocument();
        if (obj instanceof LayeredHighlightInfo) {
            LayeredHighlightInfo layeredHighlightInfo = (LayeredHighlightInfo) obj;
            if (layeredHighlightInfo.width > 0 && layeredHighlightInfo.height > 0) {
                this.component.repaint(layeredHighlightInfo.f12834x, layeredHighlightInfo.f12835y, layeredHighlightInfo.width, layeredHighlightInfo.height);
            }
            layeredHighlightInfo.height = 0;
            layeredHighlightInfo.width = 0;
            layeredHighlightInfo.p0 = document.createPosition(i2);
            layeredHighlightInfo.p1 = document.createPosition(i3);
            safeDamageRange(Math.min(i2, i3), Math.max(i2, i3));
            return;
        }
        HighlightInfo highlightInfo = (HighlightInfo) obj;
        int offset = highlightInfo.p0.getOffset();
        int offset2 = highlightInfo.p1.getOffset();
        if (i2 == offset) {
            safeDamageRange(Math.min(offset2, i3), Math.max(offset2, i3));
        } else if (i3 == offset2) {
            safeDamageRange(Math.min(i2, offset), Math.max(i2, offset));
        } else {
            safeDamageRange(offset, offset2);
            safeDamageRange(i2, i3);
        }
        highlightInfo.p0 = document.createPosition(i2);
        highlightInfo.p1 = document.createPosition(i3);
    }

    @Override // javax.swing.text.Highlighter
    public Highlighter.Highlight[] getHighlights() {
        int size = this.highlights.size();
        if (size == 0) {
            return noHighlights;
        }
        Highlighter.Highlight[] highlightArr = new Highlighter.Highlight[size];
        this.highlights.copyInto(highlightArr);
        return highlightArr;
    }

    @Override // javax.swing.text.LayeredHighlighter
    public void paintLayeredHighlights(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent, View view) {
        for (int size = this.highlights.size() - 1; size >= 0; size--) {
            HighlightInfo highlightInfoElementAt = this.highlights.elementAt(size);
            if (highlightInfoElementAt instanceof LayeredHighlightInfo) {
                LayeredHighlightInfo layeredHighlightInfo = (LayeredHighlightInfo) highlightInfoElementAt;
                int startOffset = layeredHighlightInfo.getStartOffset();
                int endOffset = layeredHighlightInfo.getEndOffset();
                if ((i2 < startOffset && i3 > startOffset) || (i2 >= startOffset && i2 < endOffset)) {
                    layeredHighlightInfo.paintLayeredHighlights(graphics, i2, i3, shape, jTextComponent, view);
                }
            }
        }
    }

    private void safeDamageRange(Position position, Position position2) {
        this.safeDamager.damageRange(position, position2);
    }

    private void safeDamageRange(int i2, int i3) throws BadLocationException {
        Document document = this.component.getDocument();
        safeDamageRange(document.createPosition(i2), document.createPosition(i3));
    }

    public void setDrawsLayeredHighlights(boolean z2) {
        this.drawsLayeredHighlights = z2;
    }

    public boolean getDrawsLayeredHighlights() {
        return this.drawsLayeredHighlights;
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultHighlighter$DefaultHighlightPainter.class */
    public static class DefaultHighlightPainter extends LayeredHighlighter.LayerPainter {
        private Color color;

        public DefaultHighlightPainter(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return this.color;
        }

        @Override // javax.swing.text.Highlighter.HighlightPainter
        public void paint(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent) {
            Rectangle bounds = shape.getBounds();
            try {
                TextUI ui = jTextComponent.getUI();
                Rectangle rectangleModelToView = ui.modelToView(jTextComponent, i2);
                Rectangle rectangleModelToView2 = ui.modelToView(jTextComponent, i3);
                Color color = getColor();
                if (color == null) {
                    graphics.setColor(jTextComponent.getSelectionColor());
                } else {
                    graphics.setColor(color);
                }
                if (rectangleModelToView.f12373y == rectangleModelToView2.f12373y) {
                    Rectangle rectangleUnion = rectangleModelToView.union(rectangleModelToView2);
                    graphics.fillRect(rectangleUnion.f12372x, rectangleUnion.f12373y, rectangleUnion.width, rectangleUnion.height);
                } else {
                    graphics.fillRect(rectangleModelToView.f12372x, rectangleModelToView.f12373y, (bounds.f12372x + bounds.width) - rectangleModelToView.f12372x, rectangleModelToView.height);
                    if (rectangleModelToView.f12373y + rectangleModelToView.height != rectangleModelToView2.f12373y) {
                        graphics.fillRect(bounds.f12372x, rectangleModelToView.f12373y + rectangleModelToView.height, bounds.width, rectangleModelToView2.f12373y - (rectangleModelToView.f12373y + rectangleModelToView.height));
                    }
                    graphics.fillRect(bounds.f12372x, rectangleModelToView2.f12373y, rectangleModelToView2.f12372x - bounds.f12372x, rectangleModelToView2.height);
                }
            } catch (BadLocationException e2) {
            }
        }

        @Override // javax.swing.text.LayeredHighlighter.LayerPainter
        public Shape paintLayer(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent, View view) {
            Rectangle bounds;
            Color color = getColor();
            if (color == null) {
                graphics.setColor(jTextComponent.getSelectionColor());
            } else {
                graphics.setColor(color);
            }
            if (i2 == view.getStartOffset() && i3 == view.getEndOffset()) {
                if (shape instanceof Rectangle) {
                    bounds = (Rectangle) shape;
                } else {
                    bounds = shape.getBounds();
                }
            } else {
                try {
                    Shape shapeModelToView = view.modelToView(i2, Position.Bias.Forward, i3, Position.Bias.Backward, shape);
                    bounds = shapeModelToView instanceof Rectangle ? (Rectangle) shapeModelToView : shapeModelToView.getBounds();
                } catch (BadLocationException e2) {
                    bounds = null;
                }
            }
            if (bounds != null) {
                bounds.width = Math.max(bounds.width, 1);
                graphics.fillRect(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
            }
            return bounds;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultHighlighter$HighlightInfo.class */
    class HighlightInfo implements Highlighter.Highlight {
        Position p0;
        Position p1;
        Highlighter.HighlightPainter painter;

        HighlightInfo() {
        }

        @Override // javax.swing.text.Highlighter.Highlight
        public int getStartOffset() {
            return this.p0.getOffset();
        }

        @Override // javax.swing.text.Highlighter.Highlight
        public int getEndOffset() {
            return this.p1.getOffset();
        }

        @Override // javax.swing.text.Highlighter.Highlight
        public Highlighter.HighlightPainter getPainter() {
            return this.painter;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultHighlighter$LayeredHighlightInfo.class */
    class LayeredHighlightInfo extends HighlightInfo {

        /* renamed from: x, reason: collision with root package name */
        int f12834x;

        /* renamed from: y, reason: collision with root package name */
        int f12835y;
        int width;
        int height;

        LayeredHighlightInfo() {
            super();
        }

        void union(Shape shape) {
            Rectangle bounds;
            if (shape == null) {
                return;
            }
            if (shape instanceof Rectangle) {
                bounds = (Rectangle) shape;
            } else {
                bounds = shape.getBounds();
            }
            if (this.width == 0 || this.height == 0) {
                this.f12834x = bounds.f12372x;
                this.f12835y = bounds.f12373y;
                this.width = bounds.width;
                this.height = bounds.height;
                return;
            }
            this.width = Math.max(this.f12834x + this.width, bounds.f12372x + bounds.width);
            this.height = Math.max(this.f12835y + this.height, bounds.f12373y + bounds.height);
            this.f12834x = Math.min(this.f12834x, bounds.f12372x);
            this.width -= this.f12834x;
            this.f12835y = Math.min(this.f12835y, bounds.f12373y);
            this.height -= this.f12835y;
        }

        void paintLayeredHighlights(Graphics graphics, int i2, int i3, Shape shape, JTextComponent jTextComponent, View view) {
            int startOffset = getStartOffset();
            int endOffset = getEndOffset();
            union(((LayeredHighlighter.LayerPainter) this.painter).paintLayer(graphics, Math.max(startOffset, i2), Math.min(endOffset, i3), shape, jTextComponent, view));
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultHighlighter$SafeDamager.class */
    class SafeDamager implements Runnable {
        private Vector<Position> p0 = new Vector<>(10);
        private Vector<Position> p1 = new Vector<>(10);
        private Document lastDoc = null;

        SafeDamager() {
        }

        @Override // java.lang.Runnable
        public synchronized void run() {
            TextUI ui;
            if (DefaultHighlighter.this.component != null && (ui = DefaultHighlighter.this.component.getUI()) != null && this.lastDoc == DefaultHighlighter.this.component.getDocument()) {
                int size = this.p0.size();
                for (int i2 = 0; i2 < size; i2++) {
                    ui.damageRange(DefaultHighlighter.this.component, this.p0.get(i2).getOffset(), this.p1.get(i2).getOffset());
                }
            }
            this.p0.clear();
            this.p1.clear();
            this.lastDoc = null;
        }

        public synchronized void damageRange(Position position, Position position2) {
            if (DefaultHighlighter.this.component == null) {
                this.p0.clear();
                this.lastDoc = null;
                return;
            }
            boolean zIsEmpty = this.p0.isEmpty();
            Document document = DefaultHighlighter.this.component.getDocument();
            if (document != this.lastDoc) {
                if (!this.p0.isEmpty()) {
                    this.p0.clear();
                    this.p1.clear();
                }
                this.lastDoc = document;
            }
            this.p0.add(position);
            this.p1.add(position2);
            if (zIsEmpty) {
                SwingUtilities.invokeLater(this);
            }
        }
    }
}
