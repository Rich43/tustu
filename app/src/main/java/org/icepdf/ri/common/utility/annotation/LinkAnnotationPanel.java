package org.icepdf.ri.common.utility.annotation;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.annotations.LinkAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/LinkAnnotationPanel.class */
public class LinkAnnotationPanel extends AnnotationPanelAdapter implements ItemListener {
    private static final int DEFAULT_HIGHLIGHT_STYLE = 1;
    private JComboBox highlightStyleBox;
    private Name highlightStyle;

    public LinkAnnotationPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(1, 2, 5, 2));
        setFocusable(true);
        createGUI();
        setEnabled(false);
        revalidate();
    }

    @Override // org.icepdf.ri.common.utility.annotation.AnnotationProperties
    public void setAnnotationComponent(AnnotationComponent newAnnotation) {
        if (newAnnotation == null || newAnnotation.getAnnotation() == null || !(newAnnotation.getAnnotation() instanceof LinkAnnotation)) {
            setEnabled(false);
            return;
        }
        this.currentAnnotationComponent = newAnnotation;
        LinkAnnotation linkAnnotation = (LinkAnnotation) this.currentAnnotationComponent.getAnnotation();
        this.highlightStyle = linkAnnotation.getHighlightMode();
        applySelectedValue(this.highlightStyleBox, this.highlightStyle);
        enableAppearanceInputComponents(linkAnnotation.getBorderType());
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        ValueLabelItem item = (ValueLabelItem) e2.getItem();
        if (e2.getStateChange() == 1) {
            if (e2.getSource() == this.highlightStyleBox) {
                this.highlightStyle = (Name) item.getValue();
            }
            updateCurrentAnnotation();
            this.currentAnnotationComponent.repaint();
        }
    }

    private void createGUI() {
        ValueLabelItem[] highlightStyleList = {new ValueLabelItem(LinkAnnotation.HIGHLIGHT_NONE, this.messageBundle.getString("viewer.utilityPane.annotation.link.none")), new ValueLabelItem(LinkAnnotation.HIGHLIGHT_INVERT, this.messageBundle.getString("viewer.utilityPane.annotation.link.invert")), new ValueLabelItem(LinkAnnotation.HIGHLIGHT_OUTLINE, this.messageBundle.getString("viewer.utilityPane.annotation.link.outline")), new ValueLabelItem(LinkAnnotation.HIGHLIGHT_PUSH, this.messageBundle.getString("viewer.utilityPane.annotation.link.push"))};
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.link.appearance.title"), 1, 0));
        this.highlightStyleBox = new JComboBox(highlightStyleList);
        this.highlightStyleBox.setSelectedIndex(1);
        this.highlightStyleBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.link.highlightType")));
        add(this.highlightStyleBox);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        safeEnable(this.highlightStyleBox, enabled);
    }

    private void enableAppearanceInputComponents(int linkType) {
        if (linkType == 0) {
            safeEnable(this.highlightStyleBox, true);
        } else {
            safeEnable(this.highlightStyleBox, true);
        }
    }

    protected boolean safeEnable(JComponent comp, boolean enabled) {
        if (comp != null) {
            comp.setEnabled(enabled);
            return true;
        }
        return false;
    }

    private void applySelectedValue(JComboBox comboBox, Object value) {
        comboBox.removeItemListener(this);
        int i2 = 0;
        while (true) {
            if (i2 >= comboBox.getItemCount()) {
                break;
            }
            ValueLabelItem currentItem = (ValueLabelItem) comboBox.getItemAt(i2);
            if (!currentItem.getValue().equals(value)) {
                i2++;
            } else {
                comboBox.setSelectedIndex(i2);
                break;
            }
        }
        comboBox.addItemListener(this);
    }
}
