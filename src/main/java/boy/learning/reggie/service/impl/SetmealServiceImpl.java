package boy.learning.reggie.service.impl;

import boy.learning.reggie.common.CustomException;
import boy.learning.reggie.dto.SetmealDto;
import boy.learning.reggie.entity.Setmeal;
import boy.learning.reggie.entity.SetmealDish;
import boy.learning.reggie.mapper.SetmealMapper;
import boy.learning.reggie.service.SetmealDishService;
import boy.learning.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时保存菜品和套餐的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        //为套餐中的菜品设置套餐id(setmealId)
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        })).collect(Collectors.toList());

        //保存套餐和菜品的关联关系，操作setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }


    /**
     * 删除套餐和菜品的关联数据
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //select count(*) from setmeal where ids in (1,2,3) and status = 1
        //查询套餐状态是否可以删除
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId,ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(lambdaQueryWrapper);

        //如果不能删除，抛出异常
        if(count > 0){
            throw new CustomException("套餐正在售卖，不能删除");
        }

        //如果可以，删除套餐表数据  setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(SetmealDish::getSetmealId,ids);

        //再删除关系表数据         setmeal_dish
        setmealDishService.remove(queryWrapper);
    }
}
