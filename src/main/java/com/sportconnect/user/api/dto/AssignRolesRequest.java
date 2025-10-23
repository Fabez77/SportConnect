package com.sportconnect.user.api.dto;

import java.util.List;
import java.util.UUID;

public record AssignRolesRequest(List<UUID> roleIds) {}
