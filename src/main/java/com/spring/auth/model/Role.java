package com.spring.auth.model;

import java.util.Set;

public enum Role {
    USER(Set.of(Permission.READ, Permission.NULL_PERMISSION)),
    GUEST(Set.of(Permission.NULL_PERMISSION)),
    ADMIN(Set.of(Permission.READ, Permission.WRITE, Permission.NULL_PERMISSION));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
