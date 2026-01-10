package dev.nickpriv.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LRUCacheWithTTLTest {

    private LRUCacheWithTTL cache;

    @BeforeEach
    public void setup() {
        cache = new LRUCacheWithTTL(2, 100); // Capacity 2, TTL 100 ms
    }

    @Test
    public void testGetAfterPut_shouldRetrieveValue() {
        cache.put(1, 10);
        assertEquals(10, cache.get(1), "Expected value 10 for key 1");
    }

    @Test
    public void testGetNonExistentKey_shouldReturnMinusOne() {
        assertEquals(-1, cache.get(2), "Expected -1 for non-existent key 2");
    }

    @Test
    public void testPutExceedingCapacity_shouldEvictLRU() {
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30); // This should evict key 1
        assertEquals(-1, cache.get(1), "Expected -1 for evicted key 1");
        assertEquals(20, cache.get(2), "Expected value 20 for key 2");
        assertEquals(30, cache.get(3), "Expected value 30 for key 3");
    }

    @Test
    public void testUpdateExistingKey_shouldUpdateValueAndOrder() {
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(1, 15); // Update key 1
        cache.put(3, 30); // This should evict key 2
        assertEquals(15, cache.get(1), "Expected updated value 15 for key 1");
        assertEquals(-1, cache.get(2), "Expected -1 for evicted key 2");
    }

    @Test
    public void testAccessOrder_shouldAffectEviction() {
        cache.put(1, 10);
        cache.put(2, 20);
        cache.get(1); // Access key 1 to make it most recently used
        cache.put(3, 30); // This should evict key 2
        assertEquals(10, cache.get(1), "Expected value 10 for key 1");
        assertEquals(-1, cache.get(2), "Expected -1 for evicted key 2");
        assertEquals(30, cache.get(3), "Expected value 30 for key 3");
    }

    @Test
    public void testTTLExpiration_shouldExpireEntry() throws InterruptedException {
        cache.put(1, 10);
        Thread.sleep(110); // Wait for TTL to expire
        assertEquals(-1, cache.get(1), "Expected -1 for expired key 1");
    }

    @Test
    public void testTTLResetOnGet_shouldResetExpiration() throws InterruptedException {
        cache.put(1, 10);
        Thread.sleep(50);
        assertEquals(10, cache.get(1), "Expected value 10 for key 1 before TTL expiration");
        Thread.sleep(60); // Total sleep time is now 110 ms
        assertEquals(10, cache.get(1), "Expected value 10 for key 1 after TTL reset");
    }

    @Test
    public void testMultipleEntriesWithTTL_shouldExpireCorrectly() throws InterruptedException {
        cache.put(1, 10);
        cache.put(2, 20);
        Thread.sleep(60);
        cache.put(3, 30); // This should evict key 1 if it has expired
        assertEquals(-1, cache.get(1), "Expected -1 for expired key 1");
        assertEquals(20, cache.get(2), "Expected value 20 for key 2");
        assertEquals(30, cache.get(3), "Expected value 30 for key 3");
    }
}
