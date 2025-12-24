package org.example.generator;

public class NotGeneratableClassException extends RuntimeException {
    public NotGeneratableClassException(String clazz) {
        super("Class " + clazz + " is not generatable");
    }
}
