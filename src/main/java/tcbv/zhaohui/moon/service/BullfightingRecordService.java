package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.BullfightingRecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import tcbv.zhaohui.moon.entity.BullfightingScoreEntity;

/**
 * 斗牛游戏记录(BullfightingRecord)表服务接口
 *
 * @author makejava
 * @since 2026-01-13 19:38:48
 */
public interface BullfightingRecordService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BullfightingRecordEntity queryById(String id);

    /**
     * 分页查询
     *
     * @param bullfightingRecordEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<BullfightingRecordEntity> queryByPage(BullfightingRecordEntity bullfightingRecordEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param bullfightingRecordEntity 实例对象
     * @return 实例对象
     */
    BullfightingRecordEntity insert(BullfightingRecordEntity bullfightingRecordEntity);

    /**
     * 修改数据
     *
     * @param bullfightingRecordEntity 实例对象
     * @return 实例对象
     */
    boolean update(BullfightingRecordEntity bullfightingRecordEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    /**
     * 保存游戏记录
     * @param bullfightingRecordEntity 游戏记录
     */
    BullfightingScoreEntity save(BullfightingRecordEntity bullfightingRecordEntity);
}
