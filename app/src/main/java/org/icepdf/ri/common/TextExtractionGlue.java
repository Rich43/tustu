package org.icepdf.ri.common;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import org.icepdf.ri.util.TextExtractionTask;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/TextExtractionGlue.class */
public class TextExtractionGlue implements ActionListener {
    private TextExtractionTask textExtractionTask;
    private ProgressMonitor progressMonitor;
    private Timer timer;

    TextExtractionGlue(TextExtractionTask task, ProgressMonitor prog) {
        this.textExtractionTask = task;
        this.progressMonitor = prog;
    }

    void setTimer(Timer t2) {
        this.timer = t2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent evt) throws IllegalArgumentException {
        this.progressMonitor.setProgress(this.textExtractionTask.getCurrent());
        String s2 = this.textExtractionTask.getMessage();
        if (s2 != null) {
            this.progressMonitor.setNote(s2);
        }
        if (this.progressMonitor.isCanceled() || this.textExtractionTask.isDone()) {
            this.progressMonitor.close();
            this.textExtractionTask.stop();
            Toolkit.getDefaultToolkit().beep();
            this.timer.stop();
        }
    }
}
