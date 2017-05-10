package seckill;

import org.apache.http.HttpRequest;

/**
 * Created by F on 2017/5/10.
 * 预处理阶段，把不必要的请求驳回
 */
public class PreProcessor {

    private static boolean reminds = true; //是否有剩余库存状态码

    //没有库存了
    private static void haveNothing(){
        //直接返回给用户，不需要继续执行
    }

    //判断是否有库存
    public static boolean isRemind(){
        if(reminds){
            //远程检测是否还有剩余，该RPC接口应由数据库服务器提供，不必完全严格检查.
            if(true){ // if(!RPC.checkReminds()){
                reminds = false;
            }
        }
        return reminds;
    }

    /*
    * 对每一个HTTPREquest请求预处理
     */
    public static void preProcessor(HttpRequest httpRequest){
        if(isRemind()){
            RequestQueue.queue.add(httpRequest);
        }else{
            haveNothing();
        }
    }



}
