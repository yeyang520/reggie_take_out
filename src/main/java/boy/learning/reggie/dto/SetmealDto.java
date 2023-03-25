package boy.learning.reggie.dto;

import boy.learning.reggie.entity.Setmeal;
import boy.learning.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
