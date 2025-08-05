package com.sun.javafx.stage;

import javafx.stage.Stage;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/StagePeerListener.class */
public class StagePeerListener extends WindowPeerListener {
    private final Stage stage;
    private final StageAccessor stageAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/stage/StagePeerListener$StageAccessor.class */
    public interface StageAccessor {
        void setIconified(Stage stage, boolean z2);

        void setMaximized(Stage stage, boolean z2);

        void setResizable(Stage stage, boolean z2);

        void setFullScreen(Stage stage, boolean z2);

        void setAlwaysOnTop(Stage stage, boolean z2);
    }

    public StagePeerListener(Stage stage, StageAccessor stageAccessor) {
        super(stage);
        this.stage = stage;
        this.stageAccessor = stageAccessor;
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedIconified(boolean iconified) {
        this.stageAccessor.setIconified(this.stage, iconified);
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedMaximized(boolean maximized) {
        this.stageAccessor.setMaximized(this.stage, maximized);
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedResizable(boolean resizable) {
        this.stageAccessor.setResizable(this.stage, resizable);
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedFullscreen(boolean fs) {
        this.stageAccessor.setFullScreen(this.stage, fs);
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedAlwaysOnTop(boolean aot) {
        this.stageAccessor.setAlwaysOnTop(this.stage, aot);
    }
}
