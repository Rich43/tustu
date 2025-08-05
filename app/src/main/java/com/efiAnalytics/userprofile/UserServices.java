package com.efianalytics.userprofile;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@XmlSeeAlso({ObjectFactory.class})
@WebService(name = "UserServices", targetNamespace = "http://userprofile.efiAnalytics.com/")
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/UserServices.class */
public interface UserServices {
    @Action(input = "http://userprofile.efiAnalytics.com/UserServices/authenticateRequest", output = "http://userprofile.efiAnalytics.com/UserServices/authenticateResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "authenticate", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.Authenticate")
    @ResponseWrapper(localName = "authenticateResponse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.AuthenticateResponse")
    @WebMethod
    AuthenticationResponse authenticate(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2);

    @Action(input = "http://userprofile.efiAnalytics.com/UserServices/createUserRequest", output = "http://userprofile.efiAnalytics.com/UserServices/createUserResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createUser", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.CreateUser")
    @ResponseWrapper(localName = "createUserResponse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.CreateUserResponse")
    @WebMethod
    UserChangeResult createUser(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "email", targetNamespace = "") String str2, @WebParam(name = "firstName", targetNamespace = "") String str3, @WebParam(name = "lastName", targetNamespace = "") String str4, @WebParam(name = "middleInitial", targetNamespace = "") String str5);

    @Action(input = "http://userprofile.efiAnalytics.com/UserServices/authenticateSessionRequest", output = "http://userprofile.efiAnalytics.com/UserServices/authenticateSessionResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "authenticateSession", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.AuthenticateSession")
    @ResponseWrapper(localName = "authenticateSessionResponse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.AuthenticateSessionResponse")
    @WebMethod
    AuthenticationResponse authenticateSession(@WebParam(name = "sessionId", targetNamespace = "") String str);

    @Action(input = "http://userprofile.efiAnalytics.com/UserServices/getUserProfileRequest", output = "http://userprofile.efiAnalytics.com/UserServices/getUserProfileResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getUserProfile", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.GetUserProfile")
    @ResponseWrapper(localName = "getUserProfileResponse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.GetUserProfileResponse")
    @WebMethod
    UserProfile getUserProfile(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2);

    @Action(input = "http://userprofile.efiAnalytics.com/UserServices/getAccessTokensRequest", output = "http://userprofile.efiAnalytics.com/UserServices/getAccessTokensResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAccessTokens", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.GetAccessTokens")
    @ResponseWrapper(localName = "getAccessTokensResponse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.GetAccessTokensResponse")
    @WebMethod
    List<RoleToken> getAccessTokens(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2);

    @Action(input = "http://userprofile.efiAnalytics.com/UserServices/updateUserProfileRequest", output = "http://userprofile.efiAnalytics.com/UserServices/updateUserProfileResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "updateUserProfile", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.UpdateUserProfile")
    @ResponseWrapper(localName = "updateUserProfileResponse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.UpdateUserProfileResponse")
    @WebMethod
    UserChangeResult updateUserProfile(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2, @WebParam(name = "newPassword", targetNamespace = "") String str3, @WebParam(name = "email", targetNamespace = "") String str4, @WebParam(name = "sendEmail", targetNamespace = "") boolean z2);

    @Action(input = "http://userprofile.efiAnalytics.com/UserServices/isUserIdInUseRequest", output = "http://userprofile.efiAnalytics.com/UserServices/isUserIdInUseResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "isUserIdInUse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.IsUserIdInUse")
    @ResponseWrapper(localName = "isUserIdInUseResponse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.IsUserIdInUseResponse")
    @WebMethod
    Boolean isUserIdInUse(@WebParam(name = "userId", targetNamespace = "") String str);

    @Action(input = "http://userprofile.efiAnalytics.com/UserServices/resetPasswordRequest", output = "http://userprofile.efiAnalytics.com/UserServices/resetPasswordResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "resetPassword", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.ResetPassword")
    @ResponseWrapper(localName = "resetPasswordResponse", targetNamespace = "http://userprofile.efiAnalytics.com/", className = "com.efianalytics.userprofile.ResetPasswordResponse")
    @WebMethod
    UserChangeResult resetPassword(@WebParam(name = "userIdOrEmail", targetNamespace = "") String str);
}
