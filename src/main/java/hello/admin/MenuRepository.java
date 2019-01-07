package hello.admin;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Created by chenchx on 2019/1/3.
 */
public interface MenuRepository extends CrudRepository<MenuDo,Long> {

    @Query(value = "select distinct m.perms\n" +
            "\t\tfrom hj_sys_menu m left join\n" +
            "\t\thj_sys_role_menu rm on m.menu_id = rm.menu_id\n" +
            "\t\tleft join hj_sys_admin_role ur\n" +
            "\t\ton rm.role_id = ur.role_id where ur.user_id\n" +
            "\t\t= ?1",nativeQuery = true)
    Set<String> listPerms(String userId);
}
