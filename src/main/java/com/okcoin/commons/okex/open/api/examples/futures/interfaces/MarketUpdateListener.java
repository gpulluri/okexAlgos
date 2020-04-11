package com.okcoin.commons.okex.open.api.examples.futures.interfaces;

import com.okcoin.commons.okex.open.api.examples.futures.models.OrderUpdate;
import com.okcoin.commons.okex.open.api.examples.futures.models.FuturesPrice;

public interface MarketUpdateListener {
    void onPriceUpdate(FuturesPrice px);
    void onOrderUpdate(OrderUpdate result);
}
