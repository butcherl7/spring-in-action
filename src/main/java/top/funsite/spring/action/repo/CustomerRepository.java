package top.funsite.spring.action.repo;

import org.springframework.data.domain.Limit;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.util.Streamable;
import top.funsite.spring.action.entity.Customer;

import java.util.List;

/**
 * @author Butcher
 */
public interface CustomerRepository extends ListCrudRepository<Customer, Long>, QueryByExampleExecutor<Customer>, PagingAndSortingRepository<Customer, Long> {

    List<Customer> findByNameIn(List<String> names);

    Streamable<Customer> findByGender(Boolean gender);

    List<Customer> findAllByName(String name, Limit limit);
}
