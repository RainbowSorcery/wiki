package com.lyra.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyra.wiki.entity.Content;
import com.lyra.wiki.entity.ContentSnapshot;
import com.lyra.wiki.entity.Doc;
import com.lyra.wiki.mapper.ContentMapper;
import com.lyra.wiki.mapper.DocMapper;
import com.lyra.wiki.service.IContentSnapshotService;
import com.lyra.wiki.service.IDocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyra.wiki.websocket.MyWebSocketHandle;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private MyWebSocketHandle webSocketHandle;

    @Autowired
    private IContentSnapshotService contentSnapshotService;

    @Override
    public List<Doc> treeList(Long ebookId) {
        QueryWrapper<Doc> queryWrapper = new QueryWrapper<>();
            
        if (ebookId != null) {
            queryWrapper.eq("ebook_id", ebookId);
        }
        List<Doc> allDocList = docMapper.selectList(queryWrapper);

        return allDocList.stream().filter((doc -> doc.getParent() == 0)).peek((doc -> {
            List<Doc> docChild = this.getDocChild(doc, allDocList);
            doc.setChildren(docChild);
        })).collect(Collectors.toList());
    }

    @Override
    public List<Doc> getSelectedTreeData(Long id, Long ebookId) {

        Doc docById = docMapper.selectById(id);
        List<Doc> docs = docMapper.selectList(new QueryWrapper<Doc>().eq("ebook_id", ebookId));

        List<Doc> disabledDocChildrenList = getChildrenAndSetDisabled(id, docs);
        if (disabledDocChildrenList != null) {
            docById.setChildren(disabledDocChildrenList);
        }
        docById.setDisabled(true);

        List<Doc> collect = treeList(ebookId);


        return setDoc(collect, docById);
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
        return allDocList.stream().filter((doc) -> Objects.equals(doc.getParent(), currentDoc.getId())).peek((doc -> {
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
        // 若此文档有子节点 那么一并删除
        getDeleteIds(docChild, deleteIds);
        // 将父id也一并添加上
        deleteIds.add(docId);

        docMapper.deleteBatchIds(deleteIds);

    }

    @Override
    @Transactional
    public void addDoc(Doc doc) {
        docMapper.insert(doc);
        Content content = new Content();
        BeanUtils.copyProperties(doc, content);
        contentMapper.insert(content);


        // 插入时也向快照表中抱保存一份
        contentSnapshotService.insertContentSnapshot(content.getContent(), doc.getId());

    }

    @Override
    public void increaseViewCount(Long id) {
        docMapper.increaseViewCount(id);
    }

    @Override
    @Async
    public void increaseVoteCount(Long id) {
        docMapper.increaseVoteCount(id);
        Doc doc = docMapper.selectById(id);

        // 进行消息推送
        if (doc != null) {
            webSocketHandle.sendMessage("文档[" + doc.getName() + "]" + "被成功点赞!");
        }

    }

    @Override
    @Transactional
    public void updateDoc(Doc doc) {
        docMapper.updateById(doc);
        Content content = new Content();
        content.setId(doc.getId());
        content.setContent(doc.getContent());

        Content selectContent = contentMapper.selectById(content.getId());

        // 插入快照表
        contentSnapshotService.insertContentSnapshot(selectContent.getContent(), doc.getId());

        if (selectContent == null) {
            contentMapper.insert(content);
        } else {

            contentMapper.updateById(content);
        }

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
