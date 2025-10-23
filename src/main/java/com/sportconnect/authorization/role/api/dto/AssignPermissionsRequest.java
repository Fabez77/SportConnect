package com.sportconnect.authorization.role.api.dto;

import java.util.List;
import java.util.UUID;

public record AssignPermissionsRequest(List<UUID> permissionIds) {}
