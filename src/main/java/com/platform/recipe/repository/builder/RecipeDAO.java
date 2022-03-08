package com.platform.recipe.repository.builder;

import com.platform.recipe.model.jpa.Recipe;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class RecipeDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Recipe> searchRecipe(List<SearchCriteria> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> query = builder.createQuery(Recipe.class);
        Root r = query.from(Recipe.class);

        Predicate predicate = builder.conjunction();

        RecipeSearchQueryCriteriaConsumer searchConsumer =
                new RecipeSearchQueryCriteriaConsumer(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        List<Recipe> result = entityManager.createQuery(query).getResultList();
        return result;
    }


}
