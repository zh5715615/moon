package tcbv.zhaohui.moon.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tcbv.zhaohui.moon.entity.TbUser;

/**
 * ;(tb_user)表数据库访问层
 * @author : http://www.chiner.pro
 * @date : 2024-11-2
 */
@Mapper
public interface TbUserDao {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbUser queryByAddress(@Param("address") String address);
    /**
     * 新增数据
     *
     * @param tbUser 实例对象
     * @return 影响行数
     */
    int insert(TbUser tbUser);
    /**
     * 更新数据
     *
     * @param tbUser 实例对象
     * @return 影响行数
     */
    int update(TbUser tbUser);
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 查询指定userId数据
     * @param userId
     * @return
     */
    TbUser queryById(@Param("userId") String userId);
}
