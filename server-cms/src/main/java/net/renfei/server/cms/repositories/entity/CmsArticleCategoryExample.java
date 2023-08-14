package net.renfei.server.cms.repositories.entity;

import java.util.ArrayList;
import java.util.List;

public class CmsArticleCategoryExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public CmsArticleCategoryExample() {
        oredCriteria = new ArrayList<>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPidIsNull() {
            addCriterion("pid is null");
            return (Criteria) this;
        }

        public Criteria andPidIsNotNull() {
            addCriterion("pid is not null");
            return (Criteria) this;
        }

        public Criteria andPidEqualTo(Long value) {
            addCriterion("pid =", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotEqualTo(Long value) {
            addCriterion("pid <>", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThan(Long value) {
            addCriterion("pid >", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidGreaterThanOrEqualTo(Long value) {
            addCriterion("pid >=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThan(Long value) {
            addCriterion("pid <", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidLessThanOrEqualTo(Long value) {
            addCriterion("pid <=", value, "pid");
            return (Criteria) this;
        }

        public Criteria andPidIn(List<Long> values) {
            addCriterion("pid in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotIn(List<Long> values) {
            addCriterion("pid not in", values, "pid");
            return (Criteria) this;
        }

        public Criteria andPidBetween(Long value1, Long value2) {
            addCriterion("pid between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andPidNotBetween(Long value1, Long value2) {
            addCriterion("pid not between", value1, value2, "pid");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameIsNull() {
            addCriterion("category_en_name is null");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameIsNotNull() {
            addCriterion("category_en_name is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameEqualTo(String value) {
            addCriterion("category_en_name =", value, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameNotEqualTo(String value) {
            addCriterion("category_en_name <>", value, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameGreaterThan(String value) {
            addCriterion("category_en_name >", value, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameGreaterThanOrEqualTo(String value) {
            addCriterion("category_en_name >=", value, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameLessThan(String value) {
            addCriterion("category_en_name <", value, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameLessThanOrEqualTo(String value) {
            addCriterion("category_en_name <=", value, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameLike(String value) {
            addCriterion("category_en_name like", value, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameNotLike(String value) {
            addCriterion("category_en_name not like", value, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameIn(List<String> values) {
            addCriterion("category_en_name in", values, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameNotIn(List<String> values) {
            addCriterion("category_en_name not in", values, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameBetween(String value1, String value2) {
            addCriterion("category_en_name between", value1, value2, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryEnNameNotBetween(String value1, String value2) {
            addCriterion("category_en_name not between", value1, value2, "categoryEnName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameIsNull() {
            addCriterion("category_zh_name is null");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameIsNotNull() {
            addCriterion("category_zh_name is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameEqualTo(String value) {
            addCriterion("category_zh_name =", value, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameNotEqualTo(String value) {
            addCriterion("category_zh_name <>", value, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameGreaterThan(String value) {
            addCriterion("category_zh_name >", value, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameGreaterThanOrEqualTo(String value) {
            addCriterion("category_zh_name >=", value, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameLessThan(String value) {
            addCriterion("category_zh_name <", value, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameLessThanOrEqualTo(String value) {
            addCriterion("category_zh_name <=", value, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameLike(String value) {
            addCriterion("category_zh_name like", value, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameNotLike(String value) {
            addCriterion("category_zh_name not like", value, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameIn(List<String> values) {
            addCriterion("category_zh_name in", values, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameNotIn(List<String> values) {
            addCriterion("category_zh_name not in", values, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameBetween(String value1, String value2) {
            addCriterion("category_zh_name between", value1, value2, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andCategoryZhNameNotBetween(String value1, String value2) {
            addCriterion("category_zh_name not between", value1, value2, "categoryZhName");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cms_article_category
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table cms_article_category
     *
     * @mbg.generated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}