package service;

import exception.BusinessException;
import util.JsonSeparatorUtil;
import util.PrimitiveUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static util.Constants.CLOSE_ARRAY_BRACKET;
import static util.Constants.CLOSE_FIGURE_BRACKET;
import static util.Constants.COMMA;
import static util.Constants.OPEN_ARRAY_BRACKET;
import static util.Constants.OPEN_FIGURE_BRACKET;

public class DefaultJsonParser<T> implements JsonParser<T> {

    @Override
    public T parseToObject(String json, Class<?> clazz) {
        final Map<String, String> fieldValueData = JsonSeparatorUtil.createFieldValueData(json);
        final ReflectionService<T> reflectionService = new ReflectionService<>();
        T newInstance = reflectionService.createEmptyInstance(clazz);
        try {
            Field[] fields = newInstance.getClass().getDeclaredFields();
            for (Field field : fields) {
                String value = fieldValueData.get(field.getName());
                field.setAccessible(true);
                if (field.getType().isPrimitive()) {
                    String primitiveName = field.getType().getName();
                    field.set(newInstance, PrimitiveUtil.getPrimitiveValue(primitiveName, value));
                    continue;
                }

                Optional<Method> valueOfMethod = reflectionService.getValueOfMethod(field.getType());
                if (value.startsWith(OPEN_FIGURE_BRACKET.toString()) && value.endsWith(CLOSE_FIGURE_BRACKET.toString())) {
                    Object result = new DefaultJsonParser<>().parseToObject(value, field.getType());
                    field.set(newInstance, field.getType().cast(result));
                    continue;
                }
                if (value.startsWith(OPEN_ARRAY_BRACKET.toString()) && value.endsWith(CLOSE_ARRAY_BRACKET.toString())) {
                    arrayProcessing(reflectionService, newInstance, value, field);
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

    @Override
    public String parseToJson(T t) {
//        TODO: Realize that
        Field[] declaredFields = t.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(t);
                System.out.println("---");
            } catch (IllegalAccessException e) {
                throw new BusinessException("sfdvbgdgsf");
            }
        }
//        declaredFields[0].getName()
        return null;
    }

    private void arrayProcessing(ReflectionService<T> reflectionService,
                                 T newInstance,
                                 String value,
                                 Field field) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        String[] arrayValues = value.substring(1, value.length() - 1).split(COMMA.toString());

        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        Type genericType = parameterizedType.getActualTypeArguments()[0];

        List<Object> valueList = new ArrayList<>();
        final Optional<Method> valueOfGenericMethod = reflectionService
                .getValueOfMethod(Class.forName(((Class<?>) genericType).getName()));
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
