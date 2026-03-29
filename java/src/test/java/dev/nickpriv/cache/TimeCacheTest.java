package dev.nickpriv.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeCacheTest {

    private static final String TEST_KEY_1 = "key1";
    private static final String TEST_VALUE_1 = "value1";
    private static final String TEST_KEY_2 = "key2";
    private static final String TEST_VALUE_2 = "value2";

    private final TimeCache cache;

    public TimeCacheTest() {
        cache = new TimeCache();
    }

    @Test
    public void testGetWhenKeyNotPresent_shouldReturnEmptyString() {
        final String output = cache.get(TEST_KEY_1, 0);
        assertEquals("", output);
    }

    @Test
    public void testGetWhenKeyPresentForTimestamp_shouldReturnValue() {
        cache.set(TEST_KEY_1, TEST_VALUE_1, 0);

        final String output = cache.get(TEST_KEY_1, 0);
        assertEquals(TEST_VALUE_1, output);
    }

    @Test
    public void testGetWhenKeyPresentForSmallerTimestamp_shouldReturnValue() {
        cache.set(TEST_KEY_1, TEST_VALUE_1, 0);

        final String output = cache.get(TEST_KEY_1, 1);
        assertEquals(TEST_VALUE_1, output);
    }

    @Test
    public void testGetWhenKeyPresentForLargerTimestamp_shouldReturnEmptyString() {
        cache.set(TEST_KEY_1, TEST_VALUE_1, 1);

        final String output = cache.get(TEST_KEY_1, 0);
        assertEquals("", output);
    }

    @Test
    public void testGetWhenKeyPresentForMultipleTimestamps_shouldReturnMostRecentValue() {
        cache.set(TEST_KEY_1, TEST_VALUE_1, 0);
        cache.set(TEST_KEY_1, TEST_VALUE_2, 1);

        final String output = cache.get(TEST_KEY_1, 1);
        assertEquals(TEST_VALUE_2, output);
    }

    @Test
    public void testGetWhenMultipleKeysPresentForTimestamp_shouldReturnAssociatedValue() {
        cache.set(TEST_KEY_1, TEST_VALUE_1, 0);
        cache.set(TEST_KEY_2, TEST_VALUE_2, 0);

        final String output = cache.get(TEST_KEY_1, 0);
        assertEquals(TEST_VALUE_1, output);
    }
}
