     package wudi;

     import jdk.nashorn.internal.codegen.CompilerConstants;

     import javax.xml.crypto.Data;
     import java.text.DateFormat;
     import java.text.SimpleDateFormat;
     import java.util.Date;
     import java.util.concurrent.Callable;
     import java.util.concurrent.ExecutionException;
     import java.util.concurrent.FutureTask;

     /*1，进程与线程
       进程：操作系统中一个程序的执行周期称为一个进程
       线程：进程中的一个任务，一个进程中包括N个线程。
       区别：
         1.每个进程拥有自己的一整套变量，是操作系统中资源分配的最小单位，
         线程依托于进程存在，多个线程共享进程的资源，线程是os中任务调度的基本单位
         2.启动，撤销一个进程的开销要比线程的大的多。
         3.没有进程就没有线程，进程一旦终止，其内部线程全部撤销。
           高并发：同一时刻线程的访问量非常非常高。
       2：Java多线程实现
         2.1 继承Thread类实现多线程
          java.lang.Thread是线程操作的核心类，JDK1.0提供
          新建一个线程最简单的方法就是直接继承Thread类而后覆写类中的run()方法（相当于主方法  线程的入口）。
          无论哪种方式实现多线程，线程启动一律调用Thread类提供的start()方法。
          start()方法解析：
            1.首先检查线程状态是否为0（线程状态为0表示未启动），如果线程已经启动再次调用start方法会抛出IllegalThreadStateException。
            一个线程的start()只能调用一次。
            2.private native void start0()通过start0真正将线程启动；
            3.JVM调用start0方法进行资源分配与系统调度，准备好资源启动线程后回调run()来执行线程的具体任务！
          2.2  Runnable接口实现多线程
             Thread类定义：public class Thread implements Runnable
              I. 在Java中多线程的处理就是一个典型的代理模式，
              其中Thread类完成资源调度，系统分配辅助线程业务类：自定义的类负责真实业务实现。
               II.使用Runnable接口实现的多线程程序类可以更好的描述资源共享。
          2.3 Callable<V>实现多线程
          覆写call<V>方法
              juc:高并发程序包
              java.util.concurrent.Callable<V>
              V call () throws Exception :线程执行后有返回值V

              取得Callable接口call方法的返回值：
              V get () throws InterruptedException
              当线程需要返回值时只能采用Callable接口实现多线程。
          3.多线程常用操作方法
           3.1 线程命名与取得
           I.通过构造方法将线程命名
           public Thread(String)
           public Thread(Runnable target,String name)

           II.设置线程名称
           public final synchronized void setName(String name)

           III.取得线程名称
           public final String getName()

           IV.取得当前正在执行的线程对象  ********重要
           public static native Thread currentThread()
           Java中的main实际上是一个主线程

           3.2线程休眠：指的是让线程暂缓执行，等到了预计时间再恢复执行。
           线程休眠会立即交出CPU。让CPU去执行其他任务。线程休眠不会释放对象锁。
           public static native void sleep(long millis) throws InterruptedException
           休眠时间使用毫秒作为单位
           从运行到阻塞态
           3.3线程让步(yield)
           暂停当前正在执行的线程对象，并执行其他线程。
           yield()会让当前线程交出CPU，但不一定立即交出。
           yield()交出CPU后，只能让拥有相同优先级的线程有获取CPU的机会，
           并且不会释放对象锁。
           public static native void yield()
           从运行态到就绪态
           3.4  join()方法
           等待该线程终止。
           意思就是如果在  主线程  中调用该方法时就会让主线程休眠，
           让调用该方法的线程run方法先执行完毕之后在开始执行主线程。
           join()不是本地方法，只是对Object类wait()方法做了一层包装而已。
           从运行态到阻塞态
           3.5线程停止
            1. 设置标记位，可以是线程正常退出。
            2. 使用Thread类中的一个interrupt() 可以中断线程。
            3.使用stop方法强制使线程退出，但是该方法不太安全所以已经被起用了。
           3.6 线程优先级(1-10)
           线程优先级指的是，线程优先级越高越有可能先执行，但仅仅是有可能而已。

           设置优先级：
           public final void setPriority(int newPriority)
           取得优先级：
           public final int getPriority()
          4.线程同步
          每一个线程对象轮番抢占资源带来的问题
           4.1 同步问题的产生
           4.2 同步处理
             4.2.1synchronized处理同步问题
             synchronized处理同步有两种模式：同步代码块，同步方法




      */
//方法1  继承Thread类
/*
class MyThread extends Thread{
    private String title;
    public MyThread(String title){
        this.title=title;
    }

    @Override
    public void run() {
        for(int i=0;i<3;i++){
            System.out.println(this.title+","+i);
        }
    }
}
*/
//方法2  Runnable接口
/*
class MyThread implements Runnable{
    private String title;
    public MyThread(String title){
        this.title=title;
    }

    @Override
    public void run() {
        for(int i=0;i<3;i++){
            System.out.println(this.title+","+i);
        }
    }
}
*/
//继承Thread类练习
/*
class MyThread extends Thread{
    private String title;
    private int tiket=10;
    public MyThread(String title){
        this.title=title;
    }
    public void run(){
        for(int i=0;i<10;i++){
            System.out.println(this.title+"还剩下"+tiket--+"票");
        }
    }
}
*/
//使用Runnable接口练习
/*
class MyThread implements Runnable{
    private int ticket=10;
    @Override
    public void run() {
        for(int i=0;i<10;i++){
            System.out.println("还剩下"+ticket--+"票");
        }
    }

}
*/
//使用Callable定义线程主体类。
/*
class MyThread implements Callable<String>{
    private int ticket=10;
    public String call(){
        for(int i=0;i<10;i++){
            System.out.println("还剩下"+ticket--+"票");
        }
        return "票卖完了，客官明天见";
    }
}
*/
//观察线程名称取得
     /*
     class MyThread implements Runnable{
         private int ticket=10;

         @Override
         public void run() {
             for(int i=0;i<10;i++){
                 System.out.println(Thread.currentThread().getName()+
                 "还剩下"+this.ticket--+"票");
             }
         }
     }
*/
     //sleep例子1
     /*
     class MyThread extends Thread{
         private int ticket=10;

         @Override
         public void run() {
             for(int i=0;i<10;i++){
                 System.out.println("还剩下"+this.ticket--+"票");
             }
         }
     }
     */
     //sleep例子2
     /*
     class MyThread extends Thread{
         private int ticket=10;

         @Override
         public void run() {
             for(int i=0;i<10;i++){
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 System.out.println(Thread.currentThread().getName()+"还剩下"+i);
             }
         }
     }
     */
     //yield方法
     /*
     class MyThread extends Thread{
         @Override
         public void run() {
             for(int i=0;i<10;i++){
                 Thread.yield();
                 System.out.println(Thread.currentThread().getName()+"还剩下"+i);
             }
         }
     }
     */
     //join()方法
     /*
     class MyThread extends Thread{
         @Override
         public void run() {
             System.out.println("子线程开始执行");
             TestDemo.printTime();
             try {
                 Thread.sleep(2000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             TestDemo.printTime();
             System.out.println("子线程结束");
         }
     }
     */
     //设计标记位停止线程
     /*
     class MyThread extends Thread{
         private boolean flag=true;
         @Override
         public void run() {
             int i = 1;
             while (flag) {
                 try {
                     Thread.sleep(1000);
                     System.out.println("第" + i + "次执行，线程名称为："
                             + Thread.currentThread().getName());
                     i++;
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }
             public void setFlag(boolean flag) {
                 this.flag = flag;
         }
     }
     */
     //线程优先级
     /*
     class MyThread implements Runnable{
         @Override
         public void run() {
             for(int i=0;i<3;i++){
                 System.out.println(Thread.currentThread().getName()+","+i);
             }
         }
     }
     */
     //守护线程
     /*
     class MyThread extends Thread{
         private int i=0;
         @Override
         public void run() {
             while(true){
                 try {
                     Thread.sleep(500);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 i++;
                 System.out.println(Thread.currentThread().getName()
                         +","+i);
             }
         }
     }
     */
     //同步问题的产生
     /*
     class MyThread implements Runnable{
         private int ticket=10;

         @Override
         public void run() {
             while(this.ticket>0){
                 System.out.println(Thread.currentThread().getName()+
                         ",还有"+ this.ticket--+"票");
             }
         }
     }
     */
     //synchronized同步代码块
     /*
     class MyThread extends Thread{
         private int ticket =30;

         @Override
         public void run() {
             while(this.ticket>0) {
                 try {
                     Thread.sleep(200);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 synchronized (this) {
                     if (this.ticket > 0) {
                         System.out.println(Thread.currentThread().getName() +
                                 ",还有" + this.ticket-- + " 张票");
                         if(this.ticket==0){
                             System.out.println("票卖完了...");
                         }
                     }
                 }
             }
         }
     }
     */
     //同步方法synchronized
     /*
     class MyThread extends Thread {
         private int ticket=30;
         @Override
         public void run() {
             while (this.ticket > 0) {
                 try {
                     Thread.sleep(300);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 sale();
             }
         }

         public synchronized void sale() {
             if (this.ticket > 0) {
                 System.out.println(Thread.currentThread().getName() +
                         ",还有" + this.ticket-- + "张票");
                 if (this.ticket == 0) {
                     System.out.println("票卖完了...");
                 }
             }
         }
     }
     */
public class TestDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
         /*
        //1
        MyThread thread1=new MyThread("线程A");
        MyThread thread2=new MyThread("线程B");
        MyThread thread3=new MyThread("线程C");
        thread1.start();
        thread2.start();
        thread3.start();
        */
        /*
        //2
        MyThread thread1=new MyThread("A");
        MyThread thread2=new MyThread("B");
        MyThread thread3=new MyThread("C");
        Thread threadA=new Thread(thread1);
        Thread threadB=new Thread(thread2);
        Thread threadC=new Thread(thread3);
        threadA.start();
        threadB.start();
        threadC.start();
        */
             //使用Lamdba表达式进行Runnable对象创建
             //new Thread(()-> System.out.println("Hello world")).start();
             //使用匿名内部类进行Runnable对象创建。
        /*
        Runnable runnable=new Runnable() {
            //匿名内部类
            @Override
            public void run() {

            }
        };
        */
        //练习
         /*
        MyThread thread1=new MyThread("黄牛A");
        MyThread thread2=new MyThread("黄牛B");
        MyThread thread3=new MyThread("黄牛C");
        thread1.start();
        thread2.start();
        thread3.start();
        */
        //练习
         /*
        MyThread mt=new MyThread();
        Thread threadA=new Thread(mt);
        Thread threadB=new Thread(mt);
        Thread threadC=new Thread(mt);
        threadA.start();
        threadB.start();
        threadC.start();
        */
        //Callable启动并取得多线程的执行结果
        /*
        Callable <String> callable= new MyThread();
        FutureTask <String> futureTask=new FutureTask<>(callable);
        Thread thread=new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());
        */
        //构造方法命名线程
        /*
        MyThread mt=new MyThread();
        Thread thread1=new Thread(mt,"黄牛A");
        Thread thread2=new Thread(mt,"黄牛B");
        Thread thread3=new Thread(mt,"黄牛C");
        Thread thread4=new Thread(mt,"黄牛D");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        */
        //Sleep方法
        /*
        System.out.println("main方法开始");
        MyThread thread1=new MyThread();
        thread1.start();
        Thread.sleep(2000);
        System.out.println("main方法结束");
        */
        /*
        MyThread mt=new MyThread();
        Thread thread1=new Thread(mt,"线程1");
        Thread thread2=new Thread(mt,"线程2");
        Thread thread3=new Thread(mt,"线程3");
        thread1.start();
        thread2.start();
        thread3.start();
        */
        //yield()方法
        /*
        MyThread mt=new MyThread();
        Thread thread1=new Thread(mt,"线程1");
        Thread thread2=new Thread(mt,"线程2");
        Thread thread3=new Thread(mt,"线程3");
        thread1.start();
        thread2.start();
        thread3.start();
        */
        //join()方法
        /*
        System.out.println("main方法开始");
        MyThread mt=new MyThread();
        Thread thread=new Thread(mt);
        thread.start();
        thread.join();
        System.out.println("main方法结束");
    }
    public static void printTime(){
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        System.out.println(time);
        */
        //设计标记位停止线程
        /*
        MyThread myThread=new MyThread();
        Thread thread1=new Thread(myThread,"子线程A");
        thread1.start();
        Thread.sleep(5000);
        myThread.setFlag(false);
        System.out.println("代码结束");
        */
        //线程优先级
        //输出主方法优先级
        /*
        System.out.println(Thread.currentThread().getName()+","
                +Thread.currentThread().getPriority());
        MyThread mt=new MyThread();
        Thread thread1=new Thread(mt,"线程A");
        Thread thread2=new Thread(mt,"线程B");
        Thread thread3=new Thread(mt,"线程C");
        thread1.setPriority(Thread.MIN_PRIORITY);
        thread2.setPriority(Thread.MAX_PRIORITY);
        thread3.setPriority(Thread.NORM_PRIORITY);
        thread1.start();
        thread2.start();
        thread3.start();
        */
        /*
        MyThread mt=new MyThread();
        Thread thread=new Thread(mt,"子线程A");
        thread.setDaemon(true);
        thread.start();
        Thread.sleep(3000);
        System.out.println("main 执行");
        System.out.println("main 执行");
        System.out.println("main 执行完了");
        */
        //同步问题产生
        /*
        MyThread mt=new MyThread();
        Thread thread1=new Thread(mt,"黄牛A");
        Thread thread2=new Thread(mt,"黄牛B");
        Thread thread3=new Thread(mt,"黄牛C");
        thread1.start();
        thread2.start();
        thread3.start();
        */
        /*
        MyThread mt=new MyThread();
        Thread thread1=new Thread(mt,"黄牛A");
        Thread thread2=new Thread(mt,"黄牛B");
        Thread thread3=new Thread(mt,"黄牛C");
        thread1.start();
        thread2.start();
        thread3.start();
        */
        /*
        MyThread mt=new MyThread();
        Thread thread1=new Thread(mt,"黄牛A");
        Thread thread2=new Thread(mt,"黄牛B");
        Thread thread3=new Thread(mt,"黄牛C");
        thread1.start();
        thread2.start();
        thread3.start();
        */

   }
}

