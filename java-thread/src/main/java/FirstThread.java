/**
 * Created by 57257 on 2017/4/28.
 */
public class FirstThread extends Thread {

    private int i;
    @Override
    public void run() {
        System.out.println(getName());
    }

    public static void main(String[] args) {

        for(int i=0;i<100;i++){
            if(i%10==0){
                new FirstThread().start();
                new FirstThread().start();
            }
        }

    }


}


class SecondThread implements Runnable{

    private int i;
    @Override
    public void run() {
        for(;i<=100;i++){
            System.out.println(Thread.currentThread().getName());
        }
    }


    public static void main(String[] args) {

        for(int i=0 ;i<100;i++){
            if(i%10==0){

                SecondThread secondThread = new SecondThread();
                new Thread(secondThread,"线程1").start();
                new Thread(secondThread,"线程2").start();

            }
        }



    }
}
