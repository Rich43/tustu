package org.icepdf.ri.common.utility.annotation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.CircleAnnotation;
import org.icepdf.core.pobjects.annotations.FreeTextAnnotation;
import org.icepdf.core.pobjects.annotations.InkAnnotation;
import org.icepdf.core.pobjects.annotations.LineAnnotation;
import org.icepdf.core.pobjects.annotations.LinkAnnotation;
import org.icepdf.core.pobjects.annotations.SquareAnnotation;
import org.icepdf.core.pobjects.annotations.TextAnnotation;
import org.icepdf.core.pobjects.annotations.TextMarkupAnnotation;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AnnotationComponent;
import org.icepdf.ri.common.views.annotations.PopupAnnotationComponent;
import org.icepdf.ri.util.PropertiesManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/AnnotationPanel.class */
public class AnnotationPanel extends AnnotationPanelAdapter {
    private GridBagConstraints constraints;
    private PropertiesManager propertiesManager;
    private JPanel annotationPanel;
    private AnnotationPanelAdapter annotationPropertyPanel;
    private ActionsPanel actionsPanel;
    private BorderPanel borderPanel;
    private FlagsPanel flagsPanel;

    public AnnotationPanel(SwingController controller) {
        this(controller, null);
    }

    public AnnotationPanel(SwingController controller, PropertiesManager propertiesManager) {
        super(controller);
        setLayout(new BorderLayout());
        this.propertiesManager = propertiesManager;
        setFocusable(true);
        setGUI();
        setEnabled(false);
    }

    public void setAnnotationUtilityToolbar(JToolBar annotationUtilityToolbar) {
        addGB(this.annotationPanel, annotationUtilityToolbar, 0, 0, 1, 1);
    }

    public AnnotationPanelAdapter buildAnnotationPropertyPanel(AnnotationComponent annotationComp) {
        if (annotationComp != null) {
            Annotation annotation = annotationComp.getAnnotation();
            if (annotation != null && (annotation instanceof LinkAnnotation)) {
                return new LinkAnnotationPanel(this.controller);
            }
            if (annotation != null && (annotation instanceof TextMarkupAnnotation)) {
                return new TextMarkupAnnotationPanel(this.controller);
            }
            if (annotation != null && (annotation instanceof LineAnnotation)) {
                return new LineAnnotationPanel(this.controller);
            }
            if (annotation != null && (annotation instanceof SquareAnnotation)) {
                return new SquareAnnotationPanel(this.controller);
            }
            if (annotation != null && (annotation instanceof CircleAnnotation)) {
                return new CircleAnnotationPanel(this.controller);
            }
            if (annotation != null && (annotation instanceof InkAnnotation)) {
                return new InkAnnotationPanel(this.controller);
            }
            if (annotation != null && (annotation instanceof TextAnnotation)) {
                return new TextAnnotationPanel(this.controller);
            }
            if (annotation != null && (annotation instanceof FreeTextAnnotation)) {
                return new FreeTextAnnotationPanel(this.controller);
            }
            return null;
        }
        return null;
    }

    @Override // org.icepdf.ri.common.utility.annotation.AnnotationProperties
    public void setAnnotationComponent(AnnotationComponent annotation) {
        if (this.annotationPropertyPanel != null) {
            this.annotationPanel.remove(this.annotationPropertyPanel);
        }
        this.annotationPropertyPanel = buildAnnotationPropertyPanel(annotation);
        if (this.annotationPropertyPanel != null) {
            this.annotationPropertyPanel.setAnnotationComponent(annotation);
            addGB(this.annotationPanel, this.annotationPropertyPanel, 0, 1, 1, 1);
        }
        this.actionsPanel.setAnnotationComponent(annotation);
        if (this.flagsPanel != null) {
            this.flagsPanel.setAnnotationComponent(annotation);
        }
        this.borderPanel.setAnnotationComponent(annotation);
        if ((this.annotationPropertyPanel instanceof LineAnnotationPanel) || (this.annotationPropertyPanel instanceof SquareAnnotationPanel) || (this.annotationPropertyPanel instanceof CircleAnnotationPanel) || (this.annotationPropertyPanel instanceof InkAnnotationPanel) || (this.annotationPropertyPanel instanceof FreeTextAnnotationPanel) || (annotation instanceof PopupAnnotationComponent)) {
            this.borderPanel.setVisible(false);
        } else {
            this.borderPanel.setVisible(true);
        }
        if (annotation instanceof PopupAnnotationComponent) {
            this.actionsPanel.setVisible(false);
            this.flagsPanel.setVisible(false);
        } else {
            this.actionsPanel.setVisible(true);
            this.flagsPanel.setVisible(true);
        }
        if (!annotation.isEditable()) {
            setEnabled(annotation.isEditable());
        }
        revalidate();
    }

    private void setGUI() {
        this.annotationPanel = new JPanel(new GridBagLayout());
        add(this.annotationPanel, "North");
        this.constraints = new GridBagConstraints();
        this.constraints.fill = 2;
        this.constraints.weightx = 1.0d;
        this.constraints.anchor = 11;
        this.constraints.anchor = 17;
        this.constraints.insets = new Insets(5, 1, 5, 1);
        this.annotationPropertyPanel = buildAnnotationPropertyPanel(null);
        this.actionsPanel = new ActionsPanel(this.controller);
        this.borderPanel = new BorderPanel(this.controller);
        if (this.propertiesManager == null || PropertiesManager.checkAndStoreBooleanProperty(this.propertiesManager, PropertiesManager.PROPERTY_SHOW_UTILITYPANE_ANNOTATION_FLAGS)) {
            this.flagsPanel = new FlagsPanel(this.controller);
        }
        if (this.annotationPropertyPanel != null) {
            addGB(this.annotationPanel, this.annotationPropertyPanel, 0, 1, 1, 1);
        }
        addGB(this.annotationPanel, this.borderPanel, 0, 2, 1, 1);
        if (this.flagsPanel != null) {
            addGB(this.annotationPanel, this.flagsPanel, 0, 3, 1, 1);
        }
        addGB(this.annotationPanel, this.actionsPanel, 0, 4, 1, 1);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (this.annotationPropertyPanel != null && this.actionsPanel != null) {
            this.annotationPropertyPanel.setEnabled(enabled);
            this.actionsPanel.setEnabled(enabled);
            this.flagsPanel.setEnabled(enabled);
            this.borderPanel.setEnabled(enabled);
        }
    }

    private void addGB(JPanel layout, Component component, int x2, int y2, int rowSpan, int colSpan) {
        this.constraints.gridx = x2;
        this.constraints.gridy = y2;
        this.constraints.gridwidth = rowSpan;
        this.constraints.gridheight = colSpan;
        layout.add(component, this.constraints);
    }
}
