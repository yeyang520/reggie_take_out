package boy.learning.reggie.service.impl;

import boy.learning.reggie.entity.SetmealDish;
import boy.learning.reggie.mapper.SetmealDishMapper;
import boy.learning.reggie.service.SetmealDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements  SetmealDishService{
}
