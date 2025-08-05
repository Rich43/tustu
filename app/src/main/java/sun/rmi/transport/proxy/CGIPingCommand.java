package sun.rmi.transport.proxy;

/* compiled from: CGIHandler.java */
/* loaded from: rt.jar:sun/rmi/transport/proxy/CGIPingCommand.class */
final class CGIPingCommand implements CGICommandHandler {
    CGIPingCommand() {
    }

    @Override // sun.rmi.transport.proxy.CGICommandHandler
    public String getName() {
        return "ping";
    }

    @Override // sun.rmi.transport.proxy.CGICommandHandler
    public void execute(String str) {
        System.out.println("Status: 200 OK");
        System.out.println("Content-type: application/octet-stream");
        System.out.println("Content-length: 0");
        System.out.println("");
    }
}
