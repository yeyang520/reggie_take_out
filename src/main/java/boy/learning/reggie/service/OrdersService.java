package boy.learning.reggie.service;

import boy.learning.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;


public interface OrdersService extends IService<Orders> {

    /**
     * 购物车下单
     * @param orders
     */
    public void submit(Orders orders);
}
