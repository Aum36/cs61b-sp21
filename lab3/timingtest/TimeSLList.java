package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        int[] arrNs = {1000, 2000, 4000, 8000, 16000, 32000, 64000};
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        int M = 10000;
        Stopwatch sw = new Stopwatch();
        double time = 0;
        for(int n : arrNs) {
            SLList<Integer> dummyList = new SLList<>();
            // Timing Code
            for(int i = 0; i < n; i++){
                dummyList.addLast(i);
            }
            System.out.println(dummyList.size());
            time = sw.elapsedTime();
            for(int i = 0; i < M; i++) {
                dummyList.addLast(M);
            }
            time = sw.elapsedTime() - time;
            Ns.addLast(n);
            times.addLast(time);
            opCounts.addLast(M);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
