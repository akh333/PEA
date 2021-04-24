package com.inmemory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApplicationScoped
public class CacheProvider {

    static Map<String, String> data = new ConcurrentHashMap<>();

    String get(String key) {
       return data.get(key);
    }

    String set(String key, String value) {
        return data.put(key,value);
    }

    List<String> search(Predicate<String> predicate){
       return  data.keySet().stream()
                .filter(predicate)
               .collect(Collectors.toList());

    }

}
