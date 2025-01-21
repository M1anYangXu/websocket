package web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "ws_staff")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    // 主键自增
    @TableId(type = IdType.AUTO)
    private Byte staffId;
    private String firstName;
    private String username;
    private String password;
    private String email;
    private String lastUpdate;
}
