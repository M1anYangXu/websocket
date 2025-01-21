package web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import web.entity.Staff;

@Mapper
public interface StaffMapper extends BaseMapper<Staff> {
    
}
