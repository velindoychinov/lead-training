package org.example.training.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

public class ServiceUtils {

  /**
   * Prepares a PageRequest based on filter and default sort.
   *
   * @param filter      the filter object containing optional page, size, sort, direction
   * @param defaultSort default sort orders to apply if not specified in filter
   * @return a PageRequest with combined sorting
   */
  public static PageRequest paging(PageFilter filter, List<Sort.Order> defaultSort) {
    int page = Math.max(0, orElse(filter.getPage(), 1) - 1);
    int size = Math.max(1, orElse(filter.getSize(), 100));

    List<Sort.Order> orders = new ArrayList<>();

    // Add filter sort if present
    if (filter.getSort() != null && !filter.getSort().isBlank()) {
      Sort.Direction direction = orElse(filter.getDirection(), Sort.Direction.ASC);
      orders.add(new Sort.Order(direction, filter.getSort()));
    }

    // Add default sort orders that are not duplicates of filter.getSort()
    if (defaultSort != null) {
      defaultSort.stream()
        .filter(o -> filter.getSort() == null || !o.getProperty().equalsIgnoreCase(filter.getSort()))
        .forEach(orders::add);
    }

    // Fallback: if orders are still empty, use defaultSort
    if (orders.isEmpty() && defaultSort != null) {
      orders.addAll(defaultSort);
    }

    return PageRequest.of(page, size, Sort.by(orders));
  }

  public static <T> T orElse(T value, T defaultValue) {
    return value != null ? value : defaultValue;
  }

  public static String orBlank(String value) {
    return value != null && !value.isBlank() ? value : null;
  }

  public static boolean eq(BigDecimal a, BigDecimal b) {
    return a != null && b != null && a.compareTo(b) == 0;
  }

  public static boolean ge(BigDecimal a, BigDecimal b) {
    return a != null && b != null && a.compareTo(b) >= 0;
  }

  public static boolean gt(BigDecimal a, BigDecimal b) {
    return a != null && b != null && a.compareTo(b) > 0;
  }

  public static <T> List<T> listOf(T ...value) {
    return Arrays.stream(value).filter(val -> val != null).toList();
  }

  // Predicate

  public static <E, T extends Comparable<? super T>>
  Optional<Predicate> ge(CriteriaBuilder cb, Root<E> root, T value, Function<Root<E>, Expression<T>> exprFn) {
    return value == null ? Optional.empty() : Optional.of(cb.greaterThanOrEqualTo(exprFn.apply(root), value));
  }

  public static <E, T extends Comparable<? super T>>
  Optional<Predicate> gt(CriteriaBuilder cb, Root<E> root, T value, Function<Root<E>, Expression<T>> exprFn) {
    return value == null ? Optional.empty() : Optional.of(cb.greaterThan(exprFn.apply(root), value));
  }

  public static <E, T extends Comparable<? super T>>
  Optional<Predicate> le(CriteriaBuilder cb, Root<E> root, T value, Function<Root<E>, Expression<T>> exprFn) {
    return value == null ? Optional.empty() : Optional.of(cb.lessThanOrEqualTo(exprFn.apply(root), value));
  }

  public static <E, T extends Comparable<? super T>>
  Optional<Predicate> lt(CriteriaBuilder cb, Root<E> root, T value, Function<Root<E>, Expression<T>> exprFn) {
    return value == null ? Optional.empty() : Optional.of(cb.lessThan(exprFn.apply(root), value));
  }

  public static <E, T>
  Optional<Predicate> eq(CriteriaBuilder cb, Root<E> root, T value, Function<Root<E>, Expression<T>> exprFn) {
    return value == null ? Optional.empty() : Optional.of(cb.equal(exprFn.apply(root), value));
  }

  public static <E, T> Optional<Predicate> in(
    CriteriaBuilder cb,
    Root<E> root,
    Collection<T> values,
    Function<Root<E>, Expression<T>> exprFn
  ) {
    if (values == null || values.isEmpty()) {
      return Optional.empty();
    }

    CriteriaBuilder.In<T> inClause = cb.in(exprFn.apply(root));
    values.forEach(inClause::value);

    return Optional.of(inClause);
  }

  @SafeVarargs
  public static <E>
  Optional<Predicate> anyLike(CriteriaBuilder cb, Root<E> root, String term, Function<Root<E>, Expression<String>>... exprFns) {
    if (term == null || term.isBlank() || exprFns == null || exprFns.length == 0) {
      return Optional.empty();
    }

    String pattern = "%" + term.toLowerCase() + "%";

    Predicate[] predicates = Arrays.stream(exprFns)
      .map(fn -> cb.like(cb.lower(fn.apply(root)), pattern))
      .toArray(Predicate[]::new);

    return Optional.of(cb.or(predicates));
  }

  public static <E, T extends Comparable<? super T>>
  Optional<Predicate> between(
    CriteriaBuilder cb,
    Root<E> root,
    T min,
    T max,
    Function<Root<E>, Expression<T>> exprFn
  ) {
    if (min != null && max != null) {
      return Optional.of(cb.between(exprFn.apply(root), min, max));
    }
    if (min != null) {
      return Optional.of(cb.greaterThanOrEqualTo(exprFn.apply(root), min));
    }
    if (max != null) {
      return Optional.of(cb.lessThanOrEqualTo(exprFn.apply(root), max));
    }
    return Optional.empty();
  }

  // Specification

  public static <E, T extends Comparable<? super T>> Specification<E> ge(T value, Function<Root<E>, Expression<T>> exprFn) {
    return (root, query, cb) -> ge(cb, root, value, exprFn).orElse(cb.conjunction());
  }

  public static <E, T extends Comparable<? super T>> Specification<E> gt(T value, Function<Root<E>, Expression<T>> exprFn) {
    return (root, query, cb) -> gt(cb, root, value, exprFn).orElse(cb.conjunction());
  }

  public static <E, T extends Comparable<? super T>> Specification<E> le(T value, Function<Root<E>, Expression<T>> exprFn) {
    return (root, query, cb) -> le(cb, root, value, exprFn).orElse(cb.conjunction());
  }

  public static <E, T extends Comparable<? super T>> Specification<E> lt(T value, Function<Root<E>, Expression<T>> exprFn) {
    return (root, query, cb) -> lt(cb, root, value, exprFn).orElse(cb.conjunction());
  }

  public static <E, T> Specification<E> eq(T value, Function<Root<E>, Expression<T>> exprFn) {
    return (root, query, cb) -> eq(cb, root, value, exprFn).orElse(cb.conjunction());
  }

  public static <E, T> Specification<E> in(Collection<T> values, Function<Root<E>, Expression<T>> exprFn) {
    return (root, query, cb) -> {
      query.distinct(true);
      return in(cb, root, values, exprFn).orElse(cb.conjunction());
    };
  }

  @SafeVarargs
  public static <E> Specification<E> anyLike(String term, Function<Root<E>, Expression<String>>... exprFns) {
    return (root, query, cb) -> anyLike(cb, root, term, exprFns).orElse(cb.conjunction());
  }

  public static <E, T extends Comparable<? super T>>
  Specification<E> between(
    T min,
    T max,
    Function<Root<E>, Expression<T>> exprFn
  ) {
    return (root, query, cb) -> between(cb, root, min, max, exprFn).orElse(cb.conjunction());
  }

}

