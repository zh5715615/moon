package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.entity.UserEntity;
import tcbv.zhaohui.moon.dao.UserDao;
import tcbv.zhaohui.moon.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2025-12-19 17:14:44
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;


    /**
     * 分页查询
     *
     * @param userEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<UserEntity> queryByPage(UserEntity userEntity, PageRequest pageRequest) {
        long total = this.userDao.count(userEntity);
        return new PageImpl<>(this.userDao.queryAllByLimit(userEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param userEntity 实例对象
     * @return 实例对象
     */
    @Override
    public UserEntity insert(UserEntity userEntity) {
        this.userDao.insert(userEntity);
        return userEntity;
    }

}
