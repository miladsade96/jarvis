package operations;

import lombok.AllArgsConstructor;
import storage.KeyValueStore;

@AllArgsConstructor
public class StringOperations {
    private final KeyValueStore store;

    public boolean set(String key, String value) {
        store.set(key, value);
        return true;
    }

    public boolean setex(String key, String value, Long expireSeconds) {
        return store.set(key, value, expireSeconds * 1000);
    }

    public String get(String key) {
        Object value = store.get(key);
        return value == null ? null : value.toString();
    }

    public Long incr(String key) {
        synchronized (store) {
            String value = get(key);
            Long numValue = 0L;

            if (value != null) {
                try {
                    numValue = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Value at key is not an integer");
                }
            }

            numValue++;
            store.set(key, String.valueOf(numValue));
            return numValue;
        }
    }

    public Long incrBy(String key, Long delta) {
        synchronized (store) {
            String value = get(key);
            Long numValue = 0L;

            if (value != null) {
                try {
                    numValue = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Value at key is not an integer");
                }
            }

            numValue += delta;
            store.set(key, String.valueOf(numValue));
            return numValue;
        }
    }

    public Long decr(String key) {
        return incrBy(key, -1L);
    }

    public Long decrBy(String key, Long delta) {
        return incrBy(key, -delta);
    }

    public long strlen(String key) {
        String value = get(key);
        return value == null ? 0 : value.length();
    }
}
