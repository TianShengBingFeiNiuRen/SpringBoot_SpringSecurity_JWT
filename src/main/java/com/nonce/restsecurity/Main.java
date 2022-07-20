package com.nonce.restsecurity;

import com.nonce.restsecurity.dao.AuthorityUserRepository;
import com.nonce.restsecurity.util.FileUtil;
import com.nonce.restsecurity.util.RandomStringUtil;
import com.nonce.restsecurity.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Andon
 * 2022/7/20
 */
@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class Main implements ApplicationRunner {

    private final AuthorityUserRepository authorityUserRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Map<String, Object>> rootMenuInfo = authorityUserRepository.findRootMenuInfo();
        if (ObjectUtils.isEmpty(rootMenuInfo)) {
            String updateTime = TimeUtil.FORMAT.get().format(System.currentTimeMillis());
            String validTime = TimeUtil.FORMAT.get().format(System.currentTimeMillis() + 66 * 365 * 24 * 60 * 60 * 1000L);
            String adminUsername = "admin";
            String adminPassword = RandomStringUtil.stringGenerate(10, true, true, true);
            FileUtil.createFileWithContent(Collections.singletonList(adminUsername), "adminUsername.txt");
            FileUtil.createFileWithContent(Collections.singletonList(adminPassword), "adminPassword.txt");

            authorityUserRepository.addUserInfo("超级管理员", adminUsername, new BCryptPasswordEncoder().encode(adminPassword), "admin@admin.com", "16688886688", validTime, updateTime, "超级管理员");
            authorityUserRepository.addRoleInfo("ROLE_ADMIN", "超级管理员", updateTime, "超级管理员");
            authorityUserRepository.addMenuInfo("/security-manage/user-manage/**", "用户管理", "0", updateTime, "用户管理", "/security-manage/user-manage");
            authorityUserRepository.addMenuInfo("/security-manage/role-manage/**", "角色管理", "0", updateTime, "角色管理", "/security-manage/role-manage");
            authorityUserRepository.addMenuInfo("/security-manage/resource-manage/**", "菜单管理", "0", updateTime, "菜单管理", "/security-manage/resource-manage");

            List<Map<String, Object>> userInfos = authorityUserRepository.findAllUserInfo(0, 10, adminUsername, "");
            List<Map<String, Object>> roleInfos = authorityUserRepository.findAllRoleInfo(0, 10, "超级管理员");
            rootMenuInfo = authorityUserRepository.findRootMenuInfo();
            List<String> userIds = userInfos.stream().map(map -> String.valueOf(map.get("id"))).collect(Collectors.toList());
            List<String> roleIds = roleInfos.stream().map(map -> String.valueOf(map.get("id"))).collect(Collectors.toList());
            List<String> menuIds = rootMenuInfo.stream().map(map -> String.valueOf(map.get("id"))).collect(Collectors.toList());
            for (String userId : userIds) {
                for (String roleId : roleIds) {
                    authorityUserRepository.addRoleForUser(Integer.parseInt(userId), Integer.parseInt(roleId), updateTime);
                }
            }
            for (String roleId : roleIds) {
                for (String menuId : menuIds) {
                    authorityUserRepository.addMenuForRole(Integer.parseInt(roleId), Integer.parseInt(menuId), updateTime);
                }
            }
            log.info("SystemInit success!! adminPassword:{}", adminPassword);
        }
    }
}
