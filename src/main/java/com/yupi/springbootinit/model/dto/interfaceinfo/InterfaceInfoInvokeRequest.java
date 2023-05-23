package com.yupi.springbootinit.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求
 *
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;


    /**
     * 请求参数
     */
    private String userRequestParams;





    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}