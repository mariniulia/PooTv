package pages;

import java.util.ArrayList;
import java.util.List;
/*
Clasa care implementeaza structura de arbore
 */

public final class Tree {
    public Node root;

    public Tree(final Page rootData) {
        root = new Node();
        root.data = rootData;
        root.children = new ArrayList<>();
    }

    public static class Node {
        public Page data;
        public List<Node> children = new ArrayList<>();

        public Node(final Page page) {
            this.data = page;
        }

        public Node() {
        }
    }
}
