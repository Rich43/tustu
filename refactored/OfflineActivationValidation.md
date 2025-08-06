# Offline Activation Validation

This module enables users to activate the application on a machine that does not have direct Internet access. The process revolves around exchanging a small text file with the activation server and validating the response offline.

## 1. Create an activation request
The dialog gathers basic system details and serialises them into a Base64 encoded string via `ActivationRequest#toBase64`.  Save this value to a file named `ActivationRequest.txt` and transfer it to a computer with Internet connectivity.

```java
ActivationRequest request = new ActivationRequest();
// populate fields: operatingSystem, hardwareId, motherboardId, deviceId,
// registrationKey, email and userId
String requestCode = request.toBase64();
```

## 2. Retrieve the activation code
Upload `ActivationRequest.txt` to <http://www.efianalytics.com/activate> or another authorised activation server. The server returns a `ActivationCode.txt` file which contains a Base64 encoded response.

## 3. Validate the activation
Back on the offline machine, load the contents of `ActivationCode.txt` into the dialog. The text is parsed into an `ActivationData`
instance. Before validation, supply the original `ActivationRequest` so that the validator can compare hardware identifiers.

```java
ActivationData data = new ActivationData(codeFromServer);
ActivationValidator validator = ActivationValidator.getInstance();
validator.setLocalRequest(request);
ActivationResult result = validator.validate(data);
```

`ActivationValidator` mirrors the behaviour of the original obfuscated code: it verifies that at least one hardware identifier in
the activation matches the local request, checks the renewal date and interprets server error codes. When the identifiers match
and the code has not expired, the activation data is accepted and stored for later use.

## 4. Common error messages
* **Invalid activation data** – the response could not be parsed or failed validation.
* **No identifiers available** – the activation request lacked usable identifiers.
* **This Activation code has expired** – the renewal date is in the past; request a new code from the activation server.

## 5. Completing the process
When the activation code is valid, the **Accept** button becomes enabled and the application can store the returned `ActivationData` for future checks.  No network connection is required after completing these steps.
