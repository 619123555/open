package com.trans.payment.core.gateway.entity;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * 网关通用参数
 */
@Data
@NoArgsConstructor
public class ApiReq implements Serializable {

    private static final long serialVersionUID = -1509461106899464983L;

    /**
     * 代理商编号
     */
    @Length(min = 1, max = 32, message = "ISV.INVALID-PARAMETER", groups = {AddGroup.class})
    @NotBlank(message = "ISV.MISSING-APP-ID", groups = {AddGroup.class})
    private String appId;

    /**
     * 业务方法编码
     */
    @Length(min = 1, max = 32, message = "ISV.INVALID-PARAMETER", groups = {AddGroup.class})
    @NotBlank(message = "ISV.MISSING-METHOD", groups = {AddGroup.class})
    private String service;

    /**
     * 加密方式 rsa
     */
    @Length(min = 1, max = 10, message = "ISV.INVALID-PARAMETER", groups = {AddGroup.class})
    @NotBlank(message = "ISV.MISSING-SIGNATURE-TYPE", groups = {AddGroup.class})
    private String signType;

    /**
     * 时间戳 yyyy-MM-dd HH:mm:ss
     */
    @Length(min = 19, max = 19, message = "ISV.INVALID-TIMESTAMP", groups = {AddGroup.class})
    @NotBlank(message = "ISV.MISSING-TIMESTAMP", groups = {AddGroup.class})
    private String timestamp;

    /**
     * 下游请求流水号(无业务意义方便查询日志和防重处理)
     */
    @Length(min = 10, max = 32, message = "ISV.INVALID-PARAMETER", groups = {AddGroup.class})
    @NotBlank(message = "ISV.MISSING-COMMON-PARAMS", groups = {AddGroup.class})
    private String requestId;

    /**
     * 版本 1.0
     */
    @Length(min = 1, max = 3, message = "ISV.INVALID-PARAMETER", groups = {AddGroup.class})
    @NotBlank(message = "ISV.MISSING-VERSION", groups = {AddGroup.class})
    private String version;

    /**
     * 业务参数(json串)
     */
    @NotBlank(message = "ISV.MISSING-COMMON-PARAMS", groups = {AddGroup.class})
    private String data;

    /**
     * 网关补充参数
     */
    private String systemId;

    public interface AddGroup {

    }
}
