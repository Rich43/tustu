package com.sun.javafx.stage;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/StageHelper.class */
public class StageHelper {
    private static StageAccessor stageAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/stage/StageHelper$StageAccessor.class */
    public interface StageAccessor {
        ObservableList<Stage> getStages();

        void initSecurityDialog(Stage stage, boolean z2);
    }

    public static ObservableList<Stage> getStages() {
        if (stageAccessor == null) {
            try {
                Class.forName(Stage.class.getName(), true, Stage.class.getClassLoader());
            } catch (ClassNotFoundException e2) {
            }
        }
        return stageAccessor.getStages();
    }

    public static void initSecurityDialog(Stage stage, boolean securityDialog) {
        stageAccessor.initSecurityDialog(stage, securityDialog);
    }

    public static void setStageAccessor(StageAccessor a2) {
        if (stageAccessor != null) {
            System.out.println("Warning: Stage accessor already set: " + ((Object) stageAccessor));
            Thread.dumpStack();
        }
        stageAccessor = a2;
    }
}
