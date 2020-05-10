package list;

/**
 * 
 * [二叉树]
 * 
 * [ 中序遍历：左 根 右 ] [ 先序遍历：根 左 右 ] [ 后序遍历：左 右 根 ]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月11日]
 */
public class BinaryTree
{
    public static void main(String[] args) {
        System.out.println("--------add---------");
        int[] list=new int[]{23,83,43,23,9,12,84};
        Tree tree = new Tree();
        for (int i : list) {
            tree.insertNode(i);
        }
        tree.printNode(Tree.PRINT_MID);
        System.out.println("--------del---------");
        tree.delNode(12);
        tree.printNode(Tree.PRINT_MID);

    }

}

class Tree
{
    // 根节点
    private Node root;

    public static final String PRINT_MID = "left_root_right";
    public static final String PRINT_FRONT = "root_left_right";
    public static final String PRINT_BACK = "left_right_root";

    // 插入节点
    public void insertNode(int key) {
        if (root == null) {
            root = new Node(key);
        }
        else {
            root.insertNode(key);
        }
    }

    // 删除节点
    public void delNode(int key) {
        Node node = root.findNode(key);
        if (node != null) {
            root.delNode(node);
        }
    }

    // 中序打印节点
    public void printNode(String order) {
        if (root != null) {
            switch (order) {
                case PRINT_MID:
                    root.printNode();
                    break;
                case PRINT_FRONT:
                    root.printNodeFront();
                    break;
                case PRINT_BACK:
                    root.printNodeBack();
                    break;
            }
            System.out.println();
        }
    }

    private class Node
    {
        // 左节点
        private Node left;
        // 右节点
        private Node right;
        // 父节点
        private Node parent;

        private int key;

        public Node(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        // 插入节点
        public void insertNode(int key) {
            // 小于等于的值插在左边
            if (key <= this.getKey()) {
                if (left == null) {
                    left = new Node(key);
                    left.parent = this;
                }
                else {
                    left.insertNode(key);
                }
            }
            // 大于的值插在右边
            else {
                if (right == null) {
                    right = new Node(key);
                    right.parent = this;
                }
                else {
                    right.insertNode(key);
                }
            }
        }

        // 删除节点
        public Node delNode(Node node) {
            // 返回删除节点，仅返回用
            Node rtnNode = node;
            Node rNode = null;
            if (node.parent == null) {
                rNode = node;
                // 为底下node.parent.left==node.left做铺垫
                node.parent = rNode;
            }

            // 删除节点下存在两个节点
            if (node.left != null && node.right != null) {
                // 获取后继节点
                Node successor = node.getSuccessor(node.right);
                // 将后继节点的父节点的左节点变成后继节点的右节点
                if (successor.right != null) {
                    successor.parent.left = successor.right;
                    successor.right.parent = successor.parent;
                }
                else {
                    successor.parent.left = null;
                }

                if (node.parent.left == null) {
                    node.parent.left = successor;
                }
                else {
                    node.parent.right = successor;
                }
                // 值转换
                node.setKey(successor.getKey());
                node.right = successor.parent;
                successor = node;
                // 改变node的指向
                node = successor;
            }
            // 删除节点下只有一个节点或没有节点
            else {
                // 删除节点左节点有值
                if (node.left != null) {
                    // 删除节点的父节点的左节点为删除节点的左节点
                    if (node.parent.left == node) {
                        node.parent.left = node.left;
                    }
                    else {
                        node.parent.right = node.left;
                    }
                }
                // 删除节点右节点有值
                else if (node.right != null) {
                    if (node.parent.left == node) {
                        node.parent.left = node.right;
                    }
                    else {
                        node.parent.right = node.right;
                    }
                }
                // 删除节点下没有节点
                else {
                    if (node.parent.left == node) {
                        node.parent.left = null;
                    }
                    else {
                        node.parent.right = null;
                    }
                }
            }
            // 删除的节点为父节点
            if (rNode != null && rNode.parent == null) {
                root = node;
            }
            return rtnNode;
        }

        // 搜索节点
        public Node findNode(int key) {
            //为当前值直接返回
            if (this.getKey() == key) {
                return this;
            }
            //该节点下还存在节点
            if (left != null || right != null) {
                if (key <= this.getKey()) {
                    return this.left.findNode(key);
                }
                else {
                    return this.right.findNode(key);
                }
            }
            return null;
        }

        /**
         * 
         * [获取后继节点] 比删除节点大的后面一个节点
         * 
         * @return
         * @exception/throws [违例类型] [违例说明]
         * @see [类、类#方法、类#成员]
         */
        public Node getSuccessor(Node parent) {
            if (parent.left != null) {
                return parent.left.getSuccessor(parent.left);
            }
            return parent;
        }

        // 中序遍历
        public void printNode() {
            if (left != null) {
                left.printNode();
            }
            System.out.print(this.getKey() + " -> ");
            if (right != null) {
                right.printNode();
            }
        }

        // 前序遍历
        public void printNodeFront() {
            System.out.print(this.getKey() + " -> ");
            if (left != null) {
                left.printNodeFront();
            }
            if (right != null) {
                right.printNodeFront();
            }
        }

        // 后序遍历
        public void printNodeBack() {
            if (left != null) {
                left.printNodeBack();
            }
            if (right != null) {
                right.printNodeBack();
            }
            System.out.print(this.getKey() + " -> ");
        }

    }

}
