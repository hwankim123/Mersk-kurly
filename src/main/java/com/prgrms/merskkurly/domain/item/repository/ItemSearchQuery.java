package com.prgrms.merskkurly.domain.item.repository;

import com.prgrms.merskkurly.domain.item.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemSearchQuery {

  private final String query;

  private ItemSearchQuery(ItemSearchQueryBuilder itemSearchQueryBuilder) {
    this.query = itemSearchQueryBuilder.query;
  }

  public String getQuery() {
    return query;
  }

  public static class ItemSearchQueryBuilder {

    private static final String BASE_QUERY = "select * from item where 1=1";
    private static final String KEYWORD_QUERY = " and name like ";
    private static final String CATEGORY_QUERY = " and category like ";

    private static final Logger log = LoggerFactory.getLogger("ItemSearchQueryBuilder");

    private StringBuilder queryBuilder;
    private String query;

    public ItemSearchQueryBuilder() {
      this.queryBuilder = new StringBuilder(BASE_QUERY);
    }

    public ItemSearchQueryBuilder keyword(String keyword) {
      if (keyword == null) {
        log.info("Keyword is null. Search for every name of item.");
        return this;
      }
      String keywordQuery = "'%" + keyword + "%'";
      this.queryBuilder.append(KEYWORD_QUERY).append(keywordQuery);
      return this;
    }

    public ItemSearchQueryBuilder category(Category category) {
      if (category == null) {
        log.info("Category is null. Search for every category of item");
        return this;
      }
      String categoryQuery = "'%" + category + "%'";
      this.queryBuilder.append(CATEGORY_QUERY).append(categoryQuery);
      return this;
    }

    public ItemSearchQuery build() {
      this.query = queryBuilder.toString();
      System.out.println("query = " + query);
      return new ItemSearchQuery(this);
    }
  }
}
