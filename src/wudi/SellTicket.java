package wudi;
class TicketTask implements Runnable{
    private int ticket=1000;
    @Override
    public void run() {
       for (int i=0;i<1000;i++) {
           /*
           //1.需要在判断处上锁（同步代码块）
           synchronized (this) {
               //任意一个时刻只能有一个线程进入判断条件
               if (ticket > 0) {
                   try {
                       Thread.sleep(200);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   System.out.println(Thread.currentThread().getName() + "还剩下" + ticket-- + "票");
               }
           }
           */
           sellTicket();
       }
    }
    //2.同步方法，在任意时刻只能有一个线程进入此方法
    public synchronized void sellTicket(){
        if(ticket>0){
            System.out.println(Thread.currentThread().getName()+ "还剩下"+ticket--+"票");
        }
    }
}
public class SellTicket {
    public static void main(String[] args) {
        TicketTask ticketTask=new TicketTask();
        Thread thread1=new Thread(ticketTask,"黄牛A");
        Thread thread2=new Thread(ticketTask,"黄牛B");
        Thread thread3=new Thread(ticketTask,"黄牛C");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
