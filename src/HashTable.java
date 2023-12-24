import java.util.*;

public class HashTable<T> implements Iterable<Entry<T>> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.80d;
    private LinkedList<Entry<T>>[] slots;
    private int count;
    private int capacity;

    public HashTable() {
        this.capacity = INITIAL_CAPACITY;
        this.slots = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            slots[i] = new LinkedList<>();
        }

    }

    public HashTable(int capacity) {
        this.capacity = capacity;
        this.slots = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            slots[i] = new LinkedList<>();
        }
    }

    public int countCollisions() {
        int collisionCount = 0;

        for (LinkedList<Entry<T>> bucket : slots) {
            if (bucket.size() > 1) {
                collisionCount += bucket.size() - 1;
            }
        }

        return collisionCount;
    }

    public void add(String key, T value) {
        int slot = findSlotNumber(key);
        LinkedList<Entry<T>> bucket = slots[slot];

        for (Entry<T> entry : bucket) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        count++;
        growIfNeeded();
    }

    private int findSlotNumber(String key) {
        return Math.abs(key.hashCode()) % this.slots.length;
    }

    private void growIfNeeded() {
        if ((double) (this.size() + 1) / this.capacity() > LOAD_FACTOR) {
            this.grow();
        }
    }

    private void grow() {
        int newCapacity = this.capacity * 2;
        LinkedList<Entry<T>>[] newSlots = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newSlots[i] = new LinkedList<>();
        }

        for (LinkedList<Entry<T>> bucket : slots) {
            for (Entry<T> entry : bucket) {
                int slot = Math.abs(entry.getKey().hashCode()) % newCapacity;
                newSlots[slot].add(entry);
            }
        }

        this.slots = newSlots;
        this.capacity = newCapacity;
    }

    public int size() {
        return count;
    }

    public int capacity() {
        return capacity;
    }

    public boolean addOrReplace(String key, T value) {
        int slot = findSlotNumber(key);
        LinkedList<Entry<T>> bucket = slots[slot];

        for (Entry<T> entry : bucket) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return false;
            }
        }

        bucket.add(new Entry<>(key, value));
        count++;
        growIfNeeded();
        return true;
    }

    public T get(String key) {
        int slot = findSlotNumber(key);
        LinkedList<Entry<T>> bucket = slots[slot];

        for (Entry<T> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public Entry<T> find(String key) {
        int slot = findSlotNumber(key);
        LinkedList<Entry<T>> bucket = slots[slot];

        for (Entry<T> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry;
            }
        }
        return null;
    }

    public boolean containsKey(String key) {
        int slot = findSlotNumber(key);
        LinkedList<Entry<T>> bucket = slots[slot];

        for (Entry<T> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }

        return false;
    }

    public boolean remove(String key) {
        int slot = findSlotNumber(key);
        LinkedList<Entry<T>> bucket = slots[slot];

        Iterator<Entry<T>> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Entry<T> entry = iterator.next();
            if (entry.getKey().equals(key)) {
                iterator.remove();
                count--;
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            slots[i].clear();
        }
        count = 0;
    }

    public Iterable<String> keys() {
        LinkedList<String> keys = new LinkedList<>();

        for (LinkedList<Entry<T>> bucket : slots) {
            for (Entry<T> entry : bucket) {
                keys.add(entry.getKey());
            }
        }

        return keys;
    }

    @Override
    public Iterator<Entry<T>> iterator() {
            LinkedList<Entry<T>> allEntries = new LinkedList<>();
            for (LinkedList<Entry<T>> bucket : slots) {
                allEntries.addAll(bucket);
            }
            return allEntries.iterator();
    }

    public double getLoadFactor(){
        return (double) (this.size() + 1) / this.capacity();
    }

}