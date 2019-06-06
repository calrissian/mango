/*
 * Copyright (C) 2019 The Calrissian Authors
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
package org.calrissian.mango.collect;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

/**
 * This class is an extension of the {@link AbstractIterator} class which provides additional
 * support for closing the {@link Iterator} quietly.
 */
public abstract class AbstractCloseableIterator<T> extends AbstractIterator<T> implements CloseableIterator<T> {
}
