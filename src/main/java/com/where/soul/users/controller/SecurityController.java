package com.where.soul.users.controller;


import com.where.soul.common.Constant;
import com.where.soul.common.Result;
import com.where.soul.common.util.FormatterUtil;
import com.where.soul.common.util.Regexp;
import com.where.soul.users.dto.UserDTO;
import com.where.soul.users.entity.Avatar;
import com.where.soul.users.entity.Users;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.where.soul.common.base.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户安全表 前端控制器
 * </p>
 *
 * @author lw
 * @since 2019-11-28
 */
@RestController
@RequestMapping("/users/security")
public class SecurityController extends BaseController {

    @GetMapping
    public Result restUserInfo(String phoneOrEmail) {
        if (phoneOrEmail == null || "".equals(phoneOrEmail)) {
            return Result.error();
        }
        Pattern patternPhone = Pattern.compile(Regexp.PHONE);
        Pattern patternEmail = Pattern.compile(Regexp.EMAIL);

        Matcher phoneMatch = patternPhone.matcher(phoneOrEmail);
        Matcher emailMatch = patternEmail.matcher(phoneOrEmail);

        // 是手机号码
        if (phoneMatch.find()) {

        }
        // 是邮箱
        if (emailMatch.find()) {

        }

        return Result.customError("数据的邮箱或手机号有误！");
    }
}
