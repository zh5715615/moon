package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2025-12-19 17:14:44
 */
public interface UserService {

    /**
     * 分页查询
     *
     * @param userEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<UserEntity> queryByPage(UserEntity userEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param userEntity 实例对象
     * @return 实例对象
     */
    UserEntity insert(UserEntity userEntity);

}
