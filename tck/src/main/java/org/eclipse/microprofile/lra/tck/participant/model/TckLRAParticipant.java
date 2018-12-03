/*
 *******************************************************************************
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.eclipse.microprofile.lra.tck.participant.model;

import org.eclipse.microprofile.lra.participant.LRAParticipant;

import javax.ws.rs.NotFoundException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class TckLRAParticipant {

    public static final AtomicInteger COMPLETED_COUNT = new AtomicInteger(0);
    public static final AtomicInteger COMPENSATED_COUNT = new AtomicInteger(0);
    
    private TckLRAParticipant() {}
    
    public static final class SuccessLRAParticipant implements LRAParticipant {
        
        @Override
        public Future<Void> completeWork(URL lraId) throws NotFoundException {
            System.out.println("Completing LRA " + lraId.toExternalForm());

            COMPLETED_COUNT.incrementAndGet();

            return null;
        }

        @Override
        public Future<Void> compensateWork(URL lraId) throws NotFoundException {
            // not used
            return null;
        }    
    }

    public static final class DelayedLRAParticipant implements LRAParticipant {
        
        public static final int DELAY = 500;

        @Override
        public Future<Void> completeWork(URL lraId) throws NotFoundException {
            // not used
            return null;
        }

        @Override
        public Future<Void> compensateWork(URL lraId) throws NotFoundException {
            System.out.println("Compensating LRA " + lraId.toExternalForm());

            CompletableFuture<Void> result = new CompletableFuture<>();

            Executors.newCachedThreadPool().submit(() -> {
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                COMPENSATED_COUNT.incrementAndGet();
                result.complete(null);
            });

            return result;
        }
    }
    
    
}
