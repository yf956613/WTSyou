package com.qingye.wtsyou.modle;

import com.qingye.wtsyou.basemodel.EntityBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pm89 on 2018/4/27.
 */

public class EntityPageData extends EntityBase {

    private Content content;

    public class Content<T extends Serializable> implements Serializable
    {
        private int page;
        private int total;
        private int page_size;
        private int page_count;
        private List<T> data = new ArrayList();

        public void setPage(int page) {
            this.page = page;
        }

        public int getPage() {

            return page;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setPageSize(int page_size) {
            this.page_size = page_size;
        }

        public int getPageSize() {
            return page_size;
        }

        public void setPageCount(int page_count) {
            this.page_count = page_count;
        }

        public int getPageCount() {
            return page_count;
        }

        public void setData(List<T> data) {
            this.data = data;
        }
        public List<T> getData() {
            return data;
        }

    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
