package sun.rmi.transport.proxy;

/* compiled from: CGIHandler.java */
/* loaded from: rt.jar:sun/rmi/transport/proxy/CGIGethostnameCommand.class */
final class CGIGethostnameCommand implements CGICommandHandler {
    CGIGethostnameCommand() {
    }

    @Override // sun.rmi.transport.proxy.CGICommandHandler
    public String getName() {
        return "gethostname";
    }

    @Override // sun.rmi.transport.proxy.CGICommandHandler
    public void execute(String str) {
        System.out.println("Status: 200 OK");
        System.out.println("Content-type: application/octet-stream");
        System.out.println("Content-length: " + CGIHandler.ServerName.length());
        System.out.println("");
        System.out.print(CGIHandler.ServerName);
        System.out.flush();
    }
}
