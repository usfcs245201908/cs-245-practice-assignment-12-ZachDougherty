import java.util.ArrayList;

public class Hashtable {
    public class Hashnode {
        String key;
        String value;
        Hashnode next;

        public Hashnode(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final double LOAD_FACTOR = 0.5;
    private ArrayList<Hashnode> buckets;
    private int numBuckets;
    private int size;

    public Hashtable() {
        buckets = new ArrayList<Hashnode>();
        numBuckets = 10;
        size = 0;
        
        for (int i=0; i < numBuckets; i++) {
            buckets.add(i, null);
        }
    }

    private int getBucket(String key) {
        int hash = key.hashCode();
        int index = hash % numBuckets;
        return index;
    }

    public void put(String key, String value) {
        int bucket = getBucket(key);
        Hashnode head = buckets.get(bucket);

        // if key already exists in the list. 
        while(head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        head = buckets.get(bucket);
        Hashnode newNode = new Hashnode(key, value);
        newNode.next = head;
        buckets.set(bucket, newNode);

        // double size if load factor exceeds 0.5
        if ((1.0*size)/numBuckets >= LOAD_FACTOR) { 
            ArrayList<Hashnode> temp = buckets; 
            buckets = new ArrayList<Hashnode>(); 
            numBuckets *= 2;
            int tempSize = size;
            size = 0; // since we add all pairs back in, need to set size to 0
                      // and then we can safely add them back in.
            for (int i = 0; i < numBuckets; i++) 
                buckets.add(null); 
  
            for (int i=0; i < tempSize; i ++) {
                Hashnode headNode = temp.get(i);
                if (headNode != null) {
                    while (headNode != null) 
                    { 
                        put(headNode.key, headNode.value); 
                        headNode = headNode.next; 
                    }
                } 
            }
        }
    }

    public String remove(String key) throws Exception {
        int bucket = getBucket(key);
        Hashnode head = buckets.get(bucket);

        Hashnode prev = null;
        while (head != null && !head.key.equals(key)) {
            prev = head;
            head = head.next;
        }

        if (head == null) {
            throw new Exception("Not in Hashtable");
        }

        if (prev != null) {
            prev.next = head.next;
        } else {
            // if node to be removed is at the head
            buckets.set(bucket, head.next);
        }

        return head.value;
    }

    public boolean containsKey(String key) {
        int bucket = getBucket(key);
        Hashnode head = buckets.get(bucket);

        while (head != null) {
            if (head.key.equals(key)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }

    public String get(String key) {
        int bucket = getBucket(key);
        Hashnode head = buckets.get(bucket);
        
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }
}