package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import models.Crud;
import models.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {
    private String _port = "5000";
    private String _host = "localhost";
    private String _protocol = "http";
    private String _baseUrl = "api/easydata/models/__default/sources";
    private String _dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private String _jsonCharset = "utf-8";

    public void setPort(String _port) {
        this._port = _port;
    }

    public void setHost(String _host) {
        this._host = _host;
    }

    public void setProtocol(String _protocol) {
        this._protocol = _protocol;
    }

    public void setBaseUrl(String _baseUrl) {
        this._baseUrl = _baseUrl;
    }

    public void setDateFormat(String _dateFormat) {
        this._dateFormat = _dateFormat;
    }

    public void setJsonCharset(String _jsonCharset) {
        this._jsonCharset = _jsonCharset;
    }

    public <T> String generateURI(Class<T> clazz, Crud crud) {
        return String.format(_protocol + "://" + _host + ":" + _port + "/" + _baseUrl + "/" + clazz.getSimpleName() + "/" + crud.toString());
    }

    public <T> List<T> get(Class<T> clazz) {
        try {
            String uri = generateURI(clazz, Crud.get);

            NetworkThread t = new NetworkThread(uri, "", Method.GET.toString(), _jsonCharset);
            t.start();
            t.join();
            String jsonResponse = t.getResult();

            List<T> array = jsonArrayToList(jsonResponse, clazz);

            return array;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public <T> List<T> jsonArrayToList(String json, Class<T> elementClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType listType =
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass);
        return objectMapper.readValue(json, listType);
    }

    public int create(Object obj) {
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat(_dateFormat)
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();

            String body = gson.toJson(obj);

            String uri = generateURI(obj.getClass(), Crud.create);

            NetworkThread t = new NetworkThread(uri, body, Method.POST.toString(), _jsonCharset);
            t.start();
            t.join();
            String jsonResponse = t.getResult();

            JsonObject jsonObj = gson.fromJson(jsonResponse, JsonObject.class);
            return jsonObj.get("record").getAsJsonObject().get("Id").getAsInt();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }

    public <T> int update(T obj) {
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat(_dateFormat)
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();

            String body = gson.toJson(obj);

            String uri = generateURI(obj.getClass(), Crud.update);

            NetworkThread t = new NetworkThread(uri, body, Method.UPDATE.toString(), _jsonCharset);
            t.start();
            t.join();
            String jsonResponse = t.getResult();


            JsonObject jsonObj = gson.fromJson(jsonResponse, JsonObject.class);
            return jsonObj.get("record").getAsJsonObject().get("Id").getAsInt();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }

    public <T> void delete(int id, Class<T> clazz) {
        try {
            String uri = generateURI(clazz, Crud.delete);
            String body = "{Id: " + id + "}";

            NetworkThread t = new NetworkThread(uri, body, Method.DELETE.toString(), _jsonCharset);
            t.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
