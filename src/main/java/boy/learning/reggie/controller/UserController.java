package boy.learning.reggie.controller;


import boy.learning.reggie.common.R;
import boy.learning.reggie.entity.User;
import boy.learning.reggie.service.UserService;
import boy.learning.reggie.utils.SMSUtils;
import boy.learning.reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController{

    @Autowired
    UserService userService;


    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user,HttpSession session){

        //获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //生成随机四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}",code);

            //调用阿里云短信服务发送验证码
            //SMSUtils.sendMessage("签名","模板",phone,code);

            //将生成的验证码保存到session
            session.setAttribute(phone,code);

            return R.success("手机验证码发送成功");
        }
        return R.error("手机验证码发送失败");
    }


    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());

        //获取手机号
        String phone = (String)map.get("phone");
        //获取验证码
        String code = (String) map.get("code");

        //从session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        //验证码比对（页面提交和session保存
        if(codeInSession !=null && codeInSession.equals(code)){
            //比对成功，登录
            //判断当前手机号是否为新用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user == null){
                //新用户,注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }

        return R.error("登陆失败");
    }

}
