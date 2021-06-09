package com.open.boss.controller.system;

import com.github.pagehelper.PageInfo;
import com.open.boss.commons.Result;
import com.open.boss.controller.BaseController;
import com.open.common.constants.Constants;
import com.open.boss.entity.DictType;
import com.open.boss.service.system.DictTypeService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping(
        value = "/dict/type"
)
public class DictTypeController extends BaseController {

    @Resource
    private DictTypeService dictTypeService;

    @Resource
    private JedisCluster jedisCluster;

    /**
     * 查询所有的字典类型值 API 接口 原接口 list(DictType dictType, HttpServletRequest request, HttpServletResponse response, Model
     * model)
     * @param dictType
     * @return
     */
    @PostMapping(value = "showDictTypeListData")
    public Result showDictTypeListData(
            DictType dictType,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        dictType.setStatus(null);
        PageInfo<DictType> page = dictTypeService.findDictPage(dictType, pageNo, pageSize);
        return ok(page);
    }

    /**
     * 查询字典类型对象 API接口 原接口 form(DictType dictType)
     * @param dictType
     * @return
     */
    @PostMapping(value = "selectDictTypeData")
    public Result selectDictTypeData(DictType dictType) {
        return ok(dictType);
    }

    /**
     * 保存字典类型对象 API接口 原接口 save(DictType dictType, Model model, RedirectAttributes redirectAttributes)
     * @param dictType
     * @return
     */
    @PostMapping(value = "saveDictTypeData")
    public Result saveDictTypeData(DictType dictType) {
        dictTypeService.save(dictType);
        String key = Constants.CARD_SYS_DICT_TYPE + dictType.getValue();
        String dictKey = Constants.CARD_SYS_DICT + dictType.getValue();
        if (jedisCluster.exists(key)) {
            jedisCluster.del(key);
        }
        if (jedisCluster.exists(dictKey)) {
            jedisCluster.del(dictKey);
        }
        return successResult("保存字典类型'" + dictType.getLabel() + "'成功");
    }

    /**
     * 删除字典类型对象 API 接口 原接口 delete(DictType dictType)
     * @param dictType
     * @return
     */
    @PostMapping(value = "deleteDictTypeData")
    public Result deleteDictTypeData(DictType dictType) {
        dictTypeService.delete(dictType);
        return successResult("删除字典类型'" + dictType.getLabel() + "'成功");
    }

    /**
     * 设置字典值是否生效或者弃用 API接口 原接口 enable(DictType dictType)
     * @param dictType
     * @return
     */
    @PostMapping(value = "enableDictTypeData")
    public Result enableDictTypeData(DictType dictType) {
        dictTypeService.enable(dictType);
        return successResult("设置成功!");
    }
}
