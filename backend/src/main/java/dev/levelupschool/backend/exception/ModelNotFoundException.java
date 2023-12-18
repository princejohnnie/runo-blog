package dev.levelupschool.backend.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(Class<?> cls, Long id) {
        super("Cannot find " + cls.getSimpleName() + " model with id: " + id);
    }
    public ModelNotFoundException(Class<?> cls, String email) {
        super("Cannot find " + cls.getSimpleName() + " model with email: " + email);
    }
}
