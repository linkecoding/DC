import org.junit.Test;

import util.FileUtil;

/**
 * Created by 尚振鸿 on 17-12-25. 10:40
 * mail:szh@codekong.cn
 */

public class FileUtilTest {

    @Test
    public void testUtil(){
        System.out.println(FileUtil.fileSystemToList("/home/szh/phone").get(0).toJsonStr());
    }
}
