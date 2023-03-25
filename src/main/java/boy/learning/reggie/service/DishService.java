package boy.learning.reggie.service;

import boy.learning.reggie.dto.DishDto;
import boy.learning.reggie.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，操作dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //更新菜品信息，同时更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);

    //根据id查询菜品分类信息和口味信息
    public DishDto getByIdWithFlavor(Long id);


}
