package com.love.strutly.vo.req;

import com.love.strutly.entity.MiniUser;
import com.love.strutly.entity.Record;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author lj
 * @Date 2020/11/9 15:59
 * @Version 1.0
 */
@Data
public class RecordAddReqVO {

    private MiniUser miniUser;

    @ApiModelProperty(value = "内容",name="msg")
    @NotBlank(message = "内容不能为空")
    private String msg;

    @Valid // 嵌套验证必须用@Valid
    @ApiModelProperty(value = "图片集合",name="imgs")
    @NotNull(message = "请至少上传一张照片")
    @Size(min = 1, message = "请至少上传一张照片")
    private List<Record.MediaVO> imgs;

    @ApiModelProperty(value = "是否公开",name="open")
    private Boolean open;
}
