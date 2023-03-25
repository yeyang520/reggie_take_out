package boy.learning.reggie.service.impl;

import boy.learning.reggie.entity.OrderDetail;
import boy.learning.reggie.mapper.OrderDetailMapper;
import boy.learning.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
