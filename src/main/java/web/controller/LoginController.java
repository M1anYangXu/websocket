package web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.entity.Staff;
import web.entity.User;
import web.service.StaffService;

@RestController
@RequestMapping("/login")
public class LoginController {
    
    @Resource
    private StaffService staffService;

    // 进入登录页面
    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        return view;
    }

    // 登出
    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession httpSession) {
        httpSession.removeAttribute("uid");
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        return view;
    }
    
    
    // 获取当前登录用户名
    @GetMapping("/currentuser")
    public User currentuser(HttpSession httpSession) {
        Long uid = (Long) httpSession.getAttribute("uid");
        LambdaQueryWrapper<Staff> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Staff::getStaffId, uid);
        Staff staff = staffService.getOne(wrapper);
        return new User(uid, staff.getUsername());
    }
    
    
    // 登录验证
    @PostMapping("/loginvalidate")
    public  ModelAndView loginValidate(@RequestParam("username") String username, @RequestParam("password") String password
        ,HttpSession httpSession) {
        LambdaQueryWrapper<Staff> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Staff::getUsername, username);

        ModelAndView view = new ModelAndView();
        if (staffService.getOne(wrapper) == null) {
            view.setViewName("login");
            return view;
        }
        Staff staff = staffService.getOne(wrapper);
        if (!password.equals(staff.getPassword())) {
            view.setViewName("fail");
            return view;
        }
        long uid = staff.getStaffId();
        httpSession.setAttribute("uid", uid);
        
        view.setViewName("chatroom");
        return view;
    }
}
