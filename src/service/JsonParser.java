package service;

public interface JsonParser<T> {

    T parseToObject(String json);

    String parseToJson(T t);
}
