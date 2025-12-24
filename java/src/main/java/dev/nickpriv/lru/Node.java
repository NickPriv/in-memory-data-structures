package dev.nickpriv.lru;

import lombok.Data;
import lombok.NonNull;

@Data
public class Node {
    private Integer key;
    private Integer value;
    private Node prev;
    private Node next;

    public Node(@NonNull final Integer key, @NonNull final Integer value) {
        this.key = key;
        this.value = value;
        prev = null;
        next = null;
    }
}
