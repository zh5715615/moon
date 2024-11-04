package tcbv.zhaohui.moon.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tcbv.zhaohui.moon.entity.TbRewardRecord;

/**
 * 开奖记录表;(tb_reward_record)表数据库访问层
 * @author : http://www.chiner.pro
 * @date : 2024-11-2
 */
@Mapper
public interface TbRewardRecordDao {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbRewardRecord queryById(String id);

    /**
     * 新增数据
     *
     * @param tbRewardRecord 实例对象
     * @return 影响行数
     */
    int insert(TbRewardRecord tbRewardRecord);
    /**
     * 批量新增数据
     *
     * @param entities List<TbRewardRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbRewardRecord> entities);
    /**
     * 更新数据
     *
     * @param tbRewardRecord 实例对象
     * @return 影响行数
     */
    int update(TbRewardRecord tbRewardRecord);
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);
}
