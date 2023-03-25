package boy.learning.reggie.service;

import boy.learning.reggie.dto.SetmealDto;
import boy.learning.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时保存菜品和套餐的关联关系
     * @param setmealDto
     */
    public abstract void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐和菜品的关联数据
     * @param ids
     */
    public abstract  void deleteWithDish(List<Long> ids);
}
