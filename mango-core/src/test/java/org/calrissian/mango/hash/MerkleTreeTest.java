package org.calrissian.mango.hash;

import org.calrissian.mango.hash.mock.MockLeaf;
import org.calrissian.mango.hash.tree.MerkleTree;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MerkleTreeTest {

    MockLeaf leaf1 = new MockLeaf("4");
    MockLeaf leaf2 = new MockLeaf("2");
    MockLeaf leaf3 = new MockLeaf("8");
    MockLeaf leaf4 = new MockLeaf("99");
    MockLeaf leaf5 = new MockLeaf("77");
    MockLeaf leaf6 = new MockLeaf("56");
    MockLeaf leaf7 = new MockLeaf("9");
    MockLeaf leaf8 = new MockLeaf("0");

    @Test
    public void testTreeBuilds() throws NoSuchAlgorithmException, IOException, ClassNotFoundException {


        List<MockLeaf> leaves = Arrays.asList(new MockLeaf[]{leaf1, leaf2, leaf8, leaf7, leaf4});

        MerkleTree<MockLeaf> tree = new MerkleTree<MockLeaf>(leaves, 2);

        System.out.println(tree);

        assertEquals("17a19db32d969668fb08f9a5491eb4fe", tree.getTopHash().getHash());
        assertEquals(2, tree.getTopHash().getChildren().size());
    }

    @Test
    public void testDiff_differentDimensionsFails() {

        List<MockLeaf> leaves = Arrays.asList(new MockLeaf[]{leaf1, leaf2});

        MerkleTree<MockLeaf> tree = new MerkleTree<MockLeaf>(leaves, 2);
        MerkleTree<MockLeaf> tree2 = new MerkleTree<MockLeaf>(leaves, 4);

        try {
            tree.diff(tree2);
            fail("Should have thrown exception");
        } catch(IllegalStateException e) { }
    }

    @Test
    public void testDiff_differentSizesFails() {

        List<MockLeaf> leaves = Arrays.asList(new MockLeaf[]{leaf1, leaf2});
        List<MockLeaf> leaves2 = Arrays.asList(new MockLeaf[]{leaf1, leaf2, leaf3});

        MerkleTree<MockLeaf> tree = new MerkleTree<MockLeaf>(leaves, 2);
        MerkleTree<MockLeaf> tree2 = new MerkleTree<MockLeaf>(leaves2, 2);

        try {
            tree.diff(tree2);
            fail("Should have thrown exception");
        } catch(IllegalStateException e) { }
    }

    @Test
    public void testEquals_false() {

        List<MockLeaf> leaves = Arrays.asList(new MockLeaf[]{leaf1, leaf2});
        List<MockLeaf> leaves2 = Arrays.asList(new MockLeaf[]{leaf1, leaf3});


        MerkleTree<MockLeaf> tree = new MerkleTree<MockLeaf>(leaves, 2);
        MerkleTree<MockLeaf> tree2 = new MerkleTree<MockLeaf>(leaves2, 2);

        assertFalse(tree.equals(tree2));
        assertFalse(tree2.equals(tree));
    }

    @Test
    public void testEquals_true() {

        List<MockLeaf> leaves = Arrays.asList(new MockLeaf[]{leaf1, leaf2});
        List<MockLeaf> leaves2 = Arrays.asList(new MockLeaf[]{leaf1, leaf2});

        MerkleTree<MockLeaf> tree = new MerkleTree<MockLeaf>(leaves, 2);
        MerkleTree<MockLeaf> tree2 = new MerkleTree<MockLeaf>(leaves2, 2);

        assertTrue(tree.equals(tree2));
        assertTrue(tree2.equals(tree));
    }

    @Test
    public void testDiff() {

        List<MockLeaf> leaves = Arrays.asList(new MockLeaf[]{leaf1, leaf2});
        List<MockLeaf> leaves2 = Arrays.asList(new MockLeaf[]{leaf1, leaf3});

        MerkleTree<MockLeaf> tree = new MerkleTree<MockLeaf>(leaves, 2);
        MerkleTree<MockLeaf> tree2 = new MerkleTree<MockLeaf>(leaves2, 2);

        List<MockLeaf> diffs = tree.diff(tree2);

        assertEquals(leaf2, diffs.get(0));
        assertEquals(1, diffs.size());

        diffs = tree2.diff(tree);

        assertEquals(leaf3, diffs.get(0));
        assertEquals(1, diffs.size());
    }
}
