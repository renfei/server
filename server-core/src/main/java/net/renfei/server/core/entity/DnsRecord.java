package net.renfei.server.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.renfei.server.core.constant.AlidnsLineEnum;
import net.renfei.server.core.constant.DnsRecordTypeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * DNS记录
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "DNS记录")
public class DnsRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = -3316408227872898096L;
    @Schema(description = "ID")
    private String id;
    @Schema(description = "域名")
    private String domainName;
    @Schema(description = "DNS记录名称")
    @NotNull
    private String name;
    @Schema(description = "类型")
    @NotNull
    private DnsRecordTypeEnum type;
    @Schema(description = "记录内容")
    @NotNull
    private String content;
    @Schema(description = "阿里云线路枚举")
    private AlidnsLineEnum alidnsLine;
    @Schema(description = "备注")
    private String comment;
    @Schema(description = "Time To Live (TTL)")
    @NotNull
    private Long ttl;
    @Schema(description = "是否被代理")
    private Boolean proxied;
}
