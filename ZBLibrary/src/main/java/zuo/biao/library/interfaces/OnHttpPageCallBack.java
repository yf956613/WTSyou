package zuo.biao.library.interfaces;

import java.util.List;

import zuo.biao.library.model.EntityBase;

/**
 * Created by pm89 on 2018/5/30.
 */

public interface OnHttpPageCallBack <T extends EntityBase, itemData> extends OnHttpCallBack {
    List<itemData> getList(T data);

    String getRequestJsonStr(int page,int pageSize);

    void emptyPagingList();

    void refreshSuccessPagingList(List<itemData> list);

    void noMorePagingList();

    void loadMoreSuccessPagingList(List<itemData> list);

    void refreshErrorPagingList();

    void loadMoreErrorPagingList();
}
