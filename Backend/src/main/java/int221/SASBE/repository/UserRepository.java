package int221.SASBE.repository;


import int221.SASBE.entities.User;

import java.util.Optional;

public interface UserRepository extends CustomRepository<User, Integer> {

    boolean existsByUsername(String username);

    boolean existsByname(String name);

    boolean existsByemail(String email);

    User findByUsername(String username);


}
