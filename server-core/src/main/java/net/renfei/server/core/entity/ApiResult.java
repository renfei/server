package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.renfei.server.core.constant.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一接口响应对象
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "统一接口响应对象")
public class ApiResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "状态码，成功为200")
    private Integer code;
    @Schema(description = "消息")
    private String message;
    @Schema(description = "服务器时间戳")
    private Integer timestamp;
    @Schema(description = "签名")
    private String signature;
    @Schema(description = "随机数")
    private String nonce;
    @Schema(description = "数据负载")
    private T data;

    public ApiResult(T data) {
        this.code = HttpStatus.SUCCESS;
        this.message = "Success.";
        this.data = data;
    }

    public static ApiResult<?> success() {
        ApiResult<?> apiResult = new ApiResult<>();
        apiResult.code = HttpStatus.SUCCESS;
        apiResult.message = "Success.";
        return apiResult;
    }
}
