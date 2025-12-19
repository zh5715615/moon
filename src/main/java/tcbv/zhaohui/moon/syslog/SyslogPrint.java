package tcbv.zhaohui.moon.syslog;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.entity.SyslogEntity;
import tcbv.zhaohui.moon.service.SyslogService;

/**
 * @author: zhaohui
 * @Title: SyslogPrint
 * @Description:
 * @date: 2025/12/19 11:38
 */
@Slf4j
@Component
public class SyslogPrint implements SyslogCallback{

    @Autowired
    private SyslogService syslogService;

    @Override
    public void handler(SyslogEntity syslogEntity) {
        if (StringUtils.isBlank(syslogEntity.getAddress())) {
            syslogEntity.setAddress("System"); //TODO 后面这部分逻辑待用户体系完整了再用真实的用户，先用System填充
        }
        syslogService.insert(syslogEntity);
    }
}
