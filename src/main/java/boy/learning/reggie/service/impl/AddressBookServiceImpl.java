package boy.learning.reggie.service.impl;

import boy.learning.reggie.entity.AddressBook;
import boy.learning.reggie.mapper.AddressBookMapper;
import boy.learning.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
