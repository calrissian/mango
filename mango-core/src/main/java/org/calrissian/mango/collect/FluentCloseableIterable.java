package org.calrissian.mango.collect;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

/**
 */
public abstract class FluentCloseableIterable<T> extends AbstractCloseableIterable<T> {

    protected FluentCloseableIterable() {
    }

    /**
     * The rest of the methods are taken from guava directly
     */

    public static <E> FluentCloseableIterable<E> from(final CloseableIterable<E> iterable) {
        return (iterable instanceof FluentCloseableIterable) ? (FluentCloseableIterable<E>) iterable
                : new FluentCloseableIterable<E>() {

            @Override
            protected void doClose() throws IOException {
                iterable.close();
            }

            @Override
            protected Iterator<E> retrieveIterator() {
                return iterable.iterator();
            }
        };
    }

    /**
     * Returns a string representation of this fluent iterable, with the format
     * {@code [e1, e2, ..., en]}.
     */
    @Override
    public String toString() {
        return Iterables.toString(this);
    }

    /**
     * Returns the number of elements in this fluent iterable.
     */
    public final int size() {
        return Iterables.size(this);
    }

    /**
     * Returns {@code true} if this fluent iterable contains any object for which
     * {@code equals(element)} is true.
     */
    public final boolean contains(Object element) {
        return Iterables.contains(this, element);
    }

    /**
     * Returns a fluent iterable whose {@code Iterator} cycles indefinitely over the elements of
     * this fluent iterable.
     *
     * <p>That iterator supports {@code remove()} if {@code iterable.iterator()} does. After
     * {@code remove()} is called, subsequent cycles omit the removed element, which is no longer in
     * this fluent iterable. The iterator's {@code hasNext()} method returns {@code true} until
     * this fluent iterable is empty.
     *
     * <p><b>Warning:</b> Typical uses of the resulting iterator may produce an infinite loop. You
     * should use an explicit {@code break} or be certain that you will eventually remove all the
     * elements.
     */
    public final FluentCloseableIterable<T> cycle() {
        return from(CloseableIterables.wrap(Iterables.cycle(this), this));
    }

    /**
     * Returns the elements from this fluent iterable that satisfy a predicate. The
     * resulting fluent iterable's iterator does not decorator {@code remove()}.
     */
    public final FluentCloseableIterable<T> filter(Predicate<? super T> predicate) {
        return from(CloseableIterables.filter(this, predicate));
    }

    /**
     * Returns the elements from this fluent iterable that are instances of class {@code type}.
     *
     * @param type the type of elements desired
     */
    public final <E> FluentCloseableIterable<E> filter(Class<E> type) {
        return from(CloseableIterables.wrap(Iterables.filter(this, type), this));
    }

    /**
     * Returns {@code true} if any element in this fluent iterable satisfies the predicate.
     */
    public final boolean anyMatch(Predicate<? super T> predicate) {
        return Iterables.any(this, predicate);
    }

    /**
     * Returns {@code true} if every element in this fluent iterable satisfies the predicate.
     * If this fluent iterable is empty, {@code true} is returned.
     */
    public final boolean allMatch(Predicate<? super T> predicate) {
        return Iterables.all(this, predicate);
    }

    /**
     * Returns an {@link com.google.common.base.Optional} containing the first element in this fluent iterable that
     * satisfies the given predicate, if such an element exists.
     *
     * <p><b>Warning:</b> avoid using a {@code predicate} that matches {@code null}. If {@code null}
     * is matched in this fluent iterable, a {@link NullPointerException} will be thrown.
     */
    public final Optional<T> firstMatch(Predicate<? super T> predicate) {
        return Iterables.tryFind(this, predicate);
    }

    /**
     * Returns a fluent iterable that applies {@code function} to each element of this
     * fluent iterable.
     *
     * <p>The returned fluent iterable's iterator supports {@code remove()} if this iterable's
     * iterator does. After a successful {@code remove()} call, this fluent iterable no longer
     * contains the corresponding element.
     */
    public final <E> FluentCloseableIterable<E> transform(Function<? super T, ? extends E> function) {
        return from(CloseableIterables.transform(this, function));
    }

    /**
     * Applies {@code function} to each element of this fluent iterable and returns
     * a fluent iterable with the concatenated combination of results.  {@code function}
     * returns an Iterable of results.
     *
     * <p>The returned fluent iterable's iterator supports {@code remove()} if this
     * function-returned iterables' iterator does. After a successful {@code remove()} call,
     * the returned fluent iterable no longer contains the corresponding element.
     *
     * @since 13.0
     */
    public <E> FluentCloseableIterable<E> transformAndConcat(
            Function<? super T, ? extends Iterable<E>> function) {
        return from(CloseableIterables.concat(transform(function)));
    }

    /**
     * Returns an {@link Optional} containing the first element in this fluent iterable.
     * If the iterable is empty, {@code Optional.absent()} is returned.
     *
     * @throws NullPointerException if the first element is null; if this is a possibility, use
     *     {@code iterator().next()} or {@link Iterables#getFirst} instead.
     */
    public final Optional<T> first() {
        Iterator<T> iterator = this.iterator();
        return iterator.hasNext()
                ? Optional.of(iterator.next())
                : Optional.<T>absent();
    }

    /**
     * Returns an {@link Optional} containing the last element in this fluent iterable.
     * If the iterable is empty, {@code Optional.absent()} is returned.
     *
     * @throws NullPointerException if the last element is null; if this is a possibility, use
     *     {@link Iterables#getLast} instead.
     */
    public final Optional<T> last() {
        // Iterables#getLast was inlined here so we don't have to throw/catch a NSEE
        Iterator<T> iterator = this.iterator();
        if (!iterator.hasNext()) {
            return Optional.absent();
        }

        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext()) {
                return Optional.of(current);
            }
        }
    }

    /**
     * Returns a view of this fluent iterable that skips its first {@code numberToSkip}
     * elements. If this fluent iterable contains fewer than {@code numberToSkip} elements,
     * the returned fluent iterable skips all of its elements.
     *
     * <p>Modifications to this fluent iterable before a call to {@code iterator()} are
     * reflected in the returned fluent iterable. That is, the its iterator skips the first
     * {@code numberToSkip} elements that exist when the iterator is created, not when {@code skip()}
     * is called.
     *
     * <p>The returned fluent iterable's iterator supports {@code remove()} if the
     * {@code Iterator} of this fluent iterable supports it. Note that it is <i>not</i>
     * possible to delete the last skipped element by immediately calling {@code remove()} on the
     * returned fluent iterable's iterator, as the {@code Iterator} contract states that a call
     * to {@code * remove()} before a call to {@code next()} will throw an
     * {@link IllegalStateException}.
     */
    public final FluentCloseableIterable<T> skip(int numberToSkip) {
        return from(CloseableIterables.wrap(Iterables.skip(this, numberToSkip), this));
    }

    /**
     * Creates a fluent iterable with the first {@code size} elements of this
     * fluent iterable. If this fluent iterable does not contain that many elements,
     * the returned fluent iterable will have the same behavior as this fluent iterable.
     * The returned fluent iterable's iterator supports {@code remove()} if this
     * fluent iterable's iterator does.
     *
     * @param size the maximum number of elements in the returned fluent iterable
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public final FluentCloseableIterable<T> limit(int size) {
        return from(CloseableIterables.limit(this, size));
    }

    /**
     * Determines whether this fluent iterable is empty.
     */
    public final boolean isEmpty() {
        return !this.iterator().hasNext();
    }

    /**
     * Returns an {@code ImmutableList} containing all of the elements from this
     * fluent iterable in proper sequence.
     */
    public final ImmutableList<T> toImmutableList() {
        return ImmutableList.copyOf(this);
    }

    /**
     * Returns an {@code ImmutableList} containing all of the elements from this
     * {@code FluentCloseableIterable} in the order specified by {@code comparator}.  To produce an
     * {@code ImmutableList} sorted by its natural ordering, use
     * {@code toSortedImmutableList(Ordering.natural())}.
     *
     * @param comparator the function by which to sort list elements
     * @throws NullPointerException if any element is null
     * @since 13.0
     */
    public final ImmutableList<T> toSortedImmutableList(Comparator<? super T> comparator) {
        return Ordering.from(comparator).immutableSortedCopy(this);
    }

    /**
     * Returns an {@code ImmutableSet} containing all of the elements from this
     * fluent iterable with duplicates removed.
     */
    public final ImmutableSet<T> toImmutableSet() {
        return ImmutableSet.copyOf(this);
    }

    /**
     * Returns an {@code ImmutableSortedSet} containing all of the elements from this
     * {@code FluentCloseableIterable} in the order specified by {@code comparator}, with duplicates
     * (determined by {@code comaprator.compare(x, y) == 0}) removed. To produce an
     * {@code ImmutableSortedSet} sorted by its natural ordering, use
     * {@code toImmutableSortedSet(Ordering.natural())}.
     *
     * @param comparator the function by which to sort set elements
     * @throws NullPointerException if any element is null
     */
    public final ImmutableSortedSet<T> toImmutableSortedSet(Comparator<? super T> comparator) {
        return ImmutableSortedSet.copyOf(comparator, this);
    }

    /**
     * Returns an array containing all of the elements from this fluent iterable in iteration order.
     *
     * @param type the type of the elements
     * @return a newly-allocated array into which all the elements of this fluent iterable have
     *     been copied
     */
    public final T[] toArray(Class<T> type) {
        return Iterables.toArray(this, type);
    }

    /**
     * Returns the element at the specified position in this fluent iterable.
     *
     * @param position position of the element to return
     * @return the element at the specified position in this fluent iterable
     * @throws IndexOutOfBoundsException if {@code position} is negative or greater than or equal to
     *     the size of this fluent iterable
     */
    public final T get(int position) {
        return Iterables.get(this, position);
    }
}
