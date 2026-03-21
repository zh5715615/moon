package tcbv.zhaohui.moon.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;

import java.util.List;

@Mapper
public interface ChainTxTaskDao {

    int insert(ChainTxTaskEntity entity);

    ChainTxTaskEntity queryById(String id);

    /** 查询待处理或处理中的任务（启动恢复用） */
    List<ChainTxTaskEntity> queryPending();

    int updateStatus(@Param("id") String id, @Param("status") int status);

    int updateFailed(@Param("id") String id, @Param("errorMsg") String errorMsg);

    int updateRetry(@Param("id") String id, @Param("retryCount") int retryCount);

    int deleteById(String id);
}
