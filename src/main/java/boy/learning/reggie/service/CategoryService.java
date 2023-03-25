package boy.learning.reggie.service;

import boy.learning.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}
