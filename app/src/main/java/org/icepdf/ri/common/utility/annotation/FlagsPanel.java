package org.icepdf.ri.common.utility.annotation;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/FlagsPanel.class */
public class FlagsPanel extends AnnotationPanelAdapter implements ItemListener {
    private JComboBox readOnlyComboBox;
    private JComboBox noRotateComboBox;
    private JComboBox noZoomComboBox;
    private JComboBox printableComboBox;

    public FlagsPanel(SwingController controller) {
        super(controller);
        setLayout(new GridLayout(4, 2, 5, 2));
        setFocusable(true);
        createGUI();
        setEnabled(false);
        revalidate();
    }

    @Override // org.icepdf.ri.common.utility.annotation.AnnotationProperties
    public void setAnnotationComponent(AnnotationComponent annotationComponent) {
        if (annotationComponent == null || annotationComponent.getAnnotation() == null) {
            setEnabled(false);
            return;
        }
        this.currentAnnotationComponent = annotationComponent;
        Annotation annotation = this.currentAnnotationComponent.getAnnotation();
        this.noRotateComboBox.setSelectedIndex(annotation.getFlagNoRotate() ? 0 : 1);
        this.noZoomComboBox.setSelectedIndex(annotation.getFlagNoZoom() ? 0 : 1);
        this.readOnlyComboBox.setSelectedIndex(annotation.getFlagReadOnly() ? 0 : 1);
        this.printableComboBox.setSelectedIndex(annotation.getFlagPrint() ? 0 : 1);
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent e2) {
        Object source = e2.getItemSelectable();
        if (source == this.noRotateComboBox) {
            this.currentAnnotationComponent.getAnnotation().setFlag(16, this.noRotateComboBox.getSelectedIndex() == 0);
            return;
        }
        if (source == this.noZoomComboBox) {
            this.currentAnnotationComponent.getAnnotation().setFlag(8, this.noZoomComboBox.getSelectedIndex() == 0);
        } else if (source == this.readOnlyComboBox) {
            this.currentAnnotationComponent.getAnnotation().setFlag(64, this.readOnlyComboBox.getSelectedIndex() == 0);
        } else if (source == this.printableComboBox) {
            this.currentAnnotationComponent.getAnnotation().setFlag(4, this.printableComboBox.getSelectedIndex() == 0);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.noRotateComboBox.setEnabled(enabled);
        this.noZoomComboBox.setEnabled(enabled);
        this.readOnlyComboBox.setEnabled(enabled);
        this.printableComboBox.setEnabled(enabled);
    }

    private void createGUI() {
        setBorder(new TitledBorder(new EtchedBorder(1), this.messageBundle.getString("viewer.utilityPane.annotation.flags.title"), 1, 0));
        ValueLabelItem[] flagList = {new ValueLabelItem(Boolean.TRUE, this.messageBundle.getString("viewer.utilityPane.annotation.flags.enabled")), new ValueLabelItem(Boolean.FALSE, this.messageBundle.getString("viewer.utilityPane.annotation.flags.disabled"))};
        this.noRotateComboBox = new JComboBox(flagList);
        this.noRotateComboBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.flags.noRotate")));
        add(this.noRotateComboBox);
        this.noZoomComboBox = new JComboBox(flagList);
        this.noZoomComboBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.flags.noZoom")));
        add(this.noZoomComboBox);
        this.readOnlyComboBox = new JComboBox(flagList);
        this.readOnlyComboBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.flags.readOnly")));
        add(this.readOnlyComboBox);
        this.printableComboBox = new JComboBox(flagList);
        this.printableComboBox.addItemListener(this);
        add(new JLabel(this.messageBundle.getString("viewer.utilityPane.annotation.flags.printable")));
        add(this.printableComboBox);
    }
}
