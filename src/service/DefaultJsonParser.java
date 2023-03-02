package service;

import exception.BusinessException;

import java.lang.reflect.Field;

public class DefaultJsonParser<T> implements JsonParser<T> {

    @Override
    public T parseToObject(String json) {
        return null;
    }

    @Override
    public String parseToJson(T t) {
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
}
