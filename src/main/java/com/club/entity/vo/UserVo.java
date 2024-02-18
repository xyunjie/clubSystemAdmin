package com.club.entity.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月07日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserVo {

    @Schema(name = "成员ID")
    @ExcelIgnore
    private Long id;

    @Schema(name = "成员姓名")
    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @Schema(name = "学号/登录账号")
    @ExcelProperty(value = "学号/登录账号", index = 1)
    private String studentId;

    @Schema(name = "登录密码")
    @ExcelIgnore
    private String password;

    @Schema(name = "手机号码")
    @ExcelProperty(value = "手机号", index = 2)
    private String phone;

    @Schema(name = "头像")
    @ExcelIgnore
    private String avatar;

    @Schema(name = "性别")
    @ExcelIgnore
    private Boolean sex;

    @Schema(name = "性别名称")
    @ExcelProperty(value = "性别", index = 3)
    private  String sexName;

    @Schema(name = "个人介绍")
    @ExcelProperty(value = "个人介绍", index = 4)
    private String userInfo;

    @Schema(name = "qq")
    @ExcelProperty(value = "QQ", index = 5)
    private String qq;

    @Schema(name = "微信账号")
    @ExcelProperty(value = "微信", index = 6)
    private String weChat;

    @Schema(name = "邮箱")
    @ExcelProperty(value = "邮箱", index = 7)
    private String mail;

    @Schema(name = "学院")
    @ExcelIgnore
    private Long college;

    @Schema(name = "学院名称")
    @ExcelProperty(value = "学院", index = 8)
    private String collegeName;

    @Schema(name = "专业")
    @ExcelIgnore
    private Long major;

    @Schema(name = "专业名称")
    @ExcelProperty(value = "专业", index = 9)
    private String majorName;

    @Schema(name = "班级")
    @ExcelIgnore
    private Long clazz;

    @Schema(name = "班级名称")
    @ExcelProperty(value = "班级", index = 10)
    private String clazzName;

    @Schema(name = "组织信息")
    @ExcelIgnore
    private String unitInfo;

    @Schema(name = "入学年级")
    @ExcelIgnore
    private Long grade;

    @Schema(name = "入学年级名称")
    @ExcelProperty(value = "年级", index = 11)
    private String gradeName;

    @Schema(name = "状态")
    @ExcelIgnore
    private Integer status;

    @Schema(name = "角色")
    @ExcelIgnore
    private String role;

}
