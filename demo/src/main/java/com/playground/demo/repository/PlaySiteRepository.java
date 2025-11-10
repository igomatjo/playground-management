package com.playground.demo.repository;

import com.playground.demo.model.PlaySiteEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaySiteRepository extends JpaRepository<PlaySiteEntity, Long> {

  @Query("SELECT ps FROM PlaySiteEntity ps JOIN ps.children c WHERE c.ticketNumber = :ticketNumber")
  Optional<PlaySiteEntity> findByChildTicketNumber(String ticketNumber);

  @Query("SELECT ps FROM PlaySiteEntity ps JOIN ps.queue q WHERE q.child.ticketNumber = :ticketNumber")
  Optional<PlaySiteEntity> findByQueueChildTicketNumber(String ticketNumber);

  default Optional<PlaySiteEntity> findPlaySiteByTicketNumber(String ticketNumber) {
    Optional<PlaySiteEntity> byTicketNumber = findByChildTicketNumber(ticketNumber);
    if (byTicketNumber.isPresent()) {
      return byTicketNumber;
    }
    return findByQueueChildTicketNumber(ticketNumber);
  }

  @Query("SELECT SUM(ps.totalVisitorsToday) FROM PlaySiteEntity ps")
  int getTotalVisitorsToday();
}
