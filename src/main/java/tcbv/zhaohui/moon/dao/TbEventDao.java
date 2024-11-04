package tcbv.zhaohui.moon.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tcbv.zhaohui.moon.entity.TbEvent;

import java.util.List;

/**
 * @author dawn
 * @date 2024/11/2 14:50
 */
@Mapper
public interface TbEventDao {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbEvent queryById(Integer id);
    /**
     * 新增数据
     *
     * @param tbEvent 实例对象
     * @return 影响行数
     */
    int insert(TbEvent tbEvent);
    /**
     * 批量新增数据
     *
     * @param entities List<TbEvent> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbEvent> entities);
    /**
     * 更新数据
     *
     * @param tbEvent 实例对象
     * @return 影响行数
     */
    int update(TbEvent tbEvent);
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);
}
