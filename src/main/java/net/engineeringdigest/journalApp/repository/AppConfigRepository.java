package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.AppConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfigEntity,String> {
}
