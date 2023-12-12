package ru.moore.task_management_systems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.moore.task_management_systems.model.Account;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, QuerydslPredicateExecutor<Account> {

}
