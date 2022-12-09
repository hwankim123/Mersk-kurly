package com.prgrms.merskkurly.domain.common.dto;

import com.prgrms.merskkurly.domain.item.dto.ItemResponse.Shortcuts;
import com.prgrms.merskkurly.domain.item.entity.Category;

import java.util.List;

public class HomeResponse {
    private List<Category> categoryList;
    private List<Shortcuts> topRankingItems;

    public HomeResponse(List<Category> categoryList, List<Shortcuts> topRankingItems) {
        this.categoryList = categoryList;
        this.topRankingItems = topRankingItems;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(
            List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Shortcuts> getTopRankingItems() {
        return topRankingItems;
    }

    public void setTopRankingItems(
            List<Shortcuts> topRankingItems) {
        this.topRankingItems = topRankingItems;
    }
}
