package br.gov.df.detran.repository;
import br.gov.df.detran.domain.Deposito;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Deposito entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {

}
