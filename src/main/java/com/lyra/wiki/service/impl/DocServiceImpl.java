package com.lyra.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyra.wiki.entity.Doc;
import com.lyra.wiki.mapper.DocMapper;
import com.lyra.wiki.service.IDocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 文档 服务实现类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements IDocService {
    @Autowired
    private DocMapper docMapper;

    @Override
    public List<Doc> treeList(Long ebookId) {
        QueryWrapper<Doc> queryWrapper = new QueryWrapper<>();
            
        if (ebookId != null) {
            queryWrapper.eq("ebook_id", ebookId);
        }
        List<Doc> allDocList = docMapper.selectList(queryWrapper);

        List<Doc> collect = allDocList.stream().filter((doc -> {
            return doc.getParent() == 0;
        })).peek((doc -> {
            List<Doc> docChild = this.getDocChild(doc, allDocList);
            doc.setChildren(docChild);
        })).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<Doc> getSelectedTreeData(Long id) {
        Doc docById = docMapper.selectById(id);
        List<Doc> docs = docMapper.selectList(null);
        List<Doc> disabledDocChildrenList = getChildrenAndSetDisabled(id, docs);
        docById.setChildren(disabledDocChildrenList);
        docById.setDisabled(true);

        List<Doc> collect = treeList(null);
        List<Doc> docs1 = setDoc(collect, docById);


        Doc doc = new Doc();
        doc.setId(0L);
        doc.setName("无");
        docs1.add(0, doc);

        return docs1;
    }

    public List<Doc> setDoc(List<Doc> treeData, Doc disableDoc) {

        return treeData.stream().peek((doc -> {
            if (Objects.equals(doc.getId(), disableDoc.getId())) {
                doc.setDisabled(true);
                doc.setChildren(disableDoc.getChildren());
            }
            setDoc(doc.getChildren(), disableDoc);
        })).collect(Collectors.toList());
    }

    private List<Doc> getChildrenAndSetDisabled(Long id, List<Doc> allDoc) {
        // 根据doc列表递归调用过滤出要禁用的列表 过滤条件为传入的id与当前id一致 递归调用过滤出children也一并设置
        return allDoc.stream().filter((doc) -> {
            doc.setDisabled(true);
            return Objects.equals(doc.getParent(), id);
        }).peek((doc -> {
            List<Doc> docChild = this.getDocChild(doc, allDoc);
            doc.setChildren(docChild);
        })).collect(Collectors.toList());

    }


    private List<Doc> getDocChild(Doc currentDoc, List<Doc> allDocList) {
        // 碧莲查询
        return allDocList.stream().filter((doc) -> {
            return Objects.equals(doc.getParent(), currentDoc.getId());
        }).peek((doc -> {
            List<Doc> docChild = this.getDocChild(doc, allDocList);
            doc.setChildren(docChild);
        })).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDoc(Long docId) {
        Doc parentDoc = docMapper.selectById(docId);

        List<Doc> allDoc = docMapper.selectList(null);

        List<Doc> docChild = getDocChild(parentDoc, allDoc);

        List<Long> deleteIds = new LinkedList<>();
        getDeleteIds(docChild, deleteIds);
        // 将父id也一并添加上
        deleteIds.add(docId);

        docMapper.deleteBatchIds(deleteIds);

    }

    private void getDeleteIds(List<Doc> docChildren, List<Long> deleteIds) {
        // 遍历docChildren元素 若元素中有子元素则递归调用 并将id保存至deleteIds中
        for (Doc docChild : docChildren) {
            if (docChild.getChildren().size() > 0) {
                getDeleteIds(docChild.getChildren(), deleteIds);
            }
            deleteIds.add(docChild.getId());
        }
    }
}
