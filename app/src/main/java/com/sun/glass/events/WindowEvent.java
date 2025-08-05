package com.sun.glass.events;

import org.apache.commons.net.ftp.FTPReply;

/* loaded from: jfxrt.jar:com/sun/glass/events/WindowEvent.class */
public class WindowEvent {
    public static final int RESIZE = 511;
    public static final int MOVE = 512;
    public static final int CLOSE = 521;
    public static final int DESTROY = 522;
    public static final int MINIMIZE = 531;
    public static final int MAXIMIZE = 532;
    public static final int RESTORE = 533;
    public static final int _FOCUS_MIN = 541;
    public static final int FOCUS_LOST = 541;
    public static final int FOCUS_GAINED = 542;
    public static final int FOCUS_GAINED_FORWARD = 543;
    public static final int FOCUS_GAINED_BACKWARD = 544;
    public static final int _FOCUS_MAX = 544;
    public static final int FOCUS_DISABLED = 545;
    public static final int FOCUS_UNGRAB = 546;

    public static String getEventName(int eventType) {
        switch (eventType) {
            case RESIZE /* 511 */:
                return "RESIZE";
            case 512:
                return "MOVE";
            case 513:
            case 514:
            case 515:
            case 516:
            case 517:
            case 518:
            case 519:
            case 520:
            case 523:
            case 524:
            case 525:
            case 526:
            case 527:
            case 528:
            case 529:
            case FTPReply.NOT_LOGGED_IN /* 530 */:
            case FTPReply.REQUEST_DENIED /* 534 */:
            case FTPReply.FAILED_SECURITY_CHECK /* 535 */:
            case FTPReply.REQUESTED_PROT_LEVEL_NOT_SUPPORTED /* 536 */:
            case 537:
            case 538:
            case 539:
            case 540:
            default:
                return "UNKNOWN";
            case 521:
                return "CLOSE";
            case 522:
                return "DESTROY";
            case MINIMIZE /* 531 */:
                return "MINIMIZE";
            case 532:
                return "MAXIMIZE";
            case 533:
                return "RESTORE";
            case 541:
                return "FOCUS_LOST";
            case FOCUS_GAINED /* 542 */:
                return "FOCUS_GAINED";
            case FOCUS_GAINED_FORWARD /* 543 */:
                return "FOCUS_GAINED_FORWARD";
            case 544:
                return "FOCUS_GAINED_BACKWARD";
            case FOCUS_DISABLED /* 545 */:
                return "FOCUS_DISABLED";
            case FOCUS_UNGRAB /* 546 */:
                return "FOCUS_UNGRAB";
        }
    }
}
