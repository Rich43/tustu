package org.icepdf.ri.common.tools;

import java.awt.Graphics;
import javax.swing.event.MouseInputListener;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/ToolHandler.class */
public interface ToolHandler extends MouseInputListener {
    void paintTool(Graphics graphics);

    void installTool();

    void uninstallTool();
}
