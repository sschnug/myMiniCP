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

    private ReversibleContext context;
    private int v;
    private Long lastMagic = -1L;

    public ReversibleInt(ReversibleContext context, int initial) {
        this.context = context;
        v = initial;
        lastMagic = context.magic;
    }

    private void trail() {
        long contextMagic = context.magic;
        if (lastMagic != contextMagic) {
            lastMagic = contextMagic;
            context.pushOnTrail(new TrailEntryInt(v));
        }
    }

    public void setValue(int v) {
        if (v != this.v) {
            trail();
            this.v = v;
        }
    }

    public void increment() { setValue(getValue()+1); }
    public void decrement() { setValue(getValue()-1); }

    public int getValue() { return this.v; }

}
