/*
 * This file is part of mini-cp.
 *
 * mini-cp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MErsHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mini-cp.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2016 L. Michel, P. Schaus, P. Van Hentenryck
 */

package minicp.reversible;


import minicp.util.NotImplementedException;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ReversibleSparseSetTest {


    @Test
    public void testExample() {

        Trail trail = new Trail();
        ReversibleSparseSet set = new ReversibleSparseSet(trail,9);

        trail.push();

        set.remove(4);
        set.remove(6);

        assertFalse(set.contains(4));
        assertFalse(set.contains(6));

        trail.pop();

        assertTrue(set.contains(4));
        assertTrue(set.contains(6));

    }

    @Test
    public void testReversibleSparseSet() {

        Trail trail = new Trail();
        ReversibleSparseSet set = new ReversibleSparseSet(trail,10);

        assertTrue(toSet(set.toArray()).equals(toSet(new int[]{0,1,2,3,4,5,6,7,8,9})));

        trail.push();

        set.remove(1);
        set.remove(0);

        assertTrue(set.getMin() == 2);

        set.remove(8);
        set.remove(9);

        assertTrue(toSet(set.toArray()).equals(toSet(new int[]{2,3,4,5,6,7})));
        assertTrue(set.getMax() == 7);

        trail.pop();
        trail.push();

        assertEquals(10, set.getSize());

        for (int i = 0; i < 10; i++) {
            assertTrue(set.contains(i));
        }
        assertFalse(set.contains(10));

        assertTrue(set.getMin() == 0);
        assertTrue(set.getMax() == 9);

        set.removeAllBut(2);

        for (int i = 0; i < 10; i++) {
            if (i != 2) assertFalse(set.contains(i));
        }
        assertTrue(set.contains(2));
        assertTrue(toSet(set.toArray()).equals(toSet(new int[]{2})));


        trail.pop();
        trail.push();

        assertEquals(10, set.getSize());

    }

    private Set<Integer> toSet(int... values) {
        Set<Integer> set = new java.util.HashSet<Integer>();
        for (int v: values) {
            set.add(v);
        }
        return set;
    }
    @Test
    public void testRangeConstructor() {

        try {

            Trail trail = new Trail();
            ReversibleSparseSet set = new ReversibleSparseSet(trail,-5,5);

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

            trail.push();

            set.remove(-4);
            set.remove(-5);
            set.remove(4);
            set.remove(5);

            assertEquals(-3,set.getMin());
            assertEquals(3,set.getMax());

            trail.push();

            set.removeAllBut(-1);
            assertEquals(-1,set.getMin());
            assertEquals(-1,set.getMax());


            trail.pop();
            trail.pop();

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

        } catch (NotImplementedException e) {
            e.print();
        }
    }

    @Test
    public void testRemoveBelow() {

        try {

            Trail trail = new Trail();
            ReversibleSparseSet set = new ReversibleSparseSet(trail,-5,5);

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

            trail.push();


            set.remove(-2);
            set.remove(-1);
            set.removeBelow(-2);



            assertEquals(0,set.getMin());
            assertEquals(5,set.getMax());

            trail.push();

            set.removeBelow(2);

            assertEquals(1,set.getMin());

            trail.pop();
            trail.pop();

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

        } catch (NotImplementedException e) {
            e.print();
        }
    }

    @Test
    public void testRemoveAbove() {

        try {

            Trail trail = new Trail();
            ReversibleSparseSet set = new ReversibleSparseSet(trail,-5,5);

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

            trail.push();


            set.remove(1);
            set.remove(2);
            set.removeAbove(2);

            assertEquals(-5,set.getMin());
            assertEquals(0,set.getMax());

            trail.push();

            set.removeAbove(-2);

            assertEquals(-3,set.getMax());

            trail.pop();
            trail.pop();

            for (int i = -5; i <= 5; i++) {
                assertTrue(set.contains(i));
            }

        } catch (NotImplementedException e) {
            e.print();
        }
    }
}
