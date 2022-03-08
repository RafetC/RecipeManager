package com.platform.recipe.repository.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

@AllArgsConstructor
@Getter
@Setter
public class RecipeSearchQueryCriteriaConsumer implements Consumer<SearchCriteria> {
    private Predicate predicate;
    private CriteriaBuilder builder;
    private Root r;

    @Override
    public void accept(SearchCriteria param) {
        if (param.getOperation().equalsIgnoreCase(">")
                && (param.getKey().length() > 0)
                && param.getValue().isPresent()
        ) {
            predicate = builder.and(predicate, builder
                    .greaterThanOrEqualTo(r.get(param.getKey()), param.getValue().get().toString()));
        } else if (param.getOperation().equalsIgnoreCase("<")
                && (param.getKey().length() > 0)
                && param.getValue().isPresent()) {
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(
                    r.get(param.getKey()), param.getValue().get().toString()));
        } else if (param.getOperation().equalsIgnoreCase(":")
                && (param.getKey().length() > 0)
                && param.getValue().isPresent()) {

            predicate = builder.and(predicate, builder.equal(
                    r.get(param.getKey()), param.getValue().get()));

        } else if (param.getOperation().equalsIgnoreCase("%")
                && (param.getKey().length() > 0)
                && param.getValue().isPresent()) {

            predicate = builder.and(predicate, builder.like(
                    r.get(param.getKey()), "%" + param.getValue().get() + "%"));

        }


    }
}
