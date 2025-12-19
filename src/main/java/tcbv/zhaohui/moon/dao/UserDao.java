package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2025-12-19 17:14:43
 */
@Mapper
public interface UserDao {


    /**
     * 查询指定行数据
     *
     * @param userEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<UserEntity> queryAllByLimit(@Param("entity") UserEntity userEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param userEntity 查询条件
     * @return 总行数
     */
    long count(UserEntity userEntity);

    /**
     * 新增数据
     *
     * @param userEntity 实例对象
     * @return 影响行数
     */
    int insert(UserEntity userEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<UserEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<UserEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<UserEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<UserEntity> entities);

    /**
     * 根据地址查询用户信息
     * @param address 用户地址
     * @return 返回匹配地址的用户实体对象
     */
    UserEntity queryByAddress(@Param("address")  String address);
}

