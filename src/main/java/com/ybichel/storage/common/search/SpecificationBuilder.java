package com.ybichel.storage.common.search;

import com.ybichel.storage.common.search.rsql.StorageRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SpecificationBuilder<E> {
    /**
     * Create JPA specification for search entity/ies.
     *
     * @param rsqlPredicate    string with RSQL search statement(predicate); might be null;
     * @param additionalFilter additional conditions which are added to the root of search statement;
     * @return JPA specification.
     */
    public static <E> Specification<E> build(final Class<E> entityClass, String rsqlPredicate, LogicalNode additionalFilter) {
        if (StringUtils.isEmpty(rsqlPredicate)) {
            if (additionalFilter == null) {
                return null;
            }

            List<Node> nodes = additionalFilter.getChildren();
            Node rootNode;
            if (nodes.size() == 1) {
                rootNode = nodes.get(0);
            } else {
                rootNode = additionalFilter;
            }

            return rootNode.accept(new StorageRsqlVisitor<>(EntitySearchUtil.getIgnoredFieldNames(entityClass)));
        }

        Node rootNode = new RSQLParser(RSQLOperators.defaultOperators()).parse(rsqlPredicate);

        if (additionalFilter != null) {
            List<Node> nodes = additionalFilter.getChildren();
            if (!nodes.isEmpty()) {
                List<Node> listNodes = new ArrayList<>();
                listNodes.add(rootNode);
                listNodes.addAll(nodes);

                if (additionalFilter instanceof AndNode) {
                    rootNode = new AndNode(listNodes);
                } else if (additionalFilter instanceof OrNode) {
                    rootNode = new OrNode(listNodes);
                }
            }
        }

        return rootNode.accept(new StorageRsqlVisitor<>(EntitySearchUtil.getIgnoredFieldNames(entityClass)));
    }
}
