package seckill;

import org.apache.http.HttpRequest;

/**
 * Created by F on 2017/5/10.
 */
public class Processor {

    //构造函数，传入一个request
    public static void processor(){
        BidInfo bidInfo = new BidInfo(RequestQueue.queue.poll());
        if(bidInfo != null){
            kill(bidInfo);
        }
    }


    /*
    发送到数据库队列
     */
    public static void kill(BidInfo bidInfo){
        //加入db的队列
    }


    static class BidInfo{
        BidInfo(HttpRequest httpRequest){
            //构造函数 do someting

        }
    }
}
