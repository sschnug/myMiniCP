/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2018. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.examples;

import minicp.engine.constraints.Cumulative;
import minicp.engine.constraints.LessOrEqual;
import minicp.engine.constraints.Maximum;
import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;
import minicp.util.*;

import java.util.ArrayList;

import static minicp.cp.Factory.*;
import static minicp.cp.Heuristics.*;

/**
 * Resource Constrained Project Scheduling Problem (RCPSP)
 * http://www.om-db.wi.tum.de/psplib/library.html
 */
public class RCPSP {


    public static void main(String[] args) throws InconsistencyException {

        // Reading the data

        InputReader reader = new InputReader(args[0]);

        int nActivities = reader.getInt();
        int nResources = reader.getInt();

        int[] capa = new int[nResources];
        for (int i = 0; i < nResources; i++) {
            capa[i] = reader.getInt();
        }

        int[] duration = new int[nActivities];
        int[][] consumption = new int[nResources][nActivities];
        int[][] successors = new int[nActivities][];


        int H = 0;
        for (int i = 0; i < nActivities; i++) {
            // durations, demand for each resource, successors
            duration[i] = reader.getInt();
            H += duration[i];
            for (int r = 0; r < nResources; r++) {
                consumption[r][i] = reader.getInt();
            }
            successors[i] = new int[reader.getInt()];
            for (int k = 0; k < successors[i].length; k++) {
                successors[i][k] = reader.getInt() - 1;
            }
        }


        // -------------------------------------------

        // The Model

        Solver cp = makeSolver();

        IntVar[] start = makeIntVarArray(cp, nActivities, H);
        IntVar[] end = new IntVar[nActivities];


        for (int i = 0; i < nActivities; i++) {
            end[i] = plus(start[i], duration[i]);
        }

        // TODO 1: add the cumulative constraint to model the resource
        // capa[r] is the capacity of resource r
        // consumption[r] is the consumption for each activity on the resource [r]
        // duration is the duration of each activity
        for (int r=0; r<nResources; r++){
            cp.post(new Cumulative(start, duration, consumption[r], capa[r]));
        }


        // TODO 2: add the precedence constraints
        // successors[i] is the sucessors of activity i
        for (int i=0; i<successors.length; i++) {
            for (int j=0; j<successors[i].length; j++) {
                cp.post(new LessOrEqual(end[i],start[successors[i][j]]));
            }
        }

        // TODO 3: minimize the makespan
        IntVar makespan = makeIntVar(cp,H);
        cp.post(new Maximum(end,makespan));

        // TODO 4: implement the search
        long startTime = System.currentTimeMillis();
        DFSearch dfs = makeDfs(cp,firstFailTimeLim(end,startTime));
        //OrderQueue oq = new OrderQueue(cp);
        //DFSearch dfs = makeDfs(cp,and(LNSTimeLim(nodes,oq,startTime),firstFailTimeLim(start,startTime)));
        cp.post(minimize(makespan, dfs));

        storingRetValues finalVals = new storingRetValues(start.length);
        dfs.onSolution(() -> {
            finalVals.UpdateSpan(makespan.getMin());
            finalVals.UpdateStarts(start);
        });

        dfs.start();
        finalVals.print();

    }
}