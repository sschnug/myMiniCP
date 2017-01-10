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

package minicp.cp.constraints;

import minicp.cp.core.Constraint;
import minicp.cp.core.IntVar;
import minicp.cp.core.Status;

public class DifferentVar extends Constraint {

    private IntVar x,y;
    private int      c;

    public DifferentVar(IntVar x, IntVar y) { // x == y + c
        this.x = x;
        this.y = y;
        this.c = 0;
    }
    public DifferentVar(IntVar x, IntVar y,int c) { // x == y + c
        this.x = x;
        this.y = y;
        this.c = c;
    }

    @Override
    public void setup() throws Status {
        x.propagateOnBind(this);
        y.propagateOnBind(this);
        if (x.isBound() || y.isBound()) {
            propagate();
        }
    }

    @Override
    public void propagate() throws Status {
        if (x.isBound()) {
            y.remove(x.getMin() - c);
        } else {
            x.remove(y.getMin() + c);
        }
    }
}
