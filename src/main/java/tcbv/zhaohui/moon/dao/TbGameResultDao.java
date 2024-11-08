package tcbv.zhaohui.moon.dao;
import java.time.LocalDateTime;
import java.util.List;

import jnr.ffi.annotations.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tcbv.zhaohui.moon.entity.TbGameResult;

/**
 * @author dawn
 * @date 2024/11/2 14:53
 */
@Mapper
public interface TbGameResultDao {
    /**
     * 查询最新的数据
     *
     * @param gameType 主键
     * @return 实例对象
     */
    TbGameResult queryByGameTypeNow(@Param("gameType") Integer gameType);
    /**
     * 查询游戏类型最大的轮次
     *
     * @return 实例对象
     */
    Integer findGameTypeNumber(@Param("gameType") Integer gameType);
    /**
     * 查询游戏类型最大的轮次
     *
     * @return 实例对象
     */
    Integer findGameTypeAndTurnsNumber(@Param("turns") Integer turns,@Param("gameType") Integer gameType);
    /**
     * 新增数据
     *
     * @param tbGameResult 实例对象
     * @return 影响行数
     */
    int insert(TbGameResult tbGameResult);
    /**
     * 更新数据
     *
     * @param tbGameResult 实例对象
     * @return 影响行数
     */
    int update(TbGameResult tbGameResult);
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    TbGameResult findDrawnTimeInfo(@Param("drawnTime")String drawnTime,@Param("gameType") Integer gameType);
}
