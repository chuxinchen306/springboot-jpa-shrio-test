package hello.admin;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chenchx on 2019/1/3.
 */
@Controller
@RequestMapping(path="/demo")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String name
            ,@RequestParam String email){
        AdminDo adminDo = new AdminDo();
        adminDo.setName(name);
        adminDo.setEmail(email);
        userRepository.save(adminDo);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<AdminDo> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/login")
    public @ResponseBody String login(@RequestParam String userName
            ,@RequestParam String password){
        UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping(path = "/loginOut")
    public @ResponseBody String loginOut(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "success";
    }
}
