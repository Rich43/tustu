package jdk.jfr.internal.dcmd;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javafx.fxml.FXMLLoader;
import jdk.jfr.EventType;
import jdk.jfr.Recording;
import jdk.jfr.SettingDescriptor;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.Utils;

/* loaded from: jfr.jar:jdk/jfr/internal/dcmd/DCmdCheck.class */
final class DCmdCheck extends AbstractDCmd {
    DCmdCheck() {
    }

    public String execute(String str, Boolean bool) throws DCmdException {
        executeInternal(str, bool);
        return getResult();
    }

    private void executeInternal(String str, Boolean bool) throws DCmdException {
        if (Logger.shouldLog(LogTag.JFR_DCMD, LogLevel.DEBUG)) {
            Logger.log(LogTag.JFR_DCMD, LogLevel.DEBUG, "Executing DCmdCheck: name=" + str + ", verbose=" + ((Object) bool));
        }
        if (bool == null) {
            bool = Boolean.FALSE;
        }
        if (str != null) {
            printRecording(findRecording(str), bool.booleanValue());
            return;
        }
        List<Recording> recordings = getRecordings();
        if (!bool.booleanValue() && recordings.isEmpty()) {
            println("No available recordings.", new Object[0]);
            println();
            println("Use jcmd " + getPid() + " JFR.start to start a recording.", new Object[0]);
            return;
        }
        boolean z2 = true;
        for (Recording recording : recordings) {
            if (!z2) {
                println();
                if (Boolean.TRUE.equals(bool)) {
                    println();
                }
            }
            z2 = false;
            printRecording(recording, bool.booleanValue());
        }
    }

    private void printRecording(Recording recording, boolean z2) {
        printGeneral(recording);
        if (z2) {
            println();
            printSetttings(recording);
        }
    }

    private void printGeneral(Recording recording) {
        print("Recording " + recording.getId() + ": name=" + recording.getName());
        Duration duration = recording.getDuration();
        if (duration != null) {
            print(" duration=");
            printTimespan(duration, "");
        }
        long maxSize = recording.getMaxSize();
        if (maxSize != 0) {
            print(" maxsize=");
            print(Utils.formatBytesCompact(maxSize));
        }
        Duration maxAge = recording.getMaxAge();
        if (maxAge != null) {
            print(" maxage=");
            printTimespan(maxAge, "");
        }
        print(" (" + recording.getState().toString().toLowerCase() + ")");
        println();
    }

    private void printSetttings(Recording recording) {
        Map<String, String> settings = recording.getSettings();
        for (EventType eventType : sortByEventPath(getFlightRecorder().getEventTypes())) {
            StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
            stringJoiner.setEmptyValue("");
            for (SettingDescriptor settingDescriptor : eventType.getSettingDescriptors()) {
                String str = eventType.getName() + FXMLLoader.CONTROLLER_METHOD_PREFIX + settingDescriptor.getName();
                if (settings.containsKey(str)) {
                    stringJoiner.add(settingDescriptor.getName() + "=" + settings.get(str));
                }
            }
            String string = stringJoiner.toString();
            if (!string.isEmpty()) {
                print(" %s (%s)", eventType.getLabel(), eventType.getName());
                println();
                println("   " + string, new Object[0]);
            }
        }
    }

    private static List<EventType> sortByEventPath(Collection<EventType> collection) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(collection);
        Collections.sort(arrayList, new Comparator<EventType>() { // from class: jdk.jfr.internal.dcmd.DCmdCheck.1
            @Override // java.util.Comparator
            public int compare(EventType eventType, EventType eventType2) {
                return eventType.getName().compareTo(eventType2.getName());
            }
        });
        return arrayList;
    }
}
