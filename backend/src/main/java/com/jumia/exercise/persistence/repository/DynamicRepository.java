package com.jumia.exercise.persistence.repository;

import com.jumia.exercise.persistence.Filter;
import com.jumia.exercise.persistence.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.jpa.domain.Specification.where;

public abstract class DynamicRepository<T> {

    abstract List<Customer> getQueryResult();

    abstract Page<Customer> getQueryResult(Pageable pageable);

    abstract Page<Customer> getQueryResult(Filter filter, Pageable pageable);

    abstract Page<T> getQueryResult(List<Filter> filters, Pageable pageable);

    protected Specification<T> getSpecificationFromFilters(List<Filter> filter) {
        Specification<T> specification = where(createSpecification(filter.remove(0)));
        for (Filter input : filter) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    private Specification<T> createSpecification(Filter input) {
        switch (input.getOperator()) {
            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case NOT_EQ:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(input.getField()),
                                (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(input.getField()),
                                (Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
            case LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()), "%" + input.getValue() + "%");
            case STARTS_WITH:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()), input.getValue() + "%");
            case ENDS_WITH:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()), "%" + input.getValue());
            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField()))
                                .value(castToRequiredType(root.get(input.getField()).getJavaType(), input.getValues()));
            case REGEX:
                return (root, query, criteriaBuilder) -> createRegexPredicate(root, criteriaBuilder, input.getField(), input.getValue(), false);
            case REGEX_IN:
                return (root, query, criteriaBuilder) -> createRegexInPredicate(root, criteriaBuilder, input.getField(), input.getValues(), false);
            case NOT_REGEX:
                return (root, query, criteriaBuilder) -> createRegexPredicate(root, criteriaBuilder, input.getField(), input.getValue(), true);
            case NOT_REGEX_IN:
                return (root, query, criteriaBuilder) -> createRegexInPredicate(root, criteriaBuilder, input.getField(), input.getValues(), true);
            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    private Predicate createRegexPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, String field, String value, boolean notEqual) {
        return criteriaBuilder.equal(
                criteriaBuilder.function("REGEXP", Integer.class,
                        root.get(field),
                        criteriaBuilder.literal(Pattern.compile(value).pattern())), notEqual ? 0 : 1);
    }

    private Predicate createRegexInPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, String field, List<String> values, boolean notEqual) {
        return notEqual
                ? criteriaBuilder.and(values.stream().map(value -> createRegexPredicate(root, criteriaBuilder, field, value, notEqual)).toArray(Predicate[]::new))
                : criteriaBuilder.or(values.stream().map(value -> createRegexPredicate(root, criteriaBuilder, field, value, notEqual)).toArray(Predicate[]::new));
    }

    private Object castToRequiredType(Class fieldType, String value) {
        if (fieldType.isAssignableFrom(String.class)) {
            return String.valueOf(value);
        } else if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value);
        }
        return null;
    }

    private Object castToRequiredType(Class fieldType, List<String> value) {
        List<Object> lists = new ArrayList<>();
        for (String s : value) {
            lists.add(castToRequiredType(fieldType, s));
        }
        return lists;
    }
}