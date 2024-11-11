package tcbv.zhaohui.moon.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tcbv.zhaohui.moon.dao.TbUserDao;
import tcbv.zhaohui.moon.entity.TbUser;
import tcbv.zhaohui.moon.utils.GsonUtil;

import javax.annotation.Resource;

@SpringBootTest
public class UserDaoTest {
    @Resource
    private TbUserDao userDao;

    @Test
    public void testQueryById() {
        TbUser user = userDao.queryById("016587eb-727b-4533-af8f-cc9950e2e9ad");
        System.out.println(GsonUtil.toJson(user));
    }
}
