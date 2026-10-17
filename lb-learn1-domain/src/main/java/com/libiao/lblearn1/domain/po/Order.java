package com.libiao.lblearn1.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order")
@Schema(description = "订单实体")
public class Order {

    @Schema(description = "订单id")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    /** 状态: 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消 */
    @Schema(description = "状态: 0-待支付 1-已支付 2-已发货 3-已完成 4-已取消")
    private Short status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @TableLogic
    private Integer deleted;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
