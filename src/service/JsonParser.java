package service;

public interface JsonParser<T> {

    T parseToObject(String json, Class<?> clazz);

    String parseToJson(T t);
}
