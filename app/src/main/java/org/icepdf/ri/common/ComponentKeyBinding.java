package org.icepdf.ri.common;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.icepdf.core.pobjects.Document;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.util.Resources;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/ComponentKeyBinding.class */
public class ComponentKeyBinding {
    public static void install(final SwingController controller, final JComponent viewerContainer) {
        Action copyText = new AbstractAction() { // from class: org.icepdf.ri.common.ComponentKeyBinding.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) {
                Document document = controller.getDocument();
                DocumentViewController documentViewController = controller.getDocumentViewController();
                if (document != null && controller.havePermissionToExtractContent() && (!documentViewController.getDocumentViewModel().isSelectAll() || document.getNumberOfPages() <= 250)) {
                    StringSelection stringSelection = new StringSelection(documentViewController.getSelectedText());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                } else {
                    Runnable doSwingWork = new Runnable() { // from class: org.icepdf.ri.common.ComponentKeyBinding.1.1
                        @Override // java.lang.Runnable
                        public void run() throws HeadlessException {
                            Resources.showMessageDialog(viewerContainer, 1, controller.getMessageBundle(), "viewer.dialog.information.copyAll.title", "viewer.dialog.information.copyAll.msg", 250);
                        }
                    };
                    SwingUtilities.invokeLater(doSwingWork);
                }
            }
        };
        InputMap inputMap = viewerContainer.getInputMap(2);
        inputMap.put(KeyStroke.getKeyStroke(67, 2), "copyText");
        viewerContainer.getActionMap().put("copyText", copyText);
    }
}
