package sun.security.tools.policytool;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import javax.swing.JTextField;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/FileMenuListener.class */
class FileMenuListener implements ActionListener {
    private PolicyTool tool;
    private ToolWindow tw;

    FileMenuListener(PolicyTool policyTool, ToolWindow toolWindow) {
        this.tool = policyTool;
        this.tw = toolWindow;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.QUIT) == 0) {
            new ToolDialog(PolicyTool.getMessage("Save.Changes"), this.tool, this.tw, true).displayUserSave(1);
            return;
        }
        if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.NEW_POLICY_FILE) == 0) {
            new ToolDialog(PolicyTool.getMessage("Save.Changes"), this.tool, this.tw, true).displayUserSave(2);
            return;
        }
        if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.OPEN_POLICY_FILE) == 0) {
            new ToolDialog(PolicyTool.getMessage("Save.Changes"), this.tool, this.tw, true).displayUserSave(3);
            return;
        }
        if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.SAVE_POLICY_FILE) != 0) {
            if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.SAVE_AS_POLICY_FILE) == 0) {
                new ToolDialog(PolicyTool.getMessage(ToolWindow.SAVE_AS_POLICY_FILE), this.tool, this.tw, true).displaySaveAsDialog(0);
                return;
            } else {
                if (PolicyTool.collator.compare(actionEvent.getActionCommand(), ToolWindow.VIEW_WARNINGS) == 0) {
                    this.tw.displayWarningLog(null);
                    return;
                }
                return;
            }
        }
        String text = ((JTextField) this.tw.getComponent(1)).getText();
        if (text == null || text.length() == 0) {
            new ToolDialog(PolicyTool.getMessage(ToolWindow.SAVE_AS_POLICY_FILE), this.tool, this.tw, true).displaySaveAsDialog(0);
            return;
        }
        try {
            this.tool.savePolicy(text);
            this.tw.displayStatusDialog(null, new MessageFormat(PolicyTool.getMessage("Policy.successfully.written.to.filename")).format(new Object[]{text}));
        } catch (FileNotFoundException e2) {
            if (text == null || text.equals("")) {
                this.tw.displayErrorDialog((Window) null, new FileNotFoundException(PolicyTool.getMessage("null.filename")));
            } else {
                this.tw.displayErrorDialog((Window) null, e2);
            }
        } catch (Exception e3) {
            this.tw.displayErrorDialog((Window) null, e3);
        }
    }
}
