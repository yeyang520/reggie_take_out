package boy.learning.reggie.controller;


import boy.learning.reggie.common.R;
import boy.learning.reggie.entity.Orders;
import boy.learning.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    OrdersService ordersService;


    /**
     * 购物车下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据 : {}",orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

}
