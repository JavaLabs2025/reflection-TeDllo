package org.example.generator;


import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;


public class Generator {
    private static final int NULL_CHANCE = 0;
    private static final int MAX_DEPTH = 4;
    private static final int ARRAY_MAX_SIZE = 12;

    private static final Set<Class<?>> WRAPPERS = Set.of(
        Boolean.class,
        Byte.class,
        Character.class,
        Double.class,
        Float.class,
        Integer.class,
        Long.class,
        Short.class
    );

    private final Random random = new Random();

    public <T> T generateValueOfType(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) generateValueOfType(clazz, 0);
    }

    public Object generateValueOfType(Class<?> clazz, int depth) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return generateValueOfType(clazz, depth, null);
    }

    public Object generateValueOfType(Class<?> clazz, int depth, Type genericType) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!clazz.isPrimitive() && random.nextInt(100) < NULL_CHANCE) {
            return null;
        }

        if (WRAPPERS.contains(clazz) || clazz.isPrimitive()) {
            return nextPrimitive(clazz);
        }
        if (clazz == String.class) {
            return nextString();
        }
        if (clazz.isEnum()) {
            return nextEnum(clazz);
        }

        // Start of potential recursion
        if (depth > MAX_DEPTH) {
            return null;
        }

        if (clazz.isArray()) {
            return nextArray(clazz.getComponentType(), depth);
        }

        if (clazz.isAssignableFrom(List.class) || clazz == ArrayList.class) {
            return nextList(depth, genericType);
        }

        if (clazz == Set.class || clazz == HashSet.class) {
            return nextSet(depth, genericType);
        }

        return nextGeneratable(clazz, depth);
    }

    private Object nextPrimitive(Class<?> type) {
        if (type == boolean.class || type == Boolean.class) {
            return random.nextBoolean();
        }
        if (type == byte.class || type == Byte.class) {
            return (byte) random.nextInt();
        }
        if (type == short.class || type == Short.class) {
            return (short) random.nextInt();
        }
        if (type == int.class || type == Integer.class) {
            return random.nextInt();
        }
        if (type == char.class || type == Character.class) {
            return nextChar();
        }
        if (type == double.class || type == Double.class) {
            return random.nextDouble();
        }
        if (type == float.class || type == Float.class) {
            return random.nextFloat();
        }
        if (type == long.class || type == Long.class) {
            return random.nextLong();
        }

        throw new IllegalArgumentException("Not a primitive or wrapper");
    }


    private String nextString() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, random.nextInt(1, ARRAY_MAX_SIZE + 1))
            .forEach(i -> sb.append(nextChar()));
        return sb.toString();
    }


    private char nextChar() {
        return (char) random.nextInt('A', 128);
    }

    private Object nextEnum(Class<?> clazz) {
        Object[] constants = clazz.getEnumConstants();
        if (constants == null || constants.length == 0) {
            return null;
        }
        return constants[random.nextInt(constants.length)];
    }

    private Object nextArray(Class<?> clazz, int depth) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        int length = random.nextInt(1, ARRAY_MAX_SIZE + 1);
        Object array = Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, generateValueOfType(clazz, depth + 1));
        }
        return array;
    }

    private Object nextList(int depth, Type genericType) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types.length == 1) {
                Type type = types[0];
                if (type instanceof Class<?> componentClass) {
                    int size = random.nextInt(1, MAX_DEPTH + 1);
                    List<Object> list = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        list.add(generateValueOfType(componentClass, depth + 1));
                    }
                    return list;
                }
            }
        }
        return new ArrayList<>();
    }

    private Object nextSet(int depth, Type genericType) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types.length == 1) {
                Type type = types[0];
                if (type instanceof Class<?> componentClass) {
                    int size = random.nextInt(1, MAX_DEPTH + 1);
                    Set<Object> set = new HashSet<>(size);
                    for (int i = 0; i < size; i++) {
                        set.add(generateValueOfType(componentClass, depth + 1));
                    }
                    return set;
                }
            }
        }
        return new HashSet<>();
    }

    private Object nextGeneratable(Class<?> clazz, int depth) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Generatable annotation = clazz.getAnnotation(Generatable.class);
        if (annotation == null) {
            throw new NotGeneratableClassException(clazz.getSimpleName());
        }

        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            return nextImplementation(annotation, depth);
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Constructor<?> constructor = Arrays.stream(constructors)
            .filter(c -> c.canAccess(null))
            .findAny()
            .orElse(null);

        if (constructor == null) {
            return null;
        }

        int paramCnt = constructor.getParameterCount();
        Object[] initArgs = new Object[paramCnt];
        for (int i = 0; i < paramCnt; i++) {
            Parameter parameter = constructor.getParameters()[i];
            initArgs[i] = generateValueOfType(
                parameter.getType(),
                depth + 1,
                parameter.getParameterizedType()
            );
        }
        return constructor.newInstance(initArgs);
    }

    private Object nextImplementation(Generatable annotation, int depth) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?>[] implementations = annotation.implementations();
        if (implementations.length == 0) {
            return null;
        }
        return generateValueOfType(implementations[random.nextInt(implementations.length)], depth + 1);
    }

}
