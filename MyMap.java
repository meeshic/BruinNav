// Binary Search Tree based Map
public class MyMap<K extends Comparable, V> {
    private int size=0;
    private EntryBSTNode<K, V> root;
    
    private class EntryBSTNode<K extends Comparable, V> implements Comparable<EntryBSTNode<K, V>> {
        private K key;
        private V value;
        EntryBSTNode<K, V> right;
        EntryBSTNode<K, V> left;
        
        EntryBSTNode(K key, V value){
            this.key = key;
            this.value = value;
        }
        
        public int compareTo(EntryBSTNode<K, V> e){
            return this.key.compareTo(e.getKey());
        }
        
        public K getKey(){ return key; };
        
        public V getValue() {return value; };
        
        public void setValue(V value) { this.value = value; };
    } //end EntryBSTNode
    
    private void add(EntryBSTNode<K, V> curr, EntryBSTNode<K, V> toAdd){
        // if key exists, update existing value to new value
        if(toAdd.compareTo(curr) == 0)
            curr.setValue(toAdd.getValue());
        
        // right
        if(toAdd.compareTo(curr) > 0){
            if (curr.right == null) curr.right = toAdd;
            else add(curr.right, toAdd);
        }
        // left
        if(toAdd.compareTo(curr) < 0){
            if (curr.left == null) curr.left = toAdd;
            else add(curr.left, toAdd);
        }
    }
    
    // Clear all entry nodes in tree
    void clear(){
        root = null;
        size = 0;
    }
    
    // Return number of entry nodes in tree
    int size(){ return size; }
    
    // Create an entry node and add to tree
    // Acts as map.put(K, V)
    void associate(K key, V value){
        EntryBSTNode<K, V> entry = new EntryBSTNode<K, V>(key, value);
        
        if(root == null) root = entry;
        else add(root, entry);
        
        size++;
    }
    
    // Get entry node with desired key and return associated value
    // Otherwise, return NULL
    V find(K key){
        return findEntry(root, key);
    }
    
    private V findEntry(EntryBSTNode<K, V> curr, K key){
        if(curr == null) return null;
        if(key.compareTo(curr.getKey()) == 0) 
            return curr.getValue();
        
        return (key.compareTo(curr.getKey()) < 0) ? findEntry(curr.left, key) : findEntry(curr.right, key);
    }
}

