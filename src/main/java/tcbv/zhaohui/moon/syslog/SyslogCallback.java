package tcbv.zhaohui.moon.syslog;

import tcbv.zhaohui.moon.entity.SyslogEntity;

/**
 * @author: zhaohui
 * @Title: SysloCallback
 * @Description:
 * @date: 2025/12/19 11:29
 */
public interface SyslogCallback {
    void handler(SyslogEntity syslogEntity);
}
