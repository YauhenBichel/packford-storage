package com.ybichel.storage.common.search.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class GenericRsqlSpecification<T> implements Specification<T> {

    private String property;
    private ComparisonOperator operator;
    private List<String> arguments;

    public GenericRsqlSpecification(final String property, final ComparisonOperator operator, final List<String> arguments) {
        super();
        this.property = property;
        this.operator = operator;
        this.arguments = arguments;
    }

    @Override
    public Predicate toPredicate(final Root<T> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
        try {
            final List<Object> args = castArguments(root);
            final Object argument = args.get(0);
            query.distinct(true);
            switch (RsqlSearchOperation.getSimpleOperator(operator)) {
                case EQUAL: {
                    if (isLikeArgument(argument)) {
                        return builder.like(getPath(root).as(String.class), argument.toString().replace('*', '%'));
                    } else if (argument == null) {
                        return builder.isNull(getPath(root));
                    } else {
                        return builder.equal(getPath(root), unEscapeArgument(argument));
                    }
                }
                case NOT_EQUAL: {
                    if (isLikeArgument(argument)) {
                        return builder.notLike(getPath(root).as(String.class), argument.toString().replace('*', '%'));
                    } else if (argument == null) {
                        return builder.isNotNull(getPath(root));
                    } else {
                        return builder.notEqual(getPath(root), unEscapeArgument(argument));
                    }
                }
                case GREATER_THAN: {
                    return builder.greaterThan(getPath(root), getComparableArgument(argument));
                }
                case GREATER_THAN_OR_EQUAL: {
                    return builder.greaterThanOrEqualTo(getPath(root), getComparableArgument(argument));
                }
                case LESS_THAN: {
                    return builder.lessThan(getPath(root), getComparableArgument(argument));
                }
                case LESS_THAN_OR_EQUAL: {
                    return builder.lessThanOrEqualTo(getPath(root), getComparableArgument(argument));
                }
                case IN:
                    return getPath(root).in(args);
                case NOT_IN:
                    return builder.not(getPath(root).in(args));
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalRsqlArgumentException(e.getMessage(), e);
        }

        return null;
    }

    // === private

    private boolean isLikeArgument(Object argument) {
        return (argument instanceof String)
                && (argument.toString().contains("*") || argument.toString().contains("%"))
                && !argument.toString().contains("\\*");
    }

    private boolean isLikeOperation(Object argument) {
        RsqlSearchOperation operation = RsqlSearchOperation.getSimpleOperator(operator);
        return isLikeArgument(argument) &&
                (operation.equals(RsqlSearchOperation.EQUAL) || operation.equals(RsqlSearchOperation.NOT_EQUAL));
    }

    private Comparable getComparableArgument(Object argument) {
        try {
            return (Comparable) argument;
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getPath(final Root<T> root) {
        // next class citizen can be left-join to main class by "." symbol
        if (property.contains(".")) {
            String[] properties = property.split("\\.");
            From<?, ?> from = root;

            for (int i = 0; i < properties.length - 1; i++) {
                final String joinPropertyAlias = properties[i];

                boolean isPropertyJoined = from.getJoins().stream()
                        .anyMatch(j -> j.getAttribute().getName().equals(joinPropertyAlias));

                if (!isPropertyJoined) {
                    from = from.join(joinPropertyAlias, JoinType.LEFT);
                } else {
                    Optional<? extends Join<?, ?>> joinFound = from.getJoins().stream()
                            .filter(j -> j.getAttribute().getName().equals(joinPropertyAlias))
                            .findFirst();

                    if (joinFound.isPresent()) {
                        from = joinFound.get();
                    }
                }
            }

            return from.get(properties[properties.length - 1]);
        }

        return root.get(property);
    }

    private List<Object> castArguments(final Root<T> root) {
        if (isLikeOperation(arguments.get(0))) {
            return new ArrayList<>(arguments);
        }

        final Class<? extends Object> type = getPath(root).getJavaType();

        final List<Object> args = arguments.stream().map(arg -> {
            if (type.equals(String.class)) {
                return arg;
            } else if (type.equals(LocalDateTime.class)) {
                return LocalDateTime.parse(arg);
            } else if (type.equals(LocalDate.class)) {
                return LocalDate.parse(arg);
            } else if (type.equals(UUID.class)) {
                return UUID.fromString(arg);
            } else if (type.equals(Integer.class) || type.isPrimitive() && type.getName().equals("int")) {
                return Integer.parseInt(arg);
            } else if (type.equals(Long.class) || type.isPrimitive() && type.getName().equals("long")) {
                return Long.parseLong(arg);
            } else if (type.equals(Double.class) || type.isPrimitive() && type.getName().equals("double")) {
                return Double.parseDouble(arg);
            } else if (type.equals(Byte.class) || type.isPrimitive() && type.getName().equals("byte")) {
                return Byte.parseByte(arg);
            } else if (type.equals(Short.class) || type.isPrimitive() && type.getName().equals("short")) {
                return Short.parseShort(arg);
            } else if (type.equals(Float.class) || type.isPrimitive() && type.getName().equals("float")) {
                return Float.parseFloat(arg);
            } else if (type.equals(Character.class) || type.isPrimitive() && type.getName().equals("char")) {
                return arg.length() == 0 ? Character.MIN_VALUE : arg.charAt(0);
            } else if (type.equals(BigDecimal.class)) {
                return new BigDecimal(arg);
            } else if (type.isEnum()) {
                try {
                    return type.getDeclaredMethod("valueOf", String.class).invoke(null, arg.toUpperCase());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            } else if (type.equals(Boolean.class) || type.isPrimitive() && type.getName().equals("boolean")) {
                return Boolean.parseBoolean(arg);
            } else {
                return arg;
            }
        }).collect(Collectors.toList());

        return args;
    }

    private Object unEscapeArgument(Object argument) {
        if (argument instanceof String) {
            return argument.toString().replace("\\*", "*");
        }
        return argument;
    }
}
