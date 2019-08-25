package Executor;

import java.util.concurrent.*;

class CallableText implements Callable<String>{
    private int ticket=20;
    @Override
    public String call() {
        for (int i=0;i<20;i++){
            if(ticket>=0){
                System.out.println(Thread.currentThread().getName()+
                "还剩下"+ticket--+"票");
            }
        }
        return Thread.currentThread().getName()+"票卖完了";
    }
}
public class ExecutorText {
   public static void main(String[] args) {
        ExecutorService executorService=new ThreadPoolExecutor(2,3,
                60,TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        CallableText callableText=new CallableText();
        for (int i=0;i<5;i++){
            executorService.submit(callableText);
        }
        executorService.shutdown();
   }
}
