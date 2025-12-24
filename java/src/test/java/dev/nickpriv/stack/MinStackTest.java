package dev.nickpriv.stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MinStackTest {

    private MinStack minStack;

    @BeforeEach
    public void setup() {
        minStack = new MinStack();
    }

    @Test
    public void testPopOnEmptyStack_shouldThrowException() {
        assertThrows(
                IllegalStateException.class,
                () -> minStack.pop(),
                "Expected IllegalStateException when popping from empty stack"
        );
    }

    @Test
    public void testTopOnEmptyStack_shouldThrowException() {
        assertThrows(
                IllegalStateException.class,
                () -> minStack.top(),
                "Expected IllegalStateException when retrieving top from empty stack"
        );
    }

    @Test
    public void testGetMinOnEmptyStack_shouldThrowException() {
        assertThrows(
                IllegalStateException.class,
                () -> minStack.getMin(),
                "Expected IllegalStateException when retrieving min from empty stack"
        );
    }

    @Test
    public void testPushAndPopSingleElement() {
        minStack.push(5);
        assertEquals(5, minStack.top(), "Expected top to be 5 after pushing 5");
        assertEquals(5, minStack.getMin(), "Expected min to be 5 after pushing 5");
        assertEquals(5, minStack.pop(), "Expected pop to return 5");
    }

    @Test
    public void testPushMultipleElementsAndGetMin() {
        minStack.push(3);
        minStack.push(1);
        minStack.push(2);
        assertEquals(1, minStack.getMin(), "Expected min to be 1 after pushing 3, 1, 2");
        minStack.pop();
        assertEquals(1, minStack.getMin(), "Expected min to still be 1 after popping 2");
        minStack.pop();
        assertEquals(3, minStack.getMin(), "Expected min to be 3 after popping 1");
    }

    @Test
    public void testPushDuplicateMins() {
        minStack.push(2);
        minStack.push(2);
        minStack.push(1);
        assertEquals(1, minStack.getMin(), "Expected min to be 1 after pushing 2, 2, 1");
        minStack.pop();
        assertEquals(2, minStack.getMin(), "Expected min to be 2 after popping 1");
        minStack.pop();
        assertEquals(2, minStack.getMin(), "Expected min to still be 2 after popping one 2");
    }
}
