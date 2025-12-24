package dev.nickpriv.cache;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    final Map<Integer, Node> nodeForKey;
    final int capacity;
    final Node dummyHead;
    final Node dummyTail;

    public LRUCache(final int capacity) {
        this.capacity = capacity;
        nodeForKey = new HashMap<>();
        dummyHead = new Node(-1, -1);
        dummyTail = new Node(-1, -1);
        dummyHead.setNext(dummyTail);
        dummyTail.setPrev(dummyHead);
    }

    public int get(final int key) {
        if (!nodeForKey.containsKey(key)) {
            return -1;
        }
        final Node node = nodeForKey.get(key);
        removeNode(node);
        addNodeToFront(node);
        return node.getValue();
    }

    public void put(final int key, final int value) {
        if (nodeForKey.containsKey(key)) {
            final Node node = nodeForKey.get(key);
            node.setValue(value);
            removeNode(node);
            addNodeToFront(node);
        } else {
            if (nodeForKey.size() >= capacity) {
                final Node lruNode = dummyTail.getPrev();
                removeNode(lruNode);
                nodeForKey.remove(lruNode.getKey());
            }
            final Node newNode = new Node(key, value);
            addNodeToFront(newNode);
            nodeForKey.put(key, newNode);
        }
    }

    private void removeNode(@NonNull final Node node) {
        final Node prev = node.getPrev();
        final Node next = node.getNext();
        prev.setNext(next);
        next.setPrev(prev);
    }

    private void addNodeToFront(@NonNull final Node node) {
        final Node firstRealNode = dummyHead.getNext();
        dummyHead.setNext(node);
        node.setPrev(dummyHead);
        node.setNext(firstRealNode);
        firstRealNode.setPrev(node);
    }
}
