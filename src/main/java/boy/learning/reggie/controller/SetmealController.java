package boy.learning.reggie.controller;


import boy.learning.reggie.common.R;
import boy.learning.reggie.dto.DishDto;
import boy.learning.reggie.dto.SetmealDto;
import boy.learning.reggie.entity.Category;
import boy.learning.reggie.entity.Dish;
import boy.learning.reggie.entity.Setmeal;
import boy.learning.reggie.service.CategoryService;
import boy.learning.reggie.service.SetmealDishService;
import boy.learning.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;
    
    @Autowired
    private CategoryService categoryService;


    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());

        setmealService.saveWithDish(setmealDto);

        return R.success("新增成功");
    }


    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        //条件查询器
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        //创建时间降序
        lambdaQueryWrapper.orderByDesc(Setmeal::getCreateTime);

        //查询
        setmealService.page(pageInfo, lambdaQueryWrapper);

        //对象拷贝,忽略records，因为泛型不一致
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        //将records和recods对应的categoryNamecopy到setmealDto中
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //拷贝setmeal套餐信息
            BeanUtils.copyProperties(item, setmealDto);
            //获取id查询对应的分类id
            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);
            //得到分类姓名并set
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);

            return setmealDto;
        }).collect(Collectors.toList());

        //单独设置records
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }


    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("删除套餐 ids = {}",ids);

        setmealService.deleteWithDish(ids);

        return R.success("套餐删除成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        //根据categoryId和status查询套餐数据
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getCreateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);

    }


}
