package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.BullfightingScoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import tcbv.zhaohui.moon.vo.BullfightingUserStatisticsVo;

import java.util.Date;

/**
 * 斗牛用户积分(BullfightingScore)表服务接口
 *
 * @author makejava
 * @since 2026-01-13 19:38:48
 */
public interface BullfightingScoreService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BullfightingScoreEntity queryById(String id);

    /**
     * 分页查询
     *
     * @param bullfightingScoreEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<BullfightingScoreEntity> queryByPage(BullfightingScoreEntity bullfightingScoreEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param bullfightingScoreEntity 实例对象
     * @return 实例对象
     */
    BullfightingScoreEntity insert(BullfightingScoreEntity bullfightingScoreEntity);

    /**
     * 修改数据
     *
     * @param bullfightingScoreEntity 实例对象
     * @return 实例对象
     */
    boolean update(BullfightingScoreEntity bullfightingScoreEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    /**
     * 用户统计
     * @param userId 用户id
     * @return 用户统计信息
     */
    BullfightingUserStatisticsVo userStatistics(String userId, Date gameDate);

    /**
     * 用户排名
     * @param userId 用户id
     * @param today 今天
     * @return 用户排名信息
     */
    BullfightingScoreEntity userRanking(String userId, Date today);
}
