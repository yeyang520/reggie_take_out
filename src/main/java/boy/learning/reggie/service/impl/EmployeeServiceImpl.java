package boy.learning.reggie.service.impl;


import boy.learning.reggie.entity.Employee;
import boy.learning.reggie.mapper.EmployeeMapper;
import boy.learning.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
