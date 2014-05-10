/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.domain;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.System.currentTimeMillis;

public class BaseEvent extends AbstractTupleCollection implements Event {

  private String id;
  private long timestamp;

  public BaseEvent(String id, long timestamp) {
    checkNotNull(id);
    this.id = id;
    this.timestamp = timestamp;
  }

  public BaseEvent() {
    this.id = UUID.randomUUID().toString();
    this.timestamp = currentTimeMillis();
  }

  public String getId() {
    return id;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
