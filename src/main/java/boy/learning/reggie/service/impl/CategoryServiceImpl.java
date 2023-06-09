package boy.learning.reggie.service.impl;

import boy.learning.reggie.common.CustomException;
import boy.learning.reggie.entity.Category;
import boy.learning.reggie.entity.Dish;
import boy.learning.reggie.entity.Setmeal;
import boy.learning.reggie.mapper.CategoryMapper;
import boy.learning.reggie.service.CategoryService;
import boy.learning.reggie.service.DishService;
import boy.learning.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;


    /**
     * 根据id删除分类，删除之前判断
     * @param id
     */
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联菜品，如果关联，抛出异常
        if(count1>0){
            //已经关联菜品，抛出异常
            throw new CustomException("当前分类关联菜品，删除失败");
        }

        //查询当前分类是否关联套餐，如果关联，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0){
            //已经关联套餐，抛出异常
            throw new CustomException("当前分类关联套餐，删除失败");
        }

        //正常删除分类
        super.removeById(id);

    }
}
