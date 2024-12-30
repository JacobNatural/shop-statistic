package com.app.model;

/**
 * Represents the different roles that a user can have in the application.
 *  <p>
 * This enum defines three roles that are associated with user permissions and access control:
 * <ul>
 *     <li>{@link #ROLE_WORKER} - A worker with limited access to the system's features.</li>
 *     <li>{@link #ROLE_LEADER} - A leader with more privileged access compared to a worker.</li>
 *     <li>{@link #ROLE_ADMIN} - An admin with the highest level of access and control over the system.</li>
 * </ul>
 *
 */
public enum Role {

    /**
     * A worker role. Workers typically have limited access to certain parts of the system,
     * focusing on specific tasks without administrative privileges.
     */
    ROLE_WORKER,

    /**
     * A leader role. Leaders usually have higher access privileges compared to workers
     * and can manage teams, but not the entire system.
     */
    ROLE_LEADER,

    /**
     * An admin role. Admins have full access to all features of the system, including user management
     * and system configurations.
     */
    ROLE_ADMIN
}
