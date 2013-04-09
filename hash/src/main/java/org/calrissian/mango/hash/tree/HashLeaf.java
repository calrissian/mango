package org.calrissian.mango.hash.tree;


import java.util.List;

/**
 * A leaf represents a single hashed "bucket" of data- it needs to be sortable. Raw data should NOT be carried along
 * as serializable properties of the Leaf because this will negate the compressed effect of the data structure.
 */
public abstract class HashLeaf implements Node {

    protected String hash;

    public HashLeaf() {}

    public HashLeaf(String hash) {
        this.hash = hash;
    }

    @Override
    public List<Node> getChildren() {
        return null;
    }

    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +  "{" +
                "hash='" + hash + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashLeaf)) return false;

        HashLeaf leaf = (HashLeaf) o;

        if (hash != null ? !hash.equals(leaf.hash) : leaf.hash != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hash != null ? hash.hashCode() : 0;
    }
}
