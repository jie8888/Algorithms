package chapter13;

import org.junit.Test;

/**
 * 红黑树:
 * 1,每个节点都有颜色：黑色或红色；
 * 2，根节点颜色为黑色；
 * 3，叶结点颜色为黑色；
 * 4，红色结点的子节点颜色为黑色；
 * 5，从某结点到叶节点的黑色结点数相同；
 * (细节决定成败)
 *
 * @author kissx on 2017/10/16.
 */
public class RedBlackTree<T extends Comparable<T>> {

    private Node<T> root = null;
    private final Node<T> nullNode = new Node<>();

    public static class Node<T> {
        Node<T> pNode;
        Node<T> lcNode;
        Node<T> rcNode;
        T value;
        boolean isRed; // true 代表红色，false 代表黑色

        public Node getLcNode() {
            return lcNode;
        }

        public Node getRcNode() {
            return rcNode;
        }

        public Node getpNode() {
            return pNode;
        }

        public T getValue() {
            return value;
        }

        public boolean isRed() {
            return isRed;
        }
    }

    public void insert(T value) {
        Node<T> pNode = nullNode;
        Node<T> cNode = root;
        while (cNode != nullNode && cNode != null) {
            pNode = cNode;
            if (value.compareTo(cNode.value) >= 0)
                cNode = cNode.rcNode;
            else
                cNode = cNode.lcNode;
        }
        Node<T> node = new Node<>();
        node.value = value;
        node.isRed = true;
        node.lcNode = nullNode;
        node.rcNode = nullNode;
        if (pNode == nullNode) {
            root = node;
            node.pNode = nullNode;
        } else if (value.compareTo(pNode.value) >= 0) {
            pNode.rcNode = node;
            node.pNode = pNode;
        } else {
            pNode.lcNode = node;
            node.pNode = pNode;
        }
        insertFixUp(node);
    }

    private void insertFixUp(Node<T> z) {
        while (z.pNode.isRed) {
            if (z.pNode.pNode.lcNode == z.pNode) {
                Node<T> y = z.pNode.pNode.rcNode;
                if (y.isRed) {                       // case 1
                    y.isRed = false;
                    z.pNode.isRed = false;
                    z.pNode.pNode.isRed = true;
                    z = z.pNode.pNode;
                } else if (z.pNode.rcNode == z) {                            // case 2
                    z.pNode.rcNode = z.lcNode;
                    z.pNode.rcNode.pNode = z.pNode;
                    y = z.pNode;
                    leftRotate(z.pNode, z);
                    z.lcNode = y;
                    z.lcNode.pNode = z;
                    z = z.lcNode;
                } else {                                                    // case 3
                    z = z.pNode;
                    z.isRed = false;
                    z.pNode.isRed = true;
                    z.pNode.lcNode = z.rcNode;
                    z.pNode.lcNode.pNode = z.pNode;
                    y = z.pNode;
                    rightRotate(z.pNode, z);
                    z.rcNode = y;
                    z.rcNode.pNode = z;
                    break;
                }
            } else {
                Node<T> y = z.pNode.pNode.lcNode;
                if (y.isRed) {                                              // case 1
                    y.isRed = false;
                    z.pNode.isRed = false;
                    z.pNode.pNode.isRed = true;
                    z = z.pNode.pNode;
                } else if (z.pNode.lcNode == z) {                            // case 2
                    z.pNode.lcNode = z.rcNode;
                    z.pNode.lcNode.pNode = z.pNode;
                    y = z.pNode;
                    rightRotate(z.pNode, z);
                    z.rcNode = y;
                    z.rcNode.pNode = z;
                    z = z.rcNode;
                } else {                                                    // case 3
                    z = z.pNode;
                    z.isRed = false;
                    z.pNode.isRed = true;
                    z.pNode.rcNode = z.lcNode;
                    z.pNode.rcNode.pNode = z.pNode;
                    y = z.pNode;
                    leftRotate(z.pNode, z);
                    z.lcNode = y;
                    z.lcNode.pNode = z;
                    break;
                }
            }
        }
        this.root.isRed = false;
    }

    private void leftRotate(Node n1, Node n2) {
        transplant(n1, n2);
        n1.rcNode = n2.lcNode;
        n1.rcNode.pNode = n1;
        n2.lcNode = n1;
        n2.lcNode.pNode = n2;
    }

    private void rightRotate(Node n1, Node n2) {
        transplant(n1, n2);
        n1.lcNode = n2.rcNode;
        n1.lcNode.pNode = n1;
        n2.rcNode = n1;
        n2.rcNode.pNode = n2;
    }

    @SuppressWarnings("unchecked")
    private void transplant(Node u, Node v) {
        if (this.root == u) {
            this.root = v;
        } else if (u.pNode.lcNode == u) {
            u.pNode.lcNode = v;
        } else {
            u.pNode.rcNode = v;
        }
        v.pNode = u.pNode;
    }

    /*

    public Node<T> delete(Node<T> z) {
        if (z != nullNode && z != null) {
            Node<T> x = nullNode;
            Node<T> y = z;
            boolean isORed = y.isRed();
            if (z.lcNode == nullNode && z.rcNode == nullNode) {
                if (z.pNode == nullNode)
                    this.root = nullNode;
                else if (z.pNode.lcNode == z)
                    z.pNode.lcNode = nullNode;
                else
                    z.pNode.rcNode = nullNode;
            } else if (z.lcNode == nullNode) {
                x = z.rcNode;
                transplant(z, z.rcNode);
            } else if (z.rcNode == nullNode) {
                x = z.lcNode;
                transplant(z, z.lcNode);
            } else {
                y = getMin(z.rcNode);
                x = y.rcNode;
                if (y.pNode == z)
                    x.pNode = y;
                isORed = y.isRed();
                if (y.rcNode != nullNode) {
                    transplant(y, y.rcNode);
                } else if (y.pNode != z)
                    y.pNode.lcNode = nullNode;
                Node<T> temp = y.pNode;
                transplant(z, y);
                y.lcNode = z.lcNode;
                y.lcNode.pNode = z;
                if (temp != z) {
                    y.rcNode = z.rcNode;
                    y.rcNode.pNode = y;
                }
            }
            if (!isORed)
                deleteFixUp(x);
        }
        return z;
    }

    private void deleteFixUp(Node<T> x) {
        while (x != root && !x.isRed) {
            if (x == x.pNode.lcNode) {
                Node<T> w = x.pNode.rcNode;
                if (w.isRed) {
                    w.isRed = false;
                    x.pNode.isRed = true;
                    leftRotate(x.pNode, x);
                    w = x.pNode.rcNode;
                }
                if (!w.lcNode.isRed && !w.rcNode.isRed) {
                    w.isRed = true;
                    x = x.pNode;
                } else {
                    if (!w.rcNode.isRed) {
                        w.lcNode.isRed = false;
                        w.isRed = true;
                        rightRotate(w, w.pNode);
                        w = x.pNode.rcNode;
                    }
                    w.isRed = x.pNode.isRed;
                    x.pNode.isRed = false;
                    w.rcNode.isRed = false;
                    leftRotate(x.pNode, x);
                    x = this.root;
                }
            } else {
                Node<T> w = x.pNode.lcNode;
                if (w.isRed) {
                    w.isRed = false;
                    x.pNode.isRed = true;
                    rightRotate(x.pNode, x);
                    w = x.pNode.lcNode;
                }
                if (!w.rcNode.isRed && !w.lcNode.isRed) {
                    w.isRed = true;
                    x = x.pNode;
                } else {
                    if (!w.lcNode.isRed) {
                        w.rcNode.isRed = false;
                        w.isRed = true;
                        leftRotate(w, w.pNode);
                        w = x.pNode.lcNode;
                    }
                    w.isRed = x.pNode.isRed;
                    x.pNode.isRed = false;
                    w.lcNode.isRed = false;
                    rightRotate(x.pNode, x);
                    x = this.root;
                }
            }
        }
    }

    private Node<T> getMin(Node<T> n) {
        Node<T> cNode = n;
        Node<T> pNode = nullNode;
        while (cNode != nullNode) {
            pNode = cNode;
            cNode = cNode.lcNode;
        }
        return pNode;
    }

    */

    /**
     * @param value value值
     * @return 返回第一个与 value 相等的结点。若没有查到则返回 NULL。
     */
    public Node<T> search(T value) {
        Node<T> cNode = root;
        while (cNode != null) {
            if (cNode.value.compareTo(value) == 0)
                return cNode;
            cNode = cNode.value.compareTo(value) > 0 ? cNode.lcNode : cNode.rcNode;
        }
        return null;
    }


//    ------------------------ TEST ------------------------

    @Test
    public void testInsert() {
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
//        redBlackTree.insert(4);
//        redBlackTree.insert(14);
//        redBlackTree.insert(5);
//        redBlackTree.insert(15);
//        redBlackTree.insert(1);
//        redBlackTree.insert(7);
//        redBlackTree.insert(2);
//        redBlackTree.insert(11);
//        redBlackTree.insert(8);
        redBlackTree.insert(11);
        redBlackTree.insert(2);
        redBlackTree.insert(14);
        redBlackTree.insert(1);
        redBlackTree.insert(7);
        redBlackTree.insert(15);
        redBlackTree.insert(5);
        redBlackTree.insert(8);
        redBlackTree.insert(4);
        System.out.println(redBlackTree.toString());
    }
/*
    @Test
    public void testDelete() {
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
        redBlackTree.insert(4);
        redBlackTree.insert(14);
        redBlackTree.insert(5);
        redBlackTree.insert(15);
        redBlackTree.insert(1);
        redBlackTree.insert(7);
        redBlackTree.insert(2);
        redBlackTree.insert(11);
        redBlackTree.insert(8);
        Node<Integer> n = redBlackTree.search(14);
        redBlackTree.delete(n);
        System.out.println("-------------END-------------");
    }
*/

}
