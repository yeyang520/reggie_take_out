package boy.learning.reggie.controller;


import boy.learning.reggie.common.R;
import boy.learning.reggie.dto.DishDto;
import boy.learning.reggie.entity.Category;
import boy.learning.reggie.entity.Dish;
import boy.learning.reggie.entity.DishFlavor;
import boy.learning.reggie.service.CategoryService;
import boy.learning.reggie.service.DishFlavorService;
import boy.learning.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功");
    }


    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Dish::getCreateTime);

        dishService.page(pageInfo,lambdaQueryWrapper);

        //对象拷贝,将分页信息，条件,结果进行拷贝,不拷贝records
        BeanUtils.copyProperties(pageInfo,dishDtoPage, "records");
        //获取原来的数据
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item -> {
            DishDto dishDto = new DishDto();
            //将查询的数据保存到dto中
            BeanUtils.copyProperties(item, dishDto);

            //查询分类id
            Long categoryId = item.getCategoryId();
            //根据id查询姓名
            Category category = categoryService.getById(categoryId);

            if(category!=null){
                String categoryName = category.getName();
                //设置分类姓名
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;

        })).collect(Collectors.toList());

        //为返回对象赋上数据结果
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    /**
     * 根据id查询信息,实现回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 删除菜品和对应口味
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    public R<String> delete(Long[] ids) {
        log.info("删除菜品和口味...");

        //删除口味
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DishFlavor::getId,ids);
        dishFlavorService.remove(lambdaQueryWrapper);

        //删除菜品
        for (int i = 0; i < ids.length; ++i){
            dishService.removeById(ids[i]);
        }
        return R.success("删除成功");
    }


    /**
     * 停售启售菜品
     * @param status 修改后的状态
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> discontinued(@PathVariable int status,Long[] ids){
        log.info("ids[0] = {},ids = {}",ids[0],ids);

        /*for(int i=0;i<ids.length;++i){
            //1.先查询原来的菜品信息
            Dish dish = dishService.getById(ids[i]);
            //判断
            if(dish == null){
                return R.success("未查询到该菜品");
            }
            //2.修改
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("停止售卖成功");*/

        Dish dish = new Dish();
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Dish::getId,ids);
        dish.setStatus(status);
        dishService.update(dish,lambdaQueryWrapper);
        return R.success("修改成功");
    }


    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造条件查询器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //查询条件，启售状态
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        //查询条件,排序
        lambdaQueryWrapper.orderByAsc(Dish::getSort);

        List<Dish> list = dishService.list(lambdaQueryWrapper);

        //复制dish到dishDto，并查询出口味信息
        List<DishDto> dishDtolist = list.stream().map((item -> {
            DishDto dishDto = new DishDto();
            //将查询的数据保存到dto中
            BeanUtils.copyProperties(item, dishDto);

            //查询分类id
            Long categoryId = item.getCategoryId();
            //根据id查询姓名
            Category category = categoryService.getById(categoryId);

            if(category!=null){
                String categoryName = category.getName();
                //设置分类姓名
                dishDto.setCategoryName(categoryName);
            }

            //查询菜品的口味
            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
            //根据id查询菜品
            queryWrapper.eq(dishId!=null,DishFlavor::getDishId,dishId);
            //select * from dish_flavor where dish_id = dishid
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavors);

            return dishDto;
        })).collect(Collectors.toList());


        return R.success(dishDtolist);
    }
}
