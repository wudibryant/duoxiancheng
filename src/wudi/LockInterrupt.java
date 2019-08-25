package wudi;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TaskInter implements Runnable{
    private Lock lock=new ReentrantLock();
    @Override
    public void run() {
           try {
               while (true){
                   lock.lockInterruptibly();
               }
           }catch (InterruptedException e) {
               System.out.println("线程响应中断");
           }finally {
           lock.unlock();
       }
    }
}
public class LockInterrupt {
    public static void main(String[] args) throws InterruptedException {
        TaskInter taskInter=new TaskInter();
        Thread thread=new Thread(taskInter);
        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}
