package test;

/**
 * @author baB_hyf
 * @date 2022/04/07
 */
public class ParentChild {

    public static void main(String[] args) {
        Child child = new Child();

    }
}

class Parent {

    static {
        System.out.println("clinit Parent before");
    }

    {
        System.out.println("init Parent before");
    }

    public Parent() {
        System.out.println("init Parent");
    }

    {
        System.out.println("init Parent after");
    }

    static {
        System.out.println("clinit Parent after");
    }

}

class Child extends Parent {

    static {
        System.out.println("clinit Child before");
    }

    {
        System.out.println("init Child before");
    }

    public Child() {
        System.out.println("init Child");
    }

    {
        System.out.println("init Child after");
    }

    static {
        System.out.println("clinit Child after");
    }

}