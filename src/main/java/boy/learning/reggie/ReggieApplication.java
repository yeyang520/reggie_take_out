package boy.learning.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */

@Slf4j
@SpringBootApplication
//扫描webFilter，创建过滤器
@ServletComponentScan
//开启注解
@EnableTransactionManagement
public class ReggieApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ReggieApplication.class);
        log.info("项目启动成功");
    }
}
