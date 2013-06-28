/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.hash.tree;

import java.security.MessageDigest;
import java.util.List;

import static org.apache.commons.codec.binary.Hex.encodeHexString;
import static org.apache.commons.codec.digest.DigestUtils.getMd5Digest;

/**
 * An internal hash node in a merkle tree that automatically derives the hashes of its children to form its own hash.
 */
public class HashNode implements Node {

    protected String hash;
    protected List<Node> children;

    public HashNode() {}

    public HashNode(List<Node> children) {
        this.children = children;

        MessageDigest digest = getMd5Digest();
        for(Node node : children)
            digest.update((node.getHash() + "\u0000").getBytes());

        hash = encodeHexString(digest.digest());
    }

    /**
     * Accessor for the children that this Node's hash is comprised of
     * @return
     */
    @Override
    public List<Node> getChildren() {
        return children;
    }

    /**
     * If this node has children, aggregates the hashes of the children. If this node is a leaf, represents the hash
     * of the data.
     * @return
     */
    @Override
    public String getHash() {

        return hash;
    }

    @Override
    public String toString() {
        return "HashNode{" +
                "hash=" + hash +
                ", children=" + children +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashNode)) return false;

        HashNode hashNode = (HashNode) o;

        if (hash != null ? !hash.equals(hashNode.hash) : hashNode.hash != null) return false;
        if (children != null ? !children.equals(hashNode.children) : hashNode.children != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hash != null ? hash.hashCode() : 0;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }
}