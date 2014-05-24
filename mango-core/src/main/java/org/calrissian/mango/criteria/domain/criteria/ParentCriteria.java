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
package org.calrissian.mango.criteria.domain.criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 11/9/12
 * Time: 1:52 PM
 */
public abstract class ParentCriteria implements Criteria {
  private static final long serialVersionUID = 1L;

  protected List<Criteria> nodes;
  protected ParentCriteria parent;

  public ParentCriteria() {
    nodes = new ArrayList<Criteria>();
  }

  public ParentCriteria(ParentCriteria parent, List<Criteria> nodes) {
    this.parent = parent;
    this.nodes = nodes;
  }

  public ParentCriteria(ParentCriteria parent) {
    this.parent = parent;
    this.nodes = new ArrayList<Criteria>();
  }


  @Override
  public ParentCriteria parent() {
    return parent;
  }

  @Override
  public List<Criteria> children() {
    return nodes;
  }

  @Override
  public void addChild(Criteria node) {
    nodes.add(node);
  }

  @Override
  public void removeChild(Criteria node) {
    nodes.remove(node);
  }

  public List<Criteria> getNodes() {
    return nodes;
  }

  public void setNodes(List<Criteria> nodes) {
    this.nodes = nodes;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
            "nodes=" + nodes +
            ", parent=" + (parent != null ? parent.getClass().getSimpleName() : "null") +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ParentCriteria that = (ParentCriteria) o;

    if (nodes != null ? !nodes.equals(that.nodes) : that.nodes != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = nodes != null ? nodes.hashCode() : 0;
    result = 31 * result + (parent != null ? parent.hashCode() : 0);
    return result;
  }
}
