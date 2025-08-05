package org.icepdf.ri.common.utility.annotation;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/AnnotationDialogAdapter.class */
public abstract class AnnotationDialogAdapter extends JDialog implements AnnotationProperties {
    protected AnnotationDialogAdapter(JFrame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    @Override // javax.swing.JDialog
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() { // from class: org.icepdf.ri.common.utility.annotation.AnnotationDialogAdapter.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                AnnotationDialogAdapter.this.setVisible(false);
                AnnotationDialogAdapter.this.dispose();
            }
        };
        JRootPane rootPane = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        rootPane.registerKeyboardAction(actionListener, stroke, 2);
        return rootPane;
    }
}
