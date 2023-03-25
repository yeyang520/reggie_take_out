package boy.learning.reggie.service.impl;

import boy.learning.reggie.entity.DishFlavor;
import boy.learning.reggie.mapper.DishFlavorMapper;
import boy.learning.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService {
}
