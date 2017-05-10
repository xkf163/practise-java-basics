package seckill;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by F on 2017/5/10.
 */
public class DB {

    public static int count = 10;
    public static ArrayBlockingQueue<Processor.BidInfo> bids = new ArrayBlockingQueue<Processor.BidInfo>(10);

    public static boolean checkReminds(){
        return true;
    }


    //处理bidInfo队列
    public static void bid(){
        //从队列取出
        Processor.BidInfo bidInfo  = bids.poll();
        while (count-- >0){
            //处理bidinfo请求逻辑


            //处理完成后再取出一个bidInfo
            bidInfo = bids.poll();
        }
    }

}
