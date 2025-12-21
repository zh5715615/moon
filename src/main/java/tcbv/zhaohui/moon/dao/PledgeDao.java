package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.PledgeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 质押表(Pledge)表数据库访问层
 *
 * @author makejava
 * @since 2025-12-21 11:01:17
 */
@Mapper
public interface PledgeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PledgeEntity queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param pledgeEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<PledgeEntity> queryAllByLimit(@Param("entity") PledgeEntity pledgeEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param pledgeEntity 查询条件
     * @return 总行数
     */
    long count(PledgeEntity pledgeEntity);

    /**
     * 新增数据
     *
     * @param pledgeEntity 实例对象
     * @return 影响行数
     */
    int insert(PledgeEntity pledgeEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PledgeEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<PledgeEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PledgeEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<PledgeEntity> entities);

    /**
     * 修改数据
     *
     * @param pledgeEntity 实例对象
     * @return 影响行数
     */
    int update(PledgeEntity pledgeEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 根据hash查找质押信息
     * @param pledgeHash 质押hash值
     * @return 质押信息
     */
    PledgeEntity queryByPledgeHash(String pledgeHash);
}

