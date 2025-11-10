package com.playground.demo.controller;

import com.playground.demo.dto.AddChildRequest;
import com.playground.demo.dto.AddChildResponse;
import com.playground.demo.dto.CreatePlaySiteRequest;
import com.playground.demo.dto.PlaySiteResponse;
import com.playground.demo.service.PlaySiteService;
import com.playground.demo.service.TestDataService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/play-sites")
public class PlaySiteController {

  private final PlaySiteService playSiteService;
  private final TestDataService testDataService;

  public PlaySiteController(PlaySiteService playSiteService, TestDataService testDataService) {
    this.playSiteService = playSiteService;
    this.testDataService = testDataService;
  }

  @PostMapping
  public ResponseEntity<PlaySiteResponse> createPlaySite(@RequestBody CreatePlaySiteRequest request) {
    PlaySiteResponse response = playSiteService.createPlaySite(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PlaySiteResponse> updatePlaySite(
      @PathVariable Long id,
      @RequestBody CreatePlaySiteRequest request) {
    return playSiteService.updatePlaySite(id, request)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PlaySiteResponse> getPlaySite(@PathVariable Long id) {
    return playSiteService.getPlaySite(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<PlaySiteResponse>> getAllPlaySites() {
    List<PlaySiteResponse> playSites = playSiteService.getAllPlaySites();
    return ResponseEntity.ok(playSites);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePlaySite(@PathVariable Long id) {
    boolean deleted = playSiteService.deletePlaySite(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @GetMapping("/{id}/utilization")
  public ResponseEntity<Integer> getPlaySiteUtilization(@PathVariable Long id) {
    return playSiteService.getPlaySiteUtilization(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/visitors/today")
  public ResponseEntity<Integer> getTotalVisitorsToday() {
    int count = playSiteService.getTotalVisitorsToday();
    return ResponseEntity.ok(count);
  }

  @PostMapping("/{id}/children")
  public ResponseEntity<AddChildResponse> addChildToPlaySite(
      @PathVariable Long id,
      @RequestBody AddChildRequest request) {
    AddChildResponse response = playSiteService.addChildToPlaySite(id, request);
    HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(response);
  }

  @DeleteMapping("/children/{ticketNumber}")
  public ResponseEntity<Void> removeChildFromPlaySite(@PathVariable String ticketNumber) {
    boolean removed = playSiteService.removeChildFromPlaySite(ticketNumber);
    return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @PostMapping("/load-data")
  public ResponseEntity<List<PlaySiteResponse>> loadTestData() {
    List<PlaySiteResponse> playSites = testDataService.loadTestData();
    return ResponseEntity.ok(playSites);
  }
}

