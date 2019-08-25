package wudi;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Task implements Runnable{
    private int tickets=500;
    private Lock ticketLock=new ReentrantLock();
    @Override
    public void run() {
        for(int i=0;i<500;i++){
          try {
              ticketLock.lock();
              if(tickets>0) {
                  System.out.println(Thread.currentThread().getName() + "还剩下" + tickets--+ "票");
              }
          }finally {
              ticketLock.unlock();
          }
        }
    }
}
public class LockText {

    public static void main(String[] args) {
        Task task=new Task();
        Thread thread1=new Thread(task,"黄牛A");
        Thread thread2=new Thread(task,"黄牛B");
        Thread thread3=new Thread(task,"黄牛C");
        thread1.start();
        thread2.start();
        thread3.start();

    }
}
