package boy.learning.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


public class UploadTest {

    @Test
    public void fileTest(){
        String fileName = "sfasdfasfdd.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }
}
