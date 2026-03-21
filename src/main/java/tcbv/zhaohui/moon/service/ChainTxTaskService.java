package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;

import java.util.List;

public interface ChainTxTaskService {

    ChainTxTaskEntity insert(ChainTxTaskEntity entity);

    ChainTxTaskEntity queryById(String id);

    List<ChainTxTaskEntity> queryPending();

    void updateStatus(String id, int status);

    void updateFailed(String id, String errorMsg);

    void updateRetry(String id, int retryCount);

    void deleteById(String id);
}
