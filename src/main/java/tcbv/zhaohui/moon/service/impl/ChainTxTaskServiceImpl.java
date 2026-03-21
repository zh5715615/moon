package tcbv.zhaohui.moon.service.impl;

import org.springframework.stereotype.Service;
import tcbv.zhaohui.moon.dao.ChainTxTaskDao;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.service.ChainTxTaskService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ChainTxTaskServiceImpl implements ChainTxTaskService {

    @Resource
    private ChainTxTaskDao chainTxTaskDao;

    @Override
    public ChainTxTaskEntity insert(ChainTxTaskEntity entity) {
        entity.setId(UUID.randomUUID().toString());
        entity.setStatus(0);
        entity.setRetryCount(0);
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        chainTxTaskDao.insert(entity);
        return entity;
    }

    @Override
    public ChainTxTaskEntity queryById(String id) {
        return chainTxTaskDao.queryById(id);
    }

    @Override
    public List<ChainTxTaskEntity> queryPending() {
        return chainTxTaskDao.queryPending();
    }

    @Override
    public void updateStatus(String id, int status) {
        chainTxTaskDao.updateStatus(id, status);
    }

    @Override
    public void updateFailed(String id, String errorMsg) {
        chainTxTaskDao.updateFailed(id, errorMsg);
    }

    @Override
    public void updateRetry(String id, int retryCount) {
        chainTxTaskDao.updateRetry(id, retryCount);
    }

    @Override
    public void deleteById(String id) {
        chainTxTaskDao.deleteById(id);
    }
}
