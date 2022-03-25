package com.ybichel.storage.common.search.rsql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.Set;

public class StorageRsqlVisitor<T> implements RSQLVisitor<Specification<T>, Void> {

    private GenericRsqlSpecBuilder<T> builder;

    public StorageRsqlVisitor() {
        this(new HashSet<String>());
    }

    public StorageRsqlVisitor(Set<String> excludedFields) {
        builder = new GenericRsqlSpecBuilder<T>(excludedFields);
    }

    @Override
    public Specification<T> visit(final AndNode node, final Void param) {
        return builder.createSpecification(node);
    }

    @Override
    public Specification<T> visit(final OrNode node, final Void param) {
        return builder.createSpecification(node);
    }

    @Override
    public Specification<T> visit(final ComparisonNode node, final Void params) {
        return builder.createSpecification(node);
    }
}
