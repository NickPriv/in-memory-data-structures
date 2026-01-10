package dev.nickpriv.cache;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU Cache with Time-To-Live (TTL) functionality.
 * Entries expire after a specified TTL duration, or when the cache reaches its capacity.
 * Each entry is assigned the same TTL duration upon insertion or update.
 */
public class LRUCacheWithTTL {

    final Map<Integer, NodeWithTimestamp> nodeForKey;
    final int capacity;
    final NodeWithTimestamp dummyHead;
    final NodeWithTimestamp dummyTail;
    final long ttlMillis;

    public LRUCacheWithTTL(final int capacity, final long ttlMillis) {
        this.capacity = capacity;
        nodeForKey = new HashMap<>();
        dummyHead = new NodeWithTimestamp(-1, -1);
        dummyTail = new NodeWithTimestamp(-1, -1);
        dummyHead.setNext(dummyTail);
        dummyTail.setPrev(dummyHead);
        this.ttlMillis = ttlMillis;
    }

    public int get(final int key) {
        expireEntries();

        if (!nodeForKey.containsKey(key)) {
            return -1;
        }
        final NodeWithTimestamp node = nodeForKey.get(key);
        node.updateTimestamp();
        removeNode(node);
        addNodeToFront(node);
        return node.getValue();
    }

    public void put(final int key, final int value) {
        expireEntries();

        if (nodeForKey.containsKey(key)) {
            final NodeWithTimestamp node = nodeForKey.get(key);
            node.updateTimestamp();
            node.setValue(value);
            removeNode(node);
            addNodeToFront(node);
        } else {
            if (nodeForKey.size() >= capacity) {
                final NodeWithTimestamp lruNode = dummyTail.getPrev();
                removeNode(lruNode);
                nodeForKey.remove(lruNode.getKey());
            }
            final NodeWithTimestamp newNode = new NodeWithTimestamp(key, value);
            addNodeToFront(newNode);
            nodeForKey.put(key, newNode);
        }
    }

    private void removeNode(@NonNull final NodeWithTimestamp node) {
        final NodeWithTimestamp prev = node.getPrev();
        final NodeWithTimestamp next = node.getNext();
        prev.setNext(next);
        next.setPrev(prev);
    }

    private void addNodeToFront(@NonNull final NodeWithTimestamp node) {
        final NodeWithTimestamp firstRealNode = dummyHead.getNext();
        dummyHead.setNext(node);
        node.setPrev(dummyHead);
        node.setNext(firstRealNode);
        firstRealNode.setPrev(node);
    }

    private void expireEntries() {
        final long currentTime = System.currentTimeMillis();
        NodeWithTimestamp current = dummyTail.getPrev();
        while (current != dummyHead && currentTime - current.getTimestampMillis() > ttlMillis) {
            NodeWithTimestamp toRemove = current;
            current = current.getPrev();
            removeNode(toRemove);
            nodeForKey.remove(toRemove.getKey());
        }
    }
}
