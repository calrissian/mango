/*
 * Copyright (C) 2017 The Calrissian Authors
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
package org.calrissian.mango.criteria.domain;

import java.util.Collection;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

public class NotInLeaf<T extends Collection> extends TermValueLeaf<T> {

    public NotInLeaf(String key, T value, ParentNode parent) {
        super(key, value, parent);
    }

    @Override
    public Node clone(ParentNode node) {
        return new NotInLeaf<>(getTerm(), getValue(), node);
    }

    @Override
    public String toString() {
        return "!in(" + getValue().stream().map(Objects::toString).collect(joining(",")) + ")";
    }
}
