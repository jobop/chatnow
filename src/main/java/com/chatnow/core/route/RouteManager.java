package com.chatnow.core.route;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouteManager {
    private static Map<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    private static Map<String, String> channelToUserMap = new ConcurrentHashMap<String, String>();

    private static volatile RouteManager routeManager = new RouteManager();

    public static RouteManager getInstance() {
        if (null == routeManager) {
            synchronized (RouteManager.class) {
                if (null == routeManager) {
                    routeManager = new RouteManager();
                }
            }
        }
        return routeManager;
    }


    public Channel getChannelByUserName(String userName) {
        return channelMap.get(userName);
    }


    public void addToChannelRoute(String userName, Channel channel) {
        channelMap.put(userName, channel);
        channelToUserMap.put(channel.id().asLongText(), userName);
    }

    public void removeByChannelId(String channelId) {
        String userName = channelToUserMap.remove(channelId);
        removeChannel(userName);
    }

    public void removeChannel(String userName) {
        channelMap.remove(userName);
    }

    public static void main(String[] args) {
        Map<String, String> channelMap = new ConcurrentHashMap<String, String>();
        channelMap.put("111", "222");
        channelMap.put("111", "333");
        System.out.println(channelMap.get("111"));

    }
}
