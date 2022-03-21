package com.lyra.wiki.service;

import com.lyra.wiki.entity.Doc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文档 服务类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
public interface IDocService extends IService<Doc> {

    List<Doc> treeList(Long ebookId);

    List<Doc> getSelectedTreeData(Long id);

    void deleteDoc(Long docId);
}
