package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bX.class */
final class bX extends AbstractAction {
    bX() {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        File[] selectedFiles;
        JFileChooser jFileChooser = (JFileChooser) actionEvent.getSource();
        try {
            if (jFileChooser.isMultiSelectionEnabled()) {
                selectedFiles = jFileChooser.getSelectedFiles();
            } else if (jFileChooser.getSelectedFile() == null) {
                return;
            } else {
                selectedFiles = new File[]{jFileChooser.getSelectedFile()};
            }
            if (selectedFiles != null && bV.i() == 0) {
                for (File file : selectedFiles) {
                    Files.delete(file.toPath());
                }
                jFileChooser.rescanCurrentDirectory();
            }
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }
}
