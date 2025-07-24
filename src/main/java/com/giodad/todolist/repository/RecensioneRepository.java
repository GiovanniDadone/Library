package com.giodad.todolist.repository;

import com.giodad.todolist.domain.Recensione;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Recensione entity.
 */
@Repository
public interface RecensioneRepository extends JpaRepository<Recensione, Long>, JpaSpecificationExecutor<Recensione> {
    @Query("select recensione from Recensione recensione where recensione.user.login = ?#{authentication.name}")
    List<Recensione> findByUserIsCurrentUser();

    default Optional<Recensione> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Recensione> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Recensione> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select recensione from Recensione recensione left join fetch recensione.user",
        countQuery = "select count(recensione) from Recensione recensione"
    )
    Page<Recensione> findAllWithToOneRelationships(Pageable pageable);

    @Query("select recensione from Recensione recensione left join fetch recensione.user")
    List<Recensione> findAllWithToOneRelationships();

    @Query("select recensione from Recensione recensione left join fetch recensione.user where recensione.id =:id")
    Optional<Recensione> findOneWithToOneRelationships(@Param("id") Long id);
}
