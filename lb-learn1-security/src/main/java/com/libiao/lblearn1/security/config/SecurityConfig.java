package com.libiao.lblearn1.security.config;

import com.libiao.lblearn1.common.properties.AuthProperties;
import com.libiao.lblearn1.security.clientUser.LoginClientUserDetailServiceImpl;
import com.libiao.lblearn1.security.filter.ClientUserAuthenticationFilter;
import com.libiao.lblearn1.security.filter.JwtAuthenticationFilter;
import com.libiao.lblearn1.security.handler.AccessDeniedHandlerImpl;
import com.libiao.lblearn1.security.handler.AuthenticationEntryPointImpl;
import com.libiao.lblearn1.security.sysUser.SysUserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 开启 Spring Security 的 Web 支持（6.x 里不加也能生效，但显式写更清晰）
@EnableMethodSecurity   // ★6.x 新注解，替代 5.x 的 @EnableGlobalMethodSecurity(prePostEnabled=true)
@RequiredArgsConstructor 
public class SecurityConfig {

    private final AuthProperties authProperties;

    // 配置安全过滤链
    @Bean
    public SecurityFilterChain adminUserFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationEntryPointImpl entryPoint,
                                           AccessDeniedHandlerImpl deniedHandler)
            throws Exception {
        http
                // 只对 /admin/** 开头的请求进行认证
                .securityMatcher("/admin/**")
                // 关掉 CSRF：前后端分离用 JWT，不靠 Session Cookie，不需要 CSRF 防护
                .csrf(AbstractHttpConfigurer::disable)
                // 关掉 Session：无状态认证，不需要 Session Cookie
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ★无状态
                // 配合 STATELESS：无状态下不需要"未认证请求先缓存、登录后再回放"这套机制
                .requestCache(RequestCacheConfigurer::disable)
                // 关掉默认的表单登录（不然还会跳内置登录页）
                .formLogin(AbstractHttpConfigurer::disable)
                // 关掉 HTTP Basic（浏览器弹窗式认证）
                .httpBasic(AbstractHttpConfigurer::disable)
                // 关掉默认登出（我们要自己的 JSON 注销，第 9 章）
                .logout(AbstractHttpConfigurer::disable)
                // 配置请求权限
                .authorizeHttpRequests(auth -> auth
                        // ---------- 白名单：不需要登录就能访问 ----------
                        .requestMatchers(authProperties.getAdminWhiteList().toArray(String[]::new)).permitAll()
                        // ★CORS 预检请求要放行
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                         // 接口文档
                        .requestMatchers("/doc.html", "/webjars/**",
                                "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 健康检查
                        .requestMatchers("/actuator/health").permitAll() 
                         // 错误处理 ★强烈建议放行，否则出错时会变成 403/401
                        .requestMatchers("/error").permitAll()
                        // 静态资源 (商品封面图存放在 classpath:/static/images/ 下, 对外路径为 /images/**)
                        .requestMatchers("/images/**", "/static/**", "/favicon.ico").permitAll()

                        // ---------- 需要角色/权限 ----------
//                        .requestMatchers("/cart/**").hasRole("admin") // 等价 hasAuthority("ROLE_ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("user:delete")

                        // ---------- 兜底：其余全部要登录 ----------
                        .anyRequest().authenticated()) // 现在开始收紧了：其余都要认证

                // 配置异常处理
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)     // 401
                        .accessDeniedHandler(deniedHandler))      // 403
                // ★ 把 JWT 过滤器插在用户名密码过滤器之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // C 端安全过滤链：参考 adminUserFilterChain，只对 /client/** 开头的请求生效
    @Bean
    public SecurityFilterChain clientUserFilterChain(HttpSecurity http, ClientUserAuthenticationFilter clientUserAuthenticationFilter,
                                                    AuthenticationEntryPointImpl entryPoint,
                                                    AccessDeniedHandlerImpl deniedHandler)
            throws Exception {
        http
                // 只对 /client/** 开头的请求进行认证
                .securityMatcher("/client/**")
                // 关掉 CSRF：前后端分离用 JWT，不靠 Session Cookie，不需要 CSRF 防护
                .csrf(AbstractHttpConfigurer::disable)
                // 关掉 Session：无状态认证，不需要 Session Cookie
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ★无状态
                // 配合 STATELESS：无状态下不需要"未认证请求先缓存、登录后再回放"这套机制
                .requestCache(RequestCacheConfigurer::disable)
                // 关掉默认的表单登录（不然还会跳内置登录页）
                .formLogin(AbstractHttpConfigurer::disable)
                // 关掉 HTTP Basic（浏览器弹窗式认证）
                .httpBasic(AbstractHttpConfigurer::disable)
                // 关掉默认登出（我们要自己的 JSON 注销）
                .logout(AbstractHttpConfigurer::disable)
                // 配置请求权限
                .authorizeHttpRequests(auth -> auth
                        // ---------- 白名单：不需要登录就能访问（如 /client/auth/login）
                        .requestMatchers(authProperties.getClientWhiteList().toArray(String[]::new)).permitAll()
                        // ★CORS 预检请求要放行
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 错误处理 ★强烈建议放行，否则出错时会变成 403/401
                        .requestMatchers("/error").permitAll()

                        // ---------- 兜底：其余全部要登录 ----------
                        .anyRequest().authenticated())

                // 配置异常处理
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)     // 401
                        .accessDeniedHandler(deniedHandler))      // 403
                // ★ 把 C 端 JWT 过滤器插在用户名密码过滤器之前
                .addFilterBefore(clientUserAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 配置密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        // DelegatingPasswordEncoder：加密结果带算法前缀 {bcrypt}$2a$10$...
        // 好处：以后想从 bcrypt 换成 argon2，老密码还能验证，可平滑迁移。
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 配置认证管理器
    /**
     * 管理端认证管理器：显式绑定 SysUserDetailsServiceImpl。
     * <p>
     * 容器里现在有两个 UserDetailsService（管理端 + 客户端），Spring Security 的
     * InitializeUserDetailsBeanManagerConfigurer 发现多于一个时会直接跳过自动装配，
     * 所以必须手动构建 DaoAuthenticationProvider，不能再依赖 AuthenticationConfiguration。
     */
    @Bean
    public AuthenticationManager adminAuthenticationManager(SysUserDetailsServiceImpl sysUserDetailsService,
                                                           PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(sysUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    /**
     * C 端认证管理器：显式绑定 LoginClientUserDetailServiceImpl，与管理端互相隔离（各自查各自的用户表）。
     */
    @Bean
    public AuthenticationManager clientUserAuthenticationManager(LoginClientUserDetailServiceImpl clientUserDetailsService,
                                                                PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(clientUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
