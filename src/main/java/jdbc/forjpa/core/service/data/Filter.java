package jdbc.forjpa.core.service.data;

import org.springframework.util.Assert;

import java.util.List;

/**
 * Model to encapsulate the filtering. Eg. EQUAL,IN.
 *
 * @auther Archan on 16/10/17.
 */
public class Filter {
    enum FilterType {
        EQUAL(FilterValueType.SINGLE), NOT_EQUAL(FilterValueType.SINGLE), IN(FilterValueType.MULTI),
        NOT_IN(FilterValueType.MULTI), BETWEEN(FilterValueType.MULTI), IS_NULL(FilterValueType.NO_VALUE), IS_NOT_NULL(FilterValueType.NO_VALUE);
        private FilterValueType valueType;

        FilterType(FilterValueType valueType) {
            this.valueType = valueType;
        }
    }

    enum FilterValueType {
        SINGLE, MULTI, NO_VALUE
    }

    private String fieldName;
    private FilterType filterType;
    private List<Object> filterValue;


    public Filter(String fieldName, FilterType filterType, List<Object> filterValue) {
        Assert.notNull(filterType, "Filter type can't be null or empty");
        Assert.notNull(fieldName, "Field name can't be null or empty");
        this.fieldName = fieldName;
        this.filterType = filterType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public List<Object> getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(List<Object> filterValue) {
        if (FilterValueType.NO_VALUE.equals(this.getFilterType().valueType)) {
            return;
        }
        Assert.notNull(filterValue, "Please pass a non null value to set.");
        if (FilterValueType.SINGLE.equals(this.getFilterType().valueType)) {
            if (filterValue.size() > 1) {
                throw new IllegalStateException("Filter " + FilterValueType.SINGLE + " should have only one value");
            } else {
                setFilterValueInternal(filterValue);
            }
        } else if (FilterValueType.MULTI.equals(this.getFilterType().valueType)) {
            setFilterValueInternal(filterValue);
        }
    }

    private void setFilterValueInternal(List<Object> filterValue) {
        this.filterValue = filterValue;
    }

}
