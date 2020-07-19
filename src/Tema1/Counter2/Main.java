package Tema1.Counter2;

public class Main {

    public static void main(String[] args) {
        RunnableAdder ra = new RunnableAdder("ThreadAdder");
        ra.start();

        RunnableSubtractor rs = new RunnableSubtractor("ThreadSubtractor");
        rs.start();
    }
}
