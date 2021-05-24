package ga.negyahu.music.area;

import ga.negyahu.music.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AreaRepository extends JpaRepository<Area,Integer> {

    @Query("select ar from Area as ar join fetch ar.accounts where ar.id = :id")
    Area findAreaWithAccountsById(Integer id);

}
