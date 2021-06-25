package ua.lviv.MiniATM.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lviv.MiniATM.entities.DebitCard;

import java.util.Optional;

@Repository
public interface DebitCardRepo extends JpaRepository<DebitCard, Long> {

    Optional<DebitCard> findByCardNum(String cardNum);

}
