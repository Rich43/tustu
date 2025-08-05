package javax.naming;

/* loaded from: rt.jar:javax/naming/LinkRef.class */
public class LinkRef extends Reference {
    static final String linkClassName = LinkRef.class.getName();
    static final String linkAddrType = "LinkAddress";
    private static final long serialVersionUID = -5386290613498931298L;

    public LinkRef(Name name) {
        super(linkClassName, new StringRefAddr(linkAddrType, name.toString()));
    }

    public LinkRef(String str) {
        super(linkClassName, new StringRefAddr(linkAddrType, str));
    }

    public String getLinkName() throws NamingException {
        RefAddr refAddr;
        if (this.className != null && this.className.equals(linkClassName) && (refAddr = get(linkAddrType)) != null && (refAddr instanceof StringRefAddr)) {
            return (String) ((StringRefAddr) refAddr).getContent();
        }
        throw new MalformedLinkException();
    }
}
