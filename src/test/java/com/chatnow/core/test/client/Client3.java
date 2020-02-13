package com.chatnow.core.test.client;

import java.io.IOException;

/**
 * Created by Enzo Cotter on 2020/2/12.
 */
public class Client3 {
    public static void main(String[] args) throws InterruptedException, IOException {
        BaseClient client = new BaseClient("127.0.0.1", 12350);
        client.start();
    }
}
