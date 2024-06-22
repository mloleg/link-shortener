package ru.mloleg.linkshortener.beanpostprocessor;

import java.lang.reflect.Method;
import java.util.List;

public record ClassMethods(Class<?> clazz, List<Method> methods) {
}
