package top.funsite.spring.action;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import top.funsite.spring.action.entity.Customer;
import top.funsite.spring.action.repo.CustomerRepository;

import java.util.List;

@SpringBootTest
class SpringInActionApplicationTests {

    @Resource
    private CustomerRepository customerRepo;

    @Resource
    private NamedParameterJdbcOperations jdbcOperations;

    @Test
    void testSave() {
        Customer josh = customerRepo.save(Customer.builder()
                .id(27)
                .name("-www-xxx")
                // .createAt(LocalDateTime.of(2023, 1, 1, 0, 0))
                .build());
        System.out.println(josh.getId());
    }

    @Test
    void testUpdate() {
        // jdbcOperations.update()
    }

    @Test
    void testListByNames() {
        List<Customer> list = customerRepo.findByNameIn(List.of("Josh", "Joshua"));
        System.out.println(list.size());
    }

    @Test
    void testStreamable() {
        List<Customer> list = customerRepo.findByGender(true)
                .toList();
        System.out.println(list.size());
    }

    @Test
    void testPagingSortingLimiting() {
        customerRepo.findAll(PageRequest.of(2, 5));
        customerRepo.findAll(Sort.sort(Customer.class).by(Customer::getId).descending());
        customerRepo.findAllByName("AAA", Limit.of(1));
    }

}
