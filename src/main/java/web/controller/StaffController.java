package web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import web.entity.Staff;
import web.entity.User;
import web.service.StaffService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/staff")
public class StaffController {
    
    @Autowired
    StaffService staffService;


    @RequestMapping("/onlineusers")
    @ResponseBody
    public Set<String> onlineusers(@RequestParam("currentuser") String currentuser) {
        ConcurrentHashMap<String, Session> map = WebSocket.getSessionPools();
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        Set<String> nameset = new HashSet<String>();
        while (it.hasNext()) {
            String entry = it.next();
            if (!entry.equals(currentuser))
                nameset.add(entry);
        }
        return nameset;
    }


    @RequestMapping("getuid")
    @ResponseBody
    public User getuid(@RequestParam("username") String username) {
        LambdaQueryWrapper<Staff> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Staff::getUsername, username);
        Staff staff = staffService.getOne(wrapper);
        long a = staff.getStaffId();
        User u = new User();
        u.setUid(a);
        return u;
    }
    
}
