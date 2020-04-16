package com.okcoin.commons.okex.open.api.examples.futures.connectivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okcoin.commons.okex.open.api.examples.futures.interfaces.MarketUpdateListener;
import com.okcoin.commons.okex.open.api.examples.futures.models.FuturesPrice;
import com.okcoin.commons.okex.open.api.examples.futures.models.OrderUpdate;
import com.okcoin.commons.okex.open.api.websocket.WebSocket;
import com.okcoin.commons.okex.open.api.websocket.WebSocketAdapter;

import java.util.List;

public class WSAdaptor extends WebSocketAdapter {

    MarketUpdateListener listener;

    public WSAdaptor(MarketUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onTextMessage(WebSocket ws, String text) throws Exception {
        JSONObject obj = JSON.parseObject(text);
        if(obj.containsKey("table")) {
            if(obj.getString("table").equals("futures/ticker")) {
                Object data = obj.get("data");
                List<Object> prices = JSON.parseArray(data.toString());
                for(Object px : prices){
                    try {
                        FuturesPrice price = JSON.parseObject(px.toString(), FuturesPrice.class);
                        listener.onPriceUpdate(price);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else if((obj.getString("table").equals("futures/order"))) {
                Object data = obj.get("data");
                List<Object> orderUpdates = JSON.parseArray(data.toString());
                for(Object update : orderUpdates){
                    try {
                        OrderUpdate orderUpdate = JSON.parseObject(update.toString(), OrderUpdate.class);
                        listener.onOrderUpdate(orderUpdate);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if(obj.containsKey("event")) {
            Object event = obj.get("data");
            System.out.println("event : " + text);
        }


        //System.out.println("ws message: " + text);
        if (text.contains("checksum")) {
            boolean res = ws.checkSum(text);
        }
    }

    @Override
    public void onWebsocketOpen(WebSocket ws) {
        System.out.println("ws open...");
        //ws.subscribe(instrument);
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) {
        cause.printStackTrace();
    }


    @Override
    public void onWebsocketClose(WebSocket ws, int code) {
        System.out.println("ws close code = " + code);
    }

    @Override
    public void onWebsocketPong(WebSocket ws) {
        //System.out.println("...");
    }
}
