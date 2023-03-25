package boy.learning.reggie.mapper;

import boy.learning.reggie.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RestController;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
