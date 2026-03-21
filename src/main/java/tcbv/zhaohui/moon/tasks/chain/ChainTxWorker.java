package tcbv.zhaohui.moon.tasks.chain;

import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;

public interface ChainTxWorker {
    void execute(ChainTxTaskEntity task, ChainTaskParams params) throws Exception;
}
