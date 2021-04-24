package com.inmemory;

public class Cache {

    String key;
    String value;

    public Cache(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public Cache() {
    }


    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
