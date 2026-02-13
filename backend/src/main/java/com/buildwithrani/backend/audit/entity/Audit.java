package com.buildwithrani.backend.audit.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to mark methods for automatic auditing.
 * Target: METHOD means you put this on top of a function.
 * Retention: RUNTIME means Spring can see this while the app is running.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    // The name of the action (e.g., "ORDER_PLACED", "PRODUCT_UPDATED")
    String action();

    // The type of object being modified (e.g., "ORDER", "CART", "PRODUCT")
    String entityType();
}