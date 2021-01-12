package com.takumi.wms.adapter.expand;


import com.takumi.wms.model.InOutDocument;

/**
 * Created by hbh on 2017/4/20.
 * 父布局Item点击监听接口
 */

public interface ItemClickListener {
    /**
     * 展开子Item
     * @param bean
     */
    void onExpandChildren(InOutDocument.LinesBean bean);

    /**
     * 隐藏子Item
     * @param bean
     */
    void onHideChildren(InOutDocument.LinesBean bean);
}
