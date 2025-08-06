"""Offline activation validator implemented in Python.

This module mirrors the behaviour of the Java activation classes found
in the refactored folder.  It can be used to validate activation codes
returned by an activation server without requiring network
connectivity.
"""

from __future__ import annotations

import base64
from dataclasses import dataclass, field
from datetime import datetime, timezone


class ActivationParseError(Exception):
    """Raised when the activation data cannot be parsed."""


@dataclass
class ActivationRequest:
    operating_system: str = ""
    hardware_id: str = ""
    motherboard_id: str = ""
    device_id: str = ""
    registration_key: str = ""
    email: str = ""
    user_id: str = ""


@dataclass
class ActivationData:
    mac_address: str = ""
    hardware_id: str = ""
    motherboard_id: str = ""
    device_id: str = ""
    registration_key: str = ""
    email: str = ""
    error_code: int = 2
    message: str = ""
    activation_count: int = 0
    renewal_date: datetime = field(
        default_factory=lambda: datetime.fromtimestamp(0, tz=timezone.utc)
    )
    raw_data: str = ""

    @classmethod
    def from_base64(cls, encoded: str) -> "ActivationData":
        instance = cls(raw_data=encoded)
        instance.parse(encoded)
        return instance

    def parse(self, encoded: str) -> None:
        try:
            decoded = base64.b64decode(encoded).decode("utf-8")
        except Exception as exc:  # pragma: no cover - simple parsing guard
            raise ActivationParseError("Invalid activation data.") from exc

        props = {}
        for line in decoded.splitlines():
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            if "=" in line:
                key, value = line.split("=", 1)
            elif ":" in line:
                key, value = line.split(":", 1)
            else:
                continue
            props[key.strip()] = value.strip()

        self.mac_address = props.get("mac", "")
        self.hardware_id = props.get("hId", "")
        self.motherboard_id = props.get("mId", "")
        self.device_id = props.get("dId", "")
        self.registration_key = props.get("rk", "")
        self.email = props.get("em", "")
        try:
            self.error_code = int(props.get("ec", "2"))
        except ValueError:
            self.error_code = 2
        self.message = props.get("msg", "")
        try:
            self.activation_count = int(props.get("actCount", "0"))
        except ValueError:
            self.activation_count = 0
        try:
            renew = int(props.get("renewDate", "0"))
            self.renewal_date = datetime.fromtimestamp(renew / 1000, tz=timezone.utc)
        except ValueError:
            self.renewal_date = datetime.fromtimestamp(0, tz=timezone.utc)


@dataclass
class ActivationResult:
    code: int = 2
    message: str = "Invalid activation data."
    data: ActivationData | None = None


class ActivationValidator:
    """Validate activation data returned by the activation server."""

    _instance: "ActivationValidator" | None = None

    def __init__(self) -> None:
        self.local_request: ActivationRequest | None = None

    @classmethod
    def get_instance(cls) -> "ActivationValidator":
        if cls._instance is None:
            cls._instance = cls()
        return cls._instance

    def set_local_request(self, request: ActivationRequest) -> None:
        self.local_request = request

    def validate(self, data: ActivationData | None) -> ActivationResult:
        result = ActivationResult()
        if data is None:
            return result

        error_code = data.error_code
        if error_code in (0, 1):
            if self.local_request is None:
                result.code = 4
                result.message = "No identifiers available."
                return result

            have_id = False
            match = False

            if self.local_request.hardware_id:
                have_id = True
                match = data.hardware_id == self.local_request.hardware_id

            if not match and self.local_request.motherboard_id:
                have_id = True
                match = data.motherboard_id == self.local_request.motherboard_id

            if not match and self.local_request.device_id:
                have_id = True
                match = data.device_id == self.local_request.device_id

            if not have_id:
                result.code = 4
                result.message = "No identifiers available."
                return result

            if not match:
                result.code = 2
                result.message = "Invalid Activation."
                return result

            if data.renewal_date < datetime.now(tz=timezone.utc):
                result.code = 1
            else:
                result.code = 0
            result.message = "Valid Activation."
            result.data = data
        elif error_code == 5:
            result.code = 5
            result.message = f"Current Activation Count: {data.activation_count}"
            result.data = data
        elif error_code == 6:
            result.code = 6
            result.message = data.message
        else:
            result.code = 2
            result.message = "Invalid activation data."
        return result
