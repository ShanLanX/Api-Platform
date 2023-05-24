package com.swx.springbootinit.model.vo;

import com.swx.apicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo {
    // 统计调用次数
    private Integer totalNum;
    private static final long serialVersionUID = 1L;


}
