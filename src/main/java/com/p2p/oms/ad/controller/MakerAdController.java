package com.p2p.oms.ad.controller;

import com.p2p.oms.ad.dto.request.ChangeAdStatusRequest;
import com.p2p.oms.ad.dto.request.CreateMakerAdRequest;
import com.p2p.oms.ad.dto.request.UpdateMakerAdRequest;
import com.p2p.oms.ad.dto.response.MakerAdResponse;
import com.p2p.oms.ad.dto.response.PageResponse;
import com.p2p.oms.ad.entity.MakerAd;
import com.p2p.oms.ad.query.SearchAdsCriteria;
import com.p2p.oms.ad.service.command.MakerAdService;
import com.p2p.oms.ad.service.query.MakerAdQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@NullMarked
@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class MakerAdController {

    private final MakerAdService commandService;
    private final MakerAdQueryService queryService;

    // --- Query Operations ---

    @GetMapping
    public ResponseEntity<PageResponse<MakerAdResponse>> search(
            @ModelAttribute SearchAdsCriteria criteria,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(queryService.search(criteria, pageable));
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponse<MakerAdResponse>> getMyAds(
            @RequestAttribute("userId") UUID userId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(queryService.getUserAds(userId, pageable));
    }

    @GetMapping("/{adId}")
    public ResponseEntity<MakerAdResponse> getAdById(@PathVariable UUID adId) {
        return ResponseEntity.ok(queryService.getAdById(adId));
    }

    // --- Command Operations ---

    @PostMapping
    public ResponseEntity<MakerAd> createAd(
            @RequestAttribute("userId") UUID userId,
            @Valid @RequestBody CreateMakerAdRequest request
    ) {
        return ResponseEntity.ok(commandService.create(userId, request));
    }

    @PutMapping("/{adId}")
    public ResponseEntity<MakerAd> updateAd(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID adId,
            @Valid @RequestBody UpdateMakerAdRequest request
    ) {
        return ResponseEntity.ok(commandService.update(adId, userId, request));
    }

    @PatchMapping("/{adId}/status")
    public ResponseEntity<Void> changeStatus(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID adId,
            @Valid @RequestBody ChangeAdStatusRequest request
    ) {
        commandService.changeStatus(adId, userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{adId}")
    public ResponseEntity<Void> deleteAd(
            @RequestAttribute("userId") UUID userId,
            @PathVariable UUID adId
    ) {
        commandService.delete(adId, userId);
        return ResponseEntity.ok().build();
    }
}