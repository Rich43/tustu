package com.efianalytics.userprofile;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/ObjectFactory.class */
public class ObjectFactory {
    private static final QName _GetUserProfileResponse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "getUserProfileResponse");
    private static final QName _Authenticate_QNAME = new QName("http://userprofile.efiAnalytics.com/", "authenticate");
    private static final QName _CreateUser_QNAME = new QName("http://userprofile.efiAnalytics.com/", "createUser");
    private static final QName _IsUserIdInUse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "isUserIdInUse");
    private static final QName _ResetPasswordResponse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "resetPasswordResponse");
    private static final QName _GetUserProfile_QNAME = new QName("http://userprofile.efiAnalytics.com/", "getUserProfile");
    private static final QName _AuthenticateResponse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "authenticateResponse");
    private static final QName _AuthenticateSession_QNAME = new QName("http://userprofile.efiAnalytics.com/", "authenticateSession");
    private static final QName _AuthenticateSessionResponse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "authenticateSessionResponse");
    private static final QName _UpdateUserProfileResponse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "updateUserProfileResponse");
    private static final QName _IsUserIdInUseResponse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "isUserIdInUseResponse");
    private static final QName _ResetPassword_QNAME = new QName("http://userprofile.efiAnalytics.com/", "resetPassword");
    private static final QName _CreateUserResponse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "createUserResponse");
    private static final QName _UpdateUserProfile_QNAME = new QName("http://userprofile.efiAnalytics.com/", "updateUserProfile");
    private static final QName _GetAccessTokens_QNAME = new QName("http://userprofile.efiAnalytics.com/", "getAccessTokens");
    private static final QName _GetAccessTokensResponse_QNAME = new QName("http://userprofile.efiAnalytics.com/", "getAccessTokensResponse");

    public UserChangeResult createUserChangeResult() {
        return new UserChangeResult();
    }

    public UserProfile createUserProfile() {
        return new UserProfile();
    }

    public UpdateUserProfileResponse createUpdateUserProfileResponse() {
        return new UpdateUserProfileResponse();
    }

    public GetAccessTokensResponse createGetAccessTokensResponse() {
        return new GetAccessTokensResponse();
    }

    public IsUserIdInUse createIsUserIdInUse() {
        return new IsUserIdInUse();
    }

    public AuthenticationResponse createAuthenticationResponse() {
        return new AuthenticationResponse();
    }

    public Authenticate createAuthenticate() {
        return new Authenticate();
    }

    public AuthenticateResponse createAuthenticateResponse() {
        return new AuthenticateResponse();
    }

    public CreateUserResponse createCreateUserResponse() {
        return new CreateUserResponse();
    }

    public CreateUser createCreateUser() {
        return new CreateUser();
    }

    public GetUserProfileResponse createGetUserProfileResponse() {
        return new GetUserProfileResponse();
    }

    public IsUserIdInUseResponse createIsUserIdInUseResponse() {
        return new IsUserIdInUseResponse();
    }

    public RoleTokenPK createRoleTokenPK() {
        return new RoleTokenPK();
    }

    public ResetPassword createResetPassword() {
        return new ResetPassword();
    }

    public AuthenticateSessionResponse createAuthenticateSessionResponse() {
        return new AuthenticateSessionResponse();
    }

    public RoleToken createRoleToken() {
        return new RoleToken();
    }

    public UserRole createUserRole() {
        return new UserRole();
    }

    public UpdateUserProfile createUpdateUserProfile() {
        return new UpdateUserProfile();
    }

    public AuthenticateSession createAuthenticateSession() {
        return new AuthenticateSession();
    }

    public ResetPasswordResponse createResetPasswordResponse() {
        return new ResetPasswordResponse();
    }

    public GetAccessTokens createGetAccessTokens() {
        return new GetAccessTokens();
    }

    public GetUserProfile createGetUserProfile() {
        return new GetUserProfile();
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "getUserProfileResponse")
    public JAXBElement<GetUserProfileResponse> createGetUserProfileResponse(GetUserProfileResponse value) {
        return new JAXBElement<>(_GetUserProfileResponse_QNAME, GetUserProfileResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "authenticate")
    public JAXBElement<Authenticate> createAuthenticate(Authenticate value) {
        return new JAXBElement<>(_Authenticate_QNAME, Authenticate.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "createUser")
    public JAXBElement<CreateUser> createCreateUser(CreateUser value) {
        return new JAXBElement<>(_CreateUser_QNAME, CreateUser.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "isUserIdInUse")
    public JAXBElement<IsUserIdInUse> createIsUserIdInUse(IsUserIdInUse value) {
        return new JAXBElement<>(_IsUserIdInUse_QNAME, IsUserIdInUse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "resetPasswordResponse")
    public JAXBElement<ResetPasswordResponse> createResetPasswordResponse(ResetPasswordResponse value) {
        return new JAXBElement<>(_ResetPasswordResponse_QNAME, ResetPasswordResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "getUserProfile")
    public JAXBElement<GetUserProfile> createGetUserProfile(GetUserProfile value) {
        return new JAXBElement<>(_GetUserProfile_QNAME, GetUserProfile.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "authenticateResponse")
    public JAXBElement<AuthenticateResponse> createAuthenticateResponse(AuthenticateResponse value) {
        return new JAXBElement<>(_AuthenticateResponse_QNAME, AuthenticateResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "authenticateSession")
    public JAXBElement<AuthenticateSession> createAuthenticateSession(AuthenticateSession value) {
        return new JAXBElement<>(_AuthenticateSession_QNAME, AuthenticateSession.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "authenticateSessionResponse")
    public JAXBElement<AuthenticateSessionResponse> createAuthenticateSessionResponse(AuthenticateSessionResponse value) {
        return new JAXBElement<>(_AuthenticateSessionResponse_QNAME, AuthenticateSessionResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "updateUserProfileResponse")
    public JAXBElement<UpdateUserProfileResponse> createUpdateUserProfileResponse(UpdateUserProfileResponse value) {
        return new JAXBElement<>(_UpdateUserProfileResponse_QNAME, UpdateUserProfileResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "isUserIdInUseResponse")
    public JAXBElement<IsUserIdInUseResponse> createIsUserIdInUseResponse(IsUserIdInUseResponse value) {
        return new JAXBElement<>(_IsUserIdInUseResponse_QNAME, IsUserIdInUseResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "resetPassword")
    public JAXBElement<ResetPassword> createResetPassword(ResetPassword value) {
        return new JAXBElement<>(_ResetPassword_QNAME, ResetPassword.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "createUserResponse")
    public JAXBElement<CreateUserResponse> createCreateUserResponse(CreateUserResponse value) {
        return new JAXBElement<>(_CreateUserResponse_QNAME, CreateUserResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "updateUserProfile")
    public JAXBElement<UpdateUserProfile> createUpdateUserProfile(UpdateUserProfile value) {
        return new JAXBElement<>(_UpdateUserProfile_QNAME, UpdateUserProfile.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "getAccessTokens")
    public JAXBElement<GetAccessTokens> createGetAccessTokens(GetAccessTokens value) {
        return new JAXBElement<>(_GetAccessTokens_QNAME, GetAccessTokens.class, null, value);
    }

    @XmlElementDecl(namespace = "http://userprofile.efiAnalytics.com/", name = "getAccessTokensResponse")
    public JAXBElement<GetAccessTokensResponse> createGetAccessTokensResponse(GetAccessTokensResponse value) {
        return new JAXBElement<>(_GetAccessTokensResponse_QNAME, GetAccessTokensResponse.class, null, value);
    }
}
