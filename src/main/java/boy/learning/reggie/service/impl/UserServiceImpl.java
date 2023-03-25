package boy.learning.reggie.service.impl;

import boy.learning.reggie.entity.User;
import boy.learning.reggie.mapper.UserMapper;
import boy.learning.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
