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
                children.add(new Node(newboard));
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

        // TODO: implement minmax player!
        // HINT: use the functions on the 'board' object to produce a new board given a
        // specific move
        // HINT: use the functions on the 'heuristic' object to produce evaluations for
        // the different board states!

        // Example:
        // int maxValue = Integer.MIN_VALUE;
        // int maxMove = 0;
        // for (int i = 0; i < board.width; i++) { // for each of the possible moves
        //     if (board.isValid(i)) { // if the move is valid
        //         Board newBoard = board.getNewBoard(i, playerId); // Get a new board resulting from that move
        //         int value = heuristic.evaluateBoard(playerId, newBoard); // evaluate that new board to get a heuristic
        //                                                                  // value from it
        //         if (value > maxValue) {
        //             maxMove = i;
        //         }
        //     }fatal: Need to specify how to reconcile divergent branches.

        // }
        // This returns the same as:
        // heuristic.getBestAction(playerId, board); // Very useful helper function!

        /*
         * This is obviously not enough (this is depth 1)
         * Your assignment is to create a data structure (tree) to store the gameboards
         * such that you can evaluate a higher depths.
         * Then, use the minmax algorithm to search through this tree to find the best
         * move/action to take!
         */

        return findMove(0, this.playerId, root, this.depth);
    }

    public int findMove(int cur, int playerId, Node curNode, int maxDepth) {
        if (cur == maxDepth) {
            return heuristic.evaluateBoard(this.playerId, curNode.getBoard());
        }

        if (playerId == 1) {
            List<Integer> values = new ArrayList<>();
            for (Node node : curNode.getMoves()){
                values.add(this.findMove(cur+1, playerId+1, node, maxDepth));
            }
            return Collections.max(values);
        } else{
            List<Integer> values = new ArrayList<>();
            for (Node node : curNode.getMoves()){
                values.add(this.findMove(cur+1, playerId-1, node, maxDepth));
            }
            return Collections.min(values);
        }

    }
}
