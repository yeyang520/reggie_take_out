package boy.learning.reggie.controller;


import boy.learning.reggie.common.R;
import boy.learning.reggie.entity.Employee;
import boy.learning.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的密码password进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据用户名user查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        //3.如果没有查询到数据返回登录失败结果
        if(emp == null){
            return R.error("登录失败");
        }

        //4.比对密码
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5.查看员工状态
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }
        //6.登录成功，将员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }


    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     * 新增员工
     * @param empoloyee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee empoloyee){
        log.info("新增员工,员工信息{}",empoloyee.toString());

        //设置初始密码，进行里MD5加密处理
        empoloyee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        
/*        empoloyee.setCreateTime(LocalDateTime.now());
          empoloyee.setUpdateTime(LocalDateTime.now());

          获取当前登录的id
          Long empId = (Long)request.getSession().getAttribute("employee");

          empoloyee.setCreateUser(empId);
          empoloyee.setUpdateUser(empId);
*/

        employeeService.save(empoloyee);

        return R.success("新增员工成功");
    }


    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize , String name ){

        log.info("page = {},pageSize={},name = {}",page,pageSize,name);
        System.out.println(pageSize);

        //1.分页构造器
        Page pageInfo = new Page(page,pageSize);

        //2.条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();

        //添加过滤条件
        lqw.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        lqw.orderByDesc(Employee::getUpdateTime);

        //3.执行查询
        employeeService.page(pageInfo,lqw);

        System.out.println(pageInfo.getRecords());


        return R.success(pageInfo);
    }

    /**
     * 修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        log.info(employee.toString());

        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);

        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    /**
     * 根据员工id查询
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        else {
            return R.error("没有查询到员工信息");
        }
    }

}
