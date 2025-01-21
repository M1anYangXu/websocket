package web.controller;

import com.alibaba.fastjson.JSON;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import web.entity.Message;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/ws/{username}")
@Component
public class WebSocket {
    
    // 静态变量 用来记录当前在线人数 保证了线程安全
    private static AtomicInteger onlineNum = new AtomicInteger();


    // concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();

    // 群发消息
    public void broadcast(String message){
        System.out.println("广播的消息message"+message);
        for (Session session: sessionPools.values()) {
            try {
                sendMessage(session, message);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    // 发送消息
    public void sendMessage(Session session,String message) throws IOException {
        System.out.println("发送消息后的sessionPools");
        if(session != null){
            // 线程加锁 防止多线程阻塞和冲突
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        }
    }
    //给指定用户发送信息
    public void sendInfo(String userName, String message){
        Session session = sessionPools.get(userName);
        try {
            sendMessage(session, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String userName){
        sessionPools.put(userName, session);
        addOnlineCount();
        System.out.println(userName + "加入webSocket！当前人数为" + onlineNum);
        // 广播上线消息
        Message msg = new Message();
        msg.setDate(new Date());
        msg.setTo("0");
        msg.setContent(userName);
        broadcast(JSON.toJSONString(msg,true));
    }

    //关闭连接时调用
    @OnClose
    public void onClose(@PathParam(value = "username") String userName){
        sessionPools.remove(userName);
        subOnlineCount();
        System.out.println(userName + "断开webSocket连接！当前人数为" + onlineNum);
        // 广播下线消息
        Message msg = new Message();
        msg.setDate(new Date());
        msg.setTo("-2");
        msg.setContent(userName);
        broadcast(JSON.toJSONString(msg,true));
    }

    //收到客户端信息后，根据接收人的username把消息推下去或者群发
    // to=-1群发消息
    @OnMessage
    public void onMessage(String message) throws IOException{
        System.out.println("服务器获得websocket的消息" + message);
        Message msg=JSON.parseObject(message, Message.class);
        msg.setDate(new Date());
        if (msg.getTo().equals("-1")) {
            broadcast(JSON.toJSONString(msg,true));
        } else {
            sendInfo(msg.getTo(), JSON.toJSONString(msg,true));
        }
    }
    
    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }


    // 增加人数
    public static void addOnlineCount(){
        onlineNum.incrementAndGet();
    }

    // 减少人数
    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }

    // 获取在线人数
    public static AtomicInteger getOnlineNumber() {
        return onlineNum;
    }
    
    // 获取session
    public static ConcurrentHashMap<String, Session> getSessionPools() {
        return sessionPools;
    }
}
