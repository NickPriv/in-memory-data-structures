package dev.nickpriv.cache;

import lombok.Data;
import lombok.NonNull;

@Data
public class NodeWithTimestamp {
    private Integer key;
    private Integer value;
    private NodeWithTimestamp prev;
    private NodeWithTimestamp next;
    private long timestampMillis;

    public NodeWithTimestamp(@NonNull final Integer key, @NonNull final Integer value) {
        this.key = key;
        this.value = value;
        prev = null;
        next = null;
        timestampMillis = System.currentTimeMillis();
    }

    public void updateTimestamp() {
        this.timestampMillis = System.currentTimeMillis();
    }
}
