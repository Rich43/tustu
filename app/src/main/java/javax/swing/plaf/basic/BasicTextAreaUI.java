package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.CompositeView;
import javax.swing.text.Element;
import javax.swing.text.GlyphView;
import javax.swing.text.JTextComponent;
import javax.swing.text.ParagraphView;
import javax.swing.text.PlainDocument;
import javax.swing.text.PlainView;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.WrappedPlainView;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextAreaUI.class */
public class BasicTextAreaUI extends BasicTextUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicTextAreaUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "TextArea";
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void installDefaults() {
        super.installDefaults();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        super.propertyChange(propertyChangeEvent);
        if (propertyChangeEvent.getPropertyName().equals("lineWrap") || propertyChangeEvent.getPropertyName().equals("wrapStyleWord") || propertyChangeEvent.getPropertyName().equals(PlainDocument.tabSizeAttribute)) {
            modelChanged();
        } else if (JTree.EDITABLE_PROPERTY.equals(propertyChangeEvent.getPropertyName())) {
            updateFocusTraversalKeys();
        }
    }

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return super.getPreferredSize(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return super.getMinimumSize(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI, javax.swing.text.ViewFactory
    public View create(Element element) {
        View plainView;
        Object property = element.getDocument().getProperty("i18n");
        if (property != null && property.equals(Boolean.TRUE)) {
            return createI18N(element);
        }
        JTextComponent component = getComponent();
        if (component instanceof JTextArea) {
            JTextArea jTextArea = (JTextArea) component;
            if (jTextArea.getLineWrap()) {
                plainView = new WrappedPlainView(element, jTextArea.getWrapStyleWord());
            } else {
                plainView = new PlainView(element);
            }
            return plainView;
        }
        return null;
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

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        Object property = ((JTextComponent) jComponent).getDocument().getProperty("i18n");
        Insets insets = jComponent.getInsets();
        if (Boolean.TRUE.equals(property)) {
            View rootView = getRootView((JTextComponent) jComponent);
            if (rootView.getViewCount() > 0) {
                int i4 = (i3 - insets.top) - insets.bottom;
                int i5 = insets.top;
                int baseline = BasicHTML.getBaseline(rootView.getView(0), (i2 - insets.left) - insets.right, i4);
                if (baseline < 0) {
                    return -1;
                }
                return i5 + baseline;
            }
            return -1;
        }
        return insets.top + jComponent.getFontMetrics(jComponent.getFont()).getAscent();
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextAreaUI$PlainParagraph.class */
    static class PlainParagraph extends ParagraphView {
        PlainParagraph(Element element) {
            super(element);
            this.layoutPool = new LogicalView(element);
            this.layoutPool.setParent(this);
        }

        @Override // javax.swing.text.FlowView, javax.swing.text.CompositeView, javax.swing.text.View
        public void setParent(View view) {
            super.setParent(view);
            if (view != null) {
                setPropertiesFromAttributes();
            }
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
            if (container instanceof JTextArea) {
                if (!((JTextArea) container).getLineWrap()) {
                    sizeRequirementsCalculateMinorAxisRequirements.minimum = sizeRequirementsCalculateMinorAxisRequirements.preferred;
                } else {
                    sizeRequirementsCalculateMinorAxisRequirements.minimum = 0;
                    sizeRequirementsCalculateMinorAxisRequirements.preferred = getWidth();
                    if (sizeRequirementsCalculateMinorAxisRequirements.preferred == Integer.MAX_VALUE) {
                        sizeRequirementsCalculateMinorAxisRequirements.preferred = 100;
                    }
                }
            }
            return sizeRequirementsCalculateMinorAxisRequirements;
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public void setSize(float f2, float f3) {
            if (((int) f2) != getWidth()) {
                preferenceChanged(null, true, true);
            }
            super.setSize(f2, f3);
        }

        /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextAreaUI$PlainParagraph$LogicalView.class */
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
