package com.trans.payment.boss.controller.system;

import com.github.pagehelper.PageInfo;
import com.trans.payment.boss.commons.Result;
import com.trans.payment.boss.controller.BaseController;
import com.trans.payment.boss.entity.Dict;
import com.trans.payment.boss.entity.DictType;
import com.trans.payment.boss.service.system.DictService;
import com.trans.payment.boss.service.system.DictTypeService;
import com.trans.payment.boss.utils.DictUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/dict"
)
public class DictController extends BaseController {


    @Resource
    private DictService dictService;

    @Resource
    private DictTypeService dictTypeService;

    @PostMapping(value = "getDictData")
    public Result getDictData(DictType dictType) {
            dictType.setStatus(null);
            List<DictType> typeList = dictTypeService.findList(dictType);
        Map<String, Object> map = new HashMap<>(16);
            map.put("data", dictType);
            map.put("dataDictTypeList", typeList);
            return successResult(map);
    }

    /**
     * 查询所有的字典值集合 API接口 原接口 list(Dict dict, HttpServletRequest request, HttpServletResponse response, Model model)
     * @param dict
     * @return
     */
    @PostMapping(value = "showDictListData")
    public Result showDictListData(
            Dict dict,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        if (dict.getDictType() != null && StringUtils
                .isNoneBlank(dict.getDictType().getKey())) {
            dict.setType(dict.getDictType().getKey());
        }
        PageInfo<Dict> page = dictService.findDictpage(dict, pageNo, pageSize);
        return ok(page);
    }


    @PostMapping(value = "save")
    public Result save(Dict dict) {
        dictService.save(dict);
        return ok(dict);
    }

    /**
     * 保存字典值对象 API接口 原接口 save(Dict dict, Model model, RedirectAttributes redirectAttributes)
     * @param dict
     * @return
     */
    @PostMapping(value = "saveDictData")
    public Result saveDictData(Dict dict, Model model) {
        Map map = new HashMap<>(16);
        //        if (!beanValidator(model, dict)) {
        //            map.put("data", errorResult("数据准确性验证错误"));
        //            return JSON.toJSONString(map); //转换为jsonp类型的数据
        //        }
        //        if (dict.getId() == null && DictUtils.getDict(dict.getType().getValue(),
        // dict.getValue()) != null) {
        //            map.put("data", errorResult("已经存在字典值为 " + dict.getValue() + " 的字典项！"));
        //            return JSON.toJSONString(map); //转换为jsonp类型的数据
        //        }
        dictService.save(dict);
        String type = dict.getType();
        if (StringUtils.isNotBlank(type)) {
            // dictTypeService.updateDictVersion(dict.getType().getValue());//增加版本号
        }
        return ok();
    }

    @PostMapping(value = "delete")
    @ResponseBody
    public Result delete(Dict dict) {
        dictService.delete(dict);
        return ok(dict);
    }

    /**
     * 删除字典值对象 API 接口 原接口 delete(Dict dict)
     * @param dict
     * @return
     */
    @PostMapping(value = "deleteDictData")
    public Result deleteDictData(Dict dict) {
        dictService.delete(dict);
        return ok("删除字典'" + dict.getLabel() + "'成功");
    }

    @PostMapping(value = "enable")
    public Result enable(Dict dict) {
        dictService.enable(dict);
        return ok(dict);
    }

    /**
     * 设置字典值是否生效或者弃用 API接口 原接口 enable(Dict dict)
     * @param dict
     * @return
     */
    @PostMapping(value = "enableDictData")
    @ResponseBody
    public Result enableDictData(Dict dict) {
        dictService.enable(dict);
        String type = dict.getType();
        if (StringUtils.isNotBlank(type)) {
        }
        return successResult("设置成功!");
    }

    /**
     * 查询所有的字典值集合信息 API接口 原接口 listData(@RequestParam(required=false) String type)
     * @param dict
     * @return
     */
    @PostMapping(value = "showDictAllListData")
    @ResponseBody
    public Result showDictAllListData(Dict dict) {
        List<Dict> list = dictService.findList(dict);
        return successResult(list);
    }

    @PostMapping(value = "treeData")
    public Result treeData(
            @RequestParam(required = true) String type,
            @RequestParam(required = false) String parentId,
            @RequestParam(required = false) Long grade,
            @RequestParam(required = false) Boolean isAll,
            HttpServletResponse response) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Dict> list = DictUtils.getDictList(type);
        for (int i = 0; i < list.size(); i++) {
            Dict e = list.get(i);
            if (grade != null) {
            }

            Map<String, Object> map = new HashMap<>(16);
            map.put("id", e.getValue());
            map.put("pId", e.getParent());
            map.put("name", e.getLabel());
            map.put("text", e.getLabel());
            map.put("sort", e.getSort());
            mapList.add(map);
        }
        return ok(mapList);
    }


    @PostMapping(value = "showDictByTypeData")
    public Result showDictByTypeData(
            @RequestParam(required = true) String value,
            @RequestParam(required = true) String version) {
        List<DictType> rlist = new ArrayList<>();
        String[] values = value.split(",");
        String[] versions = version.split(",");
        try {
            for (int v = 0; v < values.length; v++) {
                DictType dt = new DictType();
                dt.setValue(values[v]);
                dt.setVersion(new Integer(versions[v]));
                rlist.add(dt);
            }
            // 处理结束
            List<Map> returnList = new ArrayList<>();
            Map<String, Object> returnMap = null;
            for (int i = 0; i < rlist.size(); i++) {
                //DictType dictType = dictTypeService.getByDictType(rlist.get(i));
                //if(dictType == null) {
                //    continue;
                //}
                returnMap = new HashMap<>(16);
                returnMap.put("value", rlist.get(i).getValue());
                returnMap.put("version", rlist.get(i).getVersion());
                // 如果版本与数据库版本一致 跳过【后期优化】
                //if(dictType.getVersion().equals(rlist.get(i).getVersion())) {
                //    returnMap.put("list", null);
                //    returnList.add(returnMap);
                //    continue;
                //}
                String type = rlist.get(i).getValue();
                List<Dict> dictList = DictUtils.getTkDictList(type);
                // 过滤前端只需要的字段
                List<Map<String, String>> tempList = new ArrayList<>();
                Map<String, String> tempMap = null;
                for (int j = 0; j < dictList.size(); j++) {
                    tempMap = new HashMap<>(16);
                    tempMap.put("label", dictList.get(j).getLabel());
                    tempMap.put("value", dictList.get(j).getValue());
                    tempMap.put("status", dictList.get(j).getStatus());
                    tempList.add(tempMap);
                }
                returnMap.put("list", tempList);
                returnList.add(returnMap);
            }
            return ok(returnList);
        } catch (Exception e) {
            return error("系统异常");
        }
    }
}
