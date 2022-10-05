package NRow.Players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Tree.Node;

public class MinMaxPlayer extends PlayerController {
    private int depth;

    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
        // You can add functionality which runs when the player is first created (before
        // the game starts)
    }

    /**
     * Implement this method yourself!
     * 
     * @param board the current board
     * @return column integer the player chose
     */

    // function to recursively build a tree with certain depth
    public Node makeTree(int playerId, int depth, Node node) {
        Node currentroot = node;
        List<Node> children = new ArrayList<>();
        for (int i = 0; i < node.getBoard().width; i++) {
            if (node.getBoard().isValid(i)) {
                Board newboard = node.getBoard().getNewBoard(i, playerId);
                Node newnode = new Node(newboard);
                newnode.setLastMove(i);
                children.add(newnode);
            }
        }
        if (depth > 1) {
            for (int j = 0; j < children.size(); j++) {
                Node currentchild = children.get(j);
                currentchild = makeTree((playerId % 2) + 1, depth - 1, currentchild);
                children.set(j, currentchild);
            }
        }
        currentroot.addMoves(children);
        return currentroot;
    }

    @Override
    public int makeMove(Board board) {
        // create the tree of certain depth
        Node root = new Node(board);
        root = makeTree(this.playerId, this.depth, root);
        int move = findMove(0, this.playerId, root, this.depth);
        return move;
    }

    public int findMove(int cur, int playerId, Node curNode, int maxDepth) {
        if (cur == maxDepth) {
            return heuristic.evaluateBoard(this.playerId, curNode.getBoard());
        }

        if (playerId == 1) {
            int[] values = new int[30];
            for (Node node : curNode.getMoves()) {
                values[node.getLastMove()] = this.findMove(cur + 1, playerId + 1, node, maxDepth);
            }
            return this.getMaxIndex(values);
        } else {
            int[] values = new int[30];
            for (Node node : curNode.getMoves()) {
                values[node.getLastMove()] = this.findMove(cur + 1, playerId - 1, node, maxDepth);
            }
            return this.getMinIndex(values);
        }

    }
    
    public int getMaxIndex(int[] values){
        int index = -1;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < values.length; i++){
            if (max < values[i]){
                max = values[i];
                index = i;
            }
        }
        return index;
    }
    public int getMinIndex(int[] values){
        int index = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < values.length; i++){
            if (min > values[i]){
                min = values[i];
                index = i;
            }
        }
        return index;
    }
}
