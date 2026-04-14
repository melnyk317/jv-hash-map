package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private int threshold;
    private Entry<K, V>[] table;
    
    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.threshold = (int)(capacity * LOAD_FACTOR);
        this.table = new Entry[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            table = resize();
        }
        Entry<K, V> toPut = new Entry<K, V>(key, value);
        if (toPut.key == null) {
            if (table[0] == null) {
                table[0] = toPut;
                size++;
            } else {
                sameKey(toPut, 0);
            }
            return;
        }
        int index = key.hashCode() & (capacity - 1);
        if (table[index] == null) {
            table[index] = toPut;
            size++;
        } else {
            sameKey(toPut, index);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return findValue(0, key);
        } else {
            return findValue(key.hashCode() & (capacity - 1), key);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private V findValue(int index, K key) {
        Entry<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private void sameKey(Entry<K, V> toPut, int index) {
        Entry<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, toPut.key)) {
                current.value = toPut.value;
                return;
            }
            if (current.next == null) {
                current.next = toPut;
                size++;
                return;
            }
            current = current.next;
        }
    }

    private Entry<K, V>[] resize() {
        size = 0;
        capacity *= 2;
        threshold = (int)(capacity * LOAD_FACTOR);
        Entry<K, V>[] temp = table;
        table = new Entry[capacity];
        for (Entry<K, V> e : temp) {
            Entry<K, V> current = e;
            if (e != null) {
                while (current != null) {
                    put(current.key, current.value);
                    Entry<K, V> next = current.next;
                    current.next = null;
                    current = next;
                }
            }
        }
        return table;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;
        private int hash;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.hash = key == null ? 0 : key.hashCode();
        }
    }
}
