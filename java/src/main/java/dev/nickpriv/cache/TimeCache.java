package dev.nickpriv.cache;

import java.util.*;

/**
 * Time-based key value store implementation.
 */
public class TimeCache {

    private static final String NO_RESULT_RESPONSE = "";

    final Map<String, List<Data>> cache;

    public TimeCache() {
        cache = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        cache.computeIfAbsent(key, k -> new ArrayList<>()).add(new Data(value, timestamp));
    }

    public String get(String key, int timestamp) {
        if (!cache.containsKey(key)) return NO_RESULT_RESPONSE;

        return binarySearch(cache.get(key), timestamp);
    }

    private String binarySearch(final List<Data> dataForKey, int timestamp) {
        int lo = 0;
        int hi = dataForKey.size() - 1;

        while (lo <= hi) {
            int med = (hi - lo) / 2 + lo;

            if (dataForKey.get(med).timestamp() > timestamp) {
                hi = med - 1;
            } else if (dataForKey.get(med).timestamp() == timestamp || med == dataForKey.size()-1 ||
                    dataForKey.get(med + 1).timestamp() > timestamp) {
                return dataForKey.get(med).value();
            } else {
                lo = med + 1;
            }
        }

        return NO_RESULT_RESPONSE;
    }
}

record Data(String value, int timestamp) {}