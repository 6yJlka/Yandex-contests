package weekend_offer_2026_april;

import java.io.*;
import java.util.*;

public class five {

    static int n;
    static int[][] grid;
    static HashMap<Integer, Integer> stationRow = new HashMap<>();
    static HashMap<Integer, Integer> stationCol = new HashMap<>();
    static HashMap<Integer, Integer> parent = new HashMap<>();

    static int source;
    static int sink;
    static int nodeCount;
    static int edgeCount;

    static int[] head;
    static int[] to;
    static int[] next;
    static int[] cost;
    static int[] capacity;

    static long[] dist;
    static long[] potential;
    static int[] previousEdge;

    static final long INF = Long.MAX_VALUE / 4;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        n = Integer.parseInt(reader.readLine());
        grid = new int[n][n];

        for(int i = 0; i < n; i++){
            String[] line = reader.readLine().split(" ");

            for(int j = 0; j < n; j++){
                grid[i][j] = Integer.parseInt(line[j]);

                if(grid[i][j] <= -2){
                    stationRow.put(grid[i][j], i);
                    stationCol.put(grid[i][j], j);
                    parent.put(grid[i][j], grid[i][j]);
                }
            }
        }

        int m = Integer.parseInt(reader.readLine());
        for(int i = 0; i < m; i++){
            String[] line = reader.readLine().split(" ");
            int first = Integer.parseInt(line[0]);
            int second = Integer.parseInt(line[1]);
            union(first, second);
        }

        if(grid[0][0] == -1 || grid[n - 1][n - 1] == -1){
            writer.write("0");
            writer.newLine();
            writer.flush();
            writer.close();
            return;
        }

        nodeCount = 2 * n * n + 2;
        source = 2 * n * n;
        sink = source + 1;

        int stationCount = stationRow.size();
        int maxMetroEdges = stationCount * stationCount / 2;
        int maxEdges = 2 * (3 * n * n + 2 * n * (n - 1) + maxMetroEdges + 2) + 5;

        head = new int[nodeCount];
        Arrays.fill(head, -1);
        to = new int[maxEdges];
        next = new int[maxEdges];
        cost = new int[maxEdges];
        capacity = new int[maxEdges];

        buildGraph();
        buildMetroEdges();

        addEdge(source, inNode(0, 0), 2, 0);
        addEdge(outNode(n - 1, n - 1), sink, 2, 0);

        dist = new long[nodeCount];
        potential = new long[nodeCount];
        previousEdge = new int[nodeCount];

        buildPotential();

        long answer = 0;

        for(int flow = 0; flow < 2; flow++){
            if(!findPath()){
                writer.write("0");
                writer.newLine();
                writer.flush();
                writer.close();
                return;
            }

            int current = sink;
            long pathCost = 0;

            while(current != source){
                int edge = previousEdge[current];
                capacity[edge]--;
                capacity[edge ^ 1]++;
                pathCost += cost[edge];
                current = to[edge ^ 1];
            }

            answer -= pathCost;
        }

        writer.write(String.valueOf(answer));
        writer.newLine();
        writer.flush();
        reader.close();
        writer.close();
    }

    public static void buildGraph(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(grid[i][j] == -1){
                    continue;
                }

                int value = 0;
                if(grid[i][j] > 0){
                    value = grid[i][j];
                }

                if(value > 0){
                    addEdge(inNode(i, j), outNode(i, j), 1, -value);
                    addEdge(inNode(i, j), outNode(i, j), 1, 0);
                } else {
                    addEdge(inNode(i, j), outNode(i, j), 2, 0);
                }

                if(i + 1 < n && grid[i + 1][j] != -1){
                    addEdge(outNode(i, j), inNode(i + 1, j), 2, 0);
                }

                if(j + 1 < n && grid[i][j + 1] != -1){
                    addEdge(outNode(i, j), inNode(i, j + 1), 2, 0);
                }
            }
        }
    }

    public static void buildMetroEdges(){
        HashMap<Integer, ArrayList<int[]>> branches = new HashMap<>();

        for(int id : stationRow.keySet()){
            int root = find(id);
            branches.computeIfAbsent(root, key -> new ArrayList<>())
                    .add(new int[]{stationRow.get(id), stationCol.get(id)});
        }

        for(ArrayList<int[]> branch : branches.values()){
            branch.sort((first, second) -> {
                if(first[0] != second[0]){
                    return first[0] - second[0];
                }
                return first[1] - second[1];
            });

            for(int i = 0; i < branch.size(); i++){
                int[] from = branch.get(i);

                for(int j = i + 1; j < branch.size(); j++){
                    int[] toStation = branch.get(j);

                    if(from[0] <= toStation[0] && from[1] <= toStation[1]){
                        addEdge(outNode(from[0], from[1]), inNode(toStation[0], toStation[1]), 2, 0);
                    }
                }
            }
        }
    }

    public static void buildPotential(){
        Arrays.fill(potential, INF);
        potential[source] = 0;

        relax(source);

        for(int sum = 0; sum <= 2 * (n - 1); sum++){
            int start = Math.max(0, sum - (n - 1));
            int finish = Math.min(n - 1, sum);

            for(int i = start; i <= finish; i++){
                int j = sum - i;
                relax(inNode(i, j));
            }

            for(int i = start; i <= finish; i++){
                int j = sum - i;
                relax(outNode(i, j));
            }
        }

        relax(sink);

        for(int i = 0; i < nodeCount; i++){
            if(potential[i] == INF){
                potential[i] = 0;
            }
        }
    }

    public static void relax(int node){
        if(potential[node] == INF){
            return;
        }

        for(int edge = head[node]; edge != -1; edge = next[edge]){
            if(capacity[edge] == 0){
                continue;
            }

            int nextNode = to[edge];
            long newCost = potential[node] + cost[edge];

            if(newCost < potential[nextNode]){
                potential[nextNode] = newCost;
            }
        }
    }

    public static boolean findPath(){
        Arrays.fill(dist, INF);
        Arrays.fill(previousEdge, -1);

        PriorityQueue<long[]> queue = new PriorityQueue<>(Comparator.comparingLong(value -> value[0]));
        dist[source] = 0;
        queue.add(new long[]{0, source});

        while(!queue.isEmpty()){
            long[] current = queue.poll();
            long currentDist = current[0];
            int node = (int) current[1];

            if(currentDist != dist[node]){
                continue;
            }

            for(int edge = head[node]; edge != -1; edge = next[edge]){
                if(capacity[edge] == 0){
                    continue;
                }

                int nextNode = to[edge];
                long edgeCost = cost[edge] + potential[node] - potential[nextNode];
                long newDist = dist[node] + edgeCost;

                if(newDist < dist[nextNode]){
                    dist[nextNode] = newDist;
                    previousEdge[nextNode] = edge;
                    queue.add(new long[]{newDist, nextNode});
                }
            }
        }

        if(dist[sink] == INF){
            return false;
        }

        for(int i = 0; i < nodeCount; i++){
            if(dist[i] < INF){
                potential[i] += dist[i];
            }
        }

        return true;
    }

    public static void addEdge(int from, int toNode, int currentCapacity, int currentCost){
        to[edgeCount] = toNode;
        next[edgeCount] = head[from];
        cost[edgeCount] = currentCost;
        capacity[edgeCount] = currentCapacity;
        head[from] = edgeCount;
        edgeCount++;

        to[edgeCount] = from;
        next[edgeCount] = head[toNode];
        cost[edgeCount] = -currentCost;
        capacity[edgeCount] = 0;
        head[toNode] = edgeCount;
        edgeCount++;
    }

    public static int inNode(int i, int j){
        return 2 * (i * n + j);
    }

    public static int outNode(int i, int j){
        return 2 * (i * n + j) + 1;
    }

    public static int find(int value){
        if(parent.get(value) != value){
            parent.put(value, find(parent.get(value)));
        }

        return parent.get(value);
    }

    public static void union(int first, int second){
        int firstParent = find(first);
        int secondParent = find(second);

        if(firstParent != secondParent){
            parent.put(firstParent, secondParent);
        }
    }
}
