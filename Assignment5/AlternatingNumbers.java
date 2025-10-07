class Even extends Thread
{
    PrintNumbers pnobj;
    Even(PrintNumbers pnobj) {
        this.pnobj = pnobj;
    }
    @Override
    public void run() {
        pnobj.printNumber();
    }
}

class Odd extends Thread
{
    PrintNumbers pnobj;
    Odd(PrintNumbers pnobj) {
        this.pnobj = pnobj;
    }
    public void  run() {
        pnobj.printNumber();
    }
}
class PrintNumbers
{
    int count = 1;
    boolean flag = false;

    public synchronized void printNumber()
    {
        while(count <= 20) {
            if(flag) {
                System.out.println(Thread.currentThread().getName() + " -> " + count++);
                flag = false;
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            else {
                flag = true;
                notify();
            }
            notify(); // this will make sure that the process must end after the completion of the while loop.
        }
    }
}
public class AlternatingNumbers {
    public static void main(String[] args) {
        PrintNumbers a1 =  new PrintNumbers();
        Even even = new Even(a1);
        Odd odd = new Odd(a1);
        even.setName("EvenThread");
        odd.setName("OddThread");
        odd.start();
        even.start();
    }
}