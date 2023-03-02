package service;

import exception.BusinessException;
import util.PrimitiveUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleJsonParser<T> {

    private static final String EMPTY_LINE = "";
    private static final String QUOTATION_MARK = "\"";
    private static final Character COMMA = ',';
    private static final Character OPEN_ARRAY_BRACKET = '[';
    private static final Character CLOSE_ARRAY_BRACKET = ']';
    private static final Character OPEN_FIGURE_BRACKET = '{';
    private static final Character CLOSE_FIGURE_BRACKET = '}';
    private static final Character COLON = ':';

    public T parseToSimpleObject(String json, Class<?> clazz) {
        final Map<String, String> fieldValueData = createFieldValueData(json);
        T newInstance = createEmptyInstance(clazz);
        try {
            Field[] fields = newInstance.getClass().getDeclaredFields();
            for (Field field : fields) {
                String value = fieldValueData.get(field.getName());
                field.setAccessible(true);
                if (field.getType().isPrimitive()) {
                    String primitiveName = field.getType().getName();
                    field.set(newInstance,PrimitiveUtil.getPrimitiveValue(primitiveName, value));
                    continue;
                }

                Optional<Method> valueOfMethod = getValueOfMethod(field.getType());
                if (value.startsWith("{") && value.endsWith("}")) {
                    Object result = new SimpleJsonParser<>().parseToSimpleObject(value, field.getType());
                    field.set(newInstance, field.getType().cast(result));
                    continue;
                }
                if (value.startsWith(OPEN_ARRAY_BRACKET.toString()) && value.endsWith(CLOSE_ARRAY_BRACKET.toString())) {
                    arrayProcessing(newInstance, value, field);
                    continue;
                }
                if (valueOfMethod.isPresent()) {
                    field.set(newInstance, valueOfMethod.get().invoke(null, value));
                    continue;
                }
                field.set(newInstance, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("exception occurred during setting values to fields");
        }
        return newInstance;
    }

    private Map<String, String> createFieldValueData(String json) {
        json = json.substring(1, json.length() - 2);
        char[] jsonChars = (json + COMMA).toCharArray();

        final Map<String, String> fieldValueData = new HashMap<>();

        String keyData = EMPTY_LINE;
        String valueData = EMPTY_LINE;
        StringBuilder currentValue = new StringBuilder(EMPTY_LINE);
        int squareBracketCounter = 0;
        int figureBracketCounter = 0;

        for (char symbol : jsonChars) {

            if (OPEN_ARRAY_BRACKET.equals(symbol)) {
                squareBracketCounter++;
            }
            if (CLOSE_ARRAY_BRACKET.equals(symbol)) {
                squareBracketCounter--;
            }
            if (OPEN_FIGURE_BRACKET.equals(symbol)) {
                figureBracketCounter++;
            }
            if (CLOSE_FIGURE_BRACKET.equals(symbol)) {
                figureBracketCounter--;
            }

            if (COLON.equals(symbol) && squareBracketCounter == 0 && figureBracketCounter == 0) {
                keyData = currentValue.toString().trim().replace(QUOTATION_MARK, EMPTY_LINE);
                currentValue = new StringBuilder();
                continue;
            }

            if (COMMA.equals(symbol) && squareBracketCounter == 0 && figureBracketCounter == 0) {
                valueData = currentValue.toString().trim().replace(QUOTATION_MARK, EMPTY_LINE);
                currentValue = new StringBuilder();
                fieldValueData.put(keyData, valueData);
                keyData = EMPTY_LINE;
                continue;
            }
            currentValue.append(symbol);
        }
        return fieldValueData;
    }

    private T createEmptyInstance(Class<?> clazz) {
        T newInstance = null;

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Optional<Constructor<?>> emptyConstructorOpt = Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .findFirst();
        Optional<Constructor<?>> parametrizedConstructorOpt = Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterTypes().length != 0)
                .findFirst();

        Object[] parameters = null;
        if (parametrizedConstructorOpt.isPresent()) {
            int lengthOfParameters = parametrizedConstructorOpt.get().getParameterTypes().length;
            parameters = new Object[lengthOfParameters];
            for (int i = 0; i < lengthOfParameters; i++) {
                parameters[i] = null;
            }
        }
        try {
            newInstance = emptyConstructorOpt.isPresent()
                    ? (T) emptyConstructorOpt.get().newInstance()
                    : (T) parametrizedConstructorOpt.get().newInstance(parameters);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException(String.format(
                    "exception occurred during new %s instance with null fields creation",
                    clazz.getName()));
        }
        return newInstance;
    }

    private Optional<Method> getValueOfMethod(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> "valueOf" .equals(method.getName())
                        && method.getParameterTypes().length == 1
                        && Arrays.stream(method.getParameterTypes())
                        .map(Class::getName)
                        .anyMatch(name -> name.equals("java.lang.String")))
                .findFirst();
    }

    private void arrayProcessing(T newInstance, String value, Field field) throws ClassNotFoundException,
            InvocationTargetException, IllegalAccessException {
        String [] arrayValues = value.substring(1,value.length() -1).split(COMMA.toString());

        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Type genericType = parameterizedType.getActualTypeArguments()[0];

        List<Object> valueList = new ArrayList<>();
        Optional<Method> valueOfGenericMethod = getValueOfMethod(Class.forName(((Class<?>) genericType).getName()));
        if (valueOfGenericMethod.isPresent()) {
            for (String arrayValue : arrayValues) {
                valueList.add(valueOfGenericMethod.get().invoke(null, arrayValue));
            }
        } else {
            valueList.addAll(Arrays.asList(arrayValues));
        }
        field.set(newInstance, field.getType().cast(valueList));
    }


}
