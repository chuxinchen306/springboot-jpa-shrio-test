package hello.admin;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by chenchx on 2019/1/3.
 */
public interface UserRepository extends CrudRepository<AdminDo,Long> {
    AdminDo findByUserName(String username);
}
