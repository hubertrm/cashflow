package be.hubertrm.cashflow.application.mapper;

import java.util.Collection;
import java.util.List;

/**
 * The Interface GenericMapper provides the methods for mapping models to data transfer objects and back.
 * @param <M> the element type of the model object
 * @param <D> the generic type of the data transfer object
 */
public interface GenericMapper<M, D> {

    /**
     * Maps the given model object to a data transfer object
     * @param model the model object
     * @return the data transfer object
     */
    D toDto(M model);

    /**
     * Maps the given collection of model objects to a list of data transfer objects
     * @param entities the collection of model objects
     * @return the list of data transfer objects
     */
    List<D> toDtoList(Collection<M> entities);

    /**
     * Maps the given data transfer object to an model object
     * @param dto the data transfer object
     * @return the model object
     */
    M toModel(D dto);

    /**
     * Maps the given collection of model objects to a list of data transfer objects
     * @param dtoList the collection of data transfer objects
     * @return the list of model objects
     */
    List<M> toModelList(Collection<D> dtoList);
}
