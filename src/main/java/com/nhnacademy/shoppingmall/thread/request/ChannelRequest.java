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

package com.nhnacademy.shoppingmall.thread.request;

import java.util.concurrent.atomic.AtomicLong;

public abstract class ChannelRequest {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private final long requestId;

    protected ChannelRequest() {
        requestId = ID_GENERATOR.incrementAndGet();
    }

    public abstract void execute();

    @Override
    public String toString() {
        return "ChannelRequest{" +
                "requestId=" + requestId +
                '}';
    }

}
