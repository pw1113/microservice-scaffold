package com.microservice.user.service.controller;


import com.microservice.common.result.Result;
import com.microservice.user.service.domain.dto.LoginDTO;
import com.microservice.user.service.domain.dto.SendVerifyCodeDTO;
import com.microservice.user.service.domain.dto.UserCreateDTO;
import com.microservice.user.service.domain.dto.UserUpdateDTO;
import com.microservice.user.service.domain.vo.LoginVO;
import com.microservice.user.service.domain.vo.UserVO;
import com.microservice.user.service.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 *
 * @author microservice
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口，包括注册、登录、查询、修改、删除等")
public class UserController {

    private final IUserService userService;

    /**
     * 注册新用户
     */
    @PostMapping("/register")
    @Operation(summary = "注册新用户", description = "创建新用户账号，需先通过 /send-code 获取邮箱验证码。用户名和邮箱不可重复，密码将进行BCrypt加密存储。")
    public Result<Void> register(@RequestBody @Valid UserCreateDTO dto) {
        log.info("收到注册请求 -> username={}, email={}", dto.getUsername(), dto.getEmail());
        userService.register(dto);
        return Result.success("新用户注册成功", null);
    }

    /**
     * 根据 ID 查询用户
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详细信息。")
    public Result<UserVO> getUserById(@PathVariable @Parameter(description = "用户ID") Long id) {
        log.info("收到查询用户请求 -> id={}", id);
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询用户", description = "根据用户名精确查询用户信息。")
    public Result<UserVO> getUserByUsername(@PathVariable @Parameter(description = "用户名") String username) {
        log.info("收到查询用户请求 -> username={}", username);
        UserVO userVO = userService.getUserByUsername(username);
        return Result.success(userVO);
    }

    /**
     * 根据 ID 集合批量查询用户
     */
    @PostMapping("/batch")
    @Operation(summary = "根据ID集合批量查询用户", description = "传入用户ID列表，批量查询用户信息。")
    public Result<List<UserVO>> getUsersByIds(@RequestBody List<Long> ids) {
        log.info("收到批量查询用户请求 -> ids={}", ids);
        List<UserVO> userVOs = userService.getUsersByIds(ids);
        return Result.success(userVOs);
    }

    /**
     * 根据 ID 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除用户", description = "逻辑删除指定用户。")
    public Result<Void> deleteUserById(@PathVariable @Parameter(description = "用户ID") Long id) {
        log.info("收到删除用户请求 -> id={}", id);
        userService.deleteUserById(id);
        return Result.success();
    }

    /**
     * 根据 ID 集合批量删除用户
     */
    @DeleteMapping("/batch")
    @Operation(summary = "根据ID集合批量删除用户", description = "传入用户ID列表，批量逻辑删除用户。")
    public Result<Void> deleteUsersByIds(@RequestBody List<Long> ids) {
        log.info("收到批量删除用户请求 -> ids={}", ids);
        userService.deleteUsersByIds(ids);
        return Result.success("删除用户成功",null);
    }

    /**
     * 修改用户信息
     */
    @PutMapping
    @Operation(summary = "修改用户信息", description = "根据用户ID更新用户信息，用户名和密码不可通过此接口修改。")
    public Result<Void> updateUser(@RequestBody @Valid UserUpdateDTO dto) {
        log.info("收到修改用户请求 -> UserUpdateDTO={}", dto);
        userService.updateUser(dto);
        return Result.success();
    }

    /**
     * 发送邮箱验证码
     * <p>
     * 同一邮箱在同一业务类型下，5分钟内不可重复获取，重复请求返回成功但提示已发送。
     * </p>
     */
    @PostMapping("/send-code")
    @Operation(summary = "发送邮箱验证码", description = "向指定邮箱发送6位数字验证码，有效期5分钟。同一邮箱同一业务类型下5分钟内不可重复获取。")
    public Result<Void> sendVerifyCode(@RequestBody @Valid SendVerifyCodeDTO dto) {
        log.info("收到发送验证码请求 -> email={}, type={}", dto.getEmail(), dto.getType());
        String code = userService.sendVerifyCode(dto);
        if (code == null) {
            return Result.success("验证码已发送，请5分钟后再试", null);
        }
        return Result.success("验证码已发送", null);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(
            summary = "用户登录",
            description = "用户登录接口。登录流程：用户先输入用户名、密码、邮箱，" +
                    "系统向邮箱发送验证码，用户收到验证码后将用户名、密码、邮箱、验证码一并提交完成登录。" +
                    "deviceId 为可选参数，用于多设备管理。"
    )
    public Result<LoginVO> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletRequest request) {
        log.info("收到登录请求 -> username={}, email={}, code={}, deviceId={}",
                loginDTO.getUsername(),
                loginDTO.getEmail(),
                loginDTO.getCode(),
                loginDTO.getDeviceId());
        // 获取客户端 IP
        String clientIp = getClientIp(request);
        LoginVO loginVO = userService.login(loginDTO, clientIp);
        return Result.success(loginVO);
    }

    /**
     * 获取客户端真实 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
