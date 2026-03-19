/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2026. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.shoppingmall.thread.channel;


import com.nhnacademy.shoppingmall.thread.request.ChannelRequest;
import java.util.LinkedList;
import java.util.Queue;

public class RequestChannel {

    private final Queue<ChannelRequest> queue;
    private final int queueMaxSize;

    public RequestChannel(int queueMaxSize) {
        this.queueMaxSize = queueMaxSize;
        this.queue = new LinkedList<>();
    }

    public synchronized ChannelRequest getRequest() throws InterruptedException {
        // queue가 비어있다면 대기합니다.
        while (queue.isEmpty()) {
            wait();
        }

        // queue에서 request 반환합니다.
        ChannelRequest request = queue.poll();

        notifyAll();

        return request;
    }

    public synchronized void addRequest(ChannelRequest request) throws InterruptedException {
        // queue가 가득차있다면 요청이 소비될 때까지 대기합니다.
        while (queue.size() >= queueMaxSize) {
            wait();
        }

        // queue에 요청을 추가하고 대기하고 있는 스레드를 깨웁니다
        queue.offer(request);

        notifyAll();
    }

}
