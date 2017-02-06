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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mini-cp.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2016 L. Michel, P. Schaus, P. Van Hentenryck
 */

package minicp.reversible;


public class ReversibleInt {

    class TrailEntryInt implements TrailEntry {

        private final int v;

        public TrailEntryInt(int v) {
            this.v = v;
        }

        public void restore() {
            ReversibleInt.this.v = v;
        }
    }

    private Trail trail;
    private int v;
    private Long lastMagic = -1L;

    public ReversibleInt(Trail trail, int initial) {
        this.trail = trail;
        v = initial;
        lastMagic = trail.magic;
    }

    private void trail() {
        long trailMagic = trail.magic;
        if (lastMagic != trailMagic) {
            lastMagic = trailMagic;
            trail.pushOnTrail(new TrailEntryInt(v));
        }
    }

    public void setValue(int v) {
        if (v != this.v) {
            trail();
            this.v = v;
        }
    }

    public int increment() {
        setValue(getValue()+1);
        return getValue();
    }
    public int decrement() {
        setValue(getValue()-1);
        return getValue();
    }

    public int getValue() { return this.v; }

    @Override
    public String toString() {
        return ""+v;
    }
}
