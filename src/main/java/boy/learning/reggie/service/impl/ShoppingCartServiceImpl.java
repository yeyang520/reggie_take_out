package boy.learning.reggie.service.impl;

import boy.learning.reggie.entity.ShoppingCart;
import boy.learning.reggie.mapper.ShoppingCartMapper;
import boy.learning.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
