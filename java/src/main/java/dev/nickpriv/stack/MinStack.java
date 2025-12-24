package dev.nickpriv.stack;

import java.util.ArrayList;
import java.util.List;

public class MinStack {

    final List<Integer> values;
    final List<Integer> mins;

    private static final String EMPTY_STACK_ERROR = "Stack is empty";

    public MinStack() {
        values = new ArrayList<>();
        mins = new ArrayList<>();
    }

    public void push(final int val) {
        values.add(val);
        if (mins.isEmpty() || val <= mins.getLast()) {
            mins.add(val);
        }
    }

    public int pop() {
        if (values.isEmpty()) {
            throw new IllegalStateException(EMPTY_STACK_ERROR);
        }
        final int val = values.removeLast();
        if (val == mins.getLast()) {
            mins.removeLast();
        }

        return val;
    }

    public int top() {
        if (values.isEmpty()) {
            throw new IllegalStateException(EMPTY_STACK_ERROR);
        }
        return values.getLast();
    }

    public int getMin() {
        if (mins.isEmpty()) {
            throw new IllegalStateException(EMPTY_STACK_ERROR);
        }
        return mins.getLast();
    }
}
