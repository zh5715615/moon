package tcbv.zhaohui.moon.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tcbv.zhaohui.moon.entity.TbRewardRecord;
import tcbv.zhaohui.moon.entity.TbTxRecord;

import java.util.List;

/**
 * @author dawn
 * @date 2024/11/2 14:55
 */
@Mapper
public interface TbTxRecordDao {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbTxRecord queryById(String id);

    /**
     * 查询用户游戏下注单列表
     *
     * @return 实例对象
     */
    List<TbTxRecord> findByUserDraw(String userId, Integer gameType);
    /**
     *  根据游戏类型 用户id查询是否已经下注
     *
     * @return 实例对象
     */
    TbTxRecord queryByIdAndGameInfo(@Param("userId")String userId,@Param("gameType") Integer gameType,
                                    @Param("turns") Integer turns);
    /**
     *  根据游戏类型和轮次查询下注的用户
     *
     * @return 实例对象
     */
    List<TbTxRecord> findTurnsGameInfo(@Param("turns")Integer turns,@Param("gameType") Integer gameType);
    /**
     * 新增数据
     *
     * @param tbTxRecord 实例对象
     * @return 影响行数
     */
    int insert(TbTxRecord tbTxRecord);
    /**
     * 批量新增数据
     *
     * @param entities List<TbTxRecord> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbTxRecord> entities);
    /**
     * 更新数据
     *
     * @param tbTxRecord 实例对象
     * @return 影响行数
     */
    int update(TbTxRecord tbTxRecord);
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    List<TbTxRecord> isBetOn(@Param("userId") String userId, @Param("turns") Integer turns, @Param("gameType") Integer gameType);

    double betNumber(@Param("gameType") int gameType, @Param("turns") int turns, @Param("betType") Integer betType);

    List<TbTxRecord> winnerList(@Param("gameType") int gameType, @Param("turns") int turns, @Param("winner") int winner);

    List<TbTxRecord> loserList(@Param("gameType") int gameType, @Param("turns") int turns, @Param("winner") int loser);

    double betEventErrorNumber(@Param("gameType") int gameType, @Param("turns") int turns, @Param("optionId")String optionId);

    double betEventCorrectNumber(@Param("gameType") int gameType, @Param("turns") int turns, @Param("optionId")String optionId);

    List<TbTxRecord> winnerEventList(@Param("gameType") int gameType, @Param("turns") int turns, @Param("optionId")String optionId);

    List<TbTxRecord> loserEventList(@Param("gameType") int gameType, @Param("turns") int turns, @Param("optionId")String optionId);
}
