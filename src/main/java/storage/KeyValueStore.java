package storage;

import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {
    private final ConcurrentHashMap<String, Object> store = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> expirations = new ConcurrentHashMap<>();

    public boolean set(String key, Object value) {
        store.put(key, value);
        return true;
    }

    public boolean set(String key, Object value, long expirationMillis) {
        store.put(key, value);
        if (expirationMillis > 0) {
            expirations.put(key, System.currentTimeMillis() + expirationMillis);
        }
        return true;
    }

    public Object get(String key) {
        Long expiration = expirations.get(key);
        if (expiration != null && expiration < System.currentTimeMillis()) {
            store.remove(key);
            expirations.remove(key);
            return null;
        }
        return store.get(key);
    }

    public boolean delete(String key) {
        store.remove(key);
        expirations.remove(key);
        return true;
    }

    public boolean exists(String key) {
        if (expirations.containsKey(key) && System.currentTimeMillis() > expirations.get(key)) {
            store.remove(key);
            expirations.remove(key);
            return false;
        }
        return store.containsKey(key);
    }
}
