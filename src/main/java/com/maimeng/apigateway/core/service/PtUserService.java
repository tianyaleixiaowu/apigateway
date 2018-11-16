package com.maimeng.apigateway.core.service;

import com.maimeng.apigateway.core.manager.PtRoleManager;
import com.maimeng.apigateway.core.manager.PtRoleMenuManager;
import com.maimeng.apigateway.core.model.PtMenu;
import com.maimeng.apigateway.core.model.PtRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author wuweifeng wrote on 2018/10/30.
 */
@Service
public class PtUserService {
    @Resource
    private PtRoleManager ptRoleManager;
    @Resource
    private PtRoleMenuManager ptRoleMenuManager;

    public boolean checkMenu(Long userId, String path, String method) {

        List<PtRole> roleList = ptRoleManager.findRolesByUser(userId);
        List<PtMenu> menuList = ptRoleMenuManager.findAllMenuByRoles(roleList);
        for (PtMenu menu : menuList) {
            String url = menu.getUrl();
            String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
            String regEx = "^" + uri + "$";
            //如果匹配，就返回true
            boolean check = Pattern.compile(regEx).matcher(path).find() || path.startsWith(url + "/")
                    && method.equals(menu.getMethod());
            if(check) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        String url = "/group/{*}/user";
        String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
        String regEx = "^" + uri + "$";
        String finalRequestUri = "/group/1/user";
        boolean s =  Pattern.compile(regEx).matcher(finalRequestUri).find() || finalRequestUri.startsWith(url + "/");
        System.out.println(s);
    }
}
