package com.open.boss.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.open.boss.commons.Result;
import com.open.boss.mapper.ConfMapper;
import com.open.boss.service.system.MenuService;
import com.open.boss.service.system.UserService;
import com.open.common.constants.Constants;
import com.open.boss.controller.BaseController;
import com.open.boss.entity.User;
import com.open.boss.utils.UserUtils;
import com.open.common.utils.DateUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

@RestController
public class IndexController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private ConfMapper confMapper;

    @Resource
    private UserService userService;

    @Resource
    private JedisCluster jedisCluster;

    @Resource
    private MenuService menuService;

    @Value("${platform.auth:R0059}")
    private String roleCode;

    @RequestMapping(
            value = "/checkLogin",
            method = {RequestMethod.POST, RequestMethod.GET}
    )
    public Result checkLogin(HttpServletRequest request) {
        try {
            User user = UserUtils.getUser();
            if (user != null) {
                // boolean auth = this.menuService.checkAuth(request.getHeader("Referer"));
                // if (!auth) {
                //    return error("非法请求");
                // }
                user.setPassword(null);
                return ok(user);
            }
        } catch (Exception e) {
            logger.error("Exception:{}", e);
        }
        return error();
    }

    @PostMapping
        (value = "login")
    public Result login(HttpServletRequest request) {
        try {
            return this.userService.login(request);
        } catch (Exception e) {
            logger.error("Exception:{}", e);
            return error();
        }
    }

    @RequestMapping(
            value = "/logout",
            method = {RequestMethod.POST, RequestMethod.GET}
    )
    public Result logout(HttpServletRequest request) {
        try {
            String userId = UserUtils.getUser().getId();
            if (StringUtils.isNotBlank(userId)) {
                this.jedisCluster.hdel(Constants.CARD_USER_MENU, userId);
            }
        } catch (Exception e) {
            logger.error("退出清除用户菜单缓存失败", e);
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Session session = subject.getSession();
                session.removeAttribute(Constants.SESSION_USER);
                subject.logout();
            }
            String id = request.getSession().getId();
            jedisCluster.del("wx:sessions:" + id);
            jedisCluster.del("wx:sessions:expires:" + id);
            return ok("退出成功");
        } catch (Exception e) {
            logger.info("logout...");
            return error();
        }
    }

    @PostMapping("/workBench/assetUseMonitor")
    public Result assetUseMonitor(HttpServletRequest request, HttpServletResponse response) {

        List<Map<String, Object>> chartList = new ArrayList<>();
        // 入池资产总量
        Map<String, Object> totalMap = new HashMap<>(16);

        Map<String, String> map = new HashMap<>(16);
        map.put("settleDate", nextDate());
        Subject subject = SecurityUtils.getSubject();
        if (!subject.hasRole(roleCode)) {
            map.put("agentNo", getShiroSessionAgentNo());
        }
        String totalAmt = "0";
        String settleAmount = "0";
        String tradeCount = "0";
        totalMap.put("value", totalAmt);
        totalMap.put("name", "交易金额");
        // 已匹配资金方
        Map<String, Object> matchMap = new HashMap<>(16);
        matchMap.put("value", settleAmount);
        matchMap.put("name", "结算金额");
        // 待匹配资金方
        Map<String, Object> waitMatchMap = new HashMap<>(16);
        waitMatchMap.put("value", tradeCount);
        waitMatchMap.put("name", "交易笔数");

        chartList.add(totalMap);
        chartList.add(matchMap);
        chartList.add(waitMatchMap);

        Map<String, Object> amtMap = new HashMap<>(16);
        amtMap.put("totalAmt", totalAmt);
        amtMap.put("matchAmt", settleAmount);
        amtMap.put("tradeCount", tradeCount);

        JSONObject jsonb = new JSONObject();
        jsonb.put("chartList", chartList);
        jsonb.put("amtMap", amtMap);
        jsonb.put("date", nextDate());
        return ok(jsonb);
    }

    @PostMapping("/workBench/assetGap")
    public Result assetGap(HttpServletRequest request) {

        Map<String, Object> paraMap = new HashedMap();
        paraMap.put("capPty", request.getParameter("capPty"));
        paraMap.put("proNum", request.getParameter("proCd"));
        paraMap.put("bizDate", request.getParameter("bizDate"));
        List listTitle = new ArrayList();
        List listData = new ArrayList();

        // 代理商不显示
        Subject subject = SecurityUtils.getSubject();
        if (!subject.hasRole(roleCode)) {
            listTitle.add("");
            listData.add("");
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("listTitle", listTitle);
        map.put("listData", listData);
        map.put("date", DateUtils.getDate());
        if (listTitle.size() > 5) {
            map.put("yHight", listTitle.size());
        } else {
            map.put("yHight", 6);
        }
        return ok(map);
    }

    @PostMapping("/workBench/assetPrediction")
    public Result assetPrediction(HttpServletRequest request) {
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        Map<String, Object> paraMap = new HashMap<>(16);
        paraMap.put("capPty", request.getParameter("capPty"));
        paraMap.put("proNum", request.getParameter("proCd"));
        paraMap.put("flag", request.getParameter("flag"));
        paraMap.put("beginDate", beginDate);
        paraMap.put("endDate", endDate);

        List listTitle = new ArrayList();
        List listData = new ArrayList();
        List listDate = new ArrayList();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < 3; i++) {
            listTitle.add("测试" + i);
            listDate.add(formatter.format(new Date()));
        }

        List title = new ArrayList();
        // 项目名称去重并保持顺序
        for (int i = 0; i < listTitle.size(); i++) {
            if (!title.contains(listTitle.get(i))) {
                title.add(listTitle.get(i));
            }
        }

        List listD = new ArrayList();
        if (StringUtils.isEmpty(beginDate)) {
            listD = confMapper.getYearMonthFromNow();
        } else {
            // 日期去重并保持顺序
            for (int i = 0; i < listDate.size(); i++) {
                if (!listD.contains(listDate.get(i))) {
                    listD.add(listDate.get(i));
                }
            }
        }

        Map<String, Object> mapResult = new HashMap(16);
        mapResult.put("listTitle", title);
        mapResult.put("listDate", listD);

        for (int i = 0; i < title.size(); i++) {
            List listClaim = getClaimGapInfo(title.get(i).toString(), listD);
            if (CollectionUtils.isNotEmpty(listClaim)) {
                listClaim.add(0, title.get(i));
                listData.add(listClaim);
            }
        }
        mapResult.put("listData", listData);
        mapResult.put("date", nextDate());
        return ok(mapResult);
    }

    public List getClaimGapInfo(String title, List listDate) {
        List list = new ArrayList();
        return list;
    }

    public String nextDate() {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayBefore;
    }

    @PostMapping(value = "/workBench/dealtWith")
    public Result dealtWith(
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("page", "{}");
        map.put("userNo", UserUtils.getUser().getNo());
        return ok(map);
    }

}
