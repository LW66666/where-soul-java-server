package com.where.soul.bill.controller;


import com.where.soul.bill.dto.TagDTO;
import com.where.soul.bill.entity.Tag;
import com.where.soul.bill.service.ITagService;
import com.where.soul.bill.vo.TagUpdateVO;
import com.where.soul.bill.vo.TagVO;
import com.where.soul.common.Result;
import com.where.soul.common.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 账单标签表 前端控制器
 * </p>
 *
 * @author lw
 * @since 2019-11-28
 */
@RestController
@RequestMapping("/bill/tag")
public class TagController extends BaseController {

    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/add")
    public Result restAddTag(HttpServletRequest request, @Valid TagVO tagVO) {
        Integer id = getUserId(request);
        String name = tagVO.getName();
        Integer parentId = tagVO.getParentId();
        Boolean isRepeat = tagService.isTagRepeat(id, name, parentId);
        if (isRepeat) {
            return Result.customError("标签重复添加！");
        }

        if (!StringUtils.isEmpty(tagVO.getParentId())) {
            Tag pTag = tagService.getById(parentId);
            if (pTag == null) {
                return Result.customError("没有找到该父标签！");
            }
        }

        Integer integer = tagService.insetTag(id, name, parentId);
        if (integer == -1) {
            return Result.customError("添加失败！");
        }

        return Result.success();
    }

    @GetMapping
    public Result restList(HttpServletRequest request) {
        Integer id = getUserId(request);
        List<Tag> tags = tagService.selectListByUserId(id);
        TagDTO tagDTO = new TagDTO();
        return Result.success(tagDTO.buildTagDtoList(tags));
    }

    @GetMapping("/children/{id}")
    public Result restChildrenById(@PathVariable("id") String id, HttpServletRequest request) {
        Integer userId = getUserId(request);
        Integer tagId = Integer.parseInt(id);
        Tag byId = tagService.getById(tagId);
        if (byId == null) {
            return Result.customError("没有找到该标签！");
        }

        List<Tag> tags = tagService.selectListByUserId(userId);
        TagDTO tagDTO = new TagDTO(byId).build(tags);
        return Result.success(tagDTO);
    }

    @PostMapping("/update")
    public Result restChildrenById(@Valid TagUpdateVO tagVO, HttpServletRequest request) {
        Integer userId = getUserId(request);
        Integer id = tagVO.getId();
        Tag byId = tagService.getById(id);
        if (byId == null) {
            return Result.error("没有找到该标签！");
        }
        BeanUtils.copyProperties(tagVO, byId);
        Integer integer = tagService.update(userId, byId);
        if (integer == -1) {
            return Result.customError("操作异常！");
        }
        return Result.success();
    }
}
