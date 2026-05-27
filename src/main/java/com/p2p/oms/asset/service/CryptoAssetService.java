package com.p2p.oms.asset.service;

import com.p2p.oms.asset.dto.request.CreateCryptoAssetRequest;
import com.p2p.oms.asset.entity.CryptoAsset;

public interface CryptoAssetService {
    CryptoAsset create(CreateCryptoAssetRequest request);
    void deactivate();
}
