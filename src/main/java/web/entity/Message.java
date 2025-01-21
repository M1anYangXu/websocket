package web.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    // 发送者名字
    public String from;
    
    // 接受者名字
    public String to;
    
    // 要发送的内容
    public String content;

    // 发送时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date date;
}
