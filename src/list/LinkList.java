package list;

/**
 * 
 * [链表]
 * 
 * @author baB_hyf
 * @version [版本号, 2019年9月11日]
 */
public class LinkList
{
    public static void main(String[] args) {
        System.out.println("---------add----------\n");
        Linked linked = new Linked();
        linked.addNode(1);
        linked.addNode(2);
        linked.addNode(3);
        linked.addNode(4);
        linked.addNode(5);
        linked.addNode(6);
        linked.printNode();
        System.out.println("\n\n---------del----------\n");
        linked.delNode(3);
        linked.printNode();
        System.out.println("\n\n--------find----------\n");
        boolean find = linked.findNode(4);
        System.out.println("4节点是否存在：" + find);
        System.out.println("\n-------update---------\n");
        linked.updateNode(5, 15);
        linked.printNode();
        System.out.println("\n\n-------insert---------\n");
        // 前插
        linked.insertNode(2, 12);
        linked.printNode();

    }

}

class Linked
{
    // 根节点
    private Node root;
    // 记录深度
    private int currentIndex;

    // 添加节点
    public void addNode(int data) {
        if (root == null) {
            root = new Node(data);
        }
        else {
            root.addNode(data);
        }
    }

    // 删除节点
    public boolean delNode(int data) {
        if (root == null) {
            return false;
        }
        if (root.getData() == data) {
            root = root.next;
            return true;
        }
        else {
            return root.delNode(data);
        }
    }

    // 输出所有节点
    public void printNode() {
        if (root == null) {
            System.out.println("当前链表为空！");
        }
        else {
            root.printNode();
        }
    }

    // 查找节点
    public boolean findNode(int data) {
        if (root == null) {
            return false;
        }
        if (root.getData() == data) {
            return true;
        }
        else {
            return root.findNode(data);
        }
    }

    // 修改节点
    public boolean updateNode(int oldData, int newData) {
        if (root == null) {
            System.out.println("没有该节点！");
            return false;
        }
        else if (root.getData() == oldData) {
            root.setData(newData);
            return true;
        }
        else {
            return root.updateNode(oldData, newData);
        }
    }

    // 插入节点
    public boolean insertNode(int index, int data) {
        if (root == null) {
            return false;
        }

        currentIndex = 0;
        // 前插
        if (currentIndex == index) {
            Node node = new Node(data);
            node.next = root;
            root = node;
            return true;
        }
        else {
            return root.insertNode(index, data);
        }
    }

    private class Node
    {
        // 下一个节点
        private Node next;
        // 节点数据
        private int data;

        public Node(int data) {
            this.data = data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public int getData() {
            return data;
        }

        // 添加节点
        public void addNode(int data) {
            if (next == null) {
                next = new Node(data);
            }
            else {
                next.addNode(data);
            }
        }

        // 删除节点
        public boolean delNode(int data) {
            if (next == null) {
                return false;
            }
            if (next.getData() == data) {
                // 当前节点后没有节点了
                if (next.next == null) {
                    return false;
                }
                else {
                    next = next.next;
                    return true;
                }
            }
            else {
                return next.delNode(data);
            }
        }

        // 输出所有节点
        public void printNode() {
            System.out.print(this.getData());
            if (next != null) {
                System.out.print(" -> ");
                next.printNode();
            }
        }

        // 查找节点
        public boolean findNode(int data) {
            if (next == null) {
                return false;
            }
            else if (next.getData() == data) {
                return true;
            }
            else {
                return next.findNode(data);
            }
        }

        // 修改节点
        public boolean updateNode(int oldData, int newData) {
            if (next == null) {
                return false;
            }
            else if (next.getData() == oldData) {
                next.setData(newData);
                return true;
            }
            else {
                return next.updateNode(oldData, newData);
            }
        }

        // 插入节点
        public boolean insertNode(int index, int data) {
            if (next == null) {
                return false;
            }

            // 向后遍历
            if (++currentIndex == index) {
                Node node = new Node(data);
                node.next = next;
                next = node;
                return true;
            }
            else {
                return next.insertNode(index, data);
            }
        }

    }

}
