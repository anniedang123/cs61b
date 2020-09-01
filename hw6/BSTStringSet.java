import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
       Node l = find(s);
        if (l == null) {
            _root = new Node(s);
        } else {
            int c = s.compareTo(l.s);
            if (c < 0) {
                l.left = new Node(s);
            } else if (c > 0) {
                l.right = new Node(s);
            }
        }
    }


    @Override
    public boolean contains(String s) {
        Node last = find(s);
        return last != null && s.equals(last.s);
    }


    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (String lab : this) {
            result.add(lab);
        }
        return result;
    }

    private Node find(String s) {
        if (_root == null) {
            return null;
        }
        Node p;
        p = _root;
        while (true) {
            int c = s.compareTo(p.s);
            Node next;
            if (c < 0) {
                next = p.left;
            } else if (c > 0) {
                next = p.right;
            } else {
                return p;
            }
            if (next == null) {
                return p;
            } else {
                p = next;
            }
        }
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }
    private class Bounded implements Iterator<String> {

        private Stack<Node> pos;
        private Node cur;
        private String U;
        private String L;

        public Bounded(String L, String U){
            this.L = L;
            this.U = U;
            this.pos = new Stack<Node>();
            cur = _root;
        }


        @Override
        public boolean hasNext() {
            return !pos.isEmpty() || (cur != null && cur.s.compareTo(U) <= 0);
        }

        @Override
        public String next() {
            while(cur != null && cur.s.compareTo(L) >= 0){
                pos.push(cur);
                cur = cur.left;
            }

            Node retr = pos.pop();
            cur = retr.right;

            return retr.s;
        }

    }
    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new Bounded(low, high);
    }


    /** Root node of the tree. */
    private Node _root;
}
