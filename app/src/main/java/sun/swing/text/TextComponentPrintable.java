package sun.swing.text;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.JEditorPane;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import sun.font.FontDesignMetrics;
import sun.swing.text.html.FrameEditorPaneTag;

/* loaded from: rt.jar:sun/swing/text/TextComponentPrintable.class */
public class TextComponentPrintable implements CountingPrintable {
    private static final int LIST_SIZE = 1000;
    private final JTextComponent textComponentToPrint;
    private final JTextComponent printShell;
    private final MessageFormat headerFormat;
    private final MessageFormat footerFormat;
    private static final float HEADER_FONT_SIZE = 18.0f;
    private static final float FOOTER_FONT_SIZE = 12.0f;
    private final Font headerFont;
    private final Font footerFont;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean isLayouted = false;
    private final AtomicReference<FontRenderContext> frc = new AtomicReference<>(null);
    private boolean needReadLock = false;
    private final List<IntegerSegment> pagesMetrics = Collections.synchronizedList(new ArrayList());
    private final List<IntegerSegment> rowsMetrics = new ArrayList(1000);

    static {
        $assertionsDisabled = !TextComponentPrintable.class.desiredAssertionStatus();
    }

    public static Printable getPrintable(JTextComponent jTextComponent, MessageFormat messageFormat, MessageFormat messageFormat2) {
        if ((jTextComponent instanceof JEditorPane) && isFrameSetDocument(jTextComponent.getDocument())) {
            List<JEditorPane> frames = getFrames((JEditorPane) jTextComponent);
            ArrayList arrayList = new ArrayList();
            Iterator<JEditorPane> it = frames.iterator();
            while (it.hasNext()) {
                arrayList.add((CountingPrintable) getPrintable(it.next(), messageFormat, messageFormat2));
            }
            return new CompoundPrintable(arrayList);
        }
        return new TextComponentPrintable(jTextComponent, messageFormat, messageFormat2);
    }

    private static boolean isFrameSetDocument(Document document) {
        boolean z2 = false;
        if ((document instanceof HTMLDocument) && ((HTMLDocument) document).getIterator(HTML.Tag.FRAME).isValid()) {
            z2 = true;
        }
        return z2;
    }

    private static List<JEditorPane> getFrames(JEditorPane jEditorPane) {
        ArrayList arrayList = new ArrayList();
        getFrames(jEditorPane, arrayList);
        if (arrayList.size() == 0) {
            createFrames(jEditorPane);
            getFrames(jEditorPane, arrayList);
        }
        return arrayList;
    }

    private static void getFrames(Container container, List<JEditorPane> list) {
        for (Component component : container.getComponents()) {
            if ((component instanceof FrameEditorPaneTag) && (component instanceof JEditorPane)) {
                list.add((JEditorPane) component);
            } else if (component instanceof Container) {
                getFrames((Container) component, list);
            }
        }
    }

    private static void createFrames(final JEditorPane jEditorPane) {
        Runnable runnable = new Runnable() { // from class: sun.swing.text.TextComponentPrintable.1
            @Override // java.lang.Runnable
            public void run() {
                CellRendererPane cellRendererPane = new CellRendererPane();
                cellRendererPane.add(jEditorPane);
                cellRendererPane.setSize(500, 500);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (Exception e2) {
            if (e2 instanceof RuntimeException) {
                throw ((RuntimeException) e2);
            }
            throw new RuntimeException(e2);
        }
    }

    private TextComponentPrintable(JTextComponent jTextComponent, MessageFormat messageFormat, MessageFormat messageFormat2) {
        this.textComponentToPrint = jTextComponent;
        this.headerFormat = messageFormat;
        this.footerFormat = messageFormat2;
        this.headerFont = jTextComponent.getFont().deriveFont(1, HEADER_FONT_SIZE);
        this.footerFont = jTextComponent.getFont().deriveFont(0, 12.0f);
        this.printShell = createPrintShell(jTextComponent);
    }

    private JTextComponent createPrintShell(final JTextComponent jTextComponent) {
        if (SwingUtilities.isEventDispatchThread()) {
            return createPrintShellOnEDT(jTextComponent);
        }
        FutureTask futureTask = new FutureTask(new Callable<JTextComponent>() { // from class: sun.swing.text.TextComponentPrintable.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public JTextComponent call() throws Exception {
                return TextComponentPrintable.this.createPrintShellOnEDT(jTextComponent);
            }
        });
        SwingUtilities.invokeLater(futureTask);
        try {
            return (JTextComponent) futureTask.get();
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        } catch (ExecutionException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new AssertionError(cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JTextComponent createPrintShellOnEDT(final JTextComponent jTextComponent) {
        if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
            throw new AssertionError();
        }
        JTextComponent jTextComponent2 = null;
        if (jTextComponent instanceof JPasswordField) {
            jTextComponent2 = new JPasswordField() { // from class: sun.swing.text.TextComponentPrintable.3
                {
                    setEchoChar(((JPasswordField) jTextComponent).getEchoChar());
                    setHorizontalAlignment(((JTextField) jTextComponent).getHorizontalAlignment());
                }

                @Override // javax.swing.JComponent, java.awt.Component
                public FontMetrics getFontMetrics(Font font) {
                    if (TextComponentPrintable.this.frc.get() != null) {
                        return FontDesignMetrics.getMetrics(font, (FontRenderContext) TextComponentPrintable.this.frc.get());
                    }
                    return super.getFontMetrics(font);
                }
            };
        } else if (jTextComponent instanceof JTextField) {
            jTextComponent2 = new JTextField() { // from class: sun.swing.text.TextComponentPrintable.4
                {
                    setHorizontalAlignment(((JTextField) jTextComponent).getHorizontalAlignment());
                }

                @Override // javax.swing.JComponent, java.awt.Component
                public FontMetrics getFontMetrics(Font font) {
                    if (TextComponentPrintable.this.frc.get() != null) {
                        return FontDesignMetrics.getMetrics(font, (FontRenderContext) TextComponentPrintable.this.frc.get());
                    }
                    return super.getFontMetrics(font);
                }
            };
        } else if (jTextComponent instanceof JTextArea) {
            jTextComponent2 = new JTextArea() { // from class: sun.swing.text.TextComponentPrintable.5
                {
                    JTextArea jTextArea = (JTextArea) jTextComponent;
                    setLineWrap(jTextArea.getLineWrap());
                    setWrapStyleWord(jTextArea.getWrapStyleWord());
                    setTabSize(jTextArea.getTabSize());
                }

                @Override // javax.swing.JComponent, java.awt.Component
                public FontMetrics getFontMetrics(Font font) {
                    if (TextComponentPrintable.this.frc.get() != null) {
                        return FontDesignMetrics.getMetrics(font, (FontRenderContext) TextComponentPrintable.this.frc.get());
                    }
                    return super.getFontMetrics(font);
                }
            };
        } else if (jTextComponent instanceof JTextPane) {
            jTextComponent2 = new JTextPane() { // from class: sun.swing.text.TextComponentPrintable.6
                @Override // javax.swing.JComponent, java.awt.Component
                public FontMetrics getFontMetrics(Font font) {
                    if (TextComponentPrintable.this.frc.get() != null) {
                        return FontDesignMetrics.getMetrics(font, (FontRenderContext) TextComponentPrintable.this.frc.get());
                    }
                    return super.getFontMetrics(font);
                }

                @Override // javax.swing.JEditorPane
                public EditorKit getEditorKit() {
                    if (getDocument() == jTextComponent.getDocument()) {
                        return ((JTextPane) jTextComponent).getEditorKit();
                    }
                    return super.getEditorKit();
                }
            };
        } else if (jTextComponent instanceof JEditorPane) {
            jTextComponent2 = new JEditorPane() { // from class: sun.swing.text.TextComponentPrintable.7
                @Override // javax.swing.JComponent, java.awt.Component
                public FontMetrics getFontMetrics(Font font) {
                    if (TextComponentPrintable.this.frc.get() != null) {
                        return FontDesignMetrics.getMetrics(font, (FontRenderContext) TextComponentPrintable.this.frc.get());
                    }
                    return super.getFontMetrics(font);
                }

                @Override // javax.swing.JEditorPane
                public EditorKit getEditorKit() {
                    if (getDocument() == jTextComponent.getDocument()) {
                        return ((JEditorPane) jTextComponent).getEditorKit();
                    }
                    return super.getEditorKit();
                }
            };
        }
        jTextComponent2.setBorder(null);
        jTextComponent2.setOpaque(jTextComponent.isOpaque());
        jTextComponent2.setEditable(jTextComponent.isEditable());
        jTextComponent2.setEnabled(jTextComponent.isEnabled());
        jTextComponent2.setFont(jTextComponent.getFont());
        jTextComponent2.setBackground(jTextComponent.getBackground());
        jTextComponent2.setForeground(jTextComponent.getForeground());
        jTextComponent2.setComponentOrientation(jTextComponent.getComponentOrientation());
        if (jTextComponent2 instanceof JEditorPane) {
            jTextComponent2.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, jTextComponent.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES));
            jTextComponent2.putClientProperty(JEditorPane.W3C_LENGTH_UNITS, jTextComponent.getClientProperty(JEditorPane.W3C_LENGTH_UNITS));
            jTextComponent2.putClientProperty("charset", jTextComponent.getClientProperty("charset"));
        }
        jTextComponent2.setDocument(jTextComponent.getDocument());
        return jTextComponent2;
    }

    @Override // sun.swing.text.CountingPrintable
    public int getNumberOfPages() {
        return this.pagesMetrics.size();
    }

    @Override // java.awt.print.Printable
    public int print(final Graphics graphics, final PageFormat pageFormat, final int i2) throws PrinterException {
        int iPrintOnEDT;
        if (!this.isLayouted) {
            if (graphics instanceof Graphics2D) {
                this.frc.set(((Graphics2D) graphics).getFontRenderContext());
            }
            layout((int) Math.floor(pageFormat.getImageableWidth()));
            calculateRowsMetrics();
        }
        if (!SwingUtilities.isEventDispatchThread()) {
            FutureTask futureTask = new FutureTask(new Callable<Integer>() { // from class: sun.swing.text.TextComponentPrintable.8
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    return Integer.valueOf(TextComponentPrintable.this.printOnEDT(graphics, pageFormat, i2));
                }
            });
            SwingUtilities.invokeLater(futureTask);
            try {
                iPrintOnEDT = ((Integer) futureTask.get()).intValue();
            } catch (InterruptedException e2) {
                throw new RuntimeException(e2);
            } catch (ExecutionException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof PrinterException) {
                    throw ((PrinterException) cause);
                }
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                throw new RuntimeException(cause);
            }
        } else {
            iPrintOnEDT = printOnEDT(graphics, pageFormat, i2);
        }
        return iPrintOnEDT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int printOnEDT(Graphics graphics, PageFormat pageFormat, int i2) throws PrinterException {
        if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
            throw new AssertionError();
        }
        Border borderCreateEmptyBorder = BorderFactory.createEmptyBorder();
        if (this.headerFormat != null || this.footerFormat != null) {
            Object[] objArr = {Integer.valueOf(i2 + 1)};
            if (this.headerFormat != null) {
                borderCreateEmptyBorder = new TitledBorder(borderCreateEmptyBorder, this.headerFormat.format(objArr), 2, 1, this.headerFont, this.printShell.getForeground());
            }
            if (this.footerFormat != null) {
                borderCreateEmptyBorder = new TitledBorder(borderCreateEmptyBorder, this.footerFormat.format(objArr), 2, 6, this.footerFont, this.printShell.getForeground());
            }
        }
        Insets borderInsets = borderCreateEmptyBorder.getBorderInsets(this.printShell);
        updatePagesMetrics(i2, (((int) Math.floor(pageFormat.getImageableHeight())) - borderInsets.top) - borderInsets.bottom);
        if (this.pagesMetrics.size() <= i2) {
            return 1;
        }
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        borderCreateEmptyBorder.paintBorder(this.printShell, graphics2D, 0, 0, (int) Math.floor(pageFormat.getImageableWidth()), (int) Math.floor(pageFormat.getImageableHeight()));
        graphics2D.translate(0, borderInsets.top);
        graphics2D.clip(new Rectangle(0, 0, (int) pageFormat.getWidth(), (this.pagesMetrics.get(i2).end - this.pagesMetrics.get(i2).start) + 1));
        int imageableWidth = 0;
        if (ComponentOrientation.RIGHT_TO_LEFT == this.printShell.getComponentOrientation()) {
            imageableWidth = ((int) pageFormat.getImageableWidth()) - this.printShell.getWidth();
        }
        graphics2D.translate(imageableWidth, -this.pagesMetrics.get(i2).start);
        this.printShell.print(graphics2D);
        graphics2D.dispose();
        return 0;
    }

    private void releaseReadLock() {
        if (!$assertionsDisabled && SwingUtilities.isEventDispatchThread()) {
            throw new AssertionError();
        }
        Document document = this.textComponentToPrint.getDocument();
        if (document instanceof AbstractDocument) {
            try {
                ((AbstractDocument) document).readUnlock();
                this.needReadLock = true;
            } catch (Error e2) {
            }
        }
    }

    private void acquireReadLock() {
        if (!$assertionsDisabled && SwingUtilities.isEventDispatchThread()) {
            throw new AssertionError();
        }
        if (this.needReadLock) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() { // from class: sun.swing.text.TextComponentPrintable.9
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                });
            } catch (InterruptedException e2) {
            } catch (InvocationTargetException e3) {
            }
            ((AbstractDocument) this.textComponentToPrint.getDocument()).readLock();
            this.needReadLock = false;
        }
    }

    private void layout(final int i2) {
        if (!SwingUtilities.isEventDispatchThread()) {
            FutureTask futureTask = new FutureTask(new Callable<Object>() { // from class: sun.swing.text.TextComponentPrintable.10
                @Override // java.util.concurrent.Callable
                public Object call() throws Exception {
                    TextComponentPrintable.this.layoutOnEDT(i2);
                    return null;
                }
            });
            releaseReadLock();
            SwingUtilities.invokeLater(futureTask);
            try {
                try {
                    futureTask.get();
                    acquireReadLock();
                } catch (InterruptedException e2) {
                    throw new RuntimeException(e2);
                } catch (ExecutionException e3) {
                    Throwable cause = e3.getCause();
                    if (cause instanceof RuntimeException) {
                        throw ((RuntimeException) cause);
                    }
                    if (cause instanceof Error) {
                        throw ((Error) cause);
                    }
                    throw new RuntimeException(cause);
                }
            } catch (Throwable th) {
                acquireReadLock();
                throw th;
            }
        } else {
            layoutOnEDT(i2);
        }
        this.isLayouted = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void layoutOnEDT(int i2) {
        if (!$assertionsDisabled && !SwingUtilities.isEventDispatchThread()) {
            throw new AssertionError();
        }
        CellRendererPane cellRendererPane = new CellRendererPane();
        JViewport jViewport = new JViewport();
        jViewport.setBorder(null);
        Dimension dimension = new Dimension(i2, 2147482647);
        if (this.printShell instanceof JTextField) {
            dimension = new Dimension(dimension.width, this.printShell.getPreferredSize().height);
        }
        this.printShell.setSize(dimension);
        jViewport.setComponentOrientation(this.printShell.getComponentOrientation());
        jViewport.setSize(dimension);
        jViewport.add(this.printShell);
        cellRendererPane.add(jViewport);
    }

    private void updatePagesMetrics(int i2, int i3) {
        while (i2 >= this.pagesMetrics.size() && !this.rowsMetrics.isEmpty()) {
            int size = this.pagesMetrics.size() - 1;
            int i4 = size >= 0 ? this.pagesMetrics.get(size).end + 1 : 0;
            int i5 = 0;
            while (i5 < this.rowsMetrics.size() && (this.rowsMetrics.get(i5).end - i4) + 1 <= i3) {
                i5++;
            }
            if (i5 == 0) {
                this.pagesMetrics.add(new IntegerSegment(i4, (i4 + i3) - 1));
            } else {
                int i6 = i5 - 1;
                this.pagesMetrics.add(new IntegerSegment(i4, this.rowsMetrics.get(i6).end));
                for (int i7 = 0; i7 <= i6; i7++) {
                    this.rowsMetrics.remove(0);
                }
            }
        }
    }

    private void calculateRowsMetrics() {
        int length = this.printShell.getDocument().getLength();
        ArrayList<IntegerSegment> arrayList = new ArrayList(1000);
        int i2 = -1;
        int i3 = -1;
        for (int i4 = 0; i4 < length; i4++) {
            try {
                Rectangle rectangleModelToView = this.printShell.modelToView(i4);
                if (rectangleModelToView != null) {
                    int y2 = (int) rectangleModelToView.getY();
                    int height = (int) rectangleModelToView.getHeight();
                    if (height != 0 && (y2 != i2 || height != i3)) {
                        i2 = y2;
                        i3 = height;
                        arrayList.add(new IntegerSegment(y2, (y2 + height) - 1));
                    }
                }
            } catch (BadLocationException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
        }
        Collections.sort(arrayList);
        int i5 = Integer.MIN_VALUE;
        int i6 = Integer.MIN_VALUE;
        for (IntegerSegment integerSegment : arrayList) {
            if (i6 < integerSegment.start) {
                if (i6 != Integer.MIN_VALUE) {
                    this.rowsMetrics.add(new IntegerSegment(i5, i6));
                }
                i5 = integerSegment.start;
                i6 = integerSegment.end;
            } else {
                i6 = integerSegment.end;
            }
        }
        if (i6 != Integer.MIN_VALUE) {
            this.rowsMetrics.add(new IntegerSegment(i5, i6));
        }
    }

    /* loaded from: rt.jar:sun/swing/text/TextComponentPrintable$IntegerSegment.class */
    private static class IntegerSegment implements Comparable<IntegerSegment> {
        final int start;
        final int end;

        IntegerSegment(int i2, int i3) {
            this.start = i2;
            this.end = i3;
        }

        @Override // java.lang.Comparable
        public int compareTo(IntegerSegment integerSegment) {
            int i2 = this.start - integerSegment.start;
            return i2 != 0 ? i2 : this.end - integerSegment.end;
        }

        public boolean equals(Object obj) {
            return (obj instanceof IntegerSegment) && compareTo((IntegerSegment) obj) == 0;
        }

        public int hashCode() {
            return (37 * ((37 * 17) + this.start)) + this.end;
        }

        public String toString() {
            return "IntegerSegment [" + this.start + ", " + this.end + "]";
        }
    }
}
