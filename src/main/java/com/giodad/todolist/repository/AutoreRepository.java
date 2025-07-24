package com.giodad.todolist.repository;

import com.giodad.todolist.domain.Autore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Autore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoreRepository extends JpaRepository<Autore, Long>, JpaSpecificationExecutor<Autore> {}
