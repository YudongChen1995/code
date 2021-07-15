1、	两个链表有没有交叉， 两个栈实现一个队列
https://blog.csdn.net/mccand1234/article/details/52892380
public class FindIntersect {
    public static class Node {
        public int value;
        public Node next;

        public Node(int data) {
            this.value = data;
        }
    }
/*判断是否相交，如果相交，得到第一个相交点*/
public static Node getIntersectNode(Node head1, Node head2) {
    if (head1 == null || head2 == null) {
        return null;
    }
    Node loop1 = getLoopNode(head1);
    Node loop2 = getLoopNode(head2);
    if (loop1 == null && loop2 == null) {
        return noLoop(head1, head2);
    }
    if (loop1 != null && loop2 != null) {
        return bothLoop(head1, loop1, head2, loop2);
    }
    return null;
}

// 慢走k,  快走2k  假设慢点距离 环点 为m  1圈也为k
// k(慢)-m =环点.  所以头走k-m 和 慢点走 k-m 相遇在环点
public static Node getLoopNode(Node head) {
    if (head == null || head.next == null || head.next.next == null) {
        return null;
    }
    Node n1 = head.next; // n1 -> slow
    Node n2 = head.next.next; // n2 -> fast
    while (n1 != n2) {
        if (n2.next == null || n2.next.next == null) {
            return null;
        }
        n2 = n2.next.next;
        n1 = n1.next;
    }
    n2 = head; // n2 -> walk again from head
    while (n1 != n2) {
        n1 = n1.next;
        n2 = n2.next;
    }
    return n1;
}
/*无环时的判断方法*/
public static Node noLoop(Node head1, Node head2) {
    if (head1 == null || head2 == null) {
        return null;
    }
    Node cur1 = head1;
    Node cur2 = head2;
    int n = 0;
    while (cur1.next != null) {
        n++;
        cur1 = cur1.next;
    }
    while (cur2.next != null) {
        n--;
        cur2 = cur2.next;
    }
    if (cur1 != cur2) {
        return null;
    }
    //找到 长条设为 cur1  短条设为cur2
    cur1 = n > 0 ? head1 : head2;
    cur2 = cur1 == head1 ? head2 : head1;
    n = Math.abs(n);
    //走长条多出来那部分, 然后下面一起走 看等不等于
    while (n != 0) {
        n--;
        cur1 = cur1.next;
    }
    while (cur1 != cur2) {
        cur1 = cur1.next;
        cur2 = cur2.next;
    }
    return cur1;
}
/*有环时的判断方法*/
public static Node bothLoop(Node head1, Node loop1, Node head2, Node loop2) {
    Node cur1 = null;
    Node cur2 = null;
    // 同个环点, 退化成长短条问题
    if (loop1 == loop2) {
        cur1 = head1;
        cur2 = head2;
        int n = 0;
        while (cur1 != loop1) {
            n++;
            cur1 = cur1.next;
        }
        while (cur2 != loop2) {
            n--;
            cur2 = cur2.next;
        }
        cur1 = n > 0 ? head1 : head2;
        cur2 = cur1 == head1 ? head2 : head1;
        n = Math.abs(n);
        while (n != 0) {
            n--;
            cur1 = cur1.next;
        }
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
        //不同环点. 转一圈看能不能遇到 list 2
    } else {
        cur1 = loop1.next;
        while (cur1 != loop1) {
            if (cur1 == loop2) {
                return loop1;
            }
            cur1 = cur1.next;
        }
        return null;
    }
}


//两个栈实现一个队列
class Queue<E>{  //用的jdk自带的栈
    private Stack<E> s1=new Stack<>();
    private Stack<E> s2=new Stack<>();
    public void offer(E val){   //入队
        s1.push(val);
    }
public E poll() {   //出队
//s1的全部加到s2, 相当于倒序
    while (s2.empty()){
        while (!s1.empty()){
            s2.push(s1.peek());
            s1.pop();
        }
    }
    //取出顶部
    E val=s2.peek();
    s2.pop();
    //获取出队元素后，再将s2里面的元素放入s1里面。
    while (!s2.empty()){
        s1.push(s2.pop());
    }
    return val;
}

public E peek(){//查看对头元素
    while (s2.empty()){
        while (!s1.empty()){
            s2.push(s1.peek());
            s1.pop();
        }
    }
    E val=s2.peek();
    //获取出队元素后，再将s2里面的元素放入s1里面。
    while (!s2.empty()){
        s1.push(s2.pop());
    }
    return val;
}

public boolean empty(){ //判断队是否为空
  return s1.empty();
}

//两个队列实现一个栈

class SeqStack<E>{
    private Queue<E> que1; // 存放栈的元素
    private Queue<E> que2; // 做一个辅助操作

    public SeqStack(){
        this.que1 = new Queue<>();
        this.que2 = new Queue<>();
    }

    public SeqStack(int size){
        this.que1 = new Queue<>(size);
        this.que2 = new Queue<>(size);
    }

    public void push(E val){
        this.que1.offer(val);
    }

    public E pop(){
        // 从que1出队，把最后一个出队的元素返回
        E data = null;
        /**
         * 把que1里面的所有元素出队，放入que2里面，
         * 然后把que1最后一个出队的元素直接返回，不用放入que2
         */
        while(!this.que1.empty()){
            data = this.que1.poll();
            // 最后一个不放到 que2  相当于消失
            
            if(this.que1.empty()){
                break;
            }
            this.que2.offer(data);
        }

        // 获取该出栈的元素以后，再把que2的元素再放入que1里面
        while(!this.que2.empty()){
            this.que1.offer(this.que2.poll());
        }

        return data;
    }

    public E top(){
        // 从que1出队，把最后一个出队的元素返回
        E data = null;

        while(!this.que1.empty()){
            data = this.que1.poll();
            this.que2.offer(data);
        }

        // 获取该出栈的元素以后，再把que2的元素再放入que1里面
        while(!this.que2.empty()){
            this.que1.offer(this.que2.poll());
        }

        return data;
    }

    public boolean full(){
        return this.que1.full();
    }

    public boolean empty(){
        return this.que1.empty();
    }
}
