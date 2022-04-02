package com.lyra.wiki.service;

import com.lyra.wiki.entity.Ebook;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 电子书 服务类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
public interface IEbookService extends IService<Ebook> {
    /**
     * 更新ebook详情信息 如电子书下文档总数 阅读总数 点赞总数
     */
    void updateEbookInfo();
}
