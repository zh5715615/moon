package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.BullfightingPurchaseEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 购买斗牛次数记录表(BullfightingPurchase)表数据库访问层
 *
 * @author makejava
 * @since 2026-01-17 11:49:26
 */
@Mapper
public interface BullfightingPurchaseDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BullfightingPurchaseEntity queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param bullfightingPurchaseEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<BullfightingPurchaseEntity> queryAllByLimit(@Param("entity") BullfightingPurchaseEntity bullfightingPurchaseEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param bullfightingPurchaseEntity 查询条件
     * @return 总行数
     */
    long count(BullfightingPurchaseEntity bullfightingPurchaseEntity);

    /**
     * 新增数据
     *
     * @param bullfightingPurchaseEntity 实例对象
     * @return 影响行数
     */
    int insert(BullfightingPurchaseEntity bullfightingPurchaseEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<BullfightingPurchaseEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BullfightingPurchaseEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<BullfightingPurchaseEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<BullfightingPurchaseEntity> entities);

    /**
     * 修改数据
     *
     * @param bullfightingPurchaseEntity 实例对象
     * @return 影响行数
     */
    int update(BullfightingPurchaseEntity bullfightingPurchaseEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 根据hash查询
     * @param hash 哈希值
     * @return 实例对象
     */
    BullfightingPurchaseEntity queryByHash(@Param("hash") String hash);

    /**
     * 查询昨日奖励池
     * @param date 日期
     * @return 奖励池总额
     */
    int queryRewardPoolByGameDate(@Param("date") Date date);

    /**
     * 查询指定日期用户充值总金额，也就是奖池金额。
     * @param date 日期
     * @return 充值总金额
     */
    double queryTodayRewardPool(Date date);
}

